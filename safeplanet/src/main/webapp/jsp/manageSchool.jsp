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
    private UserService userService;

%>

<%
List<School> schoolList = userService.getSchoolDetails((String)session.getAttribute("USER_ID"));

try {

//System.out.println("schoolList:: "+schoolList);


%>

<style>
  .error {
      color: red;
    }

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
            <h3 class="box-title">Manage Schools </h3>
        </div>
        
        <div class="pull-right">
            <ul id="menu">
                <li><a href="javascript:void(0);" ng-if="user.role=='superAdmin'" ng-click="addSchoolFormModal()">Add School</a></li>
            </ul>
        </div>
        
        
            <div class="box-body">
                 <table id="dataTableGrid" class="table table-bordered table-striped dt-responsive nowrap">
                    <thead>
                        <tr>
                            <th>School Name</th>
                            <th>Address</th>
                            <th>Email</th>
                            <th>Phone 1</th>
                            <th>Phone 2</th>
                            <th>Phone 3</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%for(School school : schoolList) { %>
                        <tr>
                            <td><%= school.getName()%></td>
                            <td><%= school.getDisplayAddress()%></td>
                            <td><%= school.getEmailId()%></td>
                            <td><%= school.getPhoneNumber1()%></td>
                            <td><%= school.getPhoneNumber2()%></td>
                            <td><%= school.getPhoneNumber3()%></td>
                            <td>
                                <a href="javascript:void(0);" ng-click="modifySchoolForm('<%=school.getId()%>')"><i class="ion-edit"></i> Edit</a>
                            </td>
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

			<div id="addSchoolModal"  class="modal fade" tabindex="-1"
				data-width="960" style="display: none;" data-height="400" >
				<div  class="modal-header"  >
					<button type="button"  class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">Add/Update School Details</h4>
				</div>
				<div class="modal-body" style="width: 930px; height: 350px;">
					<div class="row">
						<div class="col-md-6">
							<form name="schoolDetailsForm" ng-submit="submitSchoolForm()">
								<div class="form-group">
								<input type="hidden" ng-model="schoolData.schoolID"/>
									<table  cellpadding="20" style="width: 890px; height: 350px;">
									
									
										<tr>
											<td> <label>School Name</label> </td> 
											<td> <input type="text" class="form-control" placeholder="School Name" ng-model="schoolData.name" /></td>
											<td style="padding: 10px;"> <label>Email</label> </td>
											<td> <input type="text" class="form-control" placeholder="Email" ng-model="schoolData.emailId" /></td>
										</tr>

										<tr>
											<td> <label>Display Address</label> </td>
											<td> <input type="text" class="form-control" placeholder="Display Address" ng-model="schoolData.displayAddress" /></td>
											<td style="padding: 10px;"> <label>Phone Number 1</label> </td>
											<td> <input type="text" class="form-control" placeholder="Phone Number 1" name="phoneNumber1" ng-model="schoolData.phoneNumber1" pattern="[1-9]{1}[0-9]{9}"  maxlength="10" />
											 <span class="error" ng-show="schoolDetailsForm.phoneNumber1.$error.required">Required!</span>
                                              <span class="error" ng-show="schoolDetailsForm.phoneNumber1.$error.pattern">Please match pattern [+91 9988776500]</span>
											</td>
										</tr>

										<tr>
											<td> <label>Phone Number 2</label> </td>
											<td> <input type="text" class="form-control" placeholder="Phone Number 2" ng-model="schoolData.phoneNumber2" name="phoneNumber2" pattern="[1-9]{1}[0-9]{9}"  maxlength="10"  />
											 <span class="error" ng-show="schoolDetailsForm.phoneNumber2.$error.required">Required!</span>
                                              <span class="error" ng-show="schoolDetailsForm.phoneNumber2.$error.pattern">Please match pattern [+91 9988776500]</span>
											</td>
											<td style="padding: 10px;"> <label>Phone Number 3</label> </td>
											<td> <input type="text" class="form-control" placeholder="Phone Number 3" ng-model="schoolData.phoneNumber3" name="phoneNumber3" pattern="[1-9]{1}[0-9]{9}"  maxlength="10" />
											 <span class="error" ng-show="schoolDetailsForm.phoneNumber3.$error.required">Required!</span>
                                              <span class="error" ng-show="schoolDetailsForm.phoneNumber3.$error.pattern">Please match pattern [+91 9988776500]</span>
											
											</td>
										</tr>

										<tr>
											<td> <label>Address Line 1</label> </td>
											<td> <input type="text" class="form-control" placeholder="Address Line 1" ng-model="schoolData.addressLine1" /></td>
											<td style="padding: 10px;"> <label>Address Line 2</label> </td>
											<td> <input type="text" class="form-control" placeholder="Address Line 2" ng-model="schoolData.addressLine2" /></td>
										</tr>

										<tr>
											<td> <label>State</label> </td> </td>
											 <td>
									    <select  class="form-control" ng-model="schoolData.selectedState" ng-change="loadCityByState()" > <option value="">Select State</option> 
													<option ng-repeat="state in statesList" value="{{state.stateId}}">{{state.stateName}}    
													</option> </select>
									   </td>
											 
									  
											<td style="padding: 10px;"> <label>City</label>
											<td>	    <select  class="form-control" ng-model="schoolData.selectedCity">
									       <option value="">Select City</option> 
													<option ng-repeat="city in cityList" value="{{city.cityName}}">{{city.cityName}} </option></select></td>
										</tr>
										<tr>
											<td> <label>Pin Code</label> </td>
											<td> <input type="text" class="form-control" placeholder="Pin Code" ng-model="schoolData.pinCode" /></td>
										</tr>
										<tr>
									    <td>
										
										<button type="button" data-dismiss="modal"
											class="btn btn-primary">Cancel</button>
										</td>
										
										<td>
										<button type="submit" class="btn btn-primary">Save</button>
										</td>
									</tr>
									</table>
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
                    <button type="button" class="close" data-ng-click="switchBool('showSuccessAlert')">x</button> <strong></strong>{{successTextAlert}}
                </div>
            </div>
        
    </div>
</div>