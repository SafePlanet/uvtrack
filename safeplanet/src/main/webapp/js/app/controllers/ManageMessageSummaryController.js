dashboardApp.controller('ManageMessageSummaryController', function($scope, $location ,$rootScope,$resource, $timeout,$http,routeService,$filter) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left') || $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	
	$scope.date = new Date();
    $scope.back = function() {
			$location.path("/alert.html");
		    }
    
    var time = javaRest.get_iso_date();
    var nonce = makeRandomString();
    

 $scope.loadVehicleBySchool = function(ownerName){
	
	 for(var i=0;i<=$scope.data.length;i++){
		 var array =$scope.data[i].ownerName.split(' ');
		 var name = array[0];
		 $scope.data[i].name =name;
		 var name = ownerName.split(' ');
		 var school = name[0];
		 $scope.allvehicleList =$filter('filter')($scope.data,{name:school});
	 }
 }
    
 
 //============================================================================================//
  
 $http.get('user/getSchoolDetails/' + javaRest.cookie.get('userId')).success(function(data) {
	 $scope.schooldata= angular.copy(data);
		$scope.schoolList = data;
		console.log($scope.schoolList);
		
	});
 
 //==============================================================================================//\


 $http.get('reports/getAllVehicleList/' + javaRest.cookie.get('userId')).success(function(data) { 
	 $scope.data= angular.copy(data);
		//$scope.allvehicleList = data;
		$scope.vehicleList = data;
		//console.log($scope.allvehicleList);
	});
 

 
 $scope.loadScheduleByFleet = function () { 
 	routeService.getScheduleByFleet($scope.vehicleId).$promise.then(function(scheduleList) {
 		$scope.scheduleList = scheduleList;

 	});
 }
 
 $scope.downloadmessageReport = function(){
     console.log($scope.downloadReport)
	  window.open($scope.downloadReport+ ".xls");
		

}
 
 
 $scope.loadMessageReport = function(){
	var Schoolname = $scope.name;
	 $scope.data = $filter('filter')( $scope.schooldata, { name: Schoolname });
	 var schoolId = 1000;
	 if($scope.date ==undefined){
		 alert("Select  Date.");
		 return false;
	 }
	 if($scope.vehicleId == undefined){
		 alert("Select vehicle.");
		 return false;
	 }
	/* if($scope.schedule == undefined){
		 alert("Select schedule.");
		 return false;
	 }*/
	var date = $scope.date.toLocaleDateString();
	var vehicleId =  $scope.vehicleId;
	var ScheduleId = $scope.schedule;
	var messagedate =date.replace(/\//g, "-");
    var NotifiDate =  $filter('date')($scope.date, "dd-MM-yyyy");	
    $scope.userData = $resource(
            'reports/getMessageSummaryReport/:userId',
            {
                userId: javaRest.cookie.get('userId')
            },
            {
                put: {
                    method: 'PUT',
                    isArray: true,
                    headers: {
                        'Authorization': getPutAuthToken("reports/getMessageSummaryReport/", time, nonce),
                        'x-safeplanet-date': time,
                        'nonce': nonce,
                        'Content-Type': 'application/json'
                    },
                    transformRequest: function (data, headersGetter) {
                        var headers = headersGetter();
                        var time = javaRest.get_iso_date();
                        var nonce = makeRandomString();
                        headers['Content-Type'] = "application/json";
                        headers['Authorization'] = getPutAuthToken("reports/getMessageSummaryReport/", time, nonce);
                        headers['x-safeplanet-date'] = time;
                        headers['nonce'] = nonce;

                      
                        var fromDateVar = new Date();
                        var schoolId = 1000;
                        vehicleIdVar =$scope.vehicleId;
                        scheduleVar = 14;
                        fromDateVar = $scope.date;
                        toDateVar = $scope.toDate;

                        var filterData = '{"fromDate":"' + fromDateVar.getFullYear() + "-" + addZero(fromDateVar.getMonth() + 1) + "-" + addZero(fromDateVar.getDate()) + "T"
                            + addZero(fromDateVar.getHours()) + ":" + addZero(fromDateVar.getMinutes()) + ":00" + '", "schoolId":' + schoolId + ', "vehicleId":' + vehicleIdVar + ', "schedule":' + scheduleVar + '}';
                        return filterData;
                    }
                }
            }).put();
             var response = $scope.userData;
             response.$promise.then(function(data){
	            $scope.messagenotifi = data
	         for(var i=0;i<$scope.messagenotifi.length;i++){
	   	    var date = $filter('date')($scope.messagenotifi[i].processDate, "dd/MM/yyyy");
	   	    var date =date.replace(/\//g, "-");
	   	    $scope.messagenotifi[i].date= date;
	    }
	    $scope.record =$filter('filter')($scope.messagenotifi,{date:NotifiDate});
	    $scope.downloadReport = angular.copy($scope.record);
	});
   
 
	 
 }
 
 
 
    
});
