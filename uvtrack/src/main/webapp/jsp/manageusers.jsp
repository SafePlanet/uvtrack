<%@page import="java.util.*"%>
<%@page import="com.spi.dao.*"%>
<%@page import="com.spi.domain.*"%>
<%@page import="com.spi.service.*"%>
<%@page import="com.spi.VM.*"%>
<%@page import="org.springframework.beans.factory.annotation.*"%>
<%@page import="org.springframework.web.context.support.*"%>
<%@page import="org.springframework.context.*"%>

<%!public void jspInit() {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
	}

	@Autowired
	private RouteService routeService;

	@Autowired
	private UserService userService;%>

<%
	List<UserVM> userList = userService.getAllUsersAdmin((String) session.getAttribute("USER_ID"));

	//List<Route> routeList = routeService.findAllRouteList();

	//List<School> schoolList = routeService.findAllSchoolList();
	//System.out.println("User List:: "+userList);
%>

<style>
ul#menu {
	padding: 0;
	margin-right: 10px;
}

ul#menu li {
	display: inline;
}

ul#menu li a {
	background-color: #3CA4D8;
	color: white;
	padding: 5px 12px;
	text-decoration: none;
	border-radius: 4px 4px 0 0;
}

ul#menu li a:hover {
	background-color: grey;
}
</style>

<div class="col-md-12 col-part-left" style="max-height: 500px; overflow-y: auto;">
	<div class="box box-primary">
		<div class="box-header">
			<h3 class="box-title">Manage Parents</h3>
			<div class="pull-right addbutton">
				<ul id="menu">
					<button href="#addUserModal" data-toggle="modal" ng-click="initAddUser()" class="btn btn-primary form-control">Add Parents</button>
				</ul>
			</div>
			<div class="form-group" ng-show="successTextAlert || errorTextAlert">
				<div class="col-md-12 col-xs-12 notification-center">
					<div class="alert" ng-class="{'alert-success': successTextAlert, 'alert-danger' : errorTextAlert}" ng-show="successTextAlert || errorTextAlert">
						<button type="button" class="close" ng-click="closeNotification()">x</button>
						<strong>{{successTextAlert}}{{errorTextAlert}}</strong>
					</div>
				</div>
			</div>			
		</div>
		<div class="box-body">
			<table id="dataTableGrid" class="table table-bordered table-striped dt-responsive nowrap">
				<thead>
					<tr>
						<th>Route</th>
						<th>User Name</th>
						<th>Kid Name</th>
						<th>Address</th>
						<th>Phone</th>
						<th>Email</th>
						<%--<th>Type</th>--%>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
					<%
						for (UserVM user : userList) {
					%>
					<tr>
						<td><%=user.getRouteName()%></td>
						<td><%=user.getName()%></td>
						<td><%=user.getStudentName()%></td>
						<td><%=user.getAddress()%></td>
						<td><%=user.getMobileNumber()%></td>
						<td><%=user.getEmailAddress()%></td>
						<td><a href="javascript:void(0);" ng-click="editUserForm('<%=user.getIdentifier()%>')">
								<i class="ion-edit"></i> Edit
							</a> &nbsp;|&nbsp; <a href="javascript:void(0);" ng-click="deactivateUser('<%=user.getIdentifier()%>','<%=(user.getIsEnable() == 0) ? "enable" : "disable"%>')">
								<i class="ion-edit"></i>
								<%=(user.getIsEnable() == 0) ? "Activate" : "Deactivate"%></a></td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>

			<div id="editUserModal" class="modal fade" tabindex="-1" style="display: none;">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">Edit Parents</h4>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-6">
							<form name="userForm" ng-submit="submitUserForm()">
								<div class="form-group">
									<label>First Name</label>
									<input type="text" class="form-control" ng-required="true" placeholder="First Name" ng-model="userData.firstName " />
									<label>Last Name</label>
									<input type="text" class="form-control" ng-required="true" placeholder="Last Name" ng-model="userData.lastName " />
									<label>House No</label>
									<input type="text" class="form-control" ng-required="true" placeholder="HouseNo" ng-model="userData.houseNo " />
									<label>Address</label>
									<input type="text" class="form-control" ng-required="true" placeholder="Address" ng-model="userData.address " />
									<label>Pin Code</label>
									<input type="text" numbers-only maxlength="6" class="form-control" ng-required="true" placeholder="pinCode" ng-model="userData.pinCode " />
									<label>City</label>
									<input type="text" class="form-control" ng-required="true" placeholder="City" ng-model="userData.city " />
									<label>State</label>
									<select ng-change="stateChanged()" ng-required="true" class="form-control" ng-model="selectedState" ng-options="state as state.stateName for state in statesList">
										<option value="">Select State</option>
									</select>
									<label>Phone</label>
									<input type="text" numbers-only maxlength="10" class="form-control" placeholder="Phone" ng-required="true" ng-model="userData.mobile " />
									<label>Email </label>
									<input type="email" spi-email class="form-control" ng-required="true" placeholder="Email" ng-model="userData.emailAddress " />
									<input type="hidden" ng-model="userData.userUUID">
									<button type="button" data-dismiss="modal" class="btn btn-inverse">Cancel</button>
									<button type="submit" class="btn btn-primary">Save</button>
								</div>
							</form>
						</div>
					</div>
				</div>
				<div class="modal-footer"></div>
			</div>
		</div>

		<div id="addUserModal" class="modal fade" tabindex="-1" style="display: none;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">Add Parents</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-8 col-xs-12">
						<form name="userForm" ng-submit="addUser()">
							<div class="form-group">
								<label>Student</label>
									<spi-typeahead list="internalList" ng-model="selectedStudents" ng-required="true" items="studentList" displaytag="firstName" displayfirst="firstName" displaylast="lastName"></spi-typeahead>
								<label>First Name</label>
								<input type="text" name="firstName" ng-required="true" ng-disabled="selectedStudents.length == 0" class="form-control" placeholder="First Name" ng-model="userData.firstName" />
								<label>Last Name</label>
								<input type="text" name="lastName" ng-required="true" ng-disabled="selectedStudents.length == 0" class="form-control" placeholder="Last Name" ng-model="userData.lastName" />
								<label>House No</label>
								<input type="text" name="houseNo" ng-required="true" ng-disabled="selectedStudents.length == 0" class="form-control" placeholder="houseNo" ng-model="userData.houseNo" />
								<label>Address</label>
								<input type="text" name="address" ng-required="true" ng-disabled="selectedStudents.length == 0" class="form-control" placeholder="Address" ng-model="userData.address" />
								<label>Pin Code</label>
								<input type="text" numbers-only maxlength="6" name="pinCode" ng-required="true" ng-disabled="selectedStudents.length == 0" class="form-control" placeholder="pinCode" ng-model="userData.pinCode" />
								<label>City</label>
								<input type="text" name="city" ng-required="true" ng-disabled="selectedStudents.length == 0" class="form-control" placeholder="city" ng-model="userData.city" />
								<label>State</label>
								<select ng-change="stateChanged()" ng-required="true" ng-disabled="selectedStudents.length == 0" class="form-control" ng-model="selectedState" ng-options="state as state.stateName for state in statesList">
								<option value="">Select State</option>
								</select>
								<label>Phone</label>
								<input type="text" numbers-only maxlength="10" name="phone" ng-required="true" ng-disabled="selectedStudents.length == 0" class="form-control" placeholder="phone" ng-model="userData.mobile" />
								<label>Email </label>
								<input type="email" spi-email class="form-control" ng-disabled="selectedStudents.length == 0" placeholder="email" ng-model="userData.emailAddress" />
								<input type="hidden" ng-model="userData.userUUID">
								<button type="button" data-dismiss="modal" class="btn btn-inverse">Cancel</button>
								<button type="submit" ng-disabled="selectedStudents.length == 0" class="btn btn-primary">Save</button>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="modal-footer"></div>
		</div>
	</div>

	<div class="box-footer">
		<div class="alert alert-success" ng-show="showSuccessAlert">
			<button type="button" class="close" data-ng-click="switchBool('showSuccessAlert')">x</button>
			<strong></strong>{{successTextAlert}}
		</div>
	</div>

</div>