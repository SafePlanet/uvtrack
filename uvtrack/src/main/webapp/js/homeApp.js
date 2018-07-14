'use strict';

var dashboardApp = angular.module('dashboardApp', [ 'ngRoute', 'ngCookies',
		'ngResource', 'ngSanitize', 'leaflet-directive', 'timepickerPop',
		'ngLoadingSpinner','services','directives' ]);

dashboardApp.run(['$rootScope','$location', '$routeParams', function($rootScope, $location, $routeParams) {
    $rootScope.$on('$routeChangeSuccess', function(e, current, pre) {
      //console.log('Current route name: ' + $location.path());

    });
  }]);

var cacheKey = '?v=7';
// configure our routes
dashboardApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'home.html' +  cacheKey,
		controller : 'DashboardController'
	}).when('/welcome', {
		templateUrl : 'home.html' +  cacheKey,
		controller : 'HomeController'
	}).when('/home.html', {
		templateUrl : 'home.html' +  cacheKey,
		controller : 'HomeController'
	}).when('/welcome.html', {
		templateUrl : 'home.html' +  cacheKey,
		controller : 'DashboardController'
	}).when('/editProfile.html', {
		templateUrl : 'editProfile.html' +  cacheKey,
		controller : 'EditProfileController'
	}).when('/student.html', {
		templateUrl : 'studentProfile.html' +  cacheKey,
		controller : 'StudentProfileController'
	}).when('/addstudent.html', {
		templateUrl : 'addStudent.html' +  cacheKey,
		controller : 'StudentProfileController'
	}).when('/busLocation.html', {
		templateUrl : 'viewMap.html' +  cacheKey,
		controller : 'BusLocationController'
	}).when('/allBusLocation.html', {
		templateUrl : 'viewAllBusMap.html' +  cacheKey,
		controller : 'AllBusLocationController'
	}).when('/myBus.html/:vehicle_id?', {
		templateUrl : 'myBus.html',
		controller : 'MyBusController'
	}).when('/absentMessage.html', {
		templateUrl : 'absentMessage.html' +  cacheKey,
		controller : 'absentMessageController'
	}).when('/complaintMessage.html', {
		templateUrl : 'complaintMessage.html' +  cacheKey,
		controller : 'ComplaintMessageController'
	}).when('/teacherView.html', {
		templateUrl : 'teacherView.html' +  cacheKey,
		controller : 'teacherViewController'
	}).when('/studentReport.html', {
		templateUrl : 'studentReport.html' +  cacheKey,
		controller : 'studentReportController'
	}).when('/settings.html', {
		templateUrl : 'settings.html' +  cacheKey,
		controller : 'SettingsController'
	}).when('/viewAllAlerts.html', {
		templateUrl : 'viewAllAlerts.html' +  cacheKey,
		controller : 'ViewAllAlertsController'
	}).when('/schoolInfo.html', {
		templateUrl : 'schoolInfo.html' +  cacheKey,
		controller : 'SchoolInfoController'
	}).when('/broadcastMessage.html', {
		templateUrl : 'broadcastMessage.html' +  cacheKey,
		controller : 'BroadcastMessageController'
	}).when('/addBusRoute.html', {
		templateUrl : 'addBusRoute.html' +  cacheKey,
		controller : 'addBusRouteController'
	}).when('/addStudentRoute.html', {
		templateUrl : 'jsp/studentroutes.jsp',
		controller : 'addStudentRouteController'
	}).when('/manageStudents.html', {
		templateUrl : 'manageStudents.html' +  cacheKey,
		controller : 'ManageStudentController'
	}).when('/manageRoutes.html', {
		templateUrl : 'jsp/manageroutes.jsp',
		controller : 'ManageRouteController'
	}).when('/manageWayPoints.html/:routeUUID', {
//		templateUrl : function(params) {
//			return 'jsp/managewaypoints.jsp?routeUUID=' + params.routeUUID
//		},
		templateUrl : 'manageWayPoints.html' +  cacheKey,
		controller : 'ManageWayPointController'
	}).when('/manageSchool.html', {
		templateUrl : 'manageSchool.html' +  cacheKey,
		controller : 'ManageSchoolController'
	}).when('/manageSchoolHolidays.html', {
		templateUrl : 'manageSchoolHolidays.html' +  cacheKey,
		controller : 'ManageSchoolHolidaysController'
	}).when('/manageSchoolHolidayMasterData.html', {
		templateUrl : 'manageSchoolHolidayMasterData.html' +  cacheKey,
		controller : 'ManageSchoolHolidayMasterDataController'
	}).when('/schoolHolidays.html', {
		templateUrl : 'schoolHolidays.html' +  cacheKey,
		controller : 'ManageSchoolHolidaysController'
	}).when('/schoolHolidaysNew.html', {
		templateUrl : 'schoolHolidaysNew.html',
		controller : 'SchoolHolidaysController'
	}).when('/manageBus.html', {
		templateUrl : 'jsp/manageBus.html' +  cacheKey,
		controller : 'addBusController'
	}).when('/manageDevices.html', {
		templateUrl : 'manageDevices.html' +  cacheKey,
		controller : 'ManageDevicesController'
	}).when('/addDevice.html', {
		templateUrl : 'addDevice.html' +  cacheKey,
		controller : 'addDeviceController'
	}).when('/manageUsers.html', {
		templateUrl : 'jsp/manageUsers.html' +  cacheKey,
		controller : 'ManageUsersController'
	}).when('/manageBusRoute.html', {
		templateUrl : 'jsp/manageBusRoute.jsp',
		controller : 'ManageBusRouteController'
	}).when('/fleetReport.html', {
		templateUrl : 'fleetReport.html' +  cacheKey,
		controller : 'FleetReportController'
	}).when('/fleetSummaryDetails.html', {
		templateUrl : 'fleetSummaryDetails.html' +  cacheKey,
		controller : 'FleetReportController'
	}).when('/haltReport.html', {
		templateUrl : 'haltReport.html' +  cacheKey,
		controller : 'HaltReportController'
	}).when('/overSpeedingReport.html', {
		templateUrl : 'overSpeedingReport.html' +  cacheKey,
		controller : 'OverSpeedReportController'
	}).when('/studentStopTimeReport.html', {
		templateUrl : 'studentStopTimeReport.html' +  cacheKey,
		controller : 'StudentStopReportController'
	}).when('/busStopPredictiveReport.html', {
		templateUrl : 'busStopPredictiveReport.html' +  cacheKey,
		controller : 'BusStopPredictReportController'
	}).when('/busStopTimeReport.html', {
		templateUrl : 'busStopTimeReport.html' +  cacheKey,
		controller : 'BusStopTimeReportController'
	}).when('/tripSummaryReport.html', {
		templateUrl : 'tripSummaryReport.html' +  cacheKey,
		controller : 'TripSummaryReportController'
	}).when('/positionReport.html', {
		templateUrl : 'positionReport.html' +  cacheKey,
		controller : 'positionReportController'
	}).when('/changePassword.html', {
		templateUrl : 'changePassword.html' +  cacheKey,
		controller : 'changePasswordController'
	}).when('/SendSms.html', {
		templateUrl : 'SendSms.html' +  cacheKey,
		controller : 'sendMessageController'
	}).when('/SendEmail.html', {
		templateUrl : 'SendEmail.html' +  cacheKey,
		controller : 'sendEmailController'
	}).when('/reports.html', {
		templateUrl : 'reports.html' +  cacheKey,
		controller : 'reportsController'
	}).when('/alert.html', {
		templateUrl : 'alert.html' +  cacheKey,
		controller : 'AlertController'
	}).when('/holidays.html', {
		templateUrl : 'holidays.html' +  cacheKey,
		controller : 'holidaysController'
	}).when('/aboutUs.html', {
		templateUrl : 'aboutUs.html' +  cacheKey,
	}).when('/busStop.html', {
		templateUrl : 'busStop.html' +  cacheKey,
		controller : 'BusStopController'
	}).when('/assignment.html', {
		templateUrl : 'assignment.html' +  cacheKey,
		controller : 'AssignmentController'
	}).when('/assignmentNext.html', {
		templateUrl : 'assignmentNext.html' +  cacheKey,
		controller : 'AssignmentController'
	}).when('/manageSchoolAdmin.html', {
		templateUrl : 'manageSchoolAdmin.html' +  cacheKey,
		controller : 'ManageUsersController'
	}).when('/manageSchoolTeacher.html', {
		templateUrl : 'manageSchoolTeacher.html' +  cacheKey,
		controller : 'ManageUsersController'
	}).when('/messageSummaryReport.html', {
		templateUrl : 'messageSummaryReport.html' +  cacheKey,
		controller : 'ManageMessageSummaryController'
	}).when('/studentMessageReport.html', {
		templateUrl : 'studentMessageReport.html' +  cacheKey,
		controller : 'ManageStudentNotificationController'
	}).when('/viewLogs.html', {
		templateUrl : 'viewLogs.html' +  cacheKey,
		controller : 'viewLogsController'
	}).when('/startedService.html', {
		templateUrl : 'startedService.html' +  cacheKey,
		controller : 'startedServiceController'
	}).when('/makeRequest.html', {
		templateUrl : 'makeRequest.html' +  cacheKey,
		controller : 'makeRequestReportsController'
	}).when('/markLeave.html', {
		templateUrl : 'markLeave.html' +  cacheKey,
		controller : 'StudentProfileController'
	}).when('/busInformation.html', {
		templateUrl : 'busInformation.html' +  cacheKey,
		controller : 'SchoolInfoController'
	}).when('/pickUpRequest.html', {
		templateUrl : 'pickUpRequest.html' +  cacheKey,
		controller : 'pickUpRequestController'
	}).when('/incomingRequest.html', {
		templateUrl : 'incomingRequest.html' +  cacheKey,
		controller : 'ViewAllAlertsController'
	}).when('/manageRouteScheduleDetails.html', {
		templateUrl : 'manageRouteScheduleDetails.html' +  cacheKey,
		controller : 'manageRouteScheduleController'
	}).when('/manageClassGroup.html', {
		templateUrl : 'manageClassGroup.html' +  cacheKey,
		controller : 'manageClassGroupController'
	}).when('/ParentsVisitor.html', {
		templateUrl : 'ParentsVisitor.html' +  cacheKey,
		controller : 'parentsVisitorController'
	}).when('/NormalVisitor.html', {
		templateUrl : 'NormalVisitor.html' +  cacheKey,
		controller : 'parentsVisitorController'
	}).otherwise({
		redirectTo : '/errorPage.html' +  cacheKey,
	});
} ]);

dashboardApp.config([ '$httpProvider', function($httpProvider) {
	$httpProvider.interceptors.push(function($q, $rootScope, $log) {
		return {
			'request' : function(config) {
				var time = javaRest.get_iso_date();
				var nonce = makeRandomString();
				config.headers = {
				    'Authorization': getAuthToken(config.url, time, nonce),
				    'x-safeplanet-date': time,
				    'nonce': nonce
				};
                if(config.json)
                {
                    console.log(config)
				    config.headers["Content-Type"]='application/json';
				}
				return config;
			}
		}
	});
	// $httpProvider.defaults.headers.post['Content-Type'] = undefined;
} ]);