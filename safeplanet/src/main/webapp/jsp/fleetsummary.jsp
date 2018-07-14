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

%>

<%

List<Route> routeList = routeService.findAllRouteList((String)session.getAttribute("USER_ID"));

List<School> schoolList = routeService.findAllSchoolList();
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
            <h3 class="box-title">Fleet Summary Report </h3>
        </div>
        <div class="pull-right">
            <ul id="menu">
                <li><a href="javascript:void(0);" ng-click="addRouteFormModal()">Add Route</a></li>
			  <li><a href="#addStudentRoute.html">Student Route</a></li>
			  <li><a href="#manageBusRoute.html">Bus Route</a></li>
			</ul>
        </div>
        
            <div class="box-body">
                 <table id="dataTableGrid" class="table table-bordered table-striped dt-responsive nowrap">
                    <thead>
                        <tr>
                            <th>Route Name</th>
                            <th>Route Description</th>
                            <th>Geometry</th>
                            <th>School</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%for(Route route : routeList) { %>
                        <tr>
                            <td><%= route.getRouteName()%></td>
                            <td><%= route.getRouteDescription()%></td>
                            <td><%= route.getGeometry()%></td>
                            <td><%= route.getSchool().getName()%></td>
                            <td>
                                <a href="javascript:void(0);" ng-click="modifyRouteForm('<%= route.getIdentifier()%>')" ><i class="ion-edit"></i> Edit</a>
                                &nbsp;|&nbsp;<a href="#manageWayPoints.html/<%= route.getIdentifier()%>"><i class="ion-edit"></i> WayPoints</a>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                
                <div id="addRouteModal" class="modal fade" tabindex="-1" data-width="760" style="display: none;">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">Add/Update Route</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <form name="routeForm" ng-submit="submitRouteForm()">
                                            <div class="form-group">
                                                    <label>Route Name</label>
                                                    <input type="text" class="form-control" placeholder="Route Name" ng-model="routeData.routeName"/>
                                                    <label>Route Description</label>
                                                    <textarea class="form-control" rows="3" placeholder="Route Description" ng-model="routeData.description"></textarea>
                                                    <label>Geometry</label>
                                                    <input type="text" class="form-control" placeholder="Geometry" ng-model="routeData.geometry"/>
                                                    <label>School Name</label>
                                                    <select class="form-control" ng-model="routeData.school">
                                                        <option value="">Select School</option>
                                                        <%for(School school : schoolList) { %>
                                                            <option value="<%= school.getId()%>"><%= school.getName()%></option>
                                                        <%}%>
                                                    </select>
                                                    <br/>
                                                    <input type="hidden" ng-model="routeData.routeUUID">
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