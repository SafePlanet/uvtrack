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

List<Route> routeList = routeService.findAllRouteList((String)session.getAttribute("USER_ID"));

String routeUUID = request.getParameter("routeUUID");
String routeId=request.getParameter("routeId");
String fleetId=request.getParameter("selectedFleet");
String routeName = request.getParameter("routeName");
String description = request.getParameter("description");
String geometry = request.getParameter("geometry");
String userUuid = (String)session.getAttribute("USER_ID");
Fleet fleet=null;
if(fleetId!=null && fleetId.length()>0){
	fleet=routeService.findFleetById(Long.valueOf(fleetId));
}else{
	fleet=routeService.findFleetByRouteId(Long.valueOf(routeId));
}

//System.out.println("Request Param routeName==> "+request.getParameter("routeName"));

routeService.addRoute(routeName, description, geometry, userUuid, routeUUID ,fleet);


%>