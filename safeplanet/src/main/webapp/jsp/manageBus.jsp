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
List<Fleet> fleetList = routeService.findAllFleetList((String)session.getAttribute("USER_ID"));

try{
	
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
            <h3 class="box-title">Manage Bus </h3>
        </div>
        <div class="pull-right">
            <ul id="menu">
                <li><a href="javascript:void(0);" ng-click="addBusFormModal()">Add Vehicle</a></li>
                <li><a href="#manageDevices.html">Manage Devices</a></li>
            </ul>
        </div>
        
            <div class="box-body">
                 <table id="dataTableGrid" class="table table-bordered table-striped dt-responsive nowrap">
                    <thead>
                        <tr>
                            <th>Reg.#</th>
                            <th>Owner Name</th>
                            <th>Owner Mobile</th>
                            <th>Driver Name</th>
                            <th>Driver Mobile</th>
                            <th>Conductor Name</th>
                            <th>Conductor Mobile</th>
                            <th>Ayya Name</th>
                            <th>Ayya Mobile</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%for(Fleet fleet : fleetList) { %>
                        <tr>
                            <td><%= fleet.getRegNumber()%></td>
                            <td><%= fleet.getOwnerName()%></td>
                            <td><%= fleet.getOwnerMobile()%></td>
                            <td><%= fleet.getDriverName()%></td>
                            <td><%= fleet.getDriverMobile()%></td>
                            <td><%= fleet.getNavigatorName()%></td>
                            <td><%= fleet.getNavigatorMobile()%></td>
                             <td><%= fleet.getConductor2Name()%></td>
                            <td><%= fleet.getConductor2Mobile()%></td>
                            <td><a href="javascript:void(0);" ng-click="modifyBusForm('<%=fleet.getId()%>')" ><i class="ion-edit"></i> Edit</a></td>
                        </tr>
                <% } 
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                %>
                    </tbody>
                </table>
                
                          <div id="addBusModal" class="modal fade" tabindex="-1" data-width="760" style="display: none;">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">Add/Update Bus Details</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <form name="busDetailsForm" ng-submit="submitBusForm()">
                                            <div class="form-group">
                                                    <label>Registration Number</label>
                                                    <input type="text" class="form-control" placeholder="Registration Number" ng-model="busData.regNumber"/>
                                                    <label>Owner Name</label>
                                                    <input type="text" class="form-control" placeholder="Owner Name" ng-model="busData.ownerName"/>
                                                    <label>Owner Mobile</label>
                                                    <input type="text" class="form-control" placeholder="Owner Mobile" ng-model="busData.ownerMobile"/>
                                                    <label>Driver Name</label>
                                                    <input type="text" class="form-control" placeholder="Driver Name" ng-model="busData.driverName"/>
                                                    <label>Driver Mobile</label>
                                                    <input type="text" class="form-control" placeholder="Driver Mobile" ng-model="busData.driverMobile"/>
                                                    <label>Conductor Name</label>
                                                    <input type="text" class="form-control" placeholder="Navigator Name" ng-model="busData.navigatorName"/>
                                                    <label>Conductor Mobile</label>
                                                    <input type="text" class="form-control" placeholder="Navigator Mobile" ng-model="busData.navigatorMobile"/>
                                                    <label>Ayya Name</label>
                                                    <input type="text" class="form-control" placeholder="Ayya Name" ng-model="busData.conductor2Name"/>
                                                    <label>Ayya Mobile</label>
                                                    <input type="text" class="form-control" placeholder="Ayya Mobile" ng-model="busData.conductor2Mobile"/>
                                                    <label>Fleet Model</label>
                                                    <input type="text" class="form-control" placeholder="Fleet Model" ng-model="busData.fleetModel"/>
                                                    <label>Fleet Make</label>
                                                    <input type="date" class="form-control" placeholder="Fleet Make" ng-model="busData.fleetMake" value="{{ busData.fleetMake | date: 'yyyy-MM-dd' }}" data-date-format="DD/MM/YYYY"/>
                                                    <label>Fleet Type</label>
                                                    <input type="text" class="form-control" placeholder="Fleet Type" ng-model="busData.fleetType"/>
                                                    <br>
                                                    <input type="hidden" ng-model="busData.fleetID">
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
