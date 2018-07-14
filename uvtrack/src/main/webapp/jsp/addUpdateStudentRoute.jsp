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

    @Autowired
    private StudentService studentService;

%>

<%

List<Route> routeList = routeService.findAllRouteList((String)session.getAttribute("USER_ID"));

List<School> schoolList = routeService.findAllSchoolList();

String studentRouteXREFId = request.getParameter("studentRouteXREFId");

String studentId = request.getParameter("studentId");
String schoolId = request.getParameter("schoolId");
String routeId = request.getParameter("routeId");
String wayPointId = request.getParameter("wayPointId");

//System.out.println("Request Param studentId==> "+request.getParameter("studentId"));

//studentService.updateStudentRouteDetail(Long.valueOf(studentId), Long.valueOf(routeId), Long.valueOf(wayPointId), Long.valueOf(schoolId));
//routeService.addRoute(routeName, description, geometry, school, routeUUID);




%>