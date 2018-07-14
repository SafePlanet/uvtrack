dashboardApp.controller('AlertController', function($scope, $rootScope,$http) {
	// Show reports icons
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}

	
	$scope.user = JSON.parse(localStorage.getItem("user"));
	var role = $scope.user.role;
	
	$scope.reports = {};
	$scope.reports.reportList = [];

	
	//===============Add Message summary and Student message report=============//
	$scope.reports.reportList.push({
		"link" : "#messageSummaryReport.html",
		"linkName" : "Message summary",
		"icon" : "icons/report.png",
	});
	$scope.reports.reportList.push({
		"link" : "#studentMessageReport.html",
		"linkName" : "Student Message",
		"icon" : "icons/report.png",
	});
	if(role == "superAdmin")
	{
	$scope.reports.reportList.push({
		"link" : "#viewLogs.html",
		"linkName" : "Server Log",
		"icon" : "icons/report.png",
	});
	
	$scope.reports.reportList.push({
		"link" : "#startedService.html",
		"linkName" : "Start Server",
		"icon" : "icons/report.png",
	});
	}
//=================================End==============================================//
	
	$http.get('http://localhost:8181/alertSystem/health').success(function(data){
		$scope.status= data.status;
	});
});