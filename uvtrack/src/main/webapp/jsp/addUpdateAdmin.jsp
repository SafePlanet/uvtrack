<%@page import="com.spi.VM.ShortSchoolVM"%>
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
    private StatesService stateService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private UserService userService;

%>

<%

String userUUID = request.getParameter("userUUID");
String action = request.getParameter("action");
String lastName = request.getParameter("lastName");
String firstName = request.getParameter("firstName");
String houseNo = request.getParameter("houseNo");
String address = request.getParameter("address");
String pinCode = request.getParameter("pinCode");
String city = request.getParameter("selectedCity");
String selectedState = request.getParameter("selectedState");
String mobile = request.getParameter("mobile");
String emailAddress = request.getParameter("emailAddress");
String selectedSchoolId = request.getParameter("selectedSchool");
State state=null;
if(selectedState != null && selectedState.length()>0){
	state=stateService.getStateById(Long.valueOf(selectedState));
}

if(null!=action && ("enable".equals(action)|| "disable".equals(action)))
	userService.deactivateUser(userUUID, action);
else 
	userService.addAdmin(lastName, firstName, houseNo, address, pinCode, city, state, mobile, emailAddress, userUUID, selectedSchoolId);



%>