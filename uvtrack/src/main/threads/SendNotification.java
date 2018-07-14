package com.spi.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.spi.service.NotificationThread;

public class SendNotification implements Runnable {

   String dbURL = "jdbc:mysql://localhost:3306/test";
    String username = "root";
    String password = "";
    String query = "";
    String busDirection = null;
    Date busUpStartTime;
    Date busUpEndTime;
    Date busdownStartTime;
    Date busdownEndTime;
    Connection dbCon = null;

    public static void main(String[] args) {
        System.out.println("This is currently running on the main thread, "
                + "the id is: " + Thread.currentThread().getId());
        SendNotification th1 = new SendNotification();
        Thread thread = new Thread(th1);
        thread.start();
    }

    public void run() {

        Statement stmt = null;
        ResultSet rs = null;
        ArrayList routList = new ArrayList();
        query = "SELECT r.* FROM route r, route_fleet_device_xref rfdx WHERE r.id=rfdx.route_id AND device_id IS NOT NULL AND device_id!=''";
        try { //getting database connection to MySQL server 
            dbCon = DriverManager.getConnection(dbURL, username, password);

            stmt = dbCon.prepareStatement(query);

            rs = stmt.executeQuery(query);
            while (rs.next()) {
                routList.add(rs.getString("id"));
                System.out.println("routList List1111: " + routList);
            }

            NotificationThread[] nThread = new NotificationThread[routList.size()];
            if (routList != null && routList.size() > 0) {
                for (int i = 0; routList.size() > i; i++) {
                    if (checkScheduleTime((String) routList.get(i))) {
                        nThread[i] = new NotificationThread((String) routList.get(i), busDirection, busUpStartTime, busUpEndTime, busdownStartTime, busdownEndTime);
                        nThread[i].start();
                    } else {
                        System.out.println("Thread is not process for route: " + routList.get(i) + " because time is not schedule for this route ...............");
                    }
                    // System.out.println(busDirection+busUpStartTime+busUpEndTime+busdownStartTime+busdownEndTime);
                    busDirection = null;
                    busUpStartTime = null;
                    busUpEndTime = null;
                    busdownStartTime = null;
                    busdownEndTime = null;

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon = null;
            stmt = null;
            rs = null;
        }
    }

    public boolean checkScheduleTime(String routeid) {

        Statement stmt = null;
        ResultSet rs = null;
        boolean isschedule = false;
        query = "select min(upwart_time) as upwart_starttime,max(upwart_time) as upwart_endtime,min(down_time) as down_starttime,max(down_time) as down_endtime from route_schedule_detais where route_id='" + routeid + "' ";
        try { //getting database connection to MySQL server 
            // dbCon = DriverManager.getConnection(dbURL, username, password); 
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
            String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
            Date currentTimedate = null;
            try {
                currentTimedate = parser.parse(currentTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            stmt = dbCon.prepareStatement(query);

            rs = stmt.executeQuery(query);
            while (rs.next()) {

                try {
                    Date upwart_starttime = parser.parse(rs.getString("upwart_starttime"));
                    Date upwart_endtime = parser.parse(rs.getString("upwart_endtime"));
                    Date down_starttime = parser.parse(rs.getString("down_starttime"));
                    Date down_endtime = parser.parse(rs.getString("down_endtime"));

                    upwart_starttime.setMinutes(-30);
                    upwart_endtime.setMinutes(30);
                    down_starttime.setMinutes(-30);
                    down_endtime.setMinutes(30);

                    if ((upwart_starttime.equals(currentTimedate) || upwart_starttime.before(currentTimedate)) && (upwart_endtime.equals(currentTimedate) || upwart_endtime.after(currentTimedate))) {
                        isschedule = true;
                        busDirection = "up";
                        busUpStartTime = upwart_starttime;
                        busUpEndTime = upwart_endtime;
                    }

                    if ((down_starttime.equals(currentTimedate) || down_starttime.before(currentTimedate)) && (down_endtime.equals(currentTimedate) || down_endtime.after(currentTimedate))) {
                        isschedule = true;
                        busDirection = "down";
                        busdownStartTime = down_starttime;
                        busdownEndTime = down_endtime;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // dbCon=null;
            stmt = null;
            rs = null;
        }
        return isschedule;
    }
}
