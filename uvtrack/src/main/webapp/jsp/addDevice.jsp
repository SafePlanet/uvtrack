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


String deviceId = request.getParameter("deviceId");
Devices device = null;
if(deviceId!=null && deviceId.length()>0)
{
    device = routeService.findByDeviceId(Long.valueOf(deviceId));
}
if(device==null)
{
    device = new Devices();
}

String ManfDate = request.getParameter("manfDate");
String deviceModelId = request.getParameter("deviceModelId");
String uniqueid = request.getParameter("uniqueid");
String deviceType = request.getParameter("deviceType");
String frqMode = request.getParameter("frqMode");
String frqUpdateDist = request.getParameter("frqUpdateDist");
String frqUpdateTime = request.getParameter("frqUpdateTime");
String protocol = request.getParameter("protocol");
String puchaseDate = request.getParameter("puchaseDate");
String warrantyDate = request.getParameter("warrantyDate");
String serviceEndDate = request.getParameter("serviceEndDate");
System.out.println(serviceEndDate);
String name=request.getParameter("name");
String uuid=(String)session.getAttribute("USER_ID");
String mobileNumber=request.getParameter("mobileNumber");
String schoolId=request.getParameter("schoolId");
System.out.println("schoolId:-"+schoolId);

try{
device.setManfDate(new Date(ManfDate));
}catch(Exception e){}
 	if(deviceModelId!=null && deviceModelId.length()>0)
    {
    	Long _deviceModelId = Long.valueOf(deviceModelId);
    	device.setDeviceModelType(new DeviceModelType(_deviceModelId));    
    }
//device.setDeviceSerialNo(deviceSerialNo);
device.setDeviceType(deviceType);
device.setFrqMode("T");
device.setName(name);
	if(uniqueid!=null && uniqueid.length()>0)
    {
    	Long _uniqueid = Long.valueOf(uniqueid);
    	device.setUniqueid(_uniqueid);    
    }
    
    if(mobileNumber!=null && mobileNumber.length()>0)
    {
    	Long _mobileNumber = Long.valueOf(mobileNumber);
    	device.setMobile(_mobileNumber);    
    }
//try{
    device.setFrqUpdateDist(0);
//}catch(Exception e){}

try{
    device.setFrqUpdateTime(Integer.parseInt(frqUpdateTime));
}catch(Exception e){} 

device.setProtocol(protocol);
device.setVoidInd(false);

try{
    device.setPuchaseDate(new Date(puchaseDate));
}catch(Exception e){}
try{
    device.setServiceEndDate(new Date(serviceEndDate));
}catch(Exception e){}
try{
    device.setWarrantyDate(new Date(warrantyDate));
}catch(Exception e){}
Long school = null;
if(schoolId!=null) school = Long.parseLong(schoolId);
routeService.addUpdateDevice(device,uuid,school);

%>
