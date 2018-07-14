dashboardApp
		.controller(
				'ManageDevicesController',
				function($scope, $rootScope, $resource, $timeout, $http) {
				
					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}
					// $rootScope.fixLeftSideBar();
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					$scope.deviceData = {};
                    $scope.selectedSchoolId='';

					$scope.addDeviceFormModal = function() {
						$scope.deviceData = {};
						$('#addDeviceModal').modal('show');
					}

					$scope.submitDeviceForm = function() {
						var data = $scope.deviceData;
						if($scope.user.role=='superAdmin')
						{data.schoolId=$scope.selectedSchoolId;
						}else{
							data.schoolId=0;
						}
						
                        if(data.id == null){
                          data.schoolId = $scope.selectedSchoolId || 0;
                          data.frqMode="T";
                          data.frqUpdateDist="0";
                          data.uuid =javaRest.cookie.get('userId');
                        }
						
                        console.log(data);
                        
						var reqParamsArr = [];
						for ( var p in data) {
							reqParamsArr.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(data[p]));
						}
						// reqParamsArr.push(encodeURIComponent("fleetID") + "="
						// + encodeURIComponent(fleetID));
						var reqParamData = reqParamsArr.join("&");
						// alert(reqParamData);
						var currDate = new Date();
						if ($scope.deviceData.manfDate != undefined
								&& $scope.deviceData.manfDate >= currDate) {
							alert("Mfg. Date should be less than today's date.");
							return;
						}
						if ($scope.deviceData.puchaseDate != undefined
								&& $scope.deviceData.puchaseDate > currDate) {
							alert("Purchase Date should be less than or equal to today's date.");
							return;
						}
						if ($scope.deviceData.puchaseDate != undefined
								&& $scope.deviceData.manfDate != undefined
								&& $scope.deviceData.puchaseDate <= $scope.deviceData.manfDate) {
							alert("Purchase Date should be greate than Mfg. Date.");
							return;
						}
						if ($scope.deviceData.warrantyDate == undefined) {
							alert("Enter warranty Date.");
							return;
						}
						if ($scope.deviceData.mobileNumber != undefined
								&& $scope.deviceData.mobileNumber.length < 10) {
							alert("Mobile Number should be 10 digits.");
							return;
						}
						$http(
								{
									method : 'POST',
									url : 'jsp/addDevice.jsp',
									data : $scope.deviceData, // forms user
									// object
									headers : {
										'Content-Type' : 'application/x-www-form-urlencoded'
									},
									transformRequest : function(data,
											headersGetter) {
										var headers = headersGetter();
										headers['Content-Type'] = "application/x-www-form-urlencoded; charset=UTF-8;";
										return reqParamData;
									}
								}).success(function(data) {
							$('#addDeviceModal').modal('hide');
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
									$http.get('route/getAllDevices/' + javaRest.cookie.get('userId')).success(function(Device) {
										
										$scope.deviceList = Device;//.filter(function(x){return temp.indexOf( x.deviceModelId)!=-1;});
										
									});
									$scope.showSuccessAlert = false;
								}, 2000);
							}
						});
					}
					
					//===============================Show all ScoolList=======================//
					  $http.get('user/getSchoolDetails/' + javaRest.cookie.get('userId')).success(function(data) {
							$scope.schoolList = data;
							console.log(data);
						});
					//============================End=========================================//

					
					$http.get('route/getAllDevices/' + javaRest.cookie.get('userId')).success(function(Device) {
						
						//$scope.deviceList = Device;

						var temp=[];
						var temp2=[];
						for(var i=0;i<Device.length;i++)
						{
							Device[i].isExpiring = isExpiring(Device[i]);
							if(temp.indexOf(Device[i].deviceModelId)==-1){
								temp.push(Device[i].deviceModelId);
								temp2.push(Device[i]);
							}
							
							
						}
						$scope.deviceList = Device;//.filter(function(x){return temp.indexOf( x.deviceModelId)!=-1;});
						 $http.get('user/getDeviceModelId/' + javaRest.cookie.get('userId')).success(function(data) {
								$scope.deviceListDP = data;
								console.log(data);
							});	
						$scope.expiringFleets = Device.filter(function(f){return f.isExpiring;}).map(function(f){return f.name;}).join(',');
						
					});
					
					 function isExpiring(Device) {
							var serviceEndDate = new Date(Device.serviceEndDate);

							var diff = Math.abs(serviceEndDate.getTime() - new Date().getTime());
							var days = diff / (1000 * 60 * 60 * 24);
							
							return days <= 7;
						}
					
					
					
					$scope.modifyDeviceForm = function(deviceId) {

						$scope.modifyDeviceDetails = $resource(
								'route/getDeviceDetail/:userId/:deviceId',
								{
									userId : javaRest.cookie.get('userId'),
									deviceId : deviceId
								},
								{
									get : {
										method : 'GET',
										isArray : false,
										headers : {
											'Authorization' : getAuthToken(
													"route/getDeviceDetail/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce
										}
									}
								}).get();

						$scope.modifyDeviceDetails.$promise.then(function(
								modifyDeviceDetails) {
							$scope.deviceData = modifyDeviceDetails;
							// $scope.deviceData.fleetMake = new
							// Date(modifyDeviceDetails.fleetMake);
							$scope.deviceData.manfDate = new Date(
									modifyDeviceDetails.manfDate);
							$scope.deviceData.serviceEndDate = new Date(
									modifyDeviceDetails.serviceEndDate);
							$scope.deviceData.puchaseDate = new Date(
									modifyDeviceDetails.puchaseDate);
							$scope.deviceData.warrantyDate = new Date(
									modifyDeviceDetails.warrantyDate);
							$scope.deviceData.deviceId = deviceId;
							$scope.selectedSchoolId= modifyDeviceDetails.schoolId;
							if($scope.user.role=='superAdmin'){
								$scope.mobileNumberExpression=false;
								$scope.warrantyDateExpression=false;
								$scope.puchaseDateExpression=false;
								$scope.manfDateExpression=false;
							}else{
								$scope.mobileNumberExpression=true;
								$scope.warrantyDateExpression=true;
								$scope.puchaseDateExpression=true;
								$scope.manfDateExpression=true;
							}
							$('#addDeviceModal').modal('show');

						});
					};

				});
