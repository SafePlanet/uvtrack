dashboardApp.controller('FleetReportController', function($scope, $rootScope, $resource, $timeout, $controller, $location, $http) {

	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

	$scope.back = function() {
		$location.path("/reports.html");
	}

	angular.extend(this, $controller('BaseReportController', {
		$scope : $scope
	}));
	$scope.date=new Date();
	var fTime=new Date();
    fTime.setHours(00);
    fTime.setMinutes(00);
    fTime.setSeconds(0,0)
    $scope.fromTime=fTime;
    
    var tTime=new Date();
    tTime.setHours(23);
    tTime.setMinutes(59);
    tTime.setSeconds(0,0);
    $scope.toTime=tTime;
	$scope.speedLimit = 60;
	$scope.haltLimit = 20;
	var currDate= new Date();
	$scope.showMap = function(fleetId) {
		$location.path("/myBus.html/" + fleetId);
	}
	
	$scope.loadFleetReport = function() {
		if ($scope.date > currDate) {
			bootbox.alert("Don't Select Future Date.");
			return;
		}  else if ($scope.fromTime  == undefined) {
			bootbox.alert("<b>Select To Time</b>");
			return;
		}else if ($scope.toTime == undefined) {
			bootbox.alert("<b>Select From Time</b>");
			return;
		}else if ($scope.fromTime > $scope.toTime) {
			bootbox.alert("<b>From Time should not be grater than To Time</b>");
			return;
		}
		else if ($scope.speedLimit == undefined) {
			bootbox.alert("<b>Enter Speed Limit.</b>");
			return;
		} else if ($scope.speedLimit < 0) {
			bootbox.alert("<b>Don't Enter negative Speed Limit</b>");
			return;
		} else if ($scope.haltLimit == undefined) {
			bootbox.alert("<b>Enter Halt Limit.</b>");
			return;
		} else if ($scope.haltLimit < 0) {
			bootbox.alert("<b>Don't Enter negative Halt Limit</b>");
			return;
		}
		$scope.fleetReportList = $resource('reports/getFleetSummary/:userId', {
			userId : javaRest.cookie.get('userId')
		}, {
			put : {
			method : 'PUT',
			isArray : true,
			headers : {
			'Authorization' : getPutAuthToken("reports/getFleetSummary/", time, nonce),
			'x-safeplanet-date' : time,
			'nonce' : nonce,
			'Content-Type' : 'application/json'
			},
			transformRequest : function(data, headersGetter) {
				var headers = headersGetter();
				var time = javaRest.get_iso_date();
				var nonce = makeRandomString();
				headers['Content-Type'] = "application/json";
				headers['Authorization'] = getPutAuthToken("reports/getFleetSummary/", time, nonce);
				headers['x-safeplanet-date'] = time;
				headers['nonce'] = nonce;

				var fromDateVar = new Date();
				var toTimeVar = new Date();
				var fromTimeVar = new Date();
				var vehicleIdVar = "-1";

				// vehicleIdVar = $scope.vehicleId;

				var speedLimitVar = $scope.speedLimit;
				var haltLimitVar = $scope.haltLimit;
				fromDateVar = $scope.date;
				toTimeVar = $scope.toTime;
                fromTimeVar=$scope.fromTime;

                var filterData = '{"fromDate":"' + fromDateVar.getFullYear() + "-" + addZero(fromDateVar.getMonth() + 1) + "-" + addZero(fromDateVar.getDate()) + "T" + addZero(fromTimeVar.getHours()) + ":" + addZero(fromTimeVar.getMinutes()) + ":00" + '", "toDate":"' + fromDateVar.getFullYear() + "-" + addZero(fromDateVar.getMonth() + 1) + "-" + addZero(fromDateVar.getDate()) + "T" + addZero(toTimeVar.getHours()) + ":" + addZero(toTimeVar.getMinutes()) + ":00" + '", "speedLimit":' + speedLimitVar + ', "haltLimit":' + haltLimitVar + ', "vehicleId":' + vehicleIdVar + '}';

				return filterData;
			}
			}
		}).put();

		$scope.fleetReportList.$promise.then(function(fleetReportList) {
			/*
			 * create 10 fleets at start. populate fleets available show fleets
			 * on UI that match the number of fleet size. hide the remaining
			 * 
			 * Limitation: if the #of fleets got from the server go above 10
			 * then additional $scope.fleet# will have to added and
			 * corrresponding calls to openstreetmap needs to be made
			 */
			for (var i = 0; i < $scope.fleetReportList.length; i++) {
				var fleet = $scope.fleetReportList[i];

				$http.get("http://nominatim.openstreetmap.org/reverse?format=json&lat=" + fleet.lattitude + "&lon=" + fleet.longitude + "&zoom=18&addressdetails=2").then(function(response1) {
					var data1 = angular.fromJson(response1.data);
					fleet.location = data1.address.road + " " + data1.address.suburb + " " + data1.address.city + " " + data1.address.state + " " + data1.address.postcode;

					if (fleet.location.error.indexOf("Unable to geocode") !== -1) {
						console.log("error in geocode -- fleet 1 ");
						fleet.location = "Location not found"
					}

				}, function(response1) { // error
					// scenario
					console.log("error occurred -- fleet 1 ");
					fleet.location = "error: location not found"
				});
			}
		})// promise

		setTimeout(function() {
			$scope.CreateDiv()
		}, 1000);
	}

	$scope.toggleDisplay = function(id) {// hide/show cards
		// on UI based off
		return !(id > $scope.fleetReportList.length);
	}

	$scope.CreateDiv = function() {
		for (var i = 2; i < 1; i++) { // vredkar 6/7/2017 -
			// making this block
			// unreachable as I
			// don't think it is
			// reqd.
			/*
			 * for(var i=2;i<1;i++){
			 * //alert($scope.fleetReportList[i].vehicle); alert("test");
			 * console.log("---test----"); var
			 * rowDiv=document.createElement("DIV");
			 * rowDiv.setAttribute("class","col-sm-9 col-md-6 col-lg-8");
			 * rowDiv.setAttribute("style","border:1px solid
			 * black;align:center;"); rowDiv.setAttribute("border","1"); var
			 * para = document.createElement("DIV");
			 * para.setAttribute("class","row"); var vehicleid =
			 * document.createTextNode($scope.fleetReportList[i].vehicle);
			 * rowDiv.appendChild(vehicleid); var
			 * BR=document.createElement("BR"); rowDiv.appendChild(BR);
			 * 
			 * var haltDuration =
			 * document.createTextNode($scope.fleetReportList[i].haltDuration);
			 * rowDiv.appendChild(haltDuration); var
			 * BR=document.createElement("BR"); rowDiv.appendChild(BR);
			 * 
			 * var moveDuration =
			 * document.createTextNode($scope.fleetReportList[i].moveDuration);
			 * rowDiv.appendChild(moveDuration); var
			 * BR=document.createElement("BR"); rowDiv.appendChild(BR);
			 * 
			 * var totalMovement =
			 * document.createTextNode($scope.fleetReportList[i].totalMovement);
			 * rowDiv.appendChild(totalMovement); var
			 * BR=document.createElement("BR"); rowDiv.appendChild(BR);
			 * 
			 * var avgMoveSpeed =
			 * document.createTextNode($scope.fleetReportList[i].avgMoveSpeed);
			 * rowDiv.appendChild(avgMoveSpeed); var
			 * BR=document.createElement("BR"); rowDiv.appendChild(BR);
			 * 
			 * var maxSpeed =
			 * document.createTextNode($scope.fleetReportList[i].maxSpeed);
			 * rowDiv.appendChild(maxSpeed); var
			 * BR=document.createElement("BR"); rowDiv.appendChild(BR);
			 * 
			 * var overSpeedEvents =
			 * document.createTextNode($scope.fleetReportList[i].overSpeedEvents);
			 * rowDiv.appendChild(overSpeedEvents); var
			 * BR=document.createElement("BR"); rowDiv.appendChild(BR);
			 * 
			 * var longHaltEvents =
			 * document.createTextNode($scope.fleetReportList[i].longHaltEvents);
			 * rowDiv.appendChild(longHaltEvents); var
			 * BR=document.createElement("BR"); rowDiv.appendChild(BR);
			 * 
			 * para.appendChild(rowDiv);
			 * //document.getElementById("maindiv").appendChild(para);
			 * 
			 */}// end for
	}

	$scope.downloadFleetReport = function() {
		$scope.downloadFleetReportData = $resource('reports/downloadReport/:userId/:reportName', {
		userId : javaRest.cookie.get('userId'),
		reportName : 'fleetReport'
		}, {
			get : {
			method : 'GET',
			isArray : false,
			headers : {
			'Authorization' : getAuthToken("reports/downloadReport/", time, nonce),
			'x-safeplanet-date' : time,
			'nonce' : nonce
			}
			}
		}).get();

		$scope.downloadFleetReportData.$promise.then(function(downloadFleetReportData) {
			window.open("uploads/reports/fleetSummaryReport_" + javaRest.cookie.get('userId') + ".xls");
		});
	}
});