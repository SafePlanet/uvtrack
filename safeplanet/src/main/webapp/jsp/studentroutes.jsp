<%@page import="java.util.*"%>
<%@page import="com.spi.dao.*"%>
<%@page import="com.spi.domain.*"%>
<%@page import="com.spi.service.*"%>
<%@page import="org.springframework.beans.factory.annotation.*"%>
<%@page import="org.springframework.web.context.support.*"%>
<%@page import="org.springframework.context.*"%>
 
<%!
    public void jspInit() 
    {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
    }

    @Autowired
    private RouteService routeService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

%>

<%

List<School> schoolList = userService.getAllSchoolDetails();

String userId = (String) session.getAttribute("USER_ID");

List<Route> routeList = routeService.findAllRouteList(userId);

List<Student> studentList = studentService.getAllStudentDetails(userId);

//List<School> schoolList = routeService.findAllSchoolList();
//System.out.println(schoolList.get(0).getName()+"  "+schoolList.get(0).getEmailId());


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

<div class="col-md-12 col-part-left" style="max-height:480px; overflow-y:auto; -webkit-overflow-scrolling: touch;">
    <div class="box box-primary">
        <div class="box-header">
            <h3 class="box-title">Manage Student Route </h3>
        </div>
        <div class="pull-right">
            <ul id="menu">
                <li><a href="#manageRoutes.html">Manage Routes</a></li>
                <%--<li><a href="javascript:void(0);" ng-click="addRouteFormModal()">Add Route</a></li>--%>
                <li><a href="#manageBusRoute.html">Bus Route</a></li>
            </ul>
        </div>
        
            <div class="box-body">
                 <table id="dataTableGrid" class="table table-bordered table-striped dt-responsive nowrap">
                    <thead>
                        <tr>
                            <th>Student Name</th>
                            <th>User Name</th>
                            <th>Route</th>
                            <th>School</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%for(Student student : studentList) { %>
                        <tr>
                            <td><%= student.getFirstName()+" "+student.getLastName()%></td>
                            <td><%= student.getUser().getFirstName()+" "+student.getUser().getLastName()%></td>
                            <td><%= (student.getWayPoint()!=null && student.getWayPoint().getRoute()!=null)?student.getWayPoint().getRoute().getRouteName():""%></td>
                            <td><%= student.getSchool()!=null?student.getSchool().getName():""%></td>
                            <td><a href="javascript:void(0);" ng-click="modifyRouteForm('<%= student.getId()%>', '<%= student.getWayPoint()!=null?student.getWayPoint().getId():""%>')" ><i class="ion-edit"></i> Edit Route</a></td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                
                <div id="addRouteModal" class="modal fade" tabindex="-1" data-width="760" style="display: none;">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">Add/Update Student Route</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <form name="routeForm" ng-submit="submitRouteForm()">
                                            <div class="form-group">
                                                    <label>School</label>
                                                    <select class="form-control" ng-model="routeData.schoolId">
                                                        <option value="">Select School</option>
                                                        <%for(School school : schoolList) { %>
                                                            <option value="<%= school.getId()%>"><%= school.getName()%></option>
                                                        <%}%>
                                                    </select>
                                                    <label>Route Name</label>
                                                    <select class="form-control" ng-model="routeData.routeId">
                                                        <option value="">Select Route</option>
                                                        <%for(Route route : routeList) { %>
                                                            <option value="<%= route.getId()%>"><%= route.getRouteName()%></option>
                                                        <%}%>
                                                    </select>
                                                    <label>Way Point</label>
                                                    <select class="form-control" ng-model="routeData.wayPointId" ng-options="key as value for (key, value)  in waypoints">
                                                        <option value="">Select Way Point</option>
                                                    </select>
                                                    <br/>
                                                    <input type="hidden" ng-model="routeData.studentId">
                                                    <input type="hidden" ng-model="routeData.studentRouteXREFId">
                                                    <button type="button" data-dismiss="modal" class="btn btn-inverse">Cancel</button>
                                                    <button type="submit" class="btn btn-primary">Save</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                            </div>
                </div>
            </div>

            <div class="box-footer">
                <div class="alert alert-success" ng-show="showSuccessAlert">
                    <button type="button" class="close" data-ng-click="switchBool('showSuccessAlert')">x</button> <strong></strong>{{successTextAlert}}
                </div>
            </div>
        
    </div>
</div>