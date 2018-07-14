dashboardApp.controller('addBusController', function($scope, $rootScope,$window,$resource, routeService, $timeout, $http) {

	if (!$(".sidebar-offcanvas").hasClass('collapse-left') || $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}

	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

	$scope.fleetList = [];
	$scope.busData = {};
	$scope.expiringFleets = null;
	
	$scope.addBusFormModal = function() {
		$scope.detailsVisibility = true;
		$scope.busData = {};
		$('#addBusModal').modal('show');
	}
	

	$scope.submitBusForm = function() {
		var data = $scope.busData;
         console.log(data);
		var reqParamsArr = [];
		for ( var p in data) {
			reqParamsArr.push(encodeURIComponent(p) + "=" + encodeURIComponent(data[p]));
		}
		// reqParamsArr.push(encodeURIComponent("fleetID") + "="
		// + encodeURIComponent(fleetID));
		var reqParamData = reqParamsArr.join("&");
		// alert(reqParamData);

		$http({
			method : 'POST',
			url : 'jsp/addFleet.jsp',
			data : $scope.busData, // forms user object
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			},
			transformRequest : function(data, headersGetter) {
				var headers = headersGetter();
				headers['Content-Type'] = "application/x-www-form-urlencoded; charset=UTF-8;";
				return reqParamData;
			}
		}).success(function(data) {
			$('#addBusModal').modal('hide');
			if (data.errors) {
				$scope.successTextAlert = "Error";
				$scope.showSuccessAlert = true;

				// switch flag
				$scope.switchBool = function(value) {
					$scope[value] = !$scope[value];
				};
			} else {
				$scope.successTextAlert = "Saved!!";
				$scope.showSuccessAlert = true;

				// switch flag
				$scope.switchBool = function(value) {
					$scope[value] = !$scope[value];
				};

				$timeout(function() {
					//location.reload();
					$http.get('route/findAllFleetList/' + javaRest.cookie.get('userId')).success(function(fleets) {
						
						for(var i=0;i<fleets.length;i++)
						{
							fleets[i].isExpiring = isExpiring(fleets[i]);
						}
						$scope.fleetList = fleets;
						
						$scope.expiringFleets = fleets.filter(function(f){return f.isExpiring;}).map(function(f){return f.regNumber;}).join(',');
					});
					$scope.showSuccessAlert = false;
				}, 2000);
			}
		});
	}
	routeService.getUnassignedDevice().$promise.then(function(DeivceList) {
		$scope.DeivceList = DeivceList;
	});

	$http.get('route/findAllFleetList/' + javaRest.cookie.get('userId')).success(function(fleets) {
		
		for(var i=0;i<fleets.length;i++)
		{
			fleets[i].isExpiring = isExpiring(fleets[i]);
		}
		$scope.fleetList = fleets;
		
		$scope.expiringFleets = fleets.filter(function(f){return f.isExpiring;}).map(function(f){return f.regNumber;}).join(',');
	});

	 function isExpiring(fleet) {
		var serviceEndDate = new Date(fleet.device.serviceEndDate);

		var diff = Math.abs(serviceEndDate.getTime() - new Date().getTime());
		var days = diff / (1000 * 60 * 60 * 24);
		
		return days <= 7;
	}
	$scope.modifyBusForm = function(fleetID) {
       
		$scope.modifyBusDetails = $resource('route/getFleetDetail/:userId/:fleetID', {
			userId : javaRest.cookie.get('userId'),
			fleetID : fleetID
		}, {
			get : {
				method : 'GET',
				isArray : false,
				headers : {
					'Authorization' : getAuthToken("route/getFleetDetail/", time, nonce),
					'x-safeplanet-date' : time,
					'nonce' : nonce
				}
			}
		}).get();

		$scope.modifyBusDetails.$promise.then(function(modifyBusDetails) {
			$scope.busData = modifyBusDetails;
			$scope.detailsVisibility = false;
			$scope.busData.fleetMake = new Date(modifyBusDetails.fleetMake);
			$scope.busData.fleetID = fleetID;
			$('#addBusModal').modal('show');

		});
	};

});