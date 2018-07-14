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
    private UserService userService;

    @Autowired
    private StudentService studentService;

%>

<%

String studentUUID = request.getParameter("studentUUID");
String action = request.getParameter("action");

//System.out.println("Request Param studentUUID==> "+request.getParameter("studentUUID"));

studentService.approveStudent(studentUUID, action);


%>