package com.spi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.codehaus.jettison.json.JSONObject;

public class DistanceUtils {
	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		if(lat1 == lat2 && lon1 == lon2) return 0;
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		//unit  = "K"
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}
		dist = dist * 1.15;
		
	// System.out.println("Distance:: "+dist+"  ETA:: "+time);
	//double dd = 	distanceORIG( lat1,  lon1,  lat2,  lon2, "K");
		return (dist);
	}
    
    public static double distanceOrig(double lat1, double lon1, double lat2, double lon2, String unit) {

    	String matrixURL = "https://maps.googleapis.com/maps/api/distancematrix/json?key=AIzaSyCbJTpsoGXzOqoyuujqXXKG6eDHBfPhd88";
//      AIzaSyCbJTpsoGXzOqoyuujqXXKG6eDHBfPhd88
        URL url;
        String matrixParam = "";
        JSONObject jsonRespRouteDistance = null;
        String distance = "0";
        String time = "";
        HttpsURLConnection con;

        try {
            matrixParam += "&origins=" + lat1 + "," + lon1;
            matrixParam += "&destinations=" + lat2 + "," + lon2;
//            System.out.println("Matrix Params:: "+matrixParam);
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

        if (unit == "K") {
            distance = (Double.parseDouble(distance) / 1000) + "";
        }
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

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
