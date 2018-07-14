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
School school= new School();
if(schoolList.size()>0)
{
school= schoolList.get(0);
}
List<SchoolHoliday> holidays = new ArrayList<SchoolHoliday>();
		
		for (School s : schoolList) {
			holidays.addAll(s.getHolidays());
		}
try {

//System.out.println("schoolList:: "+schoolList );


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
            <h3 class="box-title">Manage School Holidays </h3>
        </div>
        
        <div class="pull-right">
            <ul id="menu">
                <li><a href="javascript:void(0);" ng-click="addSchoolHolidayFormModal()">Add School holiday</a></li>
            </ul>
        </div>
        
        
            <div class="box-body">
                 <table id="dataTableGrid" class="table table-bordered table-striped dt-responsive nowrap">
                    <thead>
                        <tr>
                            <th>From</th>
                            <th>To</th>
                            <th>Description</th>
                            <th>Target Type</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%for(SchoolHoliday holiday : holidays) { %>
                        <tr>
                            <td><%= holiday.getFromDate().toString()%></td>
                            <td><%= holiday.getToDate().toString()%></td>
                            <td><%= holiday.getDescription()%></td>
                            <td><%= holiday.getSjaFlag()%></td>
                            <td>
                                <a href="javascript:void(0);" ng-click="modifySchoolForm('<%=holiday.getId()%>')"><i class="ion-edit"></i> Edit</a>
                               
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
                
                          <div id="addSchoolHolidayModal" class="modal fade" tabindex="-1" data-width="760" style="display: none;">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">Add/Update School Details</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <form name="schoolDetailsForm" ng-submit="submitSchoolHolidayForm()">
                                            <div class="form-group">
                                                    <label>School Name</label>
                                                    <input type="text" class="form-control" readonly="readonly" ng-init="holidayData.id='<%=school.getId()%>'" placeholder="School Name" ng-model="schoolData.schoolId"/>
                                                    <label>From Date</label>
                                                    <input type="date" class="form-control"  ng-model="holidayData.fromDate" name="fromDate" />
                                                    <label>To Date</label>
                                                    <input type="date" class="form-control" ng-model="holidayData.toDate" name="toDate" />
                                                    <label>Description</label>
                                                    <input type="text" class="form-control" placeholder="Description" ng-model="holidayData.description"/>
                                                    <label>Target Type</label>
                                                    <label>
                                                    <input type="radio" ng-model="holidayData.sjaFlag" value="s"/>
                                                    Seniors
                                                    </label>
                                                    <label>
                                                    <input type="radio" ng-model="holidayData.sjaFlag" value="j"/>
                                                    Juniors
                                                    </label>
                                                    <label>
                                                    <input type="radio" ng-model="holidayData.sjaFlag" sjaFlag="a"/>
                                                    All
                                                    </label>
                                                    
                                                    <br>
                                                    <input type="hidden" ng-model="holidayData.holidayId">
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