package com.spi.service;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.net.ssl.HttpsURLConnection;
import org.codehaus.jettison.json.JSONObject;

public class NotificationThread extends Thread {

    String routeID;
    String dbURL = "jdbc:mysql://localhost:3306/test";
    String username = "root";
    String password = "";
    String query = "";
    Connection dbCon = null;
    int haltTime = -10;
    int breakdownTime = -20;
    double arrivalDistance = 1.5;
    double arrivedDistance = 10;
    String uuid = "";
    ArrayList alertType = new ArrayList();

    String busDirection = null;
    Date busUpStartTime;
    Date busUpEndTime;
    Date busdownStartTime;
    Date busdownEndTime;

    NotificationThread(String routeID, String busDirection, Date busUpStartTime, Date busUpEndTime, Date busdownStartTime, Date busdownEndTime) {

        this.routeID = routeID;
        this.busDirection = busDirection;
        this.busUpStartTime = busUpStartTime;
        this.busUpEndTime = busUpEndTime;
        this.busdownStartTime = busdownStartTime;
        this.busdownEndTime = busdownEndTime;
        setTimeDistance();
    }

    public void run() {
        sendNotification();
    }

    public void sendNotification() {

        Statement stmt = null;
        ResultSet rs = null;

        ArrayList StudentList = new ArrayList();
        ArrayList WayList = new ArrayList();
        ArrayList WayListAlertSent = new ArrayList();

        Map wpStudentList = new HashMap();
        Map wpLatLong = new HashMap();
        Map stUserIdMap = new HashMap();
        

        double studentlat = 0.00;
        double studentlog = 0.00;
        double devicelat = 0.00;
        double devicelog = 0.00;

        try { //getting database connection to MySQL server 
            Date currentTimedate = null;
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
            String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

            System.out.println(busDirection + busUpStartTime + busUpEndTime + busdownStartTime + busdownEndTime);

            try {
                currentTimedate = parser.parse(currentTime);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if ("down".equalsIgnoreCase(busDirection)) {
                busUpStartTime = busdownStartTime;
                busUpEndTime = busdownEndTime;

            }
            boolean WayListLoaded = false;
            
            while ((busUpStartTime.equals(currentTimedate) || busUpStartTime.before(currentTimedate)) && (busUpEndTime.equals(currentTimedate) || busUpEndTime.after(currentTimedate))) {
                
                System.out.println("Thread process for route: " + routeID);

                if (!WayListLoaded || WayList == null || WayList.size() <= 0) {
                    WayList = new ArrayList();
                    query = "select id from way_point where route_id='" + routeID + "' order by sequence_number ";
                    stmt = dbCon.prepareStatement(query);
                    //Resultset returned by query 
                    rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        WayList.add(rs.getString("id"));
                        System.out.println("Way point List: " + WayList);
                        WayListLoaded = true;
                    }
                }

                if(WayList != null && WayList.size() > 0) {
                    
                    for(int j = 0; WayList.size() > j; j++) {
                        
                        /*
                        if (WayListAlertSent.contains(WayList.get(j))) {
                            continue;
                        }
                        */
                        StudentList = (ArrayList) wpStudentList.get(WayList.get(j));

                        if (StudentList == null || StudentList.size() <= 0) {
                            StudentList = new ArrayList();
                            query = "select student_id from student_route_xref where way_point_id=" + WayList.get(j) + "";
                            stmt = dbCon.prepareStatement(query);
                            //Resultset returned by query 
                            rs = stmt.executeQuery(query);
                            while (rs.next()) {
                                StudentList.add(rs.getString("student_id"));
                                System.out.println("StudentList List: " + StudentList);
                            }
                            wpStudentList.put(WayList.get(j), StudentList);
                        }
                        boolean isLongHalt = false;
                        boolean isBreakDown = false;
                        if (StudentList != null && StudentList.size() > 0) {
                            
                            for (int i = 0; StudentList.size() > i; i++) {

                                stmt = null;
                                rs = null;
                                
                                if(wpLatLong.get(WayList.get(j))==null) {                                    
                                
                                    query = "select lattitude,longitude from way_point where route_id='" + routeID + "' and id='" + WayList.get(j) + "' ";
                                    stmt = dbCon.prepareStatement(query);
                                    //Resultset returned by query 
                                    rs = stmt.executeQuery(query);
                                    if(rs.next()) {
                                        studentlat = Double.parseDouble(rs.getString("lattitude"));
                                        studentlog = Double.parseDouble(rs.getString("longitude"));
                                        wpLatLong.put(WayList.get(j), new Double[]{studentlat, studentlog});
                                    }
                                }
                                else {
                                    Double latlongArr[] = ((Double[])wpLatLong.get(WayList.get(j)));
                                    studentlat = latlongArr[0];
                                    studentlog = latlongArr[1];
                                }
                                
                                stmt = null;
                                rs = null;
                                query = "select rfd.device_id,l.lattitude,l.longitude from route_fleet_device_xref rfd left join location l on rfd.device_id=l.device_id where route_id='" + routeID + "' order by l.id desc";
                                stmt = dbCon.prepareStatement(query);
                                //Resultset returned by query 
                                rs = stmt.executeQuery(query);
                                String deviceid = "";
                                if (rs.next()) {
                                    deviceid = rs.getString("device_id");
                                    devicelat = Double.parseDouble((rs.getString("lattitude")));
                                    devicelog = Double.parseDouble((rs.getString("longitude")));
                                }

                                double totaldistanceM = distance(studentlat, studentlog, devicelat, devicelog, "M"); // Total distance in Meter 
                                double totaldistanceK = distance(studentlat, studentlog, devicelat, devicelog, "K");// Total distance in Kilo Meter
                                System.out.println("Total Distance======================" + totaldistanceK);

                                stmt = null;
                                rs = null;
                                
                                String userid = "";
                                if(stUserIdMap.get(StudentList.get(i))==null) {
                                    query = " select user_id from student where id='" + StudentList.get(i) + "';";
                                    stmt = dbCon.prepareStatement(query);
                                    //Resultset returned by query 
                                    rs = stmt.executeQuery(query);
                                    
                                    if (rs.next()) {
                                        userid = rs.getString("user_id");
                                        stUserIdMap.put(StudentList.get(i), userid);
                                    }
                                }
                                else {
                                    userid = (String)stUserIdMap.get(StudentList.get(i));
                                }
                                
                                if (checkUserConfig(userid)) {
                                    //Send Notification
                                    uuid = UUID.randomUUID().toString();
                                    System.out.println("Alert going to send user=========" + userid);
                                    if (alertType != null && alertType.contains("Long Halt") && isLongHalt(deviceid, devicelat)) {
                                        isLongHalt = true;
                                        insertAlertMessage((String) StudentList.get(i), userid, "halt", uuid);
                                        //WayListAlertSent.add(WayList.get(j));
                                        
                                    } else if (alertType != null && alertType.contains("Breakdown") && isBreakDown(deviceid, devicelat)) {
                                        isBreakDown = true;
                                        insertAlertMessage((String) StudentList.get(i), userid, "breakdown", uuid);
                                        //WayListAlertSent.add(WayList.get(j));
                                        
                                    } else if (alertType != null && !WayListAlertSent.contains("Arrival"+WayList.get(j)+"#"+userid+"#"+(String) StudentList.get(i)) && alertType.contains("Arrival") && isArrival(deviceid, userid, (String) StudentList.get(i), routeID, devicelat, totaldistanceK)) {
                                        insertAlertMessage((String) StudentList.get(i), userid, "arrival", uuid);
                                        WayListAlertSent.add("Arrival"+WayList.get(j)+"#"+userid+"#"+(String) StudentList.get(i));
                                        
                                    } else if (alertType != null && !WayListAlertSent.contains("Arrived"+WayList.get(j)+"#"+userid+"#"+(String) StudentList.get(i)) && alertType.contains("Arrival") && isArrived(deviceid, userid, (String) StudentList.get(i), devicelat, totaldistanceM)) {
                                        insertAlertMessage((String) StudentList.get(i), userid, "arrived", uuid);
                                        WayListAlertSent.add("Arrived"+WayList.get(j)+"#"+userid+"#"+(String) StudentList.get(i));
                                        
                                    } else if (alertType != null && !WayListAlertSent.contains("Departure"+WayList.get(j)+"#"+userid+"#"+(String) StudentList.get(i)) && alertType.contains("Departure") && isDeparture(deviceid, userid, (String) StudentList.get(i))) {
                                        insertAlertMessage((String) StudentList.get(i), userid, "departure", uuid);
                                        WayListAlertSent.add("Departure"+WayList.get(j)+"#"+userid+"#"+(String) StudentList.get(i));
                                    }
                                }
                            }
                            
                            if(isLongHalt || isBreakDown) {
                                Thread.sleep(10*60*1000);
                            }
                        }
                    }
                }
                currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                try {
                    currentTimedate = parser.parse(currentTime);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try{
                rs.close();
                stmt.close();
            }catch(Exception e){}
            dbCon = null;
            stmt = null;
            rs = null;
        }
    }

    public boolean isLongHalt(String deviceid, double devicelat) {
        Statement stmt = null;
        ResultSet rs = null;
        boolean flag = false;
        DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
        Date dateobj = new Date();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, haltTime);
        String previousTime = df.format(dateobj) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);
        System.out.println("halt time==========================" + previousTime);
        // ArrayList  active_days=new  ArrayList();
        query = "select lattitude,longitude from location where device_id='" + deviceid + "' and time_created>='" + previousTime + "' order by id desc ";
        try {

            System.out.println("query for users List: " + query);
            stmt = dbCon.prepareStatement(query);
            //Resultset returned by query 
            rs = stmt.executeQuery(query);
            Object obj = null;
            if (rs.next()) {
                double lat = Double.parseDouble((rs.getString("lattitude")));
                if (lat == devicelat) {
                    flag = true;
                }

            }

        } catch (Exception e) {
            System.out.println("Exception==================" + e);
            e.printStackTrace();
        } finally {
            try{
                rs.close();
                stmt.close();
            }catch(Exception e){}
            // dbCon=null;
            stmt = null;
            rs = null;
        }
        return flag;
    }

    public boolean isBreakDown(String deviceid, double devicelat) {
        Statement stmt = null;
        ResultSet rs = null;
        boolean flag = false;
        DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
        Date dateobj = new Date();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, breakdownTime);
        String previousTime = df.format(dateobj) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);
        System.out.println("halt time==========================" + previousTime);
        // ArrayList  active_days=new  ArrayList();
        query = "select lattitude,longitude from location where device_id='" + deviceid + "' and time_created>='" + previousTime + "' order by id desc ";
        try {

            System.out.println("query for users List: " + query);
            stmt = dbCon.prepareStatement(query);
            //Resultset returned by query 
            rs = stmt.executeQuery(query);
            Object obj = null;
            if (rs.next()) {
                double lat = Double.parseDouble((rs.getString("lattitude")));
                if (lat == devicelat) {
                    flag = true;
                }

            }

        } catch (Exception e) {
            System.out.println("Exception==================" + e);
            e.printStackTrace();
        } finally {
            try{
                rs.close();
                stmt.close();
            }catch(Exception e){}
            // dbCon=null;
            stmt = null;
            rs = null;
        }
        return flag;
    }

    public boolean isArrival(String deviceid, String userid, String studentid, String routeid, double devicelat, double distance) {
        boolean flag = false;
        Statement stmt = null;
        try {

            if (distance <= arrivalDistance) {
                flag = true;
                query = "insert into arrival_departure_status (student_id,user_id,device_id,route_id,created) values (" + studentid + "," + userid + "," + deviceid + "," + routeid + ",now())";
                System.out.println("query for users List: " + query);
                stmt = dbCon.prepareStatement(query);
                stmt.executeUpdate(query);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                stmt.close();
            }catch(Exception e){}
            stmt = null;
        }
        return flag;
    }

    public boolean isArrived(String deviceid, String userid, String studentid, double devicelat, double distance) {
        boolean flag = false;
        Statement stmt = null;
        ResultSet rs = null;
        try {

            if (distance <= arrivedDistance) {
                flag = true;
                query = "update arrival_departure_status set arrived='Y' where student_id=" + studentid + " and user_id=" + userid + " and device_id=" + deviceid + " AND DATE_FORMAT(created,'%y-%m-%d')=DATE_FORMAT(NOW(),'%y-%m-%d')";
                System.out.println("query for update arrival staus: " + query);
                stmt = dbCon.prepareStatement(query);
                stmt.executeUpdate(query);
            }

        } catch (Exception e) {
            System.out.println("Exception==================" + e);
            e.printStackTrace();
        } finally {
            try{
                stmt.close();
            }catch(Exception e){}
        }
        return flag;
    }

    public boolean isDeparture(String deviceid, String userid, String studentid) {
        boolean flag = false;
        String arrival = "";
        Statement stmt = null;
        ResultSet rs = null;
        try {

            query = "select id, arrived from  arrival_departure_status where student_id=" + studentid + " and user_id=" + userid + " and device_id=" + deviceid + " and arrived='Y' AND DATE_FORMAT(created,'%y-%m-%d')=DATE_FORMAT(NOW(),'%y-%m-%d') order by id desc";
            System.out.println("query forupdate arrival staus: " + query);
            stmt = dbCon.prepareStatement(query);
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                int adsId = rs.getInt("id");
                stmt = null;
                rs = null;
                query = "select moving_status from location where device_id=" + deviceid + " and moving_status='1' order by id desc";
                stmt = dbCon.prepareStatement(query);
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    flag = true;
                    stmt = null;
                    rs = null;
                    query = "update arrival_departure_status set departure='Y' where student_id=" + studentid + " and user_id=" + userid + " and device_id=" + deviceid + " and id="+adsId;
                    System.out.println("query forupdate departure staus: " + query);
                    stmt = dbCon.prepareStatement(query);
                    stmt.executeUpdate(query);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                rs.close();
                stmt.close();
            }catch(Exception e){}
            stmt = null;
            rs = null;
        }
        return flag;
    }

    public boolean checkUserConfig(String userid) {
        Statement stmt = null;
        ResultSet rs = null;
        byte[] buffer = null;
        ObjectInputStream is = null;
        boolean flag = false;
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date dateobj = new Date();
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String day = getDayName(dayOfWeek);
        System.out.println("Current day======================: " + day);
        ArrayList active_days = new ArrayList();
        query = "select user_id,active_days,alert_types from alertconfig where user_id='" + userid + "' and (pick_start_time<='" + df.format(dateobj) + "' and  pick_end_time>='" + df.format(dateobj) + "') or (drop_start_time<='" + df.format(dateobj) + "' and  drop_end_time>='" + df.format(dateobj) + "')";
        try {

            System.out.println("query for users List: " + query);
            stmt = dbCon.prepareStatement(query);

            rs = stmt.executeQuery(query);
            Object obj = null;
            while (rs.next()) {
                buffer = rs.getBytes("active_days");
                is = new ObjectInputStream(new ByteArrayInputStream(buffer));

                active_days = (ArrayList) is.readObject();
                if (active_days != null && active_days.contains(day)) {
                    flag = true;
                }
                buffer = null;
                is = null;
                buffer = rs.getBytes("alert_types");
                is = new ObjectInputStream(new ByteArrayInputStream(buffer));

                alertType = (ArrayList) is.readObject();
            }

            System.out.println("active_days for users : " + active_days);
            System.out.println("alert type  for users : " + alertType);
        } catch (Exception e) {
            System.out.println("Exception==================" + e);
            e.printStackTrace();
        } finally {
            // dbCon=null;
            try{
                rs.close();
                stmt.close();
            }catch(Exception e){}
            stmt = null;
            rs = null;
            buffer = null;
            is = null;
        }
        return flag;
    }

    public void setTimeDistance() {
        Statement stmt = null;
        ResultSet rs = null;
        query = "select * from distance_time_config";
        try {
            dbCon = DriverManager.getConnection(dbURL, username, password);
            System.out.println("query for users List: " + query);
            stmt = dbCon.prepareStatement(query);
            //Resultset returned by query 
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                this.haltTime = Integer.parseInt(rs.getString("halt_time"));
                this.breakdownTime = Integer.parseInt(rs.getString("breakdown_time"));
                this.arrivalDistance = Double.parseDouble((rs.getString("arrival_distance")));
                this.arrivedDistance = Double.parseDouble((rs.getString("arrived_distance")));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // dbCon=null;
            try{
                rs.close();
                stmt.close();
            }catch(Exception e){}
            stmt = null;
            rs = null;
        }

    }

    public boolean insertAlertMessage(String studentid, String userid, String messageType, String uuid) {
        boolean flag = false;
        Statement stmt = null;
        String messageText = "";
        try {
            if ("halt".equals(messageType)) {
                messageText = "Bust halt";
            }
            if ("breakdown".equals(messageType)) {
                messageText = "Bus breakdown";
            }
            if ("arrival".equals(messageType)) {
                messageText = "Bus about to arrive";
            }
            if ("arrived".equals(messageType)) {
                messageText = "Bus arrived";
            }
            if ("departure".equals(messageType)) {
                messageText = "Bus departed";
            }
            query = "insert into alert (student_id,user_id,add_text,time_created,alert_date_time,version,uuid) values (" + studentid + "," + userid + ",'" + messageText + "',now(),now(),1,'" + uuid + "')";
            System.out.println("query for users List: " + query);
            stmt = dbCon.prepareStatement(query);
            stmt.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                stmt.close();
            }catch(Exception e){}
            stmt = null;
        }
        return flag;
    }

    public static String getDayName(int day) {
        switch (day) {
            case 1:
                return "Sun";
            case 2:
                return "Mon";
            case 3:
                return "Tue";
            case 4:
                return "Wed";
            case 5:
                return "Thu";
            case 6:
                return "Fri";
            case 7:
                return "Sat";
        }

        return "Worng Day";
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        String matrixURL = "https://maps.googleapis.com/maps/api/distancematrix/json?key=AIzaSyB0w0tD41WTssddua5uvJWmtxAnBT2N7zw";

        URL url;
        String matrixParam = "";
        JSONObject jsonRespRouteDistance = null;
        String distance = "0";
        String time = "";
        HttpsURLConnection con;

        try {

            matrixParam += "&origins=" + lat1 + "," + lon1;
            matrixParam += "&destinations=" + lat2 + "," + lon2;
            System.out.println("Matrix Params:: "+matrixParam);
            url = new URL(matrixURL + matrixParam);

            con = (HttpsURLConnection) url.openConnection();

            String matrixResponse = getGMapMatrixResponse(con);

            jsonRespRouteDistance = new JSONObject(matrixResponse)
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0);

            distance = jsonRespRouteDistance.getJSONObject("distance").get("value").toString();
            time = jsonRespRouteDistance.getJSONObject("duration").get("text").toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            con = null;
            url=null;
            jsonRespRouteDistance = null;
        }

        /*
         double theta = lon1 - lon2;
         double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
         dist = Math.acos(dist);
         dist = rad2deg(dist);
         dist = dist * 60 * 1.1515;
         if (unit == "K") {
         dist = dist * 1.609344;
         } else if (unit == "N") {
         dist = dist * 0.8684;
         }

         return (dist);
         */
        if (unit == "K") {
            distance = (Double.parseDouble(distance) / 1000) + "";
        }
        System.out.println("Distance:: "+distance+"  ETA:: "+time);
        
        return Double.parseDouble(distance);
    }

    public static String getGMapMatrixResponse(HttpsURLConnection con) {
        String response = "";
        BufferedReader br = null;
        try {

            //System.out.println("****** Content of the URL ********");
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String input;
            while ((input = br.readLine()) != null) {
                response += input;
                //System.out.println(input);
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                br.close();
                br = null;
            } catch (IOException e) {}
        }

        return response;

    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
 /*::	This function converts decimal degrees to radians						 :*/
 /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
 /*::	This function converts radians to decimal degrees						 :*/
 /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
