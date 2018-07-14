<%@page language="java" contentType="application/json;charset=UTF-8" %>
<%@page import="java.util.*"%>
<%@page import="com.spi.dao.*"%>
<%@page import="com.spi.domain.*"%>
<%@page import="com.spi.service.*"%>
<%@page import="org.springframework.beans.factory.annotation.*"%>
<%@page import="org.springframework.web.context.support.*"%>
<%@page import="org.springframework.context.*"%>
<%@page import="org.codehaus.jettison.json.*"%>
 
<%!
    public void jspInit() 
    {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
    }

    @Autowired
    private RouteService routeService;

%>

<%

String wayPointUUID = request.getParameter("wayPointUUID");
String routeUUID = request.getParameter("routeUUID");

String name = request.getParameter("name");
String lattitude = request.getParameter("lattitude");
String longitude = request.getParameter("longitude");
String sequenceNumber = request.getParameter("sequenceNumber");
String altitude = request.getParameter("altitude");
String picktime = request.getParameter("picktime");
String droptime = request.getParameter("droptime");


//System.out.println("Request Param WayPoint Name==> "+request.getParameter("name"));
//System.out.println("Request Param routeUUID==> "+request.getParameter("routeUUID"));

routeService.addWayPoint(name, lattitude, longitude, sequenceNumber, altitude, wayPointUUID, routeUUID, picktime, droptime);


%>