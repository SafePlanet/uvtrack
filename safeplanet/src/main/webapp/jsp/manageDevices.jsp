<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
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
    private UserService userService;

%>

<%
List<Devices> deviceList = routeService.findAllDeviceList((String)session.getAttribute("USER_ID"));

Map<String, DeviceModelType> deviceModelTypeMap = routeService.getAllDeviceModelType();



List<School> schoolList = userService.getSchoolDetails((String)session.getAttribute("USER_ID"));

//System.out.println(fleetList.get(0).getDriverName()+"  "+fleetList.get(0).getOwnerName());


Role role=userService.getUserRole((String)session.getAttribute("USER_ID"));

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
            <h3 class="box-title">Manage Devices </h3>
        </div>
        <div class="pull-right">
            <ul id="menu">
                <li><a href="#manageBus.html">Manage Bus</a></li>
                <li><a href="javascript:void(0);" ng-click="addDeviceFormModal()">Add Device</a></li>                
            </ul>
        </div>
        
            <div class="box-body">
                 <table id="dataTableGrid" class="table table-bordered table-striped dt-responsive nowrap">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Unique No.</th>
                            <th>Mobile Number</th>
                            <th>Device Model</th>
                            <th>Freq. Update Time</th>
                            <th>Protocol</th>
                            <th>Purchase Date</th>
                            <th>Warranty Date</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%for(Devices device : deviceList) {
                        	 System.out.println("Device " + device.getDeviceModelType());
                        	 String deviceModelTypeId = null;
                        	 String deviceModel = "";
                        	 if(device.getDeviceModelType()!=null)
                        		 deviceModel = deviceModelTypeMap.get(device.getDeviceModelType().getId().toString()).getDeviceModel();
                          %>
                        <tr>
                        	<td><%= device.getName()%></td>
                            <td><%= device.getUniqueid()%></td>
                            <td><%= device.getMobile() == null ? "" : device.getMobile()%></td>
                            <td><%= deviceModel%></td>
                            <td><%= device.getFrqUpdateTime()%></td>
                            <td><%= device.getProtocol()%></td>
                            <td><%= (new SimpleDateFormat("dd-MM-yyyy").format(device.getPuchaseDate()))%></td>
                            <td><%= (new SimpleDateFormat("dd-MM-yyyy").format(device.getWarrantyDate()))%></td>
                            <td><a href="javascript:void(0);" ng-click="modifyDeviceForm('<%=device.getId()%>')" ><i class="ion-edit"></i> Edit</a></td>
                        </tr>
                <% }
                %>
                    </tbody>
                </table>
                
                          <div id="addDeviceModal" class="modal fade" tabindex="-1" data-width="1200" style="display: none;">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">Add/Update Device Details</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <form name="deviceDetailsForm" ng-submit="submitDeviceForm()">
                                            <div class="form-group">
                                            <input type="hidden" ng-model="deviceData.deviceId">
                                             <table style="width:1140px;height:250px;" cellpadding="10">
                                             		<tr>
                                             		<td>
                                                    <label>Device Name</label>
                                                    </td>
                                                    <td>
                                                     <input type="text" placeholder="Name" class="form-control"  ng-model="deviceData.name" maxlength=20 ng-required="required"  onkeyup="this.value = this.value.replace(/[^a-zA-Z0-9_-]/, '')" />
                                                    </td>
                                             		<td>
                                                    <label style="margin-left:10px;align:right;">Type</label>
                                                    </td>
                                                    <td>
                                                    <select name="deviceType" ng-model="deviceData.deviceType" class="form-control" ng-required="true">
                                                    	<option value="">-Select Type-</option>
														<option value="GPS" selected>GPS</option>
													</select>
                                                    </td>
                                                    <td>
                                                    <label style="margin-left:10px;align:right;">Device Model</label>
                                                    </td>
                                                    <td>
                                                    <select name="devicemodeltype" ng-model="deviceData.deviceModelId" class="form-control">
                                                    <option value="" >-Select Device Model-</option>
                                                      <% Iterator<String> deviceModelTypeMapItr =  deviceModelTypeMap.keySet().iterator();
                                                      	while(deviceModelTypeMapItr.hasNext()) { 
                                                      	DeviceModelType deviceModelType = (DeviceModelType)deviceModelTypeMap.get(deviceModelTypeMapItr.next());
                                                      %>
														<option value="<%=deviceModelType.getId()%>"><%=deviceModelType.getDeviceModel()%></option>
													  <%}%>
													</select>
                                                    </td>
                                                     <td>
                                                    <label style="margin-left:10px;align:right;">Protocol</label>
                                                    </td>
                                                    <td>
                                                    <select name="deviceProtocol" ng-model="deviceData.protocol" class="form-control" ng-required="true">
                                                    	<option value="" >-Select protocol-</option>
														<option  value="TK06A" selected>TK06A</option>
													</select>
                                                    </td>
                                                    </tr>
                                                    <tr>
                                                    <td>
                                                    <label>Unique No.</label>
                                                    </td>
                                                    <td>
                                                    <input type="text" class="form-control" placeholder="Unique No." ng-model="deviceData.uniqueid" maxlength=15 onkeyup="this.value = this.value.replace(/[^0-9]/, '')"/>
                                                    </td>
                                                    <td>
                                                    <label style="margin-left:10px;">Freq. Mode</label>
                                                    </td>
                                                   <td>
                                                    <input type="text" class="form-control" placeholder="Freq. Mode" ng-model="deviceData.frqMode"  maxlength=1 disabled="disabled" onkeyup="this.value = this.value.replace(/[^a-zA-Z]/, '')"/>
                                                    </td>
                                                    <td>
                                                    <label style="margin-left:10px;">Freq. Update Dist.</label>
                                                    </td>
                                                     <td>
                                                    <input type="text" class="form-control" placeholder="Freq. Update Dist." ng-model="deviceData.frqUpdateDist" maxlength=11 disabled="disabled" onkeyup="this.value = this.value.replace(/[^0-9]/, '')"/>
                                                    </td>
                                                    <td>
                                                    <label style="margin-left:10px;">Freq. Update Time</label>
                                                    </td>
                                                    <td>
                                                    <select type="text" class="form-control" placeholder="Freq. Update Time" ng-model="deviceData.frqUpdateTime" maxlength=3 ng-requird="true" onkeyup="this.value = this.value.replace(/[^0-9]/, '')">
                                                    <option value="" >-Select Update Time-</option>
													<option  value="10" selected>10</option>
													<option  value="20" selected>20</option>
													<option  value="30" selected>30</option>
													<option  value="40" selected>40</option>
													<option  value="50" selected>50</option>
													<option  value="60" selected>60</option>
														</select>
                                                    </td>
                                                    </tr>
                                                    <tr>
                                             		<td>
                                                    <label>Mfg. Date</label>
                                                    </td>
                                                    <td>
                                                    <input type="date" class="form-control" placeholder="Mfg. Date" ng-model="deviceData.manfDate" value="{{ deviceData.manfDate | date: 'yyyy-MM-dd' }}" data-date-format="DD/MM/YYYY" />
                                                    </td>
                                                    <td>
                                                    <label style="margin-left:10px;">Purchase Date</label>
                                                    </td>
                                                    <td>
                                                    <input type="date" class="form-control" placeholder="Purchase Date" ng-model="deviceData.puchaseDate" value="{{ deviceData.puchaseDate | date: 'yyyy-MM-dd' }}" data-date-format="DD/MM/YYYY"/>
                                                    </td>
                                                    <td>
                                                    <label style="margin-left:10px;" >Warranty Date</label>
                                                    </td>
                                                    <td>
                                                    <input type="date" class="form-control" placeholder="Warranty Date" ng-model="deviceData.warrantyDate" value="{{ deviceData.warrantyDate | date: 'yyyy-MM-dd' }}" data-date-format="DD/MM/YYYY"/>
                                                    </td>
                                                    <td>
                                                    <label style="margin-left:10px;">Mobile Number</label>
                                                    </td>
                                                    <td>
                                                    <input type="text" class="form-control" placeholder="Mobile Number" ng-model="deviceData.mobileNumber" maxlength=10 onkeyup="this.value = this.value.replace(/[^0-9]/, '')">
                                                    </td>
                                                    <td>
                                                    </tr> 
                                                    <tr>
                                                  	<% if(role.equals(Role.superAdmin)){
                                                  	%>
                                                    <td>
                                                    <label>School</label>
                                                    </td>
                                                    <td>
														<select name="schoolId" ng-model="deviceData.schoolId"  class="form-control">
		                                                    <option value="" >--Select School--</option>
		                                                      <%for(School school : schoolList) {
		                                                       %>
															  	<option value="<%=school.getId()%>"  ><%=school.getName()%></option>
															  <%}%>
														</select>
                                                    </td>
                                                    <% } %>
                                                    </tr>
                                      				</table>
                                                    <br>
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
