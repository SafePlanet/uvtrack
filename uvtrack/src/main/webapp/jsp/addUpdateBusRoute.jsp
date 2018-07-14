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

String xRefID = request.getParameter("xRefID");

String fleetID = request.getParameter("fleetID");
String routeID = request.getParameter("routeID");
//String deviceID = request.getParameter("deviceID");

Fleet fleet = routeService.findFleetById(Long.valueOf(fleetID));
Route route = routeService.findByRouteId(Long.valueOf(routeID));
//Devices device = routeService.findByDeviceId(Long.valueOf(deviceID));

RouteFleetDeviceXREF xRefDomain = new RouteFleetDeviceXREF();
if(!(xRefID==null || "-1".equals(xRefID)))
{
    xRefDomain = routeService.findByRFDXrefId(Long.valueOf(xRefID));
}

//System.out.println("Request Param xRefID==> "+request.getParameter("xRefID"));

routeService.addUpdateRouteFleetDeviceXref(xRefDomain ,fleet,route);


%>
