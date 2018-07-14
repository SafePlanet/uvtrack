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


String fleetID = request.getParameter("fleetID");
Fleet fleet = null;
if(fleetID!=null && fleetID.length()>0)
{
    fleet = routeService.findFleetById(Long.valueOf(fleetID));
}
if(fleet==null)
{
    fleet = new Fleet();
}
String deviceID=request.getParameter("selectedDeviceId");
Devices device=null;

if(deviceID!=null && deviceID.length()>0)
{
    device = routeService.findByDeviceId(Long.valueOf(deviceID));
}else{
	device=routeService.findDeviceByFleetId(Long.valueOf(fleetID));
}

String regNumber = request.getParameter("regNumber");
String ownerName = request.getParameter("ownerName");
String ownerMobile = request.getParameter("ownerMobile");
String driverName = request.getParameter("driverName");
String driverMobile = request.getParameter("driverMobile");
String navigatorName = request.getParameter("navigatorName");
String navigatorMobile = request.getParameter("navigatorMobile");
String conductor2Name = request.getParameter("conductor2Name");
String conductor2Mobile = request.getParameter("conductor2Mobile");
String fleetModel = request.getParameter("fleetModel");
String fleetMake = request.getParameter("fleetMake");
String fleetType = request.getParameter("fleetType");

//System.out.println("Request Param regNumber==> "+request.getParameter("regNumber"));

fleet.setRegNumber(regNumber);
fleet.setDriverName(driverName);
fleet.setNavigatorName(navigatorName);
fleet.setFleetType(fleetType);
fleet.setFleetModel(fleetModel);
fleet.setFleetMake(new Date(request.getParameter("fleetMake")));
fleet.setOwnerName(ownerName);
fleet.setNavigatorMobile(navigatorMobile);
fleet.setDriverMobile(driverMobile);
fleet.setOwnerMobile(ownerMobile);
fleet.setConductor2Name(conductor2Name);
fleet.setConductor2Mobile(conductor2Mobile);


routeService.addUpdateFleet(fleet ,device);


%>