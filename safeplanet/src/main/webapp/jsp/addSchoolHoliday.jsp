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

%>

<%

String schoolId = request.getParameter("schoolId");
String holidayId = request.getParameter("holidayId");
School school = null;
SchoolHoliday holiday = null;
if(schoolId!=null && schoolId.length()>0)
{
    school = userService.getSchoolDetailById(Long.valueOf(schoolId));
    if(holidayId!=null && holidayId.length()>0)
    {
    Long _holidayId = Long.valueOf(holidayId);
    
    for(SchoolHoliday h : school.getHolidays())
    {
    if(h.getId()==_holidayId)
    {
    holiday= h;
    }
    }
    
    }
}
if(holiday==null)
{
    holiday = new SchoolHoliday();
    school.getHolidays().add(holiday);
}

String fromDate = request.getParameter("fromDate");
String toDate = request.getParameter("toDate");
String description = request.getParameter("description");
String sjaFlag = request.getParameter("sjaFlag");

holiday.setFromDate(new Date(fromDate));
holiday.setToDate(new Date(toDate));
holiday.setDescription(description);
holiday.setSjaFlag(sjaFlag);

userService.addSchool(school);


%>