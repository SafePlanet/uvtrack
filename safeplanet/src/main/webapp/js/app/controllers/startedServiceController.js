dashboardApp.controller('startedServiceController', function($scope,$location,$rootScope,$resource, $timeout,$http,routeService) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left') || $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}

	
	$http.get('route/startedService/').success(function(data){
		
	});
	
	
 });
