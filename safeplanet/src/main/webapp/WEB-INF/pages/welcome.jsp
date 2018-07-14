<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<ul class="sidebar-menu" style="max-height: 480px; ">
	<li class="active"><a href="#/home.html"> <i class="ion-home"></i> <span>Home</span>
	</a></li>

	<c:if test="${it.role =='authenticated' || it.role =='teacher'}">
		<c:if test="${it.role =='authenticated'}">

			<li class="treeview"><a href="#student.html"> <i class="ion-android-contacts"></i> <span>Student Details</span> <i class="fa fa-angle-left pull-right"></i>
			</a>
				<ul class="treeview-menu">
					<li><a href="#addstudent.html"><i class="fa fa-angle-double-right"></i> Add Students</a></li>
					<li><a href="#student.html"><i class="fa fa-angle-double-right"></i> Student List</a></li>
				</ul></li>
		</c:if>

		<c:if test="${it.role =='teacher'}">
			<li><a href="#teacherView.html"><i class="ion-ios-time-outline"></i> Attendance</a></li>
			<li><a href="#studentReport.html"><i class="ion-ios-monitor-outline"></i> Attendance Report</a></li>
		</c:if>

		<c:if test="${it.role =='authenticated'}">
			<li><a href="#busLocation.html"> <i class="ion-android-bus"></i> <span>Bus Location</span>
			</a></li>
			<li><a href="#complaintMessage.html"> <i class="ion-compose"></i> <span>Complaint/Feedback</span>
			</a></li>
			<li><a href="#schoolInfo.html"> <i class="ion-university"></i> <span>School Information</span>
			</a></li>
		</c:if>
		<c:if test="${it.role =='teacher'}">
			<li><a href="#broadcastMessage.html"> <i class="ion-compose"></i> <span>Broadcast Message</span>
			</a></li>
		</c:if>

		<li><a href="#settings.html"> <i class="ion-gear-a"></i> <span>Settings</span>
		</a></li>

	</c:if>

	<c:if test="${it.role =='administrator'}">
		<li><a href="http://52.37.103.196:8082"> <i class="ion-person-stalker"></i> <span>Vehicle Track</span></a></li>
		<li><a href="#studentReport.html"> <i class="ion-ios-monitor-outline"></i> <span>Attendance Report</span></a></li>
		<li><a href="#SendSms.html"> <i class="ion-ios-monitor-outline"></i> <span>Send Message</span></a></li>
	
	<c:if test="${it.app != true}">
		<li class="treeview">
				<a href="#manageRoutes.html"> <i class="ion-map"></i> <span>Manage Bus/Routes</span> <i class="fa fa-angle-left pull-right"></i>
				</a>
				<ul class="treeview-menu">
					<li><a href="#manageRoutes.html"><i class="fa fa-angle-double-right"></i>Manage Routes</a></li>
					
						<li><a href="#addStudentRoute.html"><i class="fa fa-angle-double-right"></i>Student Routes</a></li>
						<li><a href="#manageBus.html"><i class="fa fa-angle-double-right"></i>Manage Bus</a></li>
						<li><a href="#manageBusRoute.html"><i class="fa fa-angle-double-right"></i>Bus/Device Routes</a></li>
					
				</ul>
			</li>
	</c:if>
		
		<c:if test="${it.app != true}">
			<li><a href="#manageBus.html"> <i class="ion-android-bus"></i> <span>Manage Bus</span></a></li>
			<li><a href="#manageSchool.html"> <i class="ion-university"></i> <span>Manage Schools</span></a></li>
			<li><a href="#manageStudents.html"> <i class="ion-android-contacts"></i> <span>Manage Students</span></a></li>
			<li><a href="#manageUsers.html"> <i class="ion-person-stalker"></i> <span>Manage Users</span></a></li>
		</c:if>
		<c:if test="${it.app != true}">
		<li class="treeview"><a href="#reports.html"> <i class="ion-ios-speedometer-outline"></i> <span>Reports</span> <i class="fa fa-angle-left pull-right"></i>
		</a>
			<ul class="treeview-menu">
				<li><a href="#positionReport.html"><i class="fa fa-angle-double-right"></i>Position Summary</a></li>
				<li><a href="#fleetReport.html"><i class="fa fa-angle-double-right"></i>Fleet Summary</a></li>
				<li><a href="#haltReport.html"><i class="fa fa-angle-double-right"></i>Long Halt Report</a></li>
				<li><a href="#overSpeedingReport.html"><i class="fa fa-angle-double-right"></i>Over Speeding</a></li>
				<li><a href="#busStopTimeReport.html"><i class="fa fa-angle-double-right"></i>Bus Stop Time</a></li>
				<li><a href="#studentStopTimeReport.html"><i class="fa fa-angle-double-right"></i>Student Stop</a></li>
				<li><a href="#tripSummaryReport.html"><i class="fa fa-angle-double-right"></i>Trip Summary</a></li>
			</ul></li>
		</c:if>
	</c:if>
	<li><a href="#changePassword.html"> <i class="ion-edit"></i> <span>Change Password</span>
	</a></li>

	<li><a href="#editProfile.html"> <i class="ion-edit"></i> <span>Profile</span>
	</a></li>


	<li><a href="#" ng-click="logout()"> <i class="ion-power"></i> <span>Sign Out</span>
	</a></li>
</ul>
