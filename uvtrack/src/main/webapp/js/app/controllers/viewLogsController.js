dashboardApp.controller('viewLogsController', function($scope,$location,$rootScope,$resource, $timeout,$http,routeService) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left') || $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
 
	 $scope.back = function() {
			$location.path("/alert.html");
		    }
	
	$http.get('route/readLogFile/').success(function(data){
		$scope.readfile = data;
		//console.log(data);
	});
	
	
	
 });
