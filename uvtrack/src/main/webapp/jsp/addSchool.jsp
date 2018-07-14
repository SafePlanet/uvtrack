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
    private UserService userService;

    @Autowired
    private StatesService stateService;
%>

<%


String schoolID = request.getParameter("schoolID");
School school = null;
if(schoolID!=null && schoolID.length()>0)
{
    school = userService.getSchoolDetailById(Long.valueOf(schoolID));
}
if(school==null)
{
    school = new School();
}

String name = request.getParameter("name");
String emailId = request.getParameter("emailId");
String displayAddress = request.getParameter("displayAddress");
String phoneNumber1 = request.getParameter("phoneNumber1");
String phoneNumber2 = request.getParameter("phoneNumber2");
String phoneNumber3 = request.getParameter("phoneNumber3");
String subType=request.getParameter("subType");
String subAmount=request.getParameter("subAmount");
String startDate=request.getParameter("startDate");
String endDate=request.getParameter("endDate");
String addressLine1 = request.getParameter("addressLine1");
String addressLine2 = request.getParameter("addressLine2");
String city = request.getParameter("selectedCity");
String selectedState = request.getParameter("selectedState");
String pinCode = request.getParameter("pinCode");

//System.out.println("Request Param School name==> "+request.getParameter("name"));

State state=null;
if(selectedState != null && selectedState.length()>0){
	state=stateService.getStateById(Long.valueOf(selectedState));
}
school.setName(name);
school.setEmailId(emailId);
school.setDisplayAddress(displayAddress);
school.setPhoneNumber1(phoneNumber1);
school.setPhoneNumber2(phoneNumber2);
school.setPhoneNumber3(phoneNumber3);
school.setSubType(subType);
school.setSubAmount(subAmount);
school.setServiceStartDate(new Date(startDate));
school.setServiceEndDate(new Date(endDate));
Address address = school.getAddress();
if(address == null)
{
    address = new Address();
    school.setAddress(address);
    address.setSchool(school);
}
address.setAddressLine1(addressLine1);
address.setAddressLine2(addressLine2);
address.setCity(city);
address.setState(state.getStateName());
address.setPinCode(pinCode);


if(schoolID==null){
userService.addSchool(school);
}else{
	userService.updateSchool(school);
}


%>