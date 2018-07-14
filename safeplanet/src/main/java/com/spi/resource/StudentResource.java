/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.resource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.stereotype.Component;

import com.spi.VM.ShortSchoolVM;
import com.spi.VM.ShortStudentVM;
import com.spi.VM.StudentVM;
import com.spi.VM.WayPointVM;
import com.spi.config.ApplicationConfig;
import com.spi.domain.SchoolClassSectionType;
import com.spi.domain.SchoolClassType;
import com.spi.domain.Student;
import com.spi.service.StudentService;
import com.spi.user.api.ExternalUser;
import com.spi.user.api.StudentRequest;
import com.spi.user.api.StudentRouteModel;

@Path("/students")
@Component
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class StudentResource {

	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	protected StudentService studentService;

	@Context
	protected UriInfo uriInfo;

	@Autowired
	ApplicationConfig config;

	public StudentResource() {
	}

	@Autowired
	public StudentResource(ConnectionFactoryLocator connectionFactoryLocator) {
		this.connectionFactoryLocator = connectionFactoryLocator;
	}

	@RolesAllowed({ "authenticated", "teacher", "administrator" })
	@Path("getStudentReport/{userId}/{selectDate}")
	@GET
	public Response getStudentReport(@Context SecurityContext sc, @PathParam("userId") String userId,
			@PathParam("selectDate") String selectDate) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy:MM:dd");
		Map<String, List<StudentVM>> studentList = studentService.getStudentReport(userId, df.parse(selectDate));
		return Response.ok().entity(studentList).build();
	}

	@RolesAllowed({ "authenticated", "teacher", "administrator" })
	@Path("getTeacherStudentList/{userId}")
	@GET
	public Response getTeacherStudentList(@Context SecurityContext sc, @PathParam("userId") String userId) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();

		long routeId = userMakingRequest.getRouteId();
		List<StudentVM> studentList = studentService.getTeacherStudentList(userId, routeId,true);
		return Response.ok().entity(studentList).build();
	}

	@RolesAllowed({  "teacher" })
	@Path("getTeacherStudentListByRouteId/{routeId}/{isMorning}")
	@GET
	public Response getTeacherStudentListByRouteId(@Context SecurityContext sc, @PathParam("routeId") Long routeId,@PathParam("isMorning") Boolean isMorning) {
		ExternalUser userMakingRequest = (ExternalUser) sc.getUserPrincipal();
		List<StudentVM> studentList = studentService.getTeacherStudentList(userMakingRequest.getId(), routeId,isMorning);
		return Response.ok().entity(studentList).build();
	}
	
	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("getStudentDetailList/{userId}")
	@GET
	public Response getStudentDetails(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<Student> studentList = studentService.getStudentDetails(userId);
		List<StudentVM> studentVMList = studentService.transformToStudentVM(studentList);
		return Response.ok().entity(studentVMList).build();
	}

	@RolesAllowed({ "administrator", "teacher" })
	@Path("students/{userId}/{fleetId}")
	@GET
	public Response getStudentsByFleet(@Context SecurityContext sc, @PathParam("userId") String userId, @PathParam("fleetId") long fleetId) {
		List<StudentVM> studentList = studentService.getStudentDetailsByFleet(fleetId);
		return Response.ok().entity(studentList).build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("markPresence/{userId}")
	@PUT
	public Response markPresence(@Context SecurityContext sc, @PathParam("userId") String userId, StudentRequest student) {

		String presence = "";

		if (student != null && student.getPresentFlag() == 'I') {
			presence = "Teacher marked In";
		} else if (student != null && student.getPresentFlag() == 'A') {
			presence = "Teacher marked Absent";
		}
		else if (student != null && student.getPresentFlag() == 'O') {
			presence = "Teacher marked Out";
		}
		student.setMessage(presence);

		studentService.updateStudentPresence(student,student.getStudentId());

		return Response.ok().build();
	}

	@RolesAllowed({ "administrator" })
	@GET
	@Path("getStudentRouteDetail/{userId}/{studentId}")
	public Response getStudentRouteDetail(@Context SecurityContext sc, @PathParam("userId") String userId,
			@PathParam("studentId") String studentId) {
		StudentRouteModel route = studentService.getStudentRouteDetail(Long.valueOf(studentId));
		return Response.ok().entity(route).build();
	}

	@RolesAllowed({ "administrator" })
	@GET
	@Path("getStudentsInSchool/{userId}")
	public Response getStudentsInSchool(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<ShortStudentVM> studentVM = studentService.getStudentUnassignedWithParent(userId);
		return Response.ok().entity(studentVM).build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher","gateKeeper" })
	@Path("getStudentClass/{userId}")
	@GET
	public Response getStudentClass(@Context SecurityContext sc, @PathParam("userId") String userId) {

		return Response.ok().entity(new SchoolClassType().getSchoolClass()).build();
	}
	@RolesAllowed({ "authenticated", "administrator", "teacher","gateKeeper" })
	@Path("getStudentByClass/{userId}/{className}")
	@GET
	public Response getStudentByClass(@Context SecurityContext sc, @PathParam("userId") String userId, @PathParam("className") String className) {

    List<StudentVM> student=studentService.getStudentListByClass(userId, className);
    System.out.println(student);
		return Response.ok().entity(student).build();
	}

	@RolesAllowed({ "authenticated", "administrator", "teacher" })
	@Path("getStudentSection/{userId}")
	@GET
	public Response getStudentSection(@Context SecurityContext sc, @PathParam("userId") String userId) {

		return Response.ok().entity(new SchoolClassSectionType().getSection()).build();
	}

	@RolesAllowed({ "administrator" })
	@Path("getAllStudentDetailsAdmin/{userId}")
	@GET
	public Response getAllStudentDetailsAdmin(@Context SecurityContext sc, @Context HttpServletRequest httpRequest,
			@PathParam("userId") String userId) {

		List<StudentVM> studentVMList = studentService.getAllStudentDetailsAdmin(userId);

		httpRequest.getSession().setAttribute("STUDENT_REPORT_DATA", studentVMList);
		return Response.ok().entity(studentVMList).build();
	}
	
	@RolesAllowed({ "administrator", "superAdmin" })
	@GET
	@Path("getUnassignedSchool/{userId}")
	public Response getUnassignedSchool(@Context SecurityContext sc, @PathParam("userId") String userId) {
		List<ShortSchoolVM> studentVM = studentService.getUnassignedSchool(userId);
		return Response.ok().entity(studentVM).build();
	}
	
	@RolesAllowed({ "administrator"})
	@GET
	@Path("getStudentRouteList/{userId}/{routeId}")
	public Response getStudentRouteList(@Context SecurityContext sc, @PathParam("userId") String userId,@PathParam("routeId") String routeId) {
		List<StudentVM> studentList = studentService.getStudentRouteList(routeId);
		
		return Response.ok().entity(studentList).build();
	}
	@RolesAllowed({ "administrator" })
	@GET
	@Path("getStudentsByWayPoint/{userId}/{id}")
	public Response getStudentsByWayPoint(@Context SecurityContext sc, @PathParam("userId") String userId, @PathParam("id") long waypointId) {
		List<StudentVM> wayPointStudentsList = studentService.getStudentsByWayPoint(waypointId);
		return Response.ok().entity(wayPointStudentsList).build();
	}

}
