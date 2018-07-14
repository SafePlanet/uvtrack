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
List<RouteFleetDeviceXREF> fleetList = routeService.findAllFleetListByUser((String)session.getAttribute("USER_ID"));
List<Route> routeList = routeService.findAllRouteListByUserId((String)session.getAttribute("USER_ID"));
List<Devices> fleets = routeService.findAllDeviceListNotMapped();

try{

//List<School> schoolList = routeService.findAllSchoolList();
//System.out.println("fleetList:: "+fleetList);


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
            <h3 class="box-title">Manage Bus Route </h3>
        </div>
        <div class="pull-right">
            <ul id="menu">
                <li><a href="#manageRoutes.html" >Manage Route</a></li>
			  <li><a href="#addStudentRoute.html">Student Route</a></li>
			  <li><a href="#manageBusRoute.html">Bus Route</a></li>
			</ul>
        </div>
        
            <div class="box-body">
                 <table id="dataTableGrid" class="table table-bordered table-striped dt-responsive nowrap">
                    <thead>
                        <tr>
                            <th>Reg#</th>
                            <th>Driver Name</th>
                            <th>Driver Mobile</th>
                            <th>Owner Name</th>
                            <th>Owner Mobile</th>
                            <th>Fleet Model</th>
                            <th>Route</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%for(RouteFleetDeviceXREF xref : fleetList) { %>
                        <tr>
                            <td><%= xref.getFleet().getRegNumber()%></td>
                            <td><%= xref.getFleet().getDriverName()%></td>
                            <td><%= xref.getFleet().getDriverMobile()%></td>
                            <td><%= xref.getFleet().getOwnerName()%></td>
                            <td><%=xref.getFleet().getOwnerMobile()%></td>
                            <td><%= xref.getFleet().getFleetModel()%></td>
                            <td><%= xref.getRoute().getRouteName()%></td>
                            <td><a href="javascript:void(0);" ng-click="modifyBusRouteForm('<%=xref.getFleet().getId()%>','<%=xref.getId().toString()%>')" ><i class="ion-edit"></i> Map Fleet/Route</a></td>
                        </tr>
                <% 
                        } 
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                %>
                    </tbody>
                </table>
                
                          <div id="addBusRouteModal" class="modal fade" tabindex="-1" data-width="760" style="display: none;">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">Add/Update Bus Route</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <form name="busRouteForm" ng-submit="submitBusRouteForm()">
                                            <div class="form-group">
                                                    <label>Fleet Name</label>
                                                    <select class="form-control" ng-model="fleetData.fleetID" id="deviceID" disabled="disabled">
                                                        <option value="">Select Device</option>
                                                      <%for(RouteFleetDeviceXREF rfdx : fleetList){%>
                                                    	<option  value="<%=rfdx.getFleet().getId() %>"><%=rfdx.getFleet().getRegNumber() %></option>  
                                                      <%}
                                                      %>
                                                    </select>
                                                    <label>Route Name</label>
                                                    <select class="form-control" ng-model="fleetData.routeID" id="routeID">
                                                        <option value="">Select Route</option>
                                                        <%for(Route route : routeList) { %>
                                                            <option value="<%= route.getId()%>"><%= route.getRouteName()%></option>
                                                        <%}%>
                                                    </select>
                                                    
                                                    <br/>
                                                    <input type="hidden" ng-model="fleetData.fleetID">
                                                    <input type="hidden" ng-model="fleetData.xRefID">
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
