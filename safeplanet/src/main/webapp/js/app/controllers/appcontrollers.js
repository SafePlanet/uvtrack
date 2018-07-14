'use strict';
var dashboardApp = angular.module("dashboardApp");

/*
 * myServices.factory('Auth', ['$resource', function($resource){ return { Login:
 * $resource(serviceURL + 'login', {}, { go: { method:'POST', isArray: false
 * }}), Logout: $resource(serviceURL + 'logout', {}, { go: { method:'POST',
 * isArray: false }}), Register: $resource(serviceURL + 'register', {}, { go: {
 * method:'POST', isArray: false }}), }; } ]); //Auth.Login.go({ username:
 * $scope.username, password: $scope.password })//from controller
 * 
 */

dashboardApp.controller('UserController', function($scope, $rootScope,
		$resource, $location, $http, $compile, userService) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	$scope.user = $resource('user/:userId', {
		userId : javaRest.cookie.get('userId')
	}, {
		get : {
			method : 'GET',
			isArray : false,
			headers : {
				'Authorization' : getAuthToken("user/", time, nonce),
				'x-safeplanet-date' : time,
				'nonce' : nonce
			}
		}
	}).get();

	$scope.user.$promise.then(function(user) {
		$rootScope.homeUserName = $scope.user.firstName + ' '
				+ $scope.user.lastName;
		$rootScope.userHomefirstName = $scope.user.firstName;
		userService.setUser(user);
		$rootScope.userHomeImage = "img/avatar5.png";
		if ($scope.user.userImage != undefined && $scope.user.userImage != null
				&& $scope.user.userImage != 'null'
				&& $scope.user.userImage != '') {
			$rootScope.userHomeImage = "uploads/userImages/"
					+ $scope.user.userImage;
		}

	});

	$rootScope.gotoURL = function(path) {
		$location.path(path);
	};

	var req = {
		url : 'user/userHome/' + javaRest.cookie.get('userId'),
		method : 'GET',
		headers : {
			'Authorization' : getAuthToken("user/userHome/", time, nonce),
			'x-safeplanet-date' : time,
			'nonce' : nonce
		},
		data : {}
	};
	$http(req).then(function(response) {
		var linkingFunction = $compile(response.data);
		var elem = linkingFunction($scope);
		// alert(response.data);
		$rootScope.userSidebarLinks = elem;
		if ($(".sidebar-menu").length <= 0) {
			$(".sidebar").append($rootScope.userSidebarLinks);
			$(".treeview").tree();
		}
		// $(".sidebar").append(elem);
		// $(".treeview").tree();
	}, function() {
		// alert("Error::");
	});

	/*
	 * $rootScope.fixLeftSideBar = function() {
	 * //alert($(".sidebar-menu").length); if($(".sidebar-menu").length<=0) {
	 * $(".sidebar").append($rootScope.userSidebarLinks); $(".treeview").tree(); } }
	 */
});

dashboardApp
		.controller(
				'DashboardController',
				function($scope, $rootScope, $resource, $compile, $http,
						$route, homeService) {
					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();
					 window.userId = javaRest.cookie.get('userId');
					homeService.getUserDashboard().$promise
							.then(function(dashboardLinks) {

								$rootScope.dashboardLinks = dashboardLinks;
                               // console.log(dashboardLinks);
                                console.log(window.userId);
								$rootScope.getClass = function getClass(idx) {
									if ($rootScope.dashboardLinks.linksNameList[idx] == 'Student Details') {
										return "treeview";
									} else {
										return "";
									}
								};

								$rootScope.getLink = function getLink(idx) {
									if ($rootScope.dashboardLinks.linksNameList[idx] == 'Student Details') {
										// return "#student.html";
										return $rootScope.dashboardLinks.linksList[idx];
									} else {
										return $rootScope.dashboardLinks.linksList[idx];
									}
								};

								$rootScope.openMenu = function openMenu() {
									// $(".treeview").tree();
									// $(".sidebar .treeview").tree();
								};

								$rootScope.logout = function logout() {
									javaRest.cookie.remove('token');
									javaRest.cookie.remove('userId');
									javaRest.cookie.remove('email');
									javaRest.cookie.remove('message');
									store.clear();
									window.location = 'index.html';
								};
							});

					var req = {
						url : 'user/userHome/' + javaRest.cookie.get('userId'),
						method : 'GET',
						headers : {
							'Authorization' : getAuthToken("user/userHome/",
									time, nonce),
							'x-safeplanet-date' : time,
							'nonce' : nonce
						},
						data : {}
					};
					$http(req).then(function(response) {
						var linkingFunction = $compile(response.data);
						var elem = linkingFunction($scope);
						// alert(response.data);
						// $rootScope.userSidebarLinks = elem;

						// $(".sidebar").append(elem);
						// $(".treeview").tree();
					}, function() {
						// alert("Error::");
					});
					if($scope.user.role='administrator'){
						window.message=javaRest.cookie.get('message')
						console.log(window.message)
						if(window.message > 0 && window.message < 14){
							bootbox.alert("Your account is about to expire in "+"" +window.message + " Days. Please Renew your account");
						}
					}
				});

dashboardApp
		.controller(
				'HomeController',
				function($scope, $rootScope, $resource, $compile, $http,
						homeService) {
					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					homeService.getUserDashboard().$promise
							.then(function(dashboardLinks) {
								$rootScope.dashboardLinks = dashboardLinks;

								$rootScope.getClass = function getClass(idx) {
									if ($rootScope.dashboardLinks.linksNameList[idx] == 'Student Details') {
										return "treeview";
									} else {
										return "";
									}
								};

								$rootScope.getLink = function getLink(idx) {
									if ($rootScope.dashboardLinks.linksNameList[idx] == 'Student Details') {
										// return "#student.html";
										return $rootScope.dashboardLinks.linksList[idx];
									} else {
										return $rootScope.dashboardLinks.linksList[idx];
									}
								};

								$rootScope.logout = function logout() {
									javaRest.cookie.remove('token')
									javaRest.cookie.remove('userId')
									javaRest.cookie.remove('email')
									store.clear()
									window.location = 'index.html'
								};
							});
				});

dashboardApp.controller('AlertsController', function($scope, $rootScope,
		$resource, $interval,$timeout,$http) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	// $rootScope.fixLeftSideBar();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

	$scope.updatePickUpResource = $resource(
			'user/updatePickUpRequest/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				save : {
					method : 'PUT',
					isArray : false,
					headers : {
						'Authorization' : getPutAuthToken(
								"user/updatePickUpRequest/", time,
								nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce,
						'Content-Type' : 'application/json'
					},
					transformRequest : function(data,
							headersGetter) {
						var headers = headersGetter();
						var time = javaRest.get_iso_date();
						var nonce = makeRandomString();
						headers['Content-Type'] = "application/json";
						headers['Authorization'] = getPutAuthToken(
								"user/updatePickUpRequest/", time,
								nonce);
						headers['x-safeplanet-date'] = time;
						headers['nonce'] = nonce;

						var presenceData = '{"presentFlag":"'
								+ data.presentFlag
								+ '"}';
						return presenceData;
					}
				}
			});

	$scope.markViewedAlerts = function() {
          $scope.alerts.totalElements=0;
          $scope.presenceData = {};
			$scope.presenceData.presentFlag ='U';
			
			$scope.updatePickUpResource
					.save(
							{
								userId : javaRest.cookie
										.get('userId')
							},
							$scope.presenceData,
							function(resp, headers) {
							 $scope.switchBool = function(value) {
									$scope[value] = !$scope[value];
								};

								$timeout(
										function() {
                                          $http.get('alerts/getPickUpAlert/' + javaRest.cookie.get('userId')).success(function(pickUpalerts) {
										  $scope.pickUpalerts = pickUpalerts;
											});
											
										}, 50);
							},
							function(err) {
							
								$scope.switchBool = function(value) {
									$scope[value] = !$scope[value];
								};
							});
		}
		
	

	var refreshAlerts = function() {
		$scope.alerts = $resource('alerts/:userId', {
			userId : javaRest.cookie.get('userId')
		}, {
			get : {
				method : 'GET',
				isArray : false,
				headers : {
					'Authorization' : getAuthToken("alerts/", time, nonce),
					'x-safeplanet-date' : time,
					'nonce' : nonce
				}
			}
		}).get();

		$scope.alerts.$promise.then(function(alerts) {

			// alert($scope.alerts.totalElements);
			// alert( $scope.alerts.content);

			$rootScope.alerts = $scope.alerts;
             $scope.totalno=alerts.totalElements;
		});
	}
	
	refreshAlerts();
	$interval(refreshAlerts,60000)
	
});

dashboardApp.controller('ViewAllAlertsController', function($scope, $rootScope,
		$resource, $interval,$timeout,$http) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	// $rootScope.fixLeftSideBar();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	
	$scope.aprovePickUpResource = $resource(
			'user/aprovePickUpRequest/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				save : {
					method : 'PUT',
					isArray : false,
					headers : {
						'Authorization' : getPutAuthToken(
								"user/aprovePickUpRequest/", time,
								nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce,
						'Content-Type' : 'application/json'
					},
					transformRequest : function(data,
							headersGetter) {
						var headers = headersGetter();
						var time = javaRest.get_iso_date();
						var nonce = makeRandomString();
						headers['Content-Type'] = "application/json";
						headers['Authorization'] = getPutAuthToken(
								"user/aprovePickUpRequest/", time,
								nonce);
						headers['x-safeplanet-date'] = time;
						headers['nonce'] = nonce;

						var presenceData = '{"uuid":"'
								+ data.uuid
								+'","requestAction":"'
									+ data.requestAction
									+ '"}';
						return presenceData;
					}
				}
			});
			
			$scope.aproveRequest = function aproveRequest(uuid,char) {

				$scope.presenceData = {};
				$scope.presenceData.uuid =uuid;
				$scope.presenceData.requestAction=char;
				
				$scope.aprovePickUpResource
						.save(
								{
									userId : javaRest.cookie
											.get('userId')
								},
								$scope.presenceData,
								function(resp, headers) {
									//$scope.absentStudentDetail.presentFlag = 'A';
									// success callback
									// console.log(resp);
									if($scope.presenceData.requestAction=='A'){
									$scope.successTextAlert = "Request Approved";
									}else if($scope.presenceData.requestAction=='R'){
										$scope.successTextAlert = "Request Rejected";
									}
									$scope.showSuccessAlert = true;
									
									// switch flag
									$scope.switchBool = function(value) {
										$scope[value] = !$scope[value];
									};

									$timeout(
											function() {
												refreshpickUpAlert();
												
												$scope.showSuccessAlert = false;
											}, 2000);
								},
								function(err) {
									// error callback
									// console.log(err);
									$scope.successTextAlert = "Failed to send message.";
									$scope.showSuccessAlert = true;

									// switch flag
									$scope.switchBool = function(value) {
										$scope[value] = !$scope[value];
									};
								});
			}
	

	$scope.alerts = $resource(
			'alerts/getAllAlerts/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'Authorization' : getAuthToken("alerts/getAllAlerts/",
								time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();

	$scope.alerts.$promise.then(function(alerts) {

		// alert($scope.alerts.totalElements);
		// alert( $scope.alerts.content);

		// $rootScope.alerts = $scope.alerts;

	});
	var refreshpickUpAlert = function() {
		$scope.pickUpalerts = $resource('alerts/getPickUpAlert/:userId', {
			userId : javaRest.cookie.get('userId')
		}, {
			get : {
				method : 'GET',
				isArray : true,
				headers : {
					'Authorization' : getAuthToken("alerts/getPickUpAlert/", time, nonce),
					'x-safeplanet-date' : time,
					'nonce' : nonce
				}
			}
		}).get();

		$scope.pickUpalerts.$promise.then(function(alerts) {

			// alert($scope.alerts.totalElements);
			// alert( $scope.alerts.content);

			$rootScope.alerts = $scope.alerts;

		});
	}

	refreshpickUpAlert();
		
});

dashboardApp.controller('EditProfileController', function($scope, $rootScope,
		$resource, $timeout, $location) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	// $rootScope.fixLeftSideBar();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	$scope.userResource = $resource('user/:userId', {
		userId : javaRest.cookie.get('userId')
	}, {
		get : {
			method : 'GET',
			isArray : false,
			headers : {
				'Authorization' : getAuthToken("user/", time, nonce),
				'x-safeplanet-date' : time,
				'nonce' : nonce
			}

		}
	});
	$scope.user = $scope.userResource.get();
	$scope.user.$promise.then(function(user) {
		$scope.firstName = $scope.user.firstName;
		$scope.lastName = $scope.user.lastName;
		$scope.mobile = $scope.user.mobile;
		$scope.emailAddress = $scope.user.emailAddress;
		$scope.houseNo = $scope.user.houseNo;
		$scope.address = $scope.user.address;
		$scope.city = $scope.user.city;
		$scope.state = $scope.user.state;
		$scope.pinCode = $scope.user.pinCode;
		// $scope.userPhoto = '';
	});

	$scope.saveProfile = function saveProfile() {
		$scope.updateUserResource = $resource('user/saveUserDetails/:userId', {
			userId : javaRest.cookie.get('userId')
		}, {
			save : {
				method : 'POST',
				isArray : false,
				headers : {
					'Authorization' : getPostAuthToken("user/saveUserDetails/",
							time, nonce),
					'x-safeplanet-date' : time,
					'nonce' : nonce,
					'Content-Type' : undefined
				},
				transformRequest : function(data, headersGetter) {
					var headers = headersGetter();
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();
					headers['Content-Type'] = undefined;
					headers['Authorization'] = getPostAuthToken(
							"user/saveUserDetails/", time, nonce);
					headers['x-safeplanet-date'] = time;
					headers['nonce'] = nonce;
					var formFields = angular.toJson(data, true);
					// alert(angular.toJson(data, true));
					/*
					 * if (data === undefined) return data;
					 * 
					 * var fd = new FormData(); fd.append("userPhoto",
					 * document.getElementsByName("userPhoto")[0]);
					 * angular.forEach(data, function(value, key) { if (value
					 * instanceof FileList) { if (value.length == 1) {
					 * fd.append(key, value[0]); } else { angular.forEach(value,
					 * function(file, index) { fd.append(key + '_' + index,
					 * file); }); } } else { fd.append(key, value); } });
					 */

					var form = $('form')[0];
					var fd = new FormData(form);
					fd.append("userDetails", formFields);
					return fd;
				}
			}
		});

		$scope.user.firstName = $scope.firstName;
		$scope.user.lastName = $scope.lastName;
		$scope.user.mobile = $scope.mobile;
		$scope.user.emailAddress = $scope.emailAddress;
		$scope.user.houseNo = $scope.houseNo;
		$scope.user.address = $scope.address;
		$scope.user.city = $scope.city;
		$scope.user.state = $scope.state;
		$scope.user.pinCode = $scope.pinCode;
		
		$scope.updateUserResource.save({
			userId : javaRest.cookie.get('userId')
		}, $scope.user, function(resp, headers) {
			// success callback
			// console.log(resp);
			$scope.successTextAlert = "Saved";
			$scope.showSuccessAlert = true;

			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};
			$rootScope.homeUserName = resp.firstName + ' ' + resp.lastName;
			$rootScope.userHomefirstName = resp.firstName;
			// $scope.userImage = "img/avatar5.png";
			if (resp.userImage != undefined && resp.userImage != null
					&& resp.userImage != 'null' && resp.userImage != '') {
				$rootScope.userImage = "uploads/userImages/" + resp.userImage;
			}

			$timeout(function() {
				$scope.showSuccessAlert = false;
				// $location.path("/home.html");
				window.location = "welcome.html";
			}, 1000);
		}, function(err) {
			// error callback
			// console.log(err);
			// $scope.failedTextAlert = "Error: ";
			$scope.failedTextAlert = "Error: "
					+ err.data.validationErrors[0].message;
			$scope.showFailedAlert = true;

			$timeout(function() {
				$scope.failedTextAlert = false;
			}, 3000);

			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};
		});
	};

});

dashboardApp
.controller(
		'StudentProfileController',
		function($scope, $rootScope, $resource, $timeout, $location,$http) {

			if (!$(".sidebar-offcanvas").hasClass('collapse-left')
					|| $(".row-offcanvas").hasClass('active')) {
				$("[data-toggle='offcanvas']").click();
				$(".row-offcanvas").removeClass('active');
			}
			// $rootScope.fixLeftSideBar();
			$scope.absentFromDate = new Date();
			$scope.absentToDate = new Date();
			var today = new Date();
			var time = javaRest.get_iso_date();
			var nonce = makeRandomString();
            $scope.absentReasons = $resource(
					'user/absentReasons/:userId',
					{
						userId : javaRest.cookie.get('userId')
					},
					{
						get : {
							method : 'GET',
							isArray : true,
							headers : {
								'Authorization' : getAuthToken(
										"user/absentReasons/", time,
										nonce),
								'x-safeplanet-date' : time,
								'nonce' : nonce
							}
						}
					}).get();

			$scope.absentReasons.$promise.then(function(absentReasons) {
				
				
			});

			$scope.studentDetailList = $resource(
					'user/getStudentDetails/:userId',
					{
						userId : javaRest.cookie.get('userId')
					},
					{
						get : {
							method : 'GET',
							isArray : true,
							headers : {
								'Authorization' : getAuthToken(
										"user/student/", time, nonce),
								'x-safeplanet-date' : time,
								'nonce' : nonce
							}
						}
					}).get();

			$scope.studentDetailList.$promise.then(function(
					studentDetailList) {
            });
			$scope.studentAbsentRecord = $resource(
					'user/getStudentAbsentRecord/:userId',
					{
						userId : javaRest.cookie.get('userId')
					},
					{
						get : {
							method : 'GET',
							isArray : true,
							headers : {
								'Authorization' : getAuthToken(
										"user/student/", time, nonce),
								'x-safeplanet-date' : time,
								'nonce' : nonce
							}
						}
					}).get();

			$scope.studentAbsentRecord.$promise.then(function(
					studentAbsentRecord) {
                 $scope.studentAbsentRecord=studentAbsentRecord
			});

			$scope.addStudent = function addStudent() {
				$scope.addStudentResource = $resource(
						'user/addUpdateStudent/:userId',
						{
							userId : javaRest.cookie.get('userId')
						},
						{
							save : {
								method : 'PUT',
								isArray : false,
								headers : {
									'Authorization' : getPutAuthToken(
											"user/addUpdateStudent/",
											time, nonce),
									'x-safeplanet-date' : time,
									'nonce' : nonce,
									'Content-Type' : 'application/json'
								},
								transformRequest : function(data,
										headersGetter) {
									var headers = headersGetter();
									var time = javaRest.get_iso_date();
									var nonce = makeRandomString();
									headers['Content-Type'] = "application/json";
									headers['Authorization'] = getPutAuthToken(
											"user/addUpdateStudent/",
											time, nonce);
									headers['x-safeplanet-date'] = time;
									headers['nonce'] = nonce;

									var studentData = '{"regId":"'
											+ $scope.regId
											+ '", "firstName":"'
											+ $scope.firstName
											+ '","lastName":"'
											+ $scope.lastName
											+ '", "studentClass":"'
											+ $scope.studentClass
											+ '", "section":"'
											+ $scope.section + '"}';
									return studentData;
								}
							}
						});

				$scope.student = {};
				$scope.student.regId = $scope.regId;
				$scope.student.firstName = $scope.firstName;
				$scope.student.lastName = $scope.lastName;
				$scope.student.studentClass = $scope.studentClass;
				$scope.student.section = $scope.section;
				$scope.student.wayPointId = $scope.wayPointId;
				

				$scope.addStudentResource
						.save(
								{
									userId : javaRest.cookie
											.get('userId')
								},
								$scope.student,
								function(resp, headers) {

									// console.log(resp);
									$scope.successTextAlert = "Student details has been added. You can view the details once after approved by Administrator, please check back later.";
									$scope.showSuccessAlert = true;

									// switch flag
									$scope.switchBool = function(value) {
										$scope[value] = !$scope[value];
									};

									$timeout(
											function() {
												$scope.showSuccessAlert = false;
												$location
														.path("/student.html");
											}, 10000);
								},
								function(err) {
									// error callback
									// console.log(err);
									$scope.successTextAlert = "Error: "
											+ err.data.validationErrors[0].message;
									$scope.showSuccessAlert = true;

									// switch flag
									$scope.switchBool = function(value) {
										$scope[value] = !$scope[value];
									};
								});
			};

			$scope.sendAbsentMessageResource = $resource(
					'user/absentMessage/:userId',
					{
						userId : javaRest.cookie.get('userId')
					},
					{
						save : {
							method : 'PUT',
							isArray : false,
							headers : {
								'Authorization' : getPutAuthToken(
										"user/absentMessage/", time,
										nonce),
								'x-safeplanet-date' : time,
								'nonce' : nonce,
								'Content-Type' : 'application/json'
							},
							transformRequest : function(data,
									headersGetter) {
								var headers = headersGetter();
								var time = javaRest.get_iso_date();
								var nonce = makeRandomString();
								headers['Content-Type'] = "application/json";
								headers['Authorization'] = getPutAuthToken(
										"user/absentMessage/", time,
										nonce);
								headers['x-safeplanet-date'] = time;
								headers['nonce'] = nonce;

								var presenceData = '{"studentId":"'
										+ data.studentId
										+ '", "presentFlag":"'
										+ data.presentFlag
										+ '", "absentReasonId":"'
										+ data.absentReasonId
										+ '", "absentFromDate":"'
										+ data.absentFromDate
										+ '", "absentToDate":"'
										+ data.absentToDate + '"}';
								return presenceData;
							}
						}
					});
			

			var absentStudentDetailId;
		
			$scope.showAbsentModal = function showAbsentModal(
					studentDetail) {
                $scope.absentMessage = $scope.absentReasons[0];
				$scope.studentList=$scope.studentDetailList[0];
				$('#absentMessageModal').modal('show');
				$scope.successTextAlert = "";
				$scope.disableButton = false;
			};
			$scope.disableButton = false;
			$scope.sendAbsentMessage = function sendAbsentMessage() {

				if ($scope.absentFromDate == undefined
						|| $scope.absentFromDate < today) {
					alert("From Date can not be blank or less then today.");
					return;
				} else if ($scope.absentToDate == undefined
						|| $scope.absentToDate < today) {
					alert("End Date can not be blank or less then today.");
					return;
				} else if ($scope.absentFromDate > $scope.absentToDate) {
					alert("From Date can not be after end date");
					return;
				}
               
				$scope.presenceData = {};
				$scope.presenceData.studentId = $scope.studentId;
				$scope.presenceData.presentFlag = 'A';
				$scope.presenceData.absentReasonId = $scope.absentMessage.id;
				$scope.presenceData.message = $scope.absentMessage.reason;
				$scope.presenceData.absentFromDate = javaRest
						.get_custom_iso_date($scope.absentFromDate);
				$scope.presenceData.absentToDate = javaRest
						.get_custom_iso_date($scope.absentToDate);
				// $scope.presenceData.message =
				// $("#absentMessage_"+studentDetail.studentId).val();
				$scope.disableButton = true;
				$scope.sendAbsentMessageResource
						.save(
								{
									userId : javaRest.cookie
											.get('userId')
								},
								$scope.presenceData,
								function(resp, headers) {
									//$scope.absentStudentDetail.presentFlag = 'A';
									// success callback
									// console.log(resp);
									$scope.successTextAlert = "Saved";
									$scope.showSuccessAlert = true;
									$('#absentMessageModal').modal(
											'hide');
									// switch flag
									$scope.switchBool = function(value) {
										$scope[value] = !$scope[value];
									};

									$timeout(
											function() {
												$http.get('user/getStudentAbsentRecord/' + javaRest.cookie.get('userId')).success(function(studentAbsentRecord) {
												
												$scope.studentAbsentRecord = studentAbsentRecord;
												
											});
												$scope.showSuccessAlert = false;
											}, 3000);
								},
								function(err) {
									// error callback
									// console.log(err);
									$scope.successTextAlert = "Failed to send message.";
									$scope.showSuccessAlert = true;

									// switch flag
									$scope.switchBool = function(value) {
										$scope[value] = !$scope[value];
									};
								});
			}
		});

dashboardApp
		.controller(
				'BusLocationController',
				function($scope, $rootScope, $resource, $http, $interval,
						leafletData) {

					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}
					// $rootScope.fixLeftSideBar();
			
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					angular
							.extend(
									$scope,
									{
										india : {
											lat : 28.53,
											lng : 77.38,
											zoom : 15
										},
										layers : {
											// baselayers : {
											// googleRoadmap : {
											// name : 'Google Streets',
											// layerType : 'ROADMAP',
											// type : 'google'
											// }
											// }
											baselayers : {
												osm : {
													name : 'OpenStreetMap',
													url : 'https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}',
													type : 'xyz'
												}
											}
										},
										studentPaths : {}
									});

					var local_icons = {
						default_icon : {},
						leaf_icon : {
							iconUrl : 'icons/bus.svg',
							shadowUrl : 'icons/bus.svg',
							iconSize : [ 38, 95 ], // size of the icon
							shadowSize : [ 50, 64 ], // size of the shadow
							iconAnchor : [ 22, 94 ], // point of the icon
														// which will correspond
														// to marker's location
							shadowAnchor : [ 4, 62 ], // the same for the
														// shadow
							popupAnchor : [ -3, -76 ]
						// point from which the popup should open relative to
						// the iconAnchor
						},
						div_icon : {
							type : 'div',
							iconSize : [ 230, 0 ],
							html : 'Using <strong>Bold text as an icon</strong>: Lisbon',
							popupAnchor : [ 0, 0 ]
						},
						orange_leaf_icon : {
							iconUrl : 'icons/bus.svg',
							shadowUrl : 'icons/bus.svg',
							iconSize : [ 38, 95 ],
							shadowSize : [ 50, 64 ],
							iconAnchor : [ 22, 94 ],
							shadowAnchor : [ 4, 62 ]
						}
					};
					angular.extend($scope, {
						icons : local_icons
					});

					// $scope.loadPaths = function loadPaths() {
					/*
					 * $http.get('json/paths.json').success(function(data) {
					 * $scope.studentPaths = data.paths; $scope.markers =
					 * data.markers; });
					 */
					// };
					/*
					 * $scope.changePaths = function changePaths() {
					 * console.log($scope.studentPaths);
					 * $scope.studentPaths.p1.latlngs[0] = { lat: 53, lng: -0.1 }; };
					 * 
					 */
					$scope.currentPos = function(){
				     navigator.geolocation.getCurrentPosition(function(position) {
					            var pos = {
					            		latitude: position.coords.latitude,
					            		longitude: position.coords.longitude
					            };
					            
					            $rootScope.lat = pos.latitude;
							     $rootScope.lang =pos.longitude;
							     
							     $scope.markers = {cur0: {lat: $rootScope.lat, lng: $rootScope.lang, message: "",icon:{iconUrl: "", iconSize: [24, 24], iconAnchor: [12, 24], popupAnchor: [0, 0],shadowAnchor:[0, 0],shadowSize:[0, 0]}}}
		                          $scope.studentPaths = {};
									$scope.markers = $scope.markers;
									$scope.india.lat = $scope.markers["cur0"].lat;
									$scope.india.lng = $scope.markers["cur0"].lng;
									leafletData
									.getMap("mapDivId")
									.then(function(mapL) {
										
									});
						 });
					
						
					}
					
					$scope.BusStop = function(){
						
					}
				
					var req2 = {
							url : 'route/getDeviceCurrentLocation/'
									+ javaRest.cookie.get('userId'),
							method : 'GET',
							headers : {
								'Authorization' : getAuthToken(
										"route/getDeviceCurrentLocation/", time, nonce),
								'x-safeplanet-date' : time,
								'nonce' : nonce
							},
							data : {}
						};

					$http(req2)
							.then(
									function(response) {
										
										// $scope.studentPaths =
										// response.data.paths;
										$scope.studentPaths = {};
										$scope.markers = response.data.markers;
										$scope.kmlfiles = response.data.kmlfiles;
										$scope.routePaths = response.data.paths;
										var count = Object.keys($scope.markers).length;
										var keysObjArr = Object
												.keys($scope.markers);

										$scope.india.lat = $scope.markers["cur0"].lat;
										$scope.india.lng = $scope.markers["cur0"].lng;

										if($scope.india.lat != null){
											leafletData
											.getMap("mapDivId")
											.then(
													function(mapL) {
														// alert(mapL);
														// add L.KML from
														// plugin to map
														var keysObjArr = Object
																.keys($scope.kmlfiles);
														// alert($scope.kmlfiles["kml1"].kmlfilename);
														for (var h = 0; h < keysObjArr.length; h++) {
															var kmlURL = "uploads/routeskml/"
																	+ $scope.kmlfiles["kml"
																			+ (h + 1)].kmlfilename;
															var track = new L.KML(
																	kmlURL,
																	{
																		async : true
																	});

															track
																	.on(
																			"loaded",
																			function(
																					e) {
																				mapL
																						.fitBounds(e.target
																								.getBounds());
																			});
															mapL
																	.addLayer(track);
															var pathName = $scope.routePaths["p"
																	+ (h + 1)].message;
															// alert(pathName);
															var trackOptions = {};
															trackOptions[pathName] = track;
															mapL
																	.addControl(new L.Control.Layers(
																			{},
																			trackOptions));
														}
													});
										}
										else{
											
											$scope.currentPos();
											
                                           
										}

										
									}, function() {
										// alert("Error::");
									});

					/*
					 * var mapL = leafletData.getMap("mapDivId"); alert(mapL);
					 * var track = new L.KML("uploads/routeskml/kmlsample.kml",
					 * {async: true});
					 * 
					 * track.on("loaded", function(e) {
					 * mapL.fitBounds(e.target.getBounds()); });
					 * mapL.addLayer(track);
					 * 
					 * mapL.addControl(new L.Control.Layers({},
					 * {'Track':track}));
					 */

					/*
					 * leafletData.getMap("mapDivId").then(function(mapL) {
					 * //alert(mapL); //add L.KML from plugin to map
					 * 
					 * var track = new L.KML("uploads/routeskml/kmlsample.kml",
					 * {async: true});
					 * 
					 * track.on("loaded", function(e) {
					 * mapL.fitBounds(e.target.getBounds()); });
					 * mapL.addLayer(track);
					 * 
					 * mapL.addControl(new L.Control.Layers({},
					 * {'Track':track}));
					 * 
					 * });
					 */
					$scope.initialValue = [];
					$scope.finalValue = [];

					var updateCurrentLocations = function() {
          
						// $scope.markers = new Array();
						// $scope.markers1 = new Array();

						$scope.markers1 = [];
						var map;
						
						var reqLocation = {
							url : 'route/getDeviceCurrentLocation/'
									+ javaRest.cookie.get('userId'),
							method : 'GET',
							headers : {
								'Authorization' : getAuthToken(
										"route/getDeviceCurrentLocation/",
										time, nonce),
								'x-safeplanet-date' : time,
								'nonce' : nonce
							},
							data : {}
						};

						var reqLocation1 = {
								url : 'route/getStudentCurrentLocation/'
										+ javaRest.cookie.get('userId'),
								method : 'GET',
								headers : {
									'Authorization' : getAuthToken(
											"route/getStudentCurrentLocation/",
											time, nonce),
									'x-safeplanet-date' : time,
									'nonce' : nonce
								},
								data : {}
							};

							$http(reqLocation1)
									.then(
											function(response) {
												$scope.currentMarkerLocations = response.data;
												var curCount = $scope.currentMarkerLocations.length;
												// var keysObjArr =
												// Object.keys($scope.currentMarkerLocations);
												for (var i = 0; i < curCount; i++) {
													/*
													 * $scope.markers[keysObjArr[i]].lat =
													 * $scope.currentMarkerLocations[keysObjArr[i]].lat;
													 * $scope.markers[keysObjArr[i]].lng =
													 * $scope.currentMarkerLocations[keysObjArr[i]].lng;
													 * $scope.markers[keysObjArr[i]].message =
													 * $scope.currentMarkerLocations[keysObjArr[i]].message;
													 */

													$scope.markers1
															.push({
																lat : response.data[i].lattitude,
																lng : response.data[i].longitude,
																message : "Bus Stop",
																icon : "icons/map-marker.svg"
															});
												}
												// alert('first'+$scope.markers);

											}, function() {
												// alert("Error::");
											});


						$http(reqLocation)
								.then(
										function(response) {
											$scope.currentMarkerLocations = response.data.markers;
											var curCount = Object
													.keys($scope.currentMarkerLocations).length;
											var keysObjArr = Object
													.keys($scope.currentMarkerLocations);
                                         if($scope.currentMarkerLocations != null){
                                        	
                                        	 for (var i = 0; i < curCount; i++) {
 												/*
												 * $scope.markers[keysObjArr[i]].lat =
												 * $scope.currentMarkerLocations[keysObjArr[i]].lat;
												 * $scope.markers[keysObjArr[i]].lng =
												 * $scope.currentMarkerLocations[keysObjArr[i]].lng;
												 * $scope.markers[keysObjArr[i]].message =
												 * $scope.currentMarkerLocations[keysObjArr[i]].message;
												 */

 												$scope.markers1
 														.push({
 															lat : $scope.currentMarkerLocations[keysObjArr[i]].lat,
 															lng : $scope.currentMarkerLocations[keysObjArr[i]].lng,
 															message : $scope.currentMarkerLocations["cur"
 																	+ i].message,
 															icon : "icons/bus.svg"

 														});
 											}
                                        	 
                                        	 $scope.initialValue = [];
 											// $scope.finalValue=[];
 											$scope.initialValue
 													.push($scope.markers1);
 											$scope.result = angular.equals(
 													$scope.initialValue,
 													$scope.finalValue);
 											// alert($scope.result);
 											
 											if ($scope.result == false) {
												$scope.markers = [];

												for (var i = 0; i < $scope.markers1.length; i++) {

													$scope.markers
															.push({
																lat : $scope.markers1[i].lat,
																lng : $scope.markers1[i].lng,
																message : $scope.markers1[i].message,
																icon : {
																	iconUrl : $scope.markers1[i].icon,

																	iconSize : [
																			24,
																			24 ],
																	shadowSize : [
																			50,
																			64 ],
																	iconAnchor : [
																			12,
																			24 ],
																	shadowAnchor : [
																			4,
																			62 ]
																}

															});

												}
												$scope.finalValue = [];
												$scope.finalValue
														.push($scope.markers1);
												$scope.markers1 = [];
											}
                                         }
											
									// console.log($scope.markers);
											// console.log($scope.markers1);
											// alert('last'+$scope.markers);
										}, function() {
											// alert("Error::");
										});
						// alert(''$scope.markers);
					}

					$interval(updateCurrentLocations, 10000);
				});

dashboardApp
		.controller(
				'AllBusLocationController',
				function($scope, $rootScope, $resource, $http, $interval,
						leafletData) {

					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}

					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					angular
							.extend(
									$scope,
									{
										india : {
											lat : 28.53,
											lng : 77.38,
											zoom : 13
										},
										layers : {
											baselayers : {
												osm : {
													name : 'OpenStreetMap',
													url : 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
													type : 'xyz'
												}
											}
										},
										busPaths : {}
									});

					var req2 = {
						url : 'route/getAdminAllRoute/'
								+ javaRest.cookie.get('userId'),
						method : 'GET',
						headers : {
							'Authorization' : getAuthToken(
									"route/getAdminAllRoute/", time, nonce),
							'x-safeplanet-date' : time,
							'nonce' : nonce
						},
						data : {}
					};

					$http(req2)
							.then(
									function(response) {

										$scope.busPaths = {};
										$scope.markers = response.data.markers;
										$scope.kmlfiles = response.data.kmlfiles;
										$scope.routePaths = response.data.paths;
										var count = Object.keys($scope.markers).length;
										var keysObjArr = Object
												.keys($scope.markers);

										$scope.india.lat = $scope.markers["cur0"].lat;
										$scope.india.lng = $scope.markers["cur0"].lng;

										leafletData
												.getMap("mapDivId")
												.then(
														function(mapL) {
															var keysObjArr = Object
																	.keys($scope.kmlfiles);

															for (var h = 0; h < keysObjArr.length; h++) {
																var kmlURL = "uploads/routeskml/"
																		+ $scope.kmlfiles["kml"
																				+ (h + 1)].kmlfilename;
																var track = new L.KML(
																		kmlURL,
																		{
																			async : true
																		});

																track
																		.on(
																				"loaded",
																				function(
																						e) {
																					mapL
																							.fitBounds(e.target
																									.getBounds());
																				});
																mapL
																		.addLayer(track);
																var pathName = $scope.routePaths["p"
																		+ (h + 1)].message;
																// alert(pathName);
																var trackOptions = {};
																trackOptions[pathName] = track;
																mapL
																		.addControl(new L.Control.Layers(
																				{},
																				trackOptions));
															}
														});
									}, function() {
										// alert("Error::");
									});

					var updateCurrentLocations = function() {
						var reqLocation = {
							url : 'route/getAdminDeviceCurrentLocation/'
									+ javaRest.cookie.get('userId'),
							method : 'GET',
							headers : {
								'Authorization' : getAuthToken(
										"route/getAdminDeviceCurrentLocation/",
										time, nonce),
								'x-safeplanet-date' : time,
								'nonce' : nonce
							},
							data : {}
						};

						$http(reqLocation)
								.then(
										function(response) {
											$scope.currentMarkerLocations = response.data.markers;
											var curCount = Object
													.keys($scope.currentMarkerLocations).length;
											var keysObjArr = Object
													.keys($scope.currentMarkerLocations);

											for (var i = 0; i < curCount; i++) {
												$scope.markers[keysObjArr[i]].lat = $scope.currentMarkerLocations[keysObjArr[i]].lat;
												$scope.markers[keysObjArr[i]].lng = $scope.currentMarkerLocations[keysObjArr[i]].lng;
												$scope.markers[keysObjArr[i]].message = $scope.currentMarkerLocations[keysObjArr[i]].message;
											}
										}, function() {
											// alert("Error::");
										});
					}

					$interval(updateCurrentLocations, 30000);
				});

dashboardApp.controller('studentReportController', function($scope, $rootScope,
		$resource) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	// $rootScope.fixLeftSideBar();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	var date = new Date();

	// Set the current date in the date control
	$scope.selectDate = new Date();

	var formatSelectDate = date.getFullYear() + ":"
			+ addZero(date.getMonth() + 1) + ":" + addZero(date.getDate());

	$scope.studentList = $resource(
			'students/getStudentReport/:userId/:selectDate',
			{
				userId : javaRest.cookie.get('userId'),
				selectDate : formatSelectDate
			},
			{
				get : {
					method : 'GET',
					isArray : false,
					headers : {
						'Authorization' : getAuthToken(
								"students/getStudentReport/", time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();

	$scope.studentList.$promise.then(function(studentList) {
		$scope.presentStudent = studentList["presentStudent"];
		$scope.absentStudent = studentList["absentStudent"];

	});

	$scope.loadAbsentReport = function() {

		if ($scope.selectDate == undefined) {
			alert("Select Date.");
			return;
		}

		var selectDateVar = $scope.selectDate;
		var formatSelectDate = selectDateVar.getFullYear() + ":"
				+ addZero(selectDateVar.getMonth() + 1) + ":"
				+ addZero(selectDateVar.getDate());

		$scope.studentList = $resource(
				'students/getStudentReport/:userId/:selectDate',
				{
					userId : javaRest.cookie.get('userId'),
					selectDate : formatSelectDate
				},
				{
					get : {
						method : 'GET',
						isArray : false,
						headers : {
							'Authorization' : getAuthToken(
									"students/getStudentReport/", time, nonce),
							'x-safeplanet-date' : time,
							'nonce' : nonce
						}
					}
				}).get();

		$scope.studentList.$promise.then(function(studentList) {
			$scope.presentStudent = studentList["presentStudent"];
			$scope.absentStudent = studentList["absentStudent"];
		});
	}

});


dashboardApp
		.controller(
				'SettingsController',
				function($scope, $rootScope, $resource, filterFilter, $timeout,
						$filter, settingsService) {
					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}

					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					$scope.weekDays = [ {
						name : 'Mon',
						selected : false
					}, {
						name : 'Tue',
						selected : false
					}, {
						name : 'Wed',
						selected : false
					}, {
						name : 'Thu',
						selected : false
					}, {
						name : 'Fri',
						selected : false
					}, {
						name : 'Sat',
						selected : false
					}, {
						name : 'Sun',
						selected : false
					} ];

					$scope.notificationTypes = [ {
						name : 'Arrival',
						selected : false
					}, {
						name : 'Departure',
						selected : false
					}, {
						name : 'Breakdown',
						selected : false
					}, {
						name : 'Long Halt',
						selected : false
					}, {
						name : 'Emergency',
						selected : false
					} ];

					$scope.studentChanged = function() {
						$scope.selectedAlert.pickTime = $scope.selectedAlert.pickDateTime
								.getTimeString();
						$scope.selectedAlert.dropTime = $scope.selectedAlert.dropDateTime
								.getTimeString();

						populateConfigData();
					}

					settingsService.getStudentDetails().$promise
							.then(function(data) {
								$scope.studentList = data;
								if ($scope.studentList.length > 0) {
									$scope.selectedStudent = $scope.studentList[0];

									settingsService
											.getUserNotificationSettings().$promise
											.then(function(configData) {

												$scope.configData = configData;
												populateConfigData();
											});
								}
							});

					function populateConfigData() {

						for ( var index in $scope.configData) {

							if (angular
									.isDefined($scope.configData[index].activeTime)) {

								if (angular
										.isUndefined($scope.configData[index].pickTime)
										|| $scope.configData[index].pickTime == null)
									$scope.configData[index].pickDateTime = new Date();
								else
									$scope.configData[index].pickDateTime = new Date(
											"January 01, 2000 "
													+ $scope.configData[index].pickTime);

								if (angular
										.isUndefined($scope.configData[index].dropTime)
										|| $scope.configData[index].dropTime == null)
									$scope.configData[index].dropDateTime = new Date();
								else
									$scope.configData[index].dropDateTime = new Date(
											"January 01, 2000 "
													+ $scope.configData[index].dropTime);

								if ($scope.configData[index].studentId == $scope.selectedStudent.studentId) {
									$scope.selectedAlert = $scope.configData[index];

									createDaysLabel();
									createAlertsLabel();

								}
							}
						}
					}

					$scope.populatePickupDays = function() {
						for ( var index in $scope.weekDays) {
							if ($scope.selectedAlert.days != null
									&& $scope.selectedAlert.days
											.indexOf($scope.weekDays[index].name) >= 0) {
								$scope.weekDays[index].selected = true;
							} else {
								$scope.weekDays[index].selected = false;
							}
						}
					}

					$scope.updateDaysString = function() {
						$scope.selectedAlert.days = "";
						for ( var index in $scope.weekDays) {
							if ($scope.weekDays[index].selected == true) {
								$scope.selectedAlert.days += $scope.weekDays[index].name
										+ "|";
							}
						}

						if ($scope.selectedAlert.days.length > 0)
							$scope.selectedAlert.days = $scope.selectedAlert.days
									.slice(0, -1);

						createDaysLabel();
					}

					$scope.populateNotificationAlerts = function() {
						for ( var index in $scope.notificationTypes) {
							if ($scope.selectedAlert.alerts != null
									&& $scope.selectedAlert.alerts
											.indexOf($scope.notificationTypes[index].name) >= 0) {
								$scope.notificationTypes[index].selected = true;
							} else {
								$scope.notificationTypes[index].selected = false;
							}
						}
					}

					$scope.updateAlertsString = function() {
						$scope.selectedAlert.alerts = "";
						for ( var index in $scope.notificationTypes) {
							if ($scope.notificationTypes[index].selected == true) {
								$scope.selectedAlert.alerts += $scope.notificationTypes[index].name
										+ "|";
							}
						}

						if ($scope.selectedAlert.alerts.length > 0)
							$scope.selectedAlert.alerts = $scope.selectedAlert.alerts
									.slice(0, -1);

						createAlertsLabel();
					}

					function createDaysLabel() {
						if ($scope.selectedAlert.days != null
								&& $scope.selectedAlert.days.length > 0) {
							$scope.selectedAlert.daysLabel = $scope.selectedAlert.days
									.split("|").join(",");
							if ($scope.selectedAlert.daysLabel.length > 20) {
								$scope.selectedAlert.daysLabel = $scope.selectedAlert.daysLabel
										.substring(0, 20);
								$scope.selectedAlert.daysLabel += "...";
							}
						} else {
							$scope.selectedAlert.daysLabel = "Select Days";
						}
					}

					function createAlertsLabel() {
						if ($scope.selectedAlert.alerts != null
								&& $scope.selectedAlert.alerts.length > 0) {
							$scope.selectedAlert.alertsLabel = $scope.selectedAlert.alerts
									.split("|").join(",");
							if ($scope.selectedAlert.alertsLabel.length > 20) {
								$scope.selectedAlert.alertsLabel = $scope.selectedAlert.alertsLabel
										.substring(0, 20);
								$scope.selectedAlert.alertsLabel += "...";
							}
						} else {
							$scope.selectedAlert.alertsLabel = "Select Alert Types";
						}
					}

					$scope.saveSettings = function() {
						$scope.updateUserNotification();
					}

					$scope.closeNotification = function() {
						$scope.successTextAlert = undefined;
						$scope.errorTextAlert = undefined;
					}

					$scope.updateUserNotification = function() {

						for ( var index in $scope.configData) {

							$scope.alertConfigData = $scope.configData[index];

							if (angular
									.isDefined($scope.alertConfigData.activeTime)) {

								$scope.alertConfigData.pickTime = $scope.alertConfigData.pickDateTime;
								$scope.alertConfigData.dropTime = $scope.alertConfigData.dropDateTime;

								$scope.alertConfigData.activeDays = this.daysSelection;
								$scope.alertConfigData.alertTypes = this.typesSelection;
							}
						}

						settingsService
								.updateNotificationSettings()
								.save(
										{
											userId : javaRest.cookie
													.get('userId')
										},
										$scope.configData,
										function(resp, headers) {

											for ( var index in $scope.configData) {
												if (angular
														.isDefined($scope.configData[index].activeTime)) {
													$scope.configData[index].pickTime = $scope.configData[index].pickDateTime
															.getTimeString();
													$scope.configData[index].dropTime = $scope.configData[index].dropDateTime
															.getTimeString();
												}
											}

											populateConfigData();

											$scope.successTextAlert = "Alerts saved successfully.";
											$timeout(
													function() {
														$scope.successTextAlert = undefined;
													}, 5000);

										},
										function(err) {

											$scope.errorTextAlert = "Error while updating Alerts.";

											$timeout(
													function() {
														$scope.errorTextAlert = undefined;
													}, 5000);
										});
					}

				});

dashboardApp.controller('ComplaintMessageController', function($scope,
		$rootScope, $resource, $timeout) {

	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	// $rootScope.fixLeftSideBar();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

	$scope.sendComplaintMessage = function sendComplaintMessage() {
		$scope.complaintMessageResource = $resource(
				'user/addComplaintMessage/:userId', {
					userId : javaRest.cookie.get('userId')
				}, {
					save : {
						method : 'PUT',
						isArray : false,
						headers : {
							'Authorization' : getPutAuthToken(
									"user/addComplaintMessage/", time, nonce),
							'x-safeplanet-date' : time,
							'nonce' : nonce,
							'Content-Type' : 'application/json'
						},
						transformRequest : function(data, headersGetter) {
							var headers = headersGetter();
							var time = javaRest.get_iso_date();
							var nonce = makeRandomString();
							headers['Content-Type'] = "application/json";
							headers['Authorization'] = getPutAuthToken(
									"user/addComplaintMessage/", time, nonce);
							headers['x-safeplanet-date'] = time;
							headers['nonce'] = nonce;

							var studentData = '{"message":"' + data.message
									+ '"}';
							return studentData;
						}
					}
				});

		var complaintMessageObj = {};
		complaintMessageObj.message = $scope.complaintMessage;

		$scope.complaintMessageResource.save({
			userId : javaRest.cookie.get('userId')
		}, complaintMessageObj, function(resp, headers) {

			// console.log(resp);
			$scope.successTextAlert = "Message Sent!";
			$scope.showSuccessAlert = true;

			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};

			$timeout(function() {
				$scope.showSuccessAlert = false;
			}, 5000);
		}, function(err) {
			// error callback
			// console.log(err);
			$scope.successTextAlert = "Error: "
					+ err.data.validationErrors[0].message;
			$scope.showSuccessAlert = true;

			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};
		});
	};
});

dashboardApp.controller('SchoolInfoController', function($scope, $rootScope,
		$resource, $timeout) {

	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	// $rootScope.fixLeftSideBar();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	$scope.schoolDetailList = $resource(
			'user/getSchoolInfo/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'Authorization' : getAuthToken("user/getSchoolInfo/",
								time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();

	$scope.schoolDetailList.$promise.then(function(schoolDetailList) {

	});
});

dashboardApp.controller('BroadcastMessageController', function($scope,
		$rootScope, $resource, $timeout, routeService) {

	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	// $rootScope.fixLeftSideBar();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

	$scope.sendToAll = false;

	$scope.sendBroadcastMessage = function sendBroadcastMessage() {
		
		$scope.showErrorAlert = undefined;
		$scope.showSuccessAlert = undefined;

		if (angular.isUndefined($scope.broadcastMessage)) {
			$scope.showErrorAlert = "Please enter email message to be sent.";
			return;
		}

        if ($scope.sendToAll == false) {
            if (angular.isUndefined($scope.routeId)) {
                $scope.showErrorAlert = "Please select one of the route.";
                return;
            }

            if (angular.isUndefined($scope.scheduleId)) {
                $scope.showErrorAlert = "Please select one of the schedule.";
                return;
            }
        }

		$scope.broadcastMessageResource = $resource(
				'route/sendBroadcastMessage/:userId', {
					userId : javaRest.cookie.get('userId')
				}, {
					save : {
						method : 'PUT',
						isArray : false,
						headers : {
							'Authorization' : getPutAuthToken(
									"route/sendBroadcastMessage/", time, nonce),
							'x-safeplanet-date' : time,
							'nonce' : nonce,
							'Content-Type' : 'application/json'
						},
						transformRequest : function(data, headersGetter) {
							var headers = headersGetter();
							var time = javaRest.get_iso_date();
							var nonce = makeRandomString();
							headers['Content-Type'] = "application/json";
							headers['Authorization'] = getPutAuthToken(
									"route/sendBroadcastMessage/", time, nonce);
							headers['x-safeplanet-date'] = time;
							headers['nonce'] = nonce;

							var scheduleId = $scope.scheduleId;
							var routeId = $scope.routeId;
							var sendToAll = $scope.sendToAll;
							var message = $scope.broadcastMessage;

							var messageData = '{"routeUuid":"' + routeId
									+ '", "scheduleId":"' + scheduleId
									+ '", "sendToAll":"' + sendToAll
									+ '", "message":"' + message + '"}';
							return messageData;
						}
					}
				});

		var broadcastMessageObj = {};
		broadcastMessageObj.message = $scope.broadcastMessage;

		$scope.broadcastMessageResource.save({
			userId : javaRest.cookie.get('userId')
		}, broadcastMessageObj, function(resp, headers) {

			// console.log(resp);
			$scope.successTextAlert = "Message Sent!!";
			$scope.showSuccessAlert = true;

			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};

			$timeout(function() {
				$scope.showSuccessAlert = false;
			}, 5000);
		}, function(err) {
			// error callback
			// console.log(err);
			$scope.successTextAlert = "Error: "
					+ err.data.validationErrors[0].message;
			$scope.showSuccessAlert = true;

			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};
		});
	};

	routeService.findAllRouteList().$promise.then(function(routeList) {
		$scope.routeList = routeList;
		// console.log($scope.routeList);
	});

});

dashboardApp
		.controller(
				'ManageRouteController',
				function($scope, $rootScope, $resource,routeService, $timeout, $http) {

					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}
					// $rootScope.fixLeftSideBar();
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					$scope.routeData = {};

					$scope.addRouteFormModal = function() {
$scope.detailsVisibility=true;						$scope.routeData = {};
						$('#addRouteModal').modal('show');
					}

					$scope.submitRouteForm = function() {

						var data = $scope.routeData;

						var reqParamsArr = [];
						for ( var p in data) {
							reqParamsArr.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(data[p]));
						}
						var reqParamData = reqParamsArr.join("&");
						// alert(reqParamData);

						$http(
								{
									method : 'POST',
									url : 'jsp/addUpdateRoute.jsp',
									data : $scope.routeData, // forms user
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
							$('#addRouteModal').modal('hide');
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
									location.reload();
									$scope.showSuccessAlert = false;
								}, 2000);
							}
						});
					}

					routeService.getUnassignedFleet().$promise
					.then(function(fleetList) {
						$scope.fleetList = fleetList;
					});
					$scope.modifyRouteForm = function(routeUUID) {

						$scope.modifyRouteDetails = $resource(
								'route/getModifyRouteDetail/:userId/:routeId',
								{
									userId : javaRest.cookie.get('userId'),
									routeId : routeUUID
								},
								{
									get : {
										method : 'GET',
										isArray : false,
										headers : {
											'Authorization' : getAuthToken(
													"route/getModifyRouteDetail/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce
										}
									}
								}).get();

						$scope.modifyRouteDetails.$promise
								.then(function(modifyRouteDetails) {
									$scope.routeData = modifyRouteDetails;
$scope.detailsVisibility=false;									$scope.routeData.routeUUID = modifyRouteDetails.uuid;
									$('#addRouteModal').modal('show');

								});
					};

				});

dashboardApp.controller(
				'ManageWayPointController',
				function($scope, $rootScope, $resource, $timeout, $http,
						$routeParams) {

					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();
					$scope.wayPoints=[];
					$scope.routeData = {};
					$scope.routeId = $routeParams.routeUUID;
					$scope.isEditMode = false;
					
					$scope.getAllWayPointsForRoute = function()
					{
						$http.get('route/findAllWayPointsByRouteUUID/'+ $scope.routeId).success(function(wayPoints){
							$scope.wayPoints = wayPoints;
						});
					}
					
					$scope.getAllWayPointsForRoute();
					$scope.addWayPointFormModal = function() {
						$scope.routeData = {};
						var seqNum = parseInt($('#dataTableGrid tr:last td:first').text());
						if(!seqNum > 0){
							seqNum = 0;
						}
						$scope.wayPointData={sequenceNumber:seqNum + 1};
						$scope.isEditMode = false;
						$('#addWayPointModal').modal('show');
					}

					$scope.submitWayPointForm = function() {

						var data = $scope.wayPointData;

						if(!data.id && $('#dataTableGrid tr td:nth-child(2)').text().indexOf(data.name)>-1)
						{
							alert('Please enter a different waypoint name,'+ data.name+' is already in use');
							return;
						}
						var reqParamsArr = [];
						for ( var p in data) {
							reqParamsArr.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(data[p]));
						}
						reqParamsArr.push("routeUUID="
								+ encodeURIComponent($scope.routeId));
						var reqParamData = reqParamsArr.join("&");
						// alert(reqParamData);

						$http(
								{
									method : 'POST',
									url : 'jsp/addUpdateWayPoint.jsp',
									data : $scope.wayPointData, // forms user
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
							$('#addWayPointModal').modal('hide');
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
									$scope.getAllWayPointsForRoute();
									$scope.showSuccessAlert = false;
								}, 2000);
							}
						});
					}

					$scope.modifyWayPointForm = function(wayPointId) {

						$scope.modifyWayPointDetails = $resource(
								'route/getModifyWayPointDetail/:userId/:wayPointId',
								{
									userId : javaRest.cookie.get('userId'),
									wayPointId : wayPointId
								},
								{
									get : {
										method : 'GET',
										isArray : false,
										headers : {
											'Authorization' : getAuthToken(
													"route/getModifyWayPointDetail/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce
										}
									}
								}).get();

						$scope.modifyWayPointDetails.$promise
								.then(function(modifyWayPointDetails) {
									
									var timeParts= modifyWayPointDetails.timePick.split(':');
									var hour = timeParts[0];
									var min = timeParts[1];
									
									modifyWayPointDetails.picktime = new Date(2017,0,1,hour,min);
									
									 timeParts= modifyWayPointDetails.timeDrop.split(':');
									 hour = timeParts[0];
									 min = timeParts[1];
									
									modifyWayPointDetails.droptime = new Date(2017,0,1,hour,min);
									
									$scope.wayPointData = modifyWayPointDetails;
									$scope.wayPointData.wayPointUUID = modifyWayPointDetails.uuid;
									$scope.isEditMode = true;
									$('#addWayPointModal').modal('show');

								});
					};

				});

dashboardApp.controller('addBusRouteController', function($scope, $rootScope,
		$resource, $timeout) {

});
dashboardApp
		.controller(
				'addStudentRouteController',
				function($scope, $rootScope, $resource, $timeout, $http) {
					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}
					// $rootScope.fixLeftSideBar();
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					$scope.routeData = {};

					$scope.addRouteFormModal = function() {
						$scope.routeData = {};
						$('#addRouteModal').modal('show');
					}

					$scope
							.$watch(
									'routeData.routeId',
									function(newVal) {
										if (newVal) {
											// alert(newVal);
											$scope.wayPointList = $resource(
													'route/getRouteWayPoints/:userId/:routeId',
													{
														userId : javaRest.cookie
																.get('userId'),
														routeId : newVal
													},
													{
														get : {
															method : 'GET',
															isArray : false,
															headers : {
																'Authorization' : getAuthToken(
																		"route/getRouteWayPoints/",
																		time,
																		nonce),
																'x-safeplanet-date' : time,
																'nonce' : nonce
															}
														}
													}).get();

											$scope.wayPointList.$promise
													.then(function(wayPointList) {
														$scope.waypoints = wayPointList;

													});
										}
									});

					$scope.submitRouteForm = function() {

						var data = $scope.routeData;

						var reqParamsArr = [];
						for ( var p in data) {
							reqParamsArr.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(data[p]));
						}
						var reqParamData = reqParamsArr.join("&");
						// alert(reqParamData);

						$http(
								{
									method : 'POST',
									url : 'jsp/addUpdateStudentRoute.jsp',
									data : $scope.routeData, // forms user
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
							$('#addRouteModal').modal('hide');
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
									location.reload();
									$scope.showSuccessAlert = false;
								}, 2000);
							}
						});
					}

					$scope.modifyRouteForm = function(sId, routeXrefId) {

						$scope.modifyRouteDetails = $resource(
								'students/getStudentRouteDetail/:userId/:studentId',
								{
									userId : javaRest.cookie.get('userId'),
									studentId : sId
								},
								{
									get : {
										method : 'GET',
										isArray : false,
										headers : {
											'Authorization' : getAuthToken(
													"students/getStudentRouteDetail/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce
										}
									}
								}).get();

						$scope.modifyRouteDetails.$promise.then(function(
								modifyRouteDetails) {
							$scope.routeData = modifyRouteDetails;
							// $scope.routeData.studentId = sId;
							$scope.routeData.studentRouteXREFId = routeXrefId;
							$('#addRouteModal').modal('show');

						});
					};

				});

dashboardApp
		.controller(
				'ManageUsersController',
				function($scope, $rootScope, $resource, $timeout, $http,$filter,
						$compile, userService, studentService, routeService,
						$route, $location) {

					$scope.teacherFilterState=0;
					$scope.rawTeachers=[];
					
					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}
					$scope.userData = {};
					// $scope.studentList = [];
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();
					$scope.editUserFormModal = function() {
						$scope.userData = {};
						$('#editUserModal').modal('show');
					}

					studentService.getUnassignedSchool().$promise
							.then(function(schoolList) {
								$scope.schoolList = schoolList;
							});
					studentService.getStudentsInSchool().$promise
							.then(function(studentList) {
								$scope.studentList = studentList;
								$scope.allStudentList=angular.copy(studentList);
							});

					studentService.getStates().$promise.then(function(
							statesList) {
						$scope.statesList = statesList;
						$scope.satedata= angular.copy(statesList);
					});
					
					  $scope.loadCityByState = function () {
						  var stateId = $scope.userData.selectedState;
						  if(!stateId){
							  var state = $scope.statesList.filter(function(item){return item.stateName==$scope.userData.state;});
							  stateId= (state[0]||{}).stateId||0;
						  }
					    	routeService.getCityByState(stateId).$promise.then(function(cityList) {
					    		$scope.cityList = cityList;

					    	});
					    }


					$scope.$watch("selectedStudents", function() {
						console.log(JSON.stringify($scope.selectedStudents));
					});

					$scope.$watch("selectedSchools", function() {
						console.log(JSON.stringify($scope.selectedSchools));
					});

					routeService.findAllRouteList().$promise.then(function(
							routeList) {
						$scope.routeList = routeList;
					});
					$scope.getSelValue = function() {
						$scope.selectedValue = $scope.routeName;
					}

					$scope.getAllUsersAdmin = function() {
						$http.get(
								'user/getAllUsersAdmin/'
										+ javaRest.cookie.get('userId')).then(
								function(response) {
									
									$scope.userList = response.data;
									//$scope.userList = angular.copy(response.data);
									$rootScope.userList =response.data;
									console.log(response.data);
								});
					}
					$scope.getAllUsersAdmin();
					
					$scope.getValue = function (routeName){
						
						$scope.userList =$filter('filter')($rootScope.userList,{routeName:routeName});
					}

					$scope.getAllAdministrator = function() {
						$http.get(
								'user/getAllAdministrator/'
										+ javaRest.cookie.get('userId')).then(
								function(response) {
									$scope.adminList = response.data;
								});
					}
					$scope.getAllAdministrator();
					
					$scope.getAllAdministrator = function() {
						$http.get(
								'user/getAllTeachers/'
										+ javaRest.cookie.get('userId')).then(
								function(response) {
									$scope.teacherList = response.data;
									$scope.rawTeachers = angular.copy($scope.teacherList);
								});
					}
					
					$scope.getAllAdministrator();

					$scope.$watch('teacherFilterState',function(){
						if($scope.teacherFilterState==1){
							$scope.teacherList = $scope.rawTeachers.filter(function(teacher){return teacher.isEnable==1;});
						}else if($scope.teacherFilterState==2){
							$scope.teacherList = $scope.rawTeachers.filter(function(teacher){return teacher.isEnable==0;})
						}else{
							$scope.teacherList = angular.copy($scope.rawTeachers);
						}
					});
					
					$scope.initAddUser = function() {
						$scope.userData = undefined;
						$scope.selectedStudents = [];
						$scope.internalList = [];
					}

					$scope.addUser = function() {
						if ($scope.userForm.$valid) {
							console
									.log(JSON
											.stringify($scope.selectedStudents));
							var students = "";
							for ( var index in $scope.selectedStudents) {
								students += $scope.selectedStudents[index].studentId
										+ ",";
							}

							$scope.userData.studentsList = students;
							$scope.submitUserForm();
							$('#addUserModal').modal('hide');
						}
					}

					$scope.addAdmin = function() {
						if ($scope.userForm.$valid) {
							console
									.log(JSON
											.stringify($scope.selectedStudents));
							var students = "";
							for ( var index in $scope.selectedStudents) {
								students += $scope.selectedStudents[index].studentId
										+ ",";
							}

							$scope.userData.studentsList = students;
							$scope.submitAdminForm();
							$('#addUserModal').modal('hide');
						}
					}

					$scope.submitUserForm = function() {

						var data = $scope.userData;

						var reqParamsArr = [];
						for ( var p in data) {
							reqParamsArr.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(data[p]));
						}
						var reqParamData = reqParamsArr.join("&");
						// alert(reqParamData);

						$http(
								{
									method : 'POST',
									url : 'jsp/addUpdateUser.jsp',
									data : $scope.userData, // forms user object
									headers : {
										'Content-Type' : 'application/x-www-form-urlencoded'
									},
									transformRequest : function(data,
											headersGetter) {
										var headers = headersGetter();
										headers['Content-Type'] = "application/x-www-form-urlencoded; charset=UTF-8;";
										return reqParamData;
									}
								})
								.success(
										function(data) {
											$('#editUserModal').modal('hide');
											if (data.errors) {
												$scope.successTextAlert = "Error, please try again.";
												$scope.showSuccessAlert = true;

												// switch flag
												$scope.switchBool = function(
														value) {
													$scope[value] = !$scope[value];
												};
											} else {
												$scope.successTextAlert = "User saved successfully!!!";
												$scope.showSuccessAlert = true;

												// switch flag
												$scope.switchBool = function(
														value) {
													$scope[value] = !$scope[value];
												};

												$timeout(
														function() {
															$route.reload();
															$location
																	.path("/manageUsers.html");

															$scope.showSuccessAlert = false;
															$scope.successTextAlert = undefined;
														}, 2000);
											}
										});
					}
					$scope.switchBool = function(value) {
						$scope[value] = !$scope[value];
					};
					
					// --------------for Add new School Teacher----------//
					$scope.addSchoolTeacher= function(){
						var data = angular.copy($scope.userData);

						$http.post('user/addSchoolTeacher',data,{json:true})
						.then(function(response){
							data = response.data;
							
							$('#addUserModal').modal('hide');
							$scope.showSuccessAlert = true;
							$scope.successTextAlert = data.message;
							
							$timeout(function() {
										$scope.showSuccessAlert = false;
										$scope.successTextAlert = undefined;
									}, 3000);
							
							$scope.getAllAdministrator();
						});
					};
					
					
					// ----------------for Update School
					// Teacher-------------////
					$scope.updateSchoolTeacher= function(){
						var data = angular.copy($scope.userData);
						delete data.name;
						delete data.verified;
						delete data.userUUID;
						$http.post('user/updateSchoolTeacher',data,{json:true})
						.then(function(response){
							data = response.data;
							
							$('#editUserModal').modal('hide');
							$scope.showSuccessAlert = true;
							$scope.successTextAlert = data.message;
							
							$timeout(function() {
										$scope.showSuccessAlert = false;
										$scope.successTextAlert = undefined;
									}, 3000);
							
							$scope.getAllAdministrator();
						});
					};
					
					
					// ------------To Deactivate Teacher------------//
					$scope.deleteSchoolTeacher= function(teacherUUID){
						
						$http.get('user/deleteSchoolTeacher/'+teacherUUID)
						.then(function(response){
							var data = response.data;
							
							$scope.showSuccessAlert = true;
							$scope.successTextAlert = data.message;
							
							$timeout(function() {
										$scope.showSuccessAlert = false;
										$scope.successTextAlert = undefined;
									}, 3000);
							
							$scope.getAllAdministrator();
						});
					};
					
					$scope.submitAdminForm = function() {

						var data = $scope.userData;

						var reqParamsArr = [];
						for ( var p in data) {
							reqParamsArr.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(data[p]));
						}
						var reqParamData = reqParamsArr.join("&");
						
						$http(
								{
									method : 'POST',
									url : 'jsp/addUpdateAdmin.jsp',
									data : $scope.userData, // forms user object
									headers : {
										'Content-Type' : 'application/x-www-form-urlencoded'
									},
									transformRequest : function(data,
											headersGetter) {
										var headers = headersGetter();
										headers['Content-Type'] = "application/x-www-form-urlencoded; charset=UTF-8;";
										return reqParamData;
									}
								})
								.success(
										function(data) {
											$('#editUserModal').modal('hide');
											if (data.errors) {
												$scope.successTextAlert = "Error, please try again.";
												$scope.showSuccessAlert = true;

												// switch flag
												$scope.switchBool = function(
														value) {
													$scope[value] = !$scope[value];
												};
											} else {
												$scope.successTextAlert = "User saved successfully!!!";
												$scope.showSuccessAlert = true;

												// switch flag
												$scope.switchBool = function(
														value) {
													$scope[value] = !$scope[value];
												};

												$timeout(
														function() {
															$route.reload();
															$location
																	.path("/manageSchoolAdmin.html");

															$scope.showSuccessAlert = false;
															$scope.successTextAlert = undefined;
														}, 2000);
											}
										});
						
					}
					
					 $scope.loadCity = function (id) {
						  var stateId = id;
						  if(!stateId){
							  var state = $scope.statesList.filter(function(item){return item.stateName==$scope.userData.state;});
							  stateId= (state[0]||{}).stateId||0;
						  }
					    	routeService.getCityByState(stateId).$promise.then(function(cityList) {
					    		$scope.cityList = cityList;

					    	});
					    }

				
					$scope.editUserForm = function(userUUID) {
						userService.getUserDetails(userUUID).$promise
								.then(function(editUserDetails) {
									console.log(editUserDetails);
									$scope.userData = editUserDetails;
									$scope.userData.userUUID = editUserDetails.id;
									console.log($scope.userData);
									 $scope.data = $filter('filter')( $scope.satedata, { stateId: editUserDetails.state });
									 $scope.data.state ="1";
									 $scope.data.state="2";
									 for(var i=0;i<$scope.data.length;i++){
										 var id = $scope.data[i];
								 		 $scope.userData.selectedState=id.stateId;
										 routeService.getCityByState( $scope.userData.selectedState).$promise.then(function(cityList) {
									    		$scope.cityList = cityList;
									    		 $scope.cityId =$filter('filter')($scope.cityList,{cityName:$scope.userData.city});
									    		 for(var i=0;i<$scope.cityId.length;i++){
											    		$scope.userData.selectedCity=$scope.cityId[i].cityId;
											    		 }
									    	});
									 }
									 var city = editUserDetails.city;
									 $scope.userData.selectedCity=city;
									$('#editUserModal').modal('show');

								});
					};

					$scope.deactivateUser = function(userUUID, action) {

						var data = {
							"userUUID" : userUUID,
							"action" : action
						};

						var reqParamsArr = [];
						for ( var p in data) {
							reqParamsArr.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(data[p]));
						}
						var reqParamData = reqParamsArr.join("&");
						// alert(reqParamData);

						$http(
								{
									method : 'POST',
									url : 'jsp/addUpdateUser.jsp',
									data : $scope.routeData, // forms user
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
									$route.reload();
									$location.path("/manageUsers.html");
									$scope.showSuccessAlert = false;
								}, 2000);
							}
						});
					}

				});

dashboardApp
		.controller(
				'ManageBusRouteController',
				function($scope, $rootScope, $resource, $timeout, $http) {

					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}
					// $rootScope.fixLeftSideBar();
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					$scope.fleetData = {};

					$scope.submitBusRouteForm = function() {

						var data = $scope.fleetData;

						var reqParamsArr = [];
						for ( var p in data) {
							reqParamsArr.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(data[p]));
						}
						// reqParamsArr.push(encodeURIComponent("fleetID") + "="
						// + encodeURIComponent(fleetID));
						var reqParamData = reqParamsArr.join("&");
						// alert(reqParamData);

						$http(
								{
									method : 'POST',
									url : 'jsp/addUpdateBusRoute.jsp',
									data : $scope.fleetData, // forms user
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
							$('#addBusRouteModal').modal('hide');
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
									location.reload();
									$scope.showSuccessAlert = false;
								}, 2000);
							}
						});
					}

					$scope.modifyBusRouteForm = function(fleetID, xRefID) {

						$scope.modifyBusRouteDetails = $resource(
								'route/getModifyBusRouteDetail/:userId/:xRef',
								{
									userId : javaRest.cookie.get('userId'),
									xRef : fleetID
								},
								{
									get : {
										method : 'GET',
										isArray : false,
										headers : {
											'Authorization' : getAuthToken(
													"route/getModifyBusRouteDetail/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce
										}
									}
								}).get();

						$scope.modifyBusRouteDetails.$promise
								.then(function(modifyBusRouteDetails) {
$scope.fleetData = modifyBusRouteDetails;									if ($("#deviceID option[value="
											+ modifyBusRouteDetails.deviceID
											+ "]").length == 0) {
										$('#deviceID')
												.append(
														$(
																'<option>',
																{
																	value : modifyBusRouteDetails.deviceID,
																	text : modifyBusRouteDetails.deviceName
																}));
									}

									if ($("#routeID option[value="
											+ modifyBusRouteDetails.routeID
											+ "]").length == 0) {
										$('#routeID')
												.append(
														$(
																'<option>',
																{
																	value : modifyBusRouteDetails.routeID,
																	text : modifyBusRouteDetails.routeName
																}));
									}
									$scope.fleetData = modifyBusRouteDetails;

									var ele = document
											.getElementById("routeID");
									for (var ii = 0; ii < ele.length; ii++) {
										if (ele.options[ii].value == modifyBusRouteDetails.routeID) {
											ele.options[ii].selected = true;
											break;
										}
									}
									var eledeviceID = document
											.getElementById("deviceID");
									for (var j = 0; j < eledeviceID.length; j++) {
										if (eledeviceID.options[j].value == modifyBusRouteDetails.deviceID) {
											eledeviceID.options[j].selected = true;
											break;
										}
									}

									$scope.fleetData.fleetID = fleetID;
									$scope.fleetData.xRefID = xRefID;
									$('#addBusRouteModal').modal('show');

								});
					};
				});

dashboardApp
		.controller(
				'ManageSchoolController',
				function($scope, $rootScope, $resource,routeService,studentService, $timeout, $http,$filter) {
					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}
					
					// $rootScope.fixLeftSideBar();
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					$scope.schoolData = {};
					
					
					// ================Get School
					// Details======================//
					
                     $http.get('user/getSchoolDetails/' + javaRest.cookie.get('userId')).success(function(data) {
						$scope.schoolList = data;
						console.log(data);
					});
                     
					
					// =====================End===============================//
					

					$scope.addSchoolFormModal = function() {
						$scope.schoolData = {};
						if($scope.user.role=='superAdmin'){
							
						$('#addSchoolModal').modal('show');
					}
					}
					$scope.submitSchoolForm = function() {
						var data = $scope.schoolData;
						var reqParamsArr = [];
						for ( var p in data) {
							reqParamsArr.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(data[p]));
						}
						// reqParamsArr.push(encodeURIComponent("fleetID") + "="
						// + encodeURIComponent(fleetID));
						var reqParamData = reqParamsArr.join("&");
						// alert(reqParamData);

						$http(
								{
									method : 'POST',
									url : 'jsp/addSchool.jsp',
									data : $scope.schoolData, // forms user
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
							$('#addSchoolModal').modal('hide');
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
									// location.reload();
									
									  $http.get('user/getSchoolDetails/' + javaRest.cookie.get('userId')).success(function(data) {
											$scope.schoolList = data;
											// console.log(data);
										});
									$scope.showSuccessAlert = false;
								}, 2000);
							}
						});
					}
					studentService.getStates().$promise.then(function(
							statesList) {
						$scope.statesList = statesList;
					});
					$scope.loadCityByState = function () {
				    	routeService.getCityByState($scope.schoolData.selectedState).$promise.then(function(cityList) {
				    		$scope.cityList = cityList;
				    	});
				    }

					$scope.modifySchoolForm = function(schoolID) {

						$scope.modifySchoolDetails = $resource(
								'user/getSchoolDetail/:userId/:schoolID',
								{
									userId : javaRest.cookie.get('userId'),
									schoolID : schoolID
								},{
									get : {
										method : 'GET',
										isArray : false,
										headers : {
											'Authorization' : getAuthToken(
													"user/getSchoolDetail/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce
										}
									}
								}).get();

						
						
						$scope.modifySchoolDetails.$promise.then(function(
								modifySchoolDetails) {
							
							$scope.schoolData = modifySchoolDetails;
							$scope.schoolData.subType = modifySchoolDetails.subType;
							console.log($scope.schoolData.subType)
							$scope.schoolData.schoolID = schoolID;
							$scope.schoolData.startDate = new Date(
									modifySchoolDetails.serviceStartDate);
							$scope.schoolData.endDate = new Date(
									modifySchoolDetails.serviceEndDate);
							if($scope.user.role=='superAdmin'){
								$scope.startDateExpression=false;
								$scope.endDateExpression=false;
								$scope.subTypeExpression=false;
								$scope.subAmountExpression=false;
							}else{
								$scope.startDateExpression=true;
								$scope.endDateExpression=true;
								$scope.subTypeExpression=true;
								$scope.subAmountExpression=true;
							}
							
							if($scope.schoolData.state == "New Delhi")
								{
								
								$scope.schoolData.state == "New Delhi"
								$scope.schoolData.selectedState ="2";
								
								}
							else if($scope.schoolData.state == "Uttar Pradesh"){
								$scope.schoolData.state == "Uttar Pradesh"
								$scope.schoolData.selectedState ="1";
								
							}
							
							routeService.getCityByState($scope.schoolData.selectedState).$promise.then(function(cityList) {
					    		 $scope.cityList = cityList;
					    		 $scope.cityId =$filter('filter')($scope.cityList,{cityName:$scope.schoolData.city});
					    		 for(var i=0;i<$scope.cityId.length;i++){
					    		 $scope.schoolData.selectedCity=$scope.cityId[i].cityId;
					    		 }
							});
							
							$scope.schoolData.selectedCity="2";
							// $('[name=city]').val($scope.schoolData.city);
						// $("#city").val("1");
							// console.log($scope.schoolData.city);
							$('#addSchoolModal').modal('show');

						});
					};

				});

dashboardApp.controller('ManageSchoolHolidaysController', function($scope,
		$rootScope, $resource, $timeout, $http, userService) {

	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}

	$scope.user = userService.getUser();

	$scope.schoolHolidays = {
		holidays : [],
		defaultSchool : null
	};
	$scope.schoolHolidayMasterData = [];
	$scope.schools = [];
	$scope.selectedSchoolFilter = null;

	$scope.holidayData = {
		sjaFlag : 'a',
		selectedMasterData : null
	};
	if ($scope.user.role == 'administrator') {
		$scope.getSchoolHolidayMasterData = function() {
			$http.get('user/getSchoolHolidayMasterData/1').then(
					function(response) {
						$scope.schoolHolidayMasterData = [];
						for (var i = 0; i < response.data.length; i++) {
							$scope.schoolHolidayMasterData.push({
								Id : response.data[i].id,
								Name : response.data[i].description,
								Item : response.data[i]
							});
						}
					});
		}
		$scope.getSchoolHolidayMasterData();

	}
	$scope.getHolidays = function() {
		$http.get(
				'user/getSchoolHolidaysByUserId/'
						+ javaRest.cookie.get('userId')).then(
				function(response) {
					$scope.schoolHolidays = response.data;
				});
	}
	$scope.getHolidays();

	if ($scope.user.role == 'superAdmin') {
		$http.get('user/getSchoolsForDropdown').success(function(schools) {
			$scope.schools = [];
			for (var i = 0; i < schools.length; i++) {
				var item = schools[i];
				if (item.name && item.name.length > 0)
					$scope.schools.push({
						Id : item.id,
						Name : item.name,
						Item : item
					});
			}
		});
	}

	$scope.addSchoolHolidayFormModal = function() {
		$scope.holidayData = {
			sjaFlag : 'a'
		};
		$('#addSchoolHolidayModal').modal('show');
	}

	$scope.submitSchoolHolidayForm = function() {
		var data = $scope.holidayData;

		if (!data.addToAllSchools)
			data.addToAllSchools = false;

		data.description = data.selectedMasterData.Name;

		if ($scope.user.role == 'superAdmin') {
			if (data.selectedSchool && data.selectedSchool.Id)
				data.school = {
					id : data.selectedSchool.Id
				};
		} else {
			data.school = {
				id : $scope.schoolHolidays.defaultSchool.id
			};
		}

		$http.post('user/addSchoolHoliday', data, {
			json : true
		}).then(function(response) {
			$scope.getHolidays();

			$('#addSchoolHolidayModal').modal('hide');
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
					$scope.showSuccessAlert = false;
				}, 2000);
			}

		}, function() {

		});
	}

	$scope.modifySchoolHolidayForm = function(holiday) {
		 
		
		$scope.holidayData.fromDate=new Date(holiday.fromDate);
		$scope.holidayData.toDate=new Date(holiday.toDate);
		$scope.holidayData.selectedMasterData=holiday.description;
		$scope.holidayData.sjaFlag=holiday.sjaFlag;
		$scope.holidayData.id=holiday.id;

		$('#addSchoolHolidayModal').modal('show');
	};

	$scope.deleteHoliday = function(holiday) {
		var resp = confirm('Are you sure you want to delete this holiday?');

		if (resp)
			$http.get('user/deleteSchoolHoliday/' + holiday.id).success(
					function(data) {
						$scope.getHolidays();
					});
	}

	$scope.fillDates = function(item) {
		if (item && item.Id) {
			$scope.holidayData.toDate = new Date(item.Item.toDate);
			$scope.holidayData.fromDate = new Date(item.Item.fromDate);
		}
	}

});

dashboardApp
		.controller(
				'ManageSchoolHolidayMasterDataController',
				function($scope, $rootScope, $resource, $timeout, $http,
						userService) {
					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}

					$scope.user = userService.getUser();
					$scope.schoolHolidayMasterData = [];
					$scope.masterData = null;

					$scope.getSchoolHolidayMasterData = function() {
						$http
								.get('user/getSchoolHolidayMasterData/1')
								.then(
										function(response) {
											$scope.schoolHolidayMasterData = response.data;
										});
					}
					$scope.getSchoolHolidayMasterData();

					$scope.addMasterData = function() {
						$scope.masterData = {};
						$('#add-update-model').modal('show');
					}

					$scope.addUpdateMasterData = function() {
						var data = $scope.masterData;
						$http.post('user/addSchoolHolidayMasterData', data, {
							json : true
						}).then(function(response) {
							$scope.getSchoolHolidayMasterData();
							$('#add-update-model').modal('hide');
							if (data.errors) {
								$scope.successTextAlert = "Error";
								$scope.showSuccessAlert = true;

								$scope.switchBool = function(value) {
									$scope[value] = !$scope[value];
								};
							} else {
								$scope.successTextAlert = "Saved!!";
								$scope.showSuccessAlert = true;

								$scope.switchBool = function(value) {
									$scope[value] = !$scope[value];
								};

								$timeout(function() {
									$scope.showSuccessAlert = false;
								}, 2000);
							}

						}, function() {

						});
					}

					$scope.editMasterData = function(masterData) {
						masterData.toDate = new Date(masterData.toDate);
						masterData.fromDate = new Date(masterData.fromDate);
						$scope.masterData = masterData;
						$('#add-update-model').modal('show');
					};

					$scope.deleteMasterData = function(masterData) {
						var resp = confirm('Are you sure you want to delete this holiday master data?');

						if (resp)
							$http.get(
									'user/deleteSchoolHolidayMasterData/'
											+ masterData.id).success(
									function(data) {
										$scope.getSchoolHolidayMasterData();
									});
					}

				});

dashboardApp
		.controller(
				'ManageStudentController',
				function($scope, $rootScope, $resource, $timeout, $http,
						routeService, studentService, $route, $location,$filter) {

					if (!$(".sidebar-offcanvas").hasClass('collapse-left')
							|| $(".row-offcanvas").hasClass('active')) {
						$("[data-toggle='offcanvas']").click();
						$(".row-offcanvas").removeClass('active');
					}
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();
					$scope.juniorSel = true;
					$scope.seniorSel = true;
					
					$scope.student = {};
					$scope.student.dropTime = new Date();
					$scope.student.pickTime = new Date();
					// $scope.routeName.name = 'All Routes';
					$scope.selectedValue = 'All Routes';

					routeService.findAllRouteList().$promise.then(function(
							routeList) {
						$scope.routeList = routeList;
					});

					$scope.classIncludes = [];
					$scope.approveFlag = [];
					$scope.approveFlag.push("Y");
					$scope.allStudentList=[];
				// ==========================CheckBox
				// filter=======================//
					 $('.product-list').on('change', function() {
					        $('.product-list').not(this).prop('checked', false);  
					    });
					 
					 $scope.checkboxes = [true,true,true,false];
					 $scope.checkAll = false;
					 $scope.selectedList = {};
					 $scope.$watch('checkboxes', function(newValue, oldValue){
						 if(newValue){
							$http.get('students/getAllStudentDetailsAdmin/'+javaRest.cookie.get('userId')).success(function(data){
								
								$scope.allStudentList= angular.copy(data);
								$rootScope.allStudentList = $scope.allStudentList;
								
								 $scope.studentList = $rootScope.allStudentList.filter(function(s){
										
										return ["I", "II", "III", "IV",
											"U", "V", "VI", "VII", "VIII", "IX", "X",
											"XI", "XII","Nursery", "Pre Primary",
											"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1 && s.isApproved == 'Y' && (!$scope.routeName || s.routeName==$scope.routeName);
									});
								//console.log($scope.studentList);
								 $rootScope.student=$scope.studentList;
							});
						 }
						
						 
				        });
					 
				
					 
					 $scope.click = function(checkboxes){
						 
						 
						 if(checkboxes[0] == true && checkboxes[1] == true && checkboxes[2] == true && checkboxes[3] == true){
							 
							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
									
									return ["I", "II", "III", "IV",
										"U", "V", "VI", "VII", "VIII", "IX", "X",
										"XI", "XII","Nursery", "Pre Primary",
										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1  && (!$scope.routeName || s.routeName==$scope.routeName);
								});
							 
							 $rootScope.student =$scope.studentList;
						 }
						 
                           if(checkboxes[0] == false && checkboxes[1] == false && checkboxes[2] == true && checkboxes[3] == true){
							 
							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
									
									return ["I", "II", "III", "IV",
										"U", "V", "VI", "VII", "VIII", "IX", "X",
										"XI", "XII","Nursery", "Pre Primary",
										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1  && (!$scope.routeName || s.routeName==$scope.routeName);
								});
							 $rootScope.student =$scope.studentList;
						 }
						 
                          if(checkboxes[0] == true && checkboxes[1] == false && checkboxes[2] == true && checkboxes[3] == true){
							 
							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
									
									return ["I", "II", "III", "IV",
										"U", "V", "VI", "VII", "VIII", "IX", "X",
										"XI", "XII"].indexOf(s.studentClass)>-1  && (!$scope.routeName || s.routeName==$scope.routeName);
								});
							 $rootScope.student  =$scope.studentList;
						 }
                          
                          if(checkboxes[0] == true && checkboxes[1] == false && checkboxes[2] == true && checkboxes[3] == false){
 							 
 							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
 									
 									return ["I", "II", "III", "IV",
 										"U", "V", "VI", "VII", "VIII", "IX", "X",
 										"XI", "XII"].indexOf(s.studentClass)>-1 && s.isApproved == 'Y' && (!$scope.routeName || s.routeName==$scope.routeName);
 								});
 							 
 							$rootScope.student  =$scope.studentList;
 						 }
                          
                          if(checkboxes[0] == true && checkboxes[1] == false && checkboxes[2] == false && checkboxes[3] == true){
  							 
  							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
  									
  									return ["I", "II", "III", "IV",
  										"U", "V", "VI", "VII", "VIII", "IX", "X",
  										"XI", "XII"].indexOf(s.studentClass)>-1 && s.isApproved !== 'Y' && (!$scope.routeName || s.routeName==$scope.routeName);
  								});
  							$rootScope.student  =$scope.studentList;
  						 }
                          
                          if(checkboxes[0] == false && checkboxes[1] == true && checkboxes[2] == true && checkboxes[3] == false){
 							 
 							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
 									
 									return ["Nursery", "Pre Primary",
										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1 && s.isApproved == 'Y' && (!$scope.routeName || s.routeName==$scope.routeName);
 								});
 							$rootScope.student  =$scope.studentList;
 						 }
                          if(checkboxes[0] == false && checkboxes[1] == true && checkboxes[2] == false && checkboxes[3] == true){
  							 
  							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
  									
  									return ["Nursery", "Pre Primary",
 										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1 && s.isApproved !== 'Y' && (!$scope.routeName || s.routeName==$scope.routeName);
  								});
  							$rootScope.student  =$scope.studentList;
  						 }
                          
                          if(checkboxes[0] == false && checkboxes[1] == true && checkboxes[2] == true && checkboxes[3] == true){
   							 
   							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
   									
   									return ["Nursery", "Pre Primary",
  										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1  && (!$scope.routeName || s.routeName==$scope.routeName);
   								});
   							$rootScope.student  =$scope.studentList;
   						 }
                          
                          if(checkboxes[0] == false && checkboxes[1] == false && checkboxes[2] == true && checkboxes[3] == false){
 							 
 							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
 									
 									return ["I", "II", "III", "IV",
 										"U", "V", "VI", "VII", "VIII", "IX", "X",
 										"XI", "XII","Nursery", "Pre Primary",
 										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1 && s.isApproved == 'Y' && (!$scope.routeName || s.routeName==$scope.routeName);
 								});
 							$rootScope.student =$scope.studentList;
 						 }
                          
                          if(checkboxes[0] == false && checkboxes[1] == false && checkboxes[2] == false && checkboxes[3] == true){
  							 
  							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
  									
  									return ["I", "II", "III", "IV",
  										"U", "V", "VI", "VII", "VIII", "IX", "X",
  										"XI", "XII","Nursery", "Pre Primary",
  										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1 && s.isApproved !== 'Y' && (!$scope.routeName || s.routeName==$scope.routeName);
  								});
  							$rootScope.student=$scope.studentList;
  						 }
                          
                          if(checkboxes[0] == false && checkboxes[1] == false && checkboxes[2] == false && checkboxes[3] == false){
   							 
   							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
   									
   									return ["I", "II", "III", "IV",
   										"U", "V", "VI", "VII", "VIII", "IX", "X",
   										"XI", "XII","Nursery", "Pre Primary",
   										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1 && (!$scope.routeName || s.routeName==$scope.routeName);
   								});
   							$rootScope.student =$scope.studentList;
   						 }
                          
                          if(checkboxes[0] == true && checkboxes[1] == true && checkboxes[2] == false && checkboxes[3] == false){
    							 
    							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
    									
    									return ["I", "II", "III", "IV",
    										"U", "V", "VI", "VII", "VIII", "IX", "X",
    										"XI", "XII","Nursery", "Pre Primary",
    										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1 && (!$scope.routeName || s.routeName==$scope.routeName);
    								});
    							 $rootScope.student  =$scope.studentList;
    						 }
                          
                          if(checkboxes[0] == true && checkboxes[1] == true && checkboxes[2] == false && checkboxes[3] == true){
 							 
 							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
 									
 									return ["I", "II", "III", "IV",
 										"U", "V", "VI", "VII", "VIII", "IX", "X",
 										"XI", "XII","Nursery", "Pre Primary",
 										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1 && s.isApproved !== 'Y' && (!$scope.routeName || s.routeName==$scope.routeName);
 								});
 							$rootScope.student =$scope.studentList;
 						 }
                          
                          if(checkboxes[0] == true && checkboxes[1] == true && checkboxes[2] == true && checkboxes[3] == false){
  							 
  							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
  									
  									return ["I", "II", "III", "IV",
  										"U", "V", "VI", "VII", "VIII", "IX", "X",
  										"XI", "XII","Nursery", "Pre Primary",
  										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1 && s.isApproved == 'Y' && (!$scope.routeName || s.routeName==$scope.routeName);
  								});
  							$rootScope.student  =$scope.studentList;
  						 }
                          
                          if(checkboxes[0] == true && checkboxes[1] == false && checkboxes[2] == false && checkboxes[3] == false){
   							 
   							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
   									
   									return ["I", "II", "III", "IV",
   										"U", "V", "VI", "VII", "VIII", "IX", "X",
   										"XI", "XII"].indexOf(s.studentClass)>-1 && (!$scope.routeName || s.routeName==$scope.routeName);
   								});
   							$rootScope.student =$scope.studentList;
   						 }
                          if(checkboxes[0] == false && checkboxes[1] == true && checkboxes[2] == false && checkboxes[3] == false){
    							 
    							 $scope.studentList = $rootScope.allStudentList.filter(function(s){
    									
    									return ["Nursery", "Pre Primary",
      										"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1 && (!$scope.routeName || s.routeName==$scope.routeName);
    								});
    							 $rootScope.student  =$scope.studentList;
    						 }
							 
					 }
					 
										// ==========================End=====================================//
					/*$scope.$watchGroup(['studentTypeFilter','routeName'],function(){
						var filter = $scope.studentTypeFilter;
						// console.log( $scope.allStudentList)
						
						if(filter==0)
						{
							$scope.studentList = $scope.allStudentList.filter(function(s){
								
								return ["I", "II", "III", "IV",
									"U", "V", "VI", "VII", "VIII", "IX", "X",
									"XI", "XII"].indexOf(s.studentClass)>-1 && (!$scope.routeName || s.routeName==$scope.routeName);
							});
						}else if(filter==1)
							{
							$scope.studentList = $scope.allStudentList.filter(function(s){
								return ["Nursery", "Pre Primary",
									"Pre-Nursery", "Pre_Primary"].indexOf(s.studentClass)>-1 && (!$scope.routeName || s.routeName==$scope.routeName);
								
							});
							}else{
								$scope.studentList = $scope.allStudentList.filter(function(s){
									
									return s.isApproved != 'Y' && (!$scope.routeName || s.routeName==$scope.routeName);
								});
							}
						
					});*/

					
					
				/*
				 * $http.get('students/getAllStudentDetailsAdmin/'+javaRest.cookie.get('userId')).success(function(data){
				 * 
				 * $scope.allStudentList= angular.copy(data);
				 * $scope.studentList=data;
				 * 
				 * });
				 */
					
					routeService.findAllRouteList().$promise.then(function(
							routeList) {
						$scope.routeList = routeList;
					});

					studentService.getStudentClass().$promise.then(function(
							studentClass) {
						$scope.studentClass = studentClass;
					});

					studentService.getStudentSection().$promise.then(function(
							studentSection) {
						$scope.studentSection = studentSection;
					});
					$scope.loadWayPointByRoute = function () {
						routeService.findAllWayPointListforRoute($scope.student.routeId).$promise.then(function(wayPointList) {
							$scope.wayPointList = wayPointList;
						});
					}
					
					$scope.loadDetailsByWayPoint = function () {
						if($scope.student.wayPointId == -1){
							$scope.detailsVisibility = false;
							return
						}
						$scope.detailsVisibility = true;
						console.log($scope.student.wayPointId);
						routeService.getScheduleByWayPoint($scope.student.wayPointId).$promise.then(function(wayPointDetails) {
							$scope.wayPointDetails = wayPointDetails;
						});
						studentService.getStudentsByWayPoint($scope.student.wayPointId).$promise.then(function(wayPointStudentsList) {
							$scope.wayPointStudentsList = wayPointStudentsList;
						});
					}

					$scope.addStudent = function() {
						$scope.detailsVisibility = false;
						$scope.student = {};
						$scope.student.drop = new Date();
						$scope.student.winterPickup = new Date();
						$scope.student.pickTimeSummer = new Date();

					}

					$scope.closeNotification = function() {
						$scope.successTextAlert = undefined;
						$scope.errorTextAlert = undefined;
					}

					$scope.saveStudent = function() {

						studentService
								.addStudent()
								.save(
										{
											userId : javaRest.cookie
													.get('userId')
										},
										$scope.student,
										function(resp, headers) {

											$("#addStudentModal").modal('hide');
											$scope.successTextAlert = "Student details has been added. You can view the details once after approved by Administrator, please check back later.";
											$scope.showSuccessAlert = true;

											
											  $timeout(function () {
											  $scope.showSuccessAlert = false;
											  $route.reload();
											  $location.path("/manageStudents.html"); },
											  2000);
											 

											 angular.copy({}, $scope.student);
											$scope.studentList = [];
											// $scope.listStudent();

										},
										function(err) {
											$scope.errorTextAlert = "Error: "
													+ err.message;
											$("#addStudentModal").modal('hide');
											angular.copy({}, $scope.student);
											$timeout(
													function() {
														$location.reload();
														$scope.errorTextAlert = undefined;
													}, 10000);
										});

					}

					$scope.approveStudent = function(studentUUID, isApproved) {
						var action = isApproved == 'N' ? 'approve'
								: 'disapprove';
						var data = {
							"studentUUID" : studentUUID,
							"action" : action
						};

						var reqParamsArr = [];
						for ( var p in data) {
							reqParamsArr.push(encodeURIComponent(p) + "="
									+ encodeURIComponent(data[p]));
						}
						var reqParamData = reqParamsArr.join("&");
						// alert(reqParamData);

						$http(
								{
									method : 'POST',
									url : 'jsp/approveStudents.jsp',
									data : $scope.routeData, // forms user
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
									location.reload();
									$scope.showSuccessAlert = false;
								}, 2000);
							}
						});
					}

					$scope.downloadStudentReport = function() {
						$scope.downloadStudentReportData = $resource(
								'reports/downloadReport/:userId/:reportName',
								{
									userId : javaRest.cookie.get('userId'),
									reportName : 'studentReport'
								},
								{
									get : {
										method : 'GET',
										isArray : false,
										headers : {
											'Authorization' : getAuthToken(
													"reports/downloadReport/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce
										}
									}
								}).get();

						$scope.downloadStudentReportData.$promise
								.then(function(downloadStudentReportData) {
									window
											.open("uploads/reports/studentReport_"
													+ javaRest.cookie
															.get('userId')
													+ ".xls");
								});
					}
					$scope.downloadStudentReportAllData = function() {
						$scope.downloadStudentReportData = $resource(
								'reports/downloadReport/:userId/:reportName',
								{
									userId : javaRest.cookie.get('userId'),
									reportName : 'studentReportAllData'
								},
								{
									get : {
										method : 'GET',
										isArray : false,
										headers : {
											'Authorization' : getAuthToken(
													"reports/downloadReport/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce
										}
									}
								}).get();

						$scope.downloadStudentReportData.$promise
								.then(function(downloadStudentReportData) {
									window
											.open("uploads/reports/studentReport_"
													+ javaRest.cookie
															.get('userId')
													+ ".xls");
								});
					}

					$scope.getrootValue = function(routeName) {
						
					  $scope.studentList =$filter('filter')( $rootScope.student,{routeName:routeName});
						//$scope.selectedValue = $scope.routeName;
					}
					var dropTemp;
					var summerTemp;
					var winterTemp;
					$scope.closePopup = function(student) {
						student.drop = dropTemp;
						student.pickTimeSummer = summerTemp;
						student.winterPickup = winterTemp;
					}
					$scope.modifyStudentForm = function(student) {

						$scope.detailsVisibility = true;
						$scope.student = angular.copy(student);
						$scope.loadWayPointByRoute();
						$scope.loadDetailsByWayPoint();
						$scope.student.drop = new Date();
						$scope.student.winterPickup = new Date();
						$scope.student.pickTimeSummer = new Date();
						
						$('#addStudentModal').modal('show');
					};

				});


dashboardApp.controller('reportsController', function($scope, $rootScope) {

	// Show reports icons
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}

	$scope.reports = {};
	$scope.reports.reportList = [];

	$scope.reports.reportList.push({
		"link" : "#positionReport.html",
		"linkName" : "Position Summary",
		"icon" : "icons/report.png",
	});
	$scope.reports.reportList.push({
		"link" : "#fleetReport.html",
		"linkName" : "Fleet Summary",
		"icon" : "icons/report.png",
	});
	$scope.reports.reportList.push({
		"link" : "#haltReport.html",
		"linkName" : "Long Halt",
		"icon" : "icons/report.png",
	});
	$scope.reports.reportList.push({
		"link" : "#overSpeedingReport.html",
		"linkName" : "Over Speeding",
		"icon" : "icons/report.png",
	});
	$scope.reports.reportList.push({
		"link" : "#busStopTimeReport.html",
		"linkName" : "Bus Stop Time",
		"icon" : "icons/report.png",
	});
	$scope.reports.reportList.push({
		"link" : "#studentStopTimeReport.html",
		"linkName" : "Student Stop",
		"icon" : "icons/report.png",
	});
	$scope.reports.reportList.push({
		"link" : "#busStopPredictiveReport.html",
		"linkName" : "Predict Bus Stop",
		"icon" : "icons/report.png",
	});
	$scope.reports.reportList.push({
		"link" : "#tripSummaryReport.html",
		"linkName" : "Trip Summary",
		"icon" : "icons/report.png",
	});
	// ===============Add Message summary and Student message
	// report=============//
	/*
	 * $scope.reports.reportList.push({ "link" : "#messageSummaryReport.html",
	 * "linkName" : "Message summary", "icon" : "icons/report.png", });
	 * $scope.reports.reportList.push({ "link" : "#studentSummaryReport.html",
	 * "linkName" : "Student Message", "icon" : "icons/report.png", });
	 * $scope.reports.reportList.push({ "link" : "#viewLogs.html", "linkName" :
	 * "View Logs", "icon" : "icons/report.png", });
	 */
	
// =================================End==============================================//
});

dashboardApp.controller('holidaysController', function($scope, $rootScope) {

	// Show reports icons
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}

	$scope.links = [];

	$scope.links.push({
		"link" : "#manageSchoolHolidays.html",
		"linkName" : "Manage Holidays",
		"icon" : "icons/holiday-icon.png",
	});
	$scope.links.push({
		"link" : "#manageSchoolHolidayMasterData.html",
		"linkName" : "Manage Master Data",
		"icon" : "icons/holiday-icon.png",
	});

});
dashboardApp.controller('BaseReportController', function($scope, $rootScope,
		$resource, $timeout) {

	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}

	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

	var currentDate = new Date();
	$scope.fromDate = new Date(currentDate.getFullYear(), currentDate
			.getMonth(), currentDate.getDate());
	$scope.toDate = new Date(currentDate.getFullYear(), currentDate.getMonth(),
			currentDate.getDate(), 23, 59);

	$scope.allVehicleList = $resource(
			'reports/getAllVehicleList/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'Authorization' : getAuthToken(
								"reports/getAllVehicleList/", time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();

	$scope.allVehicleList.$promise.then(function(allVehicleList) {

	});

});


dashboardApp
		.controller(
				'HaltReportController',
				function($scope, $rootScope, $resource, $timeout, $controller,
						$location) {

					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					$scope.back = function() {
						$location.path("/reports.html");
					}

					angular.extend(this, $controller('BaseReportController', {
						$scope : $scope
					}));
					
					$scope.haltLimit=20;
                    
					$scope.loadHaltReport = function() {

						if ($scope.fromDate == undefined) {
							alert("Please select From Date.");
							return;
						} else if ($scope.toDate == undefined) {
							alert("Please select To Date.");
							return;
						} else if ($scope.toDate < $scope.fromDate) {
							bootbox
									.alert("<b>From Date should not be grater than To Date</b>");
							return;
						} else if ($scope.haltLimit == undefined) {
							bootbox.alert("<b>Please enter Halt Limit.</b>");
							return;
						} else if ($scope.haltLimit < 0) {
							bootbox
									.alert("<b>Don't enter negative Halt Limit.</b>");
							return;
						} else if ($scope.vehicleId == undefined
								|| $scope.vehicleId == "") {
							bootbox.alert("<b>Please select Vehicle.</b>");
							return;
						}

						$scope.haltReportList = $resource(
								'reports/getHaltSummary/:userId',
								{
									userId : javaRest.cookie.get('userId')
								},
								{
									put : {
										method : 'PUT',
										isArray : true,
										headers : {
											'Authorization' : getPutAuthToken(
													"reports/getHaltSummary/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce,
											'Content-Type' : 'application/json'
										},
										transformRequest : function(data,
												headersGetter) {
											var headers = headersGetter();
											var time = javaRest.get_iso_date();
											var nonce = makeRandomString();
											headers['Content-Type'] = "application/json";
											headers['Authorization'] = getPutAuthToken(
													"reports/getHaltSummary/",
													time, nonce);
											headers['x-safeplanet-date'] = time;
											headers['nonce'] = nonce;

											var haltLimitVar = -1;
											var fromDateVar = new Date();
											var toDateVar = new Date();
											var vehicleIdVar = "-1";

											vehicleIdVar = $scope.vehicleId;

											haltLimitVar = $scope.haltLimit;
											fromDateVar = $scope.fromDate;
											toDateVar = $scope.toDate;

											var filterData = '{"fromDate":"'
													+ fromDateVar.getFullYear()
													+ "-"
													+ addZero(fromDateVar
															.getMonth() + 1)
													+ "-"
													+ addZero(fromDateVar
															.getDate())
													+ "T"
													+ addZero(fromDateVar
															.getHours())
													+ ":"
													+ addZero(fromDateVar
															.getMinutes())
													+ ":00"
													+ '", "toDate":"'
													+ toDateVar.getFullYear()
													+ "-"
													+ addZero(toDateVar
															.getMonth() + 1)
													+ "-"
													+ addZero(toDateVar
															.getDate())
													+ "T"
													+ addZero(toDateVar
															.getHours())
													+ ":"
													+ addZero(toDateVar
															.getMinutes())
													+ ":00" + '", "haltLimit":'
													+ haltLimitVar
													+ ', "vehicleId":'
													+ vehicleIdVar + '}';

											return filterData;
										}
									}
								}).put();

						$scope.haltReportList.$promise.then(function(
								haltReportList) {

						});
					}

					$scope.downloadHaltReport = function() {
						$scope.downloadHaltReportData = $resource(
								'reports/downloadReport/:userId/:reportName',
								{
									userId : javaRest.cookie.get('userId'),
									reportName : 'haltReport'
								},
								{
									get : {
										method : 'GET',
										isArray : false,
										headers : {
											'Authorization' : getAuthToken(
													"reports/downloadReport/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce
										}
									}
								}).get();

						$scope.downloadHaltReportData.$promise.then(function(
								downloadHaltReportData) {
							window.open("uploads/reports/haltSummaryReport_"
									+ javaRest.cookie.get('userId') + ".xls");
						});
					}
				});

dashboardApp.controller('BusStopTimeReportController', function ($scope, $rootScope, $resource, $timeout, $controller, $location, routeService) {

    var time = javaRest.get_iso_date();
    var nonce = makeRandomString();

    $scope.back = function () {
        $location.path("/reports.html");
    }

    angular.extend(this, $controller('BaseReportController', {
        $scope: $scope
    }));

    $scope.loadBusStopTimeReport = function () {

        if ($scope.fromDate == undefined) {
            alert("Please select From Date.");
            return;
        } else if ($scope.toDate == undefined) {
            alert("Please select To Date.");
            return;
        }else if ($scope.toDate < $scope.fromDate) {
       	 bootbox.alert("<b>From Date should not be grater than To Date</b>");
         return;
        }else if ($scope.vehicleId == undefined || $scope.vehicleId == "") {
            bootbox.alert("<b>Please select Vehicle.</b>");
            return;
        } else if ($scope.schedule == undefined || $scope.vehicleId == "") {
            bootbox.alert("<b>Please select schedule.</b>");
            return;
        }

        $scope.busStopTimeReportList = $resource(
            'reports/getBusStopTimeReport/:userId',
            {
                userId: javaRest.cookie.get('userId')
            },
            {
                put: {
                    method: 'PUT',
                    isArray: true,
                    headers: {
                        'Authorization': getPutAuthToken("reports/getBusStopTimeReport/", time, nonce),
                        'x-safeplanet-date': time,
                        'nonce': nonce,
                        'Content-Type': 'application/json'
                    },
                    transformRequest: function (data, headersGetter) {
                        var headers = headersGetter();
                        var time = javaRest.get_iso_date();
                        var nonce = makeRandomString();
                        headers['Content-Type'] = "application/json";
                        headers['Authorization'] = getPutAuthToken("reports/getBusStopTimeReport/", time, nonce);
                        headers['x-safeplanet-date'] = time;
                        headers['nonce'] = nonce;

                        var speedLimitVar = -1;
                        var fromDateVar = new Date();
                        var toDateVar = new Date();
                        var vehicleIdVar = "-1";

                        vehicleIdVar = $scope.vehicleId;

                        var scheduleVar = "-1";

                        scheduleVar = $scope.schedule;

                        fromDateVar = $scope.fromDate;
                        toDateVar = $scope.toDate;

                        var filterData = '{"fromDate":"' + fromDateVar.getFullYear() + "-" + addZero(fromDateVar.getMonth() + 1) + "-" + addZero(fromDateVar.getDate()) + "T"
                            + addZero(fromDateVar.getHours()) + ":" + addZero(fromDateVar.getMinutes()) + ":00" + '", "toDate":"' + toDateVar.getFullYear() + "-"
                            + addZero(toDateVar.getMonth() + 1) + "-" + addZero(toDateVar.getDate()) + "T" + addZero(toDateVar.getHours()) + ":" + addZero(toDateVar.getMinutes()) + ":00"
                            + '", "speedLimit":' + speedLimitVar + ', "vehicleId":' + vehicleIdVar + ', "schedule":' + scheduleVar + '}';
                        return filterData;
                    }
                }
            }).put();

        $scope.busStopTimeReportList.$promise.then(function (busStopTimeReportList) {

        });
    }
    
    $scope.loadScheduleByFleet = function () {
    	routeService.getScheduleByFleet($scope.vehicleId).$promise.then(function(scheduleList) {
    		$scope.scheduleList = scheduleList;

    	});
    }

    $scope.downloadBusStopTimeReport = function () {
        $scope.downloadBusStopTimeReportData = $resource('reports/downloadReport/:userId/:reportName', {
            userId: javaRest.cookie.get('userId'),
            reportName: 'busStopTimeReport'
        }, {
            get: {
                method: 'GET',
                isArray: false,
                headers: {
                    'Authorization': getAuthToken("reports/downloadReport/", time, nonce),
                    'x-safeplanet-date': time,
                    'nonce': nonce
                }
            }
        }).get();

        $scope.downloadBusStopTimeReportData.$promise.then(function (downloadBusStopTimeReportData) {
            window.open("uploads/reports/busStopTimeReport_" + javaRest.cookie.get('userId') + ".xls");
        });
    }
});

dashboardApp.controller('BusStopPredictReportController', function($scope, $rootScope, $resource, $timeout, $controller, $location, routeService) {

    var time = javaRest.get_iso_date();
    var nonce = makeRandomString();

    $scope.back = function() {
	$location.path("/reports.html");
    }

    angular.extend(this, $controller('BaseReportController', {
	$scope : $scope
    }));

    $scope.loadBusStopTimeReport = function() {

	if ($scope.fromDate == undefined) {
	    alert("Please select From Date.");
	    return;
	} else if ($scope.toDate == undefined) {
	    alert("Please select To Date.");
	    return;
	} else if ($scope.toDate <$scope.fromDate) {
	    bootbox.alert("<b>From Date should not be grater than To Date.</b>");
	    return;
	} else if ($scope.vehicleId == undefined || $scope.vehicleId == "") {
	    bootbox.alert("<b>Please select Vehicle.</b>");
	    return;
	} else if ($scope.schedule == undefined || $scope.vehicleId == "") {
	    bootbox.alert("<b>Please select schedule.</b>");
	    return;
	}

	$scope.busStopTimeReportList = $resource(
		'reports/getBusStopPredictiveReport/:userId',
		{
		    userId : javaRest.cookie.get('userId')
		},
		{
		    put : {
			method : 'PUT',
			isArray : true,
			headers : {
			    'Authorization' : getPutAuthToken("reports/getBusStopPredictiveReport/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce,
			    'Content-Type' : 'application/json'
			},
			transformRequest : function(data, headersGetter) {
			    var headers = headersGetter();
			    var time = javaRest.get_iso_date();
			    var nonce = makeRandomString();
			    headers['Content-Type'] = "application/json";
			    headers['Authorization'] = getPutAuthToken("reports/getBusStopPredictiveReport/", time, nonce);
			    headers['x-safeplanet-date'] = time;
			    headers['nonce'] = nonce;

			    var speedLimitVar = -1;
			    var fromDateVar = new Date();
			    var toDateVar = new Date();
			    var vehicleIdVar = "-1";

			    vehicleIdVar = $scope.vehicleId;

			    var scheduleVar = "-1";

			    scheduleVar = $scope.schedule;

			    fromDateVar = $scope.fromDate;
			    toDateVar = $scope.toDate;

			    var filterData = '{"fromDate":"' + fromDateVar.getFullYear() + "-" + addZero(fromDateVar.getMonth() + 1) + "-" + addZero(fromDateVar.getDate()) + "T"
				    + addZero(fromDateVar.getHours()) + ":" + addZero(fromDateVar.getMinutes()) + ":00" + '", "toDate":"' + toDateVar.getFullYear() + "-"
				    + addZero(toDateVar.getMonth() + 1) + "-" + addZero(toDateVar.getDate()) + "T" + addZero(toDateVar.getHours()) + ":" + addZero(toDateVar.getMinutes()) + ":00"
				    + '", "speedLimit":' + speedLimitVar + ', "vehicleId":' + vehicleIdVar + ', "schedule":' + scheduleVar + '}';
			    return filterData;
			}
		    }
		}).put();

	$scope.busStopTimeReportList.$promise.then(function(busStopTimeReportList) {

	});
    }
    $scope.loadScheduleByFleetPredict = function () {
    	routeService.getScheduleByFleet($scope.vehicleId).$promise.then(function(scheduleList) {
    		$scope.scheduleList = scheduleList;

    	});
    }

    $scope.downloadBusStopTimeReport = function() {
	$scope.downloadBusStopTimeReportData = $resource('reports/downloadReport/:userId/:reportName', {
	    userId : javaRest.cookie.get('userId'),
	    reportName : 'busStopTimeReport'
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

	$scope.downloadBusStopTimeReportData.$promise.then(function(downloadBusStopTimeReportData) {
	    window.open("uploads/reports/busStopTimeReport_" + javaRest.cookie.get('userId') + ".xls");
	});
    }
});


dashboardApp.controller('StudentStopReportController', function($scope, $rootScope, $resource, $timeout, $controller, $location) {

	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

	$scope.back = function() {
		$location.path("/reports.html");
	}

	angular.extend(this, $controller('BaseReportController', {
		$scope : $scope
	}));

	$scope.loadStudentStopTimeReport = function() {

		if ($scope.fromDate == undefined) {
			bootbox.alert("<b>Please select Date.</b>");
			return;
		} else if ($scope.vehicleId == undefined || $scope.vehicleId == "") {
			bootbox.alert("<b>Please select Vehicle.</b>");
			return;
		} else if ($scope.studentId == undefined || $scope.studentId == "") {
			bootbox.alert("<b>Please select a Student.</b>");
			return;
		}

		$scope.studentStopTimeReportList = $resource(
				'reports/getStudentStopTimeReport/:userId',
				{
					userId : javaRest.cookie.get('userId')
				},
				{
					put : {
						method : 'PUT',
						isArray : true,
						headers : {
							'Authorization' : getPutAuthToken(
									"reports/getStudentStopTimeReport/", time,
									nonce),
							'x-safeplanet-date' : time,
							'nonce' : nonce,
							'Content-Type' : 'application/json'
						},
						transformRequest : function(data, headersGetter) {
							var headers = headersGetter();
							var time = javaRest.get_iso_date();
							var nonce = makeRandomString();
							headers['Content-Type'] = "application/json";
							headers['Authorization'] = getPutAuthToken(
									"reports/getStudentStopTimeReport/", time,
									nonce);
							headers['x-safeplanet-date'] = time;
							headers['nonce'] = nonce;

							var fromDateVar = new Date();
							var vehicleIdVar = "-1";
							var studentIdVar = "-1";

							fromDateVar = $scope.fromDate;
							vehicleIdVar = $scope.vehicleId;
							studentIdVar = $scope.studentId;

							var filterData = '{"fromDate":"'
									+ fromDateVar.getFullYear() + "-"
									+ addZero(fromDateVar.getMonth() + 1) + "-"
									+ addZero(fromDateVar.getDate()) + "T"
									+ addZero(fromDateVar.getHours()) + ":"
									+ addZero(fromDateVar.getMinutes()) + ":00"
									+ '", "vehicleId":' + vehicleIdVar
									+ ', "studentId":' + studentIdVar + '}';
							return filterData;
						}
					}
				}).put();

		$scope.studentStopTimeReportList.$promise.then(function(
				studentStopTimeReportList) {

		});
	}
	
	$scope.loadStudentsByFleet = function() {
		$scope.loadStudentsByFleetData = $resource(
				'students/students/:userId/:fleetId',
				{
					userId : javaRest.cookie.get('userId'),
					fleetId : $scope.vehicleId
				},
				{
					get : {
						method : 'GET',
						isArray : true,
						headers : {
							'Authorization' : getAuthToken(
									"students/students/", time, nonce),
							'x-safeplanet-date' : time,
							'nonce' : nonce
						}
					}
				}).get();

		$scope.loadStudentsByFleetData.$promise.then(function(
				studentListByFleet) {
			$scope.studentListByFleet = studentListByFleet;
		});

	}

	$scope.downloadStudentStopTimeReport = function() {
		$scope.downloadStudentStopTimeReportData = $resource(
				'reports/downloadReport/:userId/:reportName',
				{
					userId : javaRest.cookie.get('userId'),
					reportName : 'studentStopTimeReport'
				},
				{
					get : {
						method : 'GET',
						isArray : false,
						headers : {
							'Authorization' : getAuthToken(
									"reports/downloadReport/", time, nonce),
							'x-safeplanet-date' : time,
							'nonce' : nonce
						}
					}
				}).get();

		$scope.downloadStudentStopTimeReportData.$promise.then(function(
				downloadStudentStopTimeReportData) {
			window.open("uploads/reports/studentStopTimeReport_"
					+ javaRest.cookie.get('userId') + ".xls");
		});
	}
});

dashboardApp
		.controller(
				'OverSpeedReportController',
				function($scope, $rootScope, $resource, $timeout, $controller,
						$location,routeService) {

					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					$scope.back = function() {
						$location.path("/reports.html");
					}

					angular.extend(this, $controller('BaseReportController', {
						$scope : $scope
					}));
					 
					$scope.speedLimit=60;
                    
                    $scope.loadSpeedByFleet = function () {
                    	routeService.getSpeedByFleet($scope.vehicleId).$promise.then(function(deviceVm) {
                    		$scope.speedLimit=deviceVm.speedLimit;
                    	});
                    }
					$scope.loadOverSpeedReport = function() {

						if ($scope.fromDate == undefined) {
							alert("Please select From Date.");
							return;
						} else if ($scope.toDate == undefined) {
							alert("Please select To Date.");
							return;
						} else if ($scope.toDate < $scope.fromDate) {
							bootbox
									.alert("<b>From Date should not be grater than To Date</b>");
							return;
						} else if ($scope.vehicleId == undefined
								|| $scope.vehicleId == "") {
							bootbox.alert("<b>Please select Vehicle.</b>");
							return;
						} else if ($scope.speedLimit == undefined) {
							bootbox.alert("<b>Please enter Speed Limit.</b>");
							return;
						} else if ($scope.speedLimit < 0) {
							bootbox
									.alert("<b>Don't enter negative Speed Limit</b>");
							return;
						}

						$scope.overSpeedReportList = $resource(
								'reports/getOverSpeedingSummary/:userId',
								{
									userId : javaRest.cookie.get('userId')
								},
								{
									put : {
										method : 'PUT',
										isArray : true,
										headers : {
											'Authorization' : getPutAuthToken(
													"reports/getOverSpeedingSummary/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce,
											'Content-Type' : 'application/json'
										},
										transformRequest : function(data,
												headersGetter) {
											var headers = headersGetter();
											var time = javaRest.get_iso_date();
											var nonce = makeRandomString();
											headers['Content-Type'] = "application/json";
											headers['Authorization'] = getPutAuthToken(
													"reports/getOverSpeedingSummary/",
													time, nonce);
											headers['x-safeplanet-date'] = time;
											headers['nonce'] = nonce;

											var speedLimitVar = -1;
											var fromDateVar = new Date();
											var toDateVar = new Date();
											var vehicleIdVar = "-1";

											vehicleIdVar = $scope.vehicleId;

											speedLimitVar = $scope.speedLimit;
											fromDateVar = $scope.fromDate;
											toDateVar = $scope.toDate;
											var filterData = '{"fromDate":"'
												+ fromDateVar.getFullYear()
												+ "-"
												+ addZero(fromDateVar
														.getMonth() + 1)
												+ "-"
												+ addZero(fromDateVar
														.getDate())
												+ "T"
												+ addZero(fromDateVar
														.getHours())
												+ ":"
												+ addZero(fromDateVar
														.getMinutes())
												+ ":00"
												+ '", "toDate":"'
												+ toDateVar.getFullYear()
												+ "-"
												+ addZero(toDateVar
														.getMonth() + 1)
												+ "-"
												+ addZero(toDateVar
														.getDate())
												+ "T"
												+ addZero(toDateVar
														.getHours())
												+ ":"
												+ addZero(toDateVar
														.getMinutes())
												+ ":00"
												+ '", "speedLimit":'
												+ speedLimitVar
												+ ', "vehicleId":'
												+ vehicleIdVar + '}';

										return filterData;
									}
								}
							}).put();
						
						$scope.overSpeedReportList.$promise.then(function(
								overSpeedReportList) {

						});
					}

					$scope.downloadOverSpeedReport = function() {
						$scope.downloadOverSpeedReportData = $resource(
								'reports/downloadReport/:userId/:reportName',
								{
									userId : javaRest.cookie.get('userId'),
									reportName : 'overSpeedingReport'
								},
								{
									get : {
										method : 'GET',
										isArray : false,
										headers : {
											'Authorization' : getAuthToken(
													"reports/downloadReport/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce
										}
									}
								}).get();

						$scope.downloadOverSpeedReportData.$promise
								.then(function(downloadOverSpeedReportData) {
									window
											.open("uploads/reports/overSpeedingReport_"
													+ javaRest.cookie
															.get('userId')
													+ ".xls");
								});
					}
				});


dashboardApp
		.controller(
				'TripSummaryReportController',
				function($scope, $rootScope, $resource, $location, $timeout,
						$controller, leafletData) {

					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					$scope.back = function() {
						$location.path("/reports.html");
					}

					var myMovingMarker;

					angular.extend(this, $controller('BaseReportController', {
						$scope : $scope
					}));
					$scope.date= new Date();
					var fTime=new Date();
				    fTime.setHours(0);
				    fTime.setMinutes(0);
				    fTime.setSeconds(0,0)
				    $scope.fromTime=fTime;
				    
				    var tTime=new Date();
				    tTime.setHours(23);
				    tTime.setMinutes(59);
				    tTime.setSeconds(0,0);
				    $scope.toTime=tTime;
					$scope.tripSummaryReportList = {};

					angular
							.extend(
									$scope,
									{
										india : {
											lat : 28.53,
											lng : 77.38,
											zoom : 13
										},
										layers : {
											baselayers : {
												osm : {
													name : 'OpenStreetMap',
													url : 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
													type : 'xyz'
												}
											}
										},
										tripSummaryPath : {
											p1 : {
												color : '#008000',
												weight : 4,
												latlngs : []
											}
										},
										markers : {
											Start : {
												"lat" : 28.65,
												"lng" : 77.36,
												"icon" : {
													"iconUrl" : "icons/map-marker-green.png",
													"iconSize" : [ 24, 24 ],
													"iconAnchor" : [ 12, 24 ],
													"popupAnchor" : [ 0, 0 ],
													"shadowSize" : [ 0, 0 ],
													"shadowAnchor" : [ 0, 0 ]
												}
											},
											End : {
												"lat" : 28.66,
												"lng" : 77.38,
												"icon" : {
													"iconUrl" : "icons/map-marker.svg",
													"iconSize" : [ 24, 24 ],
													"iconAnchor" : [ 12, 24 ],
													"popupAnchor" : [ 0, 0 ],
													"shadowSize" : [ 0, 0 ],
													"shadowAnchor" : [ 0, 0 ]
												}
											}
										}
									});
					var currDate= new Date();
					$scope.loadTripReport = function() {

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
						} else if ($scope.vehicleId == undefined
								|| $scope.vehicleId == "") {
							bootbox.alert("<b>Please select Vehicle.</b>");
							return;
						}

						$scope.tripReportList = $resource(
								'reports/getTripSummary/:userId',
								{
									userId : javaRest.cookie.get('userId')
								},
								{
									put : {
										method : 'PUT',
										isArray : false,
										headers : {
											'Authorization' : getPutAuthToken(
													"reports/getTripSummary/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce,
											'Content-Type' : 'application/json'
										},
										transformRequest : function(data,
												headersGetter) {
											var headers = headersGetter();
											var time = javaRest.get_iso_date();
											var nonce = makeRandomString();
											headers['Content-Type'] = "application/json";
											headers['Authorization'] = getPutAuthToken(
													"reports/getTripSummary/",
													time, nonce);
											headers['x-safeplanet-date'] = time;
											headers['nonce'] = nonce;

											var speedLimitVar = -1;
											var haltLimitVar = -1;
											var fromDateVar = new Date();
											var toTimeVar = new Date();
											var fromTimeVar = new Date();
											var vehicleIdVar = "-1";

											fromDateVar = $scope.date;
											toTimeVar = $scope.toTime;
											fromTimeVar=$scope.fromTime;
											vehicleIdVar = $scope.vehicleId;

											var filterData = '{"fromDate":"'
													+ fromDateVar.getFullYear()
													+ "-"
													+ addZero(fromDateVar
															.getMonth() + 1)
													+ "-"
													+ addZero(fromDateVar
															.getDate())
													+ "T"
													+ addZero(fromTimeVar
															.getHours())
													+ ":"
													+ addZero(fromTimeVar
															.getMinutes())
													+ ":00"
													+ '", "toDate":"'
													+ fromDateVar.getFullYear()
													+ "-"
													+ addZero(fromDateVar
															.getMonth() + 1)
													+ "-"
													+ addZero(fromDateVar
															.getDate())
													+ "T"
													+ addZero(toTimeVar
															.getHours())
													+ ":"
													+ addZero(toTimeVar
															.getMinutes())
													+ ":00"
													+ '", "speedLimit":'
													+ speedLimitVar
													+ ', "haltLimit":'
													+ haltLimitVar
													+ ', "vehicleId":'
													+ vehicleIdVar + '}';

											return filterData;
										}
									}
								}).put();

						$scope.tripReportList.$promise
								.then(function(tripReportList) {
									$scope.tripSummaryReportList = tripReportList.tripSummaryReportList;
									$scope.tripSummaryPath.p1.latlngs = tripReportList.tripSummaryPath;

									$scope.markers.Start.lat = tripReportList.startLat;
									$scope.markers.Start.lng = tripReportList.startLng;

									$scope.markers.End.lat = tripReportList.endLat;
									$scope.markers.End.lng = tripReportList.endLng;
								});
					}

					$scope.downloadTripReport = function() {
						$scope.downloadTripReportData = $resource(
								'reports/downloadReport/:userId/:reportName',
								{
									userId : javaRest.cookie.get('userId'),
									reportName : 'tripReport'
								},
								{
									get : {
										method : 'GET',
										isArray : false,
										headers : {
											'Authorization' : getAuthToken(
													"reports/downloadReport/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce
										}
									}
								}).get();

						$scope.downloadTripReportData.$promise.then(function(
								downloadTripReportData) {
							window.open("uploads/reports/tripSummaryReport_"
									+ javaRest.cookie.get('userId') + ".xls");
						});
					}
				});

dashboardApp.controller('sendMessageController', function($scope, $rootScope, $resource, $interval, routeService, $timeout, $location, $route) {
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	$scope.sendToAll = false;

	$scope.textChanged = function() {
		$scope.showErrorAlert = undefined;
		$scope.showSuccessAlert = undefined;
	}
	$scope.sendToAllText = "No";
	$scope.SendSms = true;
	$scope.AppSms = true;
	$scope.changeText = function() {

		if (angular.isDefined($scope.sendToAll) && $scope.sendToAll == "1") {
			$scope.sendToAllText = "Yes";
		} else {
			$scope.sendToAllText = "No";
		}

	}
	$scope.allSmsTemplateList = $resource('system/MessageTemplates',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'Authorization' : getAuthToken("system/MessageTemplates/", time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();

	$scope.allSmsTemplateList.$promise.then(function(allSmsTemplateList) {
		$scope.allSmsTemplateList = allSmsTemplateList;
	});
	$scope.loadStudentByRoute = function () {
		routeService.getStudentByRoute($scope.routeId).$promise.then(function(studentList) {
    		$scope.studentList = studentList;
});
    }
	$scope.selectAllMsg = function() {
		if ($scope.sendToAll == true) {
			$scope.scheduleId = "";
			$scope.routeId = "";
			$scope.disableRoute = true;
			$scope.disableSchedule = true;
            $scope.disableStudent=true;
		} 
		else {
			$scope.disableStudent=false;
			$scope.disableRoute = false;
			$scope.disableSchedule = false;
		}
	};
	
	
	$scope.showSampleMessage = function() {
		
		for (var i=0;i<$scope.allSmsTemplateList.length;i++){
			if($scope.allSmsTemplateList[i].id==$scope.messageTemplateId){
				$scope.selectedMessageTemplate = $scope.allSmsTemplateList[i];
				break;
			}
		}
		
	}
	
	$scope.sendMessage = function sendMessage() {
		// Undefined
		
		$scope.showErrorAlert = undefined;
		$scope.showSuccessAlert = undefined;
		$scope.disableButton = true;
		if (angular.isUndefined($scope.messageTemplateId)
				|| $scope.messageTemplateId == "") {
			$scope.successTextAlert = "";
			$scope.showErrorAlert = "Please enter SMS Type to be sent.";
			$scope.disableButton = false;
			return;
		}

		if ($scope.sendToAll == false) {
			if (angular.isUndefined($scope.routeId)) {
				$scope.showErrorAlert = "Please select one of the route.";
				$scope.disableButton = false;
				return;
			}
			
			if (angular.isUndefined($scope.scheduleId)) {
				$scope.showErrorAlert = "Please select one of the schedule.";
				$scope.disableButton = false;
				return;
			}
		}
		
		if (($scope.messageTemplateId == 1001 && angular
				.isUndefined($scope.message))
				|| ($scope.messageTemplateId == 1001 && $scope.message == "")) {
			$scope.successTextAlert = "";
			$scope.showErrorAlert = "Please enter sms message to be sent.";
			$scope.disableButton = false;
			return;
		}
		// $scope.sendMessage = function sendMessage() {
		$scope.disableButton = false;
		// }
		$scope.sendMessageResource = $resource('route/sendMessage/:userId', {
			userId : javaRest.cookie.get('userId')
		}, {
			save : {
				method : 'PUT',
				isArray : false,
				headers : {
					'Authorization' : getPutAuthToken("route/sendMessage/", time, nonce),
					'x-safeplanet-date' : time,
					'nonce' : nonce,
					'Content-Type' : 'application/json'
				},
				transformRequest : function(data, headersGetter) {
					var headers = headersGetter();
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();
					headers['Content-Type'] = "application/json";
					headers['Authorization'] = getPutAuthToken(
							"route/sendMessage/", time, nonce);
					headers['x-safeplanet-date'] = time;
					headers['nonce'] = nonce;

					var sendMessageData = '{"routeUuid":"' + $scope.routeId
							+ '", "scheduleId":"' + $scope.scheduleId
							+ '", "messageTemplateId":"' + $scope.messageTemplateId
							+ '", "sendToAll":"' + $scope.sendToAll
							+ '", "sendSms":"' + $scope.SendSms
							+ '","studentId":"' +  sendMessageObj.studentId
							+ '", "sendAppMessage":"' + $scope.AppSms
							+ '", "message":"' + $scope.message + '"}';
					return sendMessageData;
				}
			}
		});

		var sendMessageObj = {};
		sendMessageObj.messageTemplateId = $scope.messageTemplateId;
		sendMessageObj.routeUuid = $scope.routeId;
		sendMessageObj.scheduleId = $scope.scheduleId;
		sendMessageObj.message = $scope.message;
        sendMessageObj.studentId=$scope.studentId;
        if(sendMessageObj.studentId==null){
        	sendMessageObj.studentId=1;
        }
		$scope.sendMessageResource.save({
			userId : javaRest.cookie.get('userId')
		}, sendMessageObj, function(resp, headers) {

			// console.log(resp);
			$scope.successTextAlert = "Messages sent Successfully.";
			$scope.showSuccessAlert = true;
			$scope.disableButton = true;
			$scope.messageTemplateId = "";
			$scope.scheduleId = "";
			$scope.routeUuid = "";
			$scope.message = "";
		// $scope.sendToAll = false;
			
			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};

			$timeout(function() {
				$scope.showSuccessAlert = false;
				$route.reload();
				$location.path("/SendSms.html");
			}, 3000);
		}, function(err) {
			// error callback
			// console.log(err);
			$scope.successTextAlert = "Error: in sending message.";
			$scope.showFailedAlert = true;
			$scope.disableButton = false;
			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};

			$timeout(function() {
				$scope.failedTextAlert = false;
			}, 3000);
		});
	};

	routeService.findAllRouteList().$promise.then(function(routeList) {
		$scope.routeList = routeList;
	});
	
	$scope.loadScheduleByRoute = function () {
    	routeService.getScheduleByRoute($scope.routeId).$promise.then(function(scheduleList) {
    		$scope.scheduleList = scheduleList;
    		$scope.loadStudentByRoute();
    	});
    }
	$scope.changeSchedule=function (){
	if($scope.studentId==1){
		$scope.disableSchedule = false;
	}else{
		$scope.disableSchedule = true;
	}
	}
	
});

dashboardApp.controller('sendEmailController', function($scope, $rootScope,
		$resource, $interval, routeService, $timeout, $location) {
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

	$scope.textChanged = function() {
		$scope.showErrorAlert = undefined;
		$scope.showSuccessAlert = undefined;
	}

	$scope.sendToAllText = "No";
	$scope.sendToAll = false;
	$scope.changeText = function() {
		if (angular.isDefined($scope.sendToAll) && $scope.sendToAll == "1") {
			$scope.sendToAllText = "Yes";
		} else {
			$scope.sendToAllText = "No";
		}
	}

	$scope.allEmailTemplateList = $resource(
			'system/getAllEmailTemplates',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'Authorization' : getAuthToken(
								"system/getAllEmailTemplates/", time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();

	$scope.allEmailTemplateList.$promise.then(function(allEmailTemplateList) {
		$scope.allEmailTemplateList = allEmailTemplateList;
	});

	$scope.selectAllEmail = function() {
		if ($scope.sendToAll == true) {
			$scope.scheduleId = "";
			$scope.routeId = "";
			$scope.disableRoute = true;
			$scope.disableSchedule = true;

		}
		else {

			$scope.disableRoute = false;
			$scope.disableSchedule = false;
		}
	};

	$scope.sendEmail = function sendEmail() {

		// Undefined
		$scope.showErrorAlert = undefined;
		$scope.showSuccessAlert = undefined;
		$scope.disableButton = true;
		if (angular.isUndefined($scope.emailTemplateId)
				|| $scope.emailTemplateId == "") {
			$scope.successTextAlert = "";
			$scope.showErrorAlert = "Please enter  Email Type to be sent.";
			$scope.disableButton = false;
			return;
		}
		if ($scope.sendToAll == false) {
			if (angular.isUndefined($scope.routeId) || $scope.routeId == "") {
				$scope.successTextAlert = "";
				$scope.showErrorAlert = "Please select one of the route.";
				$scope.disableButton = false;
				return;
			}

			if (angular.isUndefined($scope.scheduleId)
					|| $scope.scheduleId == "") {
				$scope.successTextAlert = "";
				$scope.showErrorAlert = "Please select one of the schedule.";
				$scope.disableButton = false;
				return;
			}
		}

		if (($scope.emailTemplateId == 1001 && angular
				.isUndefined($scope.subject))
				|| ($scope.emailTemplateId == 1001 && $scope.subject == "")) {
			$scope.successTextAlert = "";
			$scope.showErrorAlert = "Please enter email Subject to be sent.";
			$scope.disableButton = false;
			return;
		}
		if (($scope.emailTemplateId == 1001 && angular
				.isUndefined($scope.message))
				|| ($scope.emailTemplateId == 1001 && $scope.message == "")) {
			$scope.successTextAlert = "";
			$scope.showErrorAlert = "Please enter email message to be sent.";
			$scope.disableButton = false;

			return;
		}

		$scope.sendEmailResource = $resource('system/sendEmail/:userId', {
			userId : javaRest.cookie.get('userId')
		}, {
			save : {
				method : 'PUT',
				isArray : false,
				headers : {
					'Authorization' : getPutAuthToken("system/sendEmail/",
							time, nonce),
					'x-safeplanet-date' : time,
					'nonce' : nonce,
					'Content-Type' : 'application/json'
				},
				transformRequest : function(data, headersGetter) {
					var headers = headersGetter();
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();
					headers['Content-Type'] = "application/json";
					headers['Authorization'] = getPutAuthToken(
							"system/sendEmail/", time, nonce);
					headers['x-safeplanet-date'] = time;
					headers['nonce'] = nonce;

					var sendEmailData = '{"routeUuid":"' + $scope.routeId
							+ '", "subject":"' + $scope.subject
							+ '", "emailTemplateId":"' + $scope.emailTemplateId
							+ '", "scheduleId":"' + $scope.scheduleId
							+ '", "sendToAll":"' + $scope.sendToAll
							+ '", "message":"' + $scope.message + '"}';
					return sendEmailData;
				}
			}
		});

		var sendEmailObj = {};
		sendEmailObj.emailTemplateId = $scope.emailTemplateId;
		sendEmailObj.routeId = $scope.routeId;
		sendEmailObj.scheduleId = $scope.scheduleId;
		sendEmailObj.subject = $scope.subject;
		sendEmailObj.message = $scope.message;

		$scope.sendEmailResource.save({
			userId : javaRest.cookie.get('userId')
		}, sendEmailObj, function(resp, headers) {

			// console.log(resp);
			$scope.successTextAlert = "Email sent Sucessfully";
			$scope.showSuccessAlert = true;
			$scope.disableButton = true;
			sendEmailObj.emailTemplateId = $scope.emailTemplateId;
			$scope.routeId = "";
			$scope.scheduleId = "";
			$scope.subject = "";
			$scope.message = "";
			$scope.emailTemplateId = "";

			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};

			$timeout(function() {
				$scope.showSuccessAlert = false;
				$location.path("/SendEmail.html");
			}, 6000);
		}, function(err) {
			// error callback
			// console.log(err);
			$scope.successTextAlert = "Error: in sending email.";
			$scope.showFailedAlert = true;
			$scope.disableButton = false;
			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};

			$timeout(function() {
				$scope.failedTextAlert = false;
			}, 3000);
		});
	};

	routeService.findAllRouteList().$promise.then(function(routeList) {
		$scope.routeList = routeList;

	});
	
	$scope.loadScheduleByRoute = function () {
    	routeService.getScheduleByRoute($scope.routeId).$promise.then(function(scheduleList) {
    		$scope.scheduleList = scheduleList;

    	});
    }
});

dashboardApp
		.controller(
				'BusStopController',
				function($scope, $rootScope, $resource, $http, $interval,
						leafletData, $timeout, $route, $location) {

					var nonce = makeRandomString();
					var time = javaRest.get_iso_date();
					$scope.studentDetailList = $resource(
							'user/getStudentDetails/:userId',
							{
								userId : javaRest.cookie.get('userId')
							},
							{
								get : {
									method : 'GET',
									isArray : true,
									headers : {
										'Authorization' : getAuthToken(
												"user/student/", time, nonce),
										'x-safeplanet-date' : time,
										'nonce' : nonce
									}
								}
							}).get();

					$scope.studentDetailList.$promise.then(function(
							studentDetailList) {

					});
					// $scope.lattitude= $scope.studentDetailList[0].lattitude;
					// $scope.longitude= $scope.studentDetailList[0].longitude;

					$scope.sendmailForLatLongApproval = function() {
						// alert(javaRest.cookie.get('userId'));
						// console.log(javaRest.cookie.get('userId'));

						$scope.showErrorAlert = undefined;
						$scope.showSuccessAlert = undefined;

						$scope.updateLatLongResource = $resource(
								'user/sendmailForLatLongApproval/:userId',
								{
									userId : javaRest.cookie.get('userId')
								},
								{
									save : {
										method : 'PUT',
										isArray : false,
										headers : {
											'Authorization' : getPutAuthToken(
													"user/sendmailForLatLongApproval/",
													time, nonce),
											'x-safeplanet-date' : time,
											'nonce' : nonce,
											'Content-Type' : 'application/json'
										},
										transformRequest : function(data,
												headersGetter) {
											var headers = headersGetter();
											var time = javaRest.get_iso_date();
											var nonce = makeRandomString();
											headers['Content-Type'] = "application/json";
											headers['Authorization'] = getPutAuthToken(
													"user/sendmailForLatLongApproval/",
													time, nonce);
											headers['x-safeplanet-date'] = time;
											headers['nonce'] = nonce;
											var waypoint = document
													.getElementById('waypoint').value;
											var lattitude = document
													.getElementById('lat').value;
											var longitude = document
													.getElementById('long').value;
											// var message =
											// $scope.broadcastMessage;
											;
											var frmData = '{"wayPointId":"'
													+ waypoint
													+ '", "lattitude":"'
													+ lattitude
													+ '", "longitude":"'
													+ longitude + '"}';
											// alert(frmData);

											return frmData;

										}
									}
								});

						var messageObj = {};
						messageObj.message = $scope.message;

						$scope.updateLatLongResource
								.save(
										{
											userId : javaRest.cookie
													.get('userId')
										},
										messageObj,
										function(resp, headers) {

											// console.log(resp);
											$scope.successTextAlert = "Mail has been sent for approval";
											$scope.showSuccessAlert = true;

											// switch flag
											$scope.switchBool = function(value) {
												$scope[value] = !$scope[value];
											};

											$timeout(
													function() {
														$route.reload();
														$location
																.path("/busStop.html");

														$scope.showSuccessAlert = false;
														$scope.successTextAlert = undefined;
													}, 2000);
										},
										function(err) {
											// error callback
											// console.log(err);
											$scope.successTextAlert = "Error: ";// +
																				// err.data.validationErrors[0].message;
											$scope.showSuccessAlert = true;

											// switch flag
											$scope.switchBool = function(value) {
												$scope[value] = !$scope[value];
											};
										});

					}

				});

dashboardApp.controller('SchoolHolidaysController', function($scope,
		$rootScope, $resource, $timeout, $http, userService) {

	$scope.schoolHolidays = [];

	$scope.getHolidayDetails = function() {
		$http.get(
				'user/getSchoolHolidaysByUserId/'
						+ javaRest.cookie.get('userId')).then(
				function(response) {
					$scope.schoolHolidayDetails = response.data;
				});
	}
	$scope.getHolidayDetails();

});
dashboardApp.controller('pickUpRequestController', function($scope,
		$rootScope, $resource, $timeout, $http,$controller, userService) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	// $rootScope.fixLeftSideBar();
	$scope.absentFromDate = new Date();
	$scope.absentToDate = new Date();
	var today = new Date();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

	$scope.studentDetailList = $resource(
			'user/getStudentDetails/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'Authorization' : getAuthToken(
								"user/student/", time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();

	$scope.studentDetailList.$promise.then(function(
			studentDetailList) {
    });
	$scope.pickUpRecord = $resource(
			'user/getPickUpRecord/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'Authorization' : getAuthToken(
								"user/getPickUpRecord/", time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();

	$scope.pickUpRecord.$promise.then(function(
	pickUpRecord) {
		$scope.pickUpRecord=pickUpRecord;
    });
	

	$scope.sendPickUpRequestResource = $resource(
			'user/PickUpRequest/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				save : {
					method : 'PUT',
					isArray : false,
					headers : {
						'Authorization' : getPutAuthToken(
								"user/PickUpRequest/", time,
								nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce,
						'Content-Type' : 'application/json'
					},
					transformRequest : function(data,
							headersGetter) {
						var headers = headersGetter();
						var time = javaRest.get_iso_date();
						var nonce = makeRandomString();
						headers['Content-Type'] = "application/json";
						headers['Authorization'] = getPutAuthToken(
								"user/PickUpRequest/", time,
								nonce);
						headers['x-safeplanet-date'] = time;
						headers['nonce'] = nonce;

						var presenceData = '{"studentId":"'
								+ data.studentId
								+ '", "presentFlag":"'
								+ data.presentFlag
								+ '","pickOrDrop":"'
								+ data.pickOrDrop
								+ '", "message":"'
								+ data.message
								+ '", "pickUpTime":"'
								+ data.absentFromDate
								+ '"}';
						return presenceData;
					}
				}
			});
	

	$scope.showPickUpModal = function showPickUpModal(
			studentDetail) {
		var fTime = new Date();
		
		 fTime.setHours(13);
		    fTime.setMinutes(0);
		    fTime.setSeconds(0,0)
		    $scope.pickUpTime=fTime;
        $scope.studentList=$scope.studentDetailList[0];
		$('#pickUpRequestModal').modal('show');
		$scope.successTextAlert = "";
		$scope.disableButton = false;
	};
	$scope.disableButton = false;
	$scope.sendPickUpRequest = function sendPickUpRequest() {

		if ($scope.absentFromDate == undefined
				|| $scope.absentFromDate < today) {
			alert("From Date can not be blank or less then today.");
			return;
		} 
       
		$scope.presenceData = {};
		$scope.presenceData.studentId = $scope.studentId;
		$scope.presenceData.presentFlag = 'U';
		$scope.presenceData.pickOrDrop = $scope.pickOrDrop;
		$scope.presenceData.message = $scope.pickUpReason;
		$scope.presenceData.absentFromDate = javaRest
				.get_custom_iso_date($scope.pickUpTime);
	
		$scope.disableButton = true;
		$scope.sendPickUpRequestResource
				.save(
						{
							userId : javaRest.cookie
									.get('userId')
						},
						$scope.presenceData,
						function(resp, headers) {
							$scope.successTextAlert = "Request Sent";
							$scope.showSuccessAlert = true;
							$('#pickUpRequestModal').modal(
									'hide');
							// switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};

							$timeout(
									function() {
										$http.get('user/getPickUpRecord/' + javaRest.cookie.get('userId')).success(function(pickUpRecord) {
											
											$scope.pickUpRecord = pickUpRecord;
											
										});
										
										$scope.showSuccessAlert = false;
									}, 3000);
						},
						function(err) {
							$scope.successTextAlert = "Failed to send message.";
							$scope.showSuccessAlert = true;

							// switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};
						});
	}
	
});
dashboardApp.controller('manageClassGroupController', function($scope,
		$rootScope, $resource, $timeout, $http,$controller, userService) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	// $rootScope.fixLeftSideBar();
	
	
	var today = new Date();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	$scope.editClassGroup= function editClassGroup(classGroup){
		$scope.className=$scope.classList;
		$scope.groupName=classGroup.groupName;
		$scope.uuid=classGroup.uuid;
		$('#classGroupModal').modal('show');
		$scope.successTextAlert = "";
		$scope.disableButton = false;
	}

	$scope.classGroupRecord= $resource(
			'user/getClassGroup/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'Authorization' : getAuthToken(
								"user/getClassGroup/", time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();

	$scope.classGroupRecord.$promise.then(function(
			classGroupRecord) {
		$scope.classGroupRecord=classGroupRecord;
    });

	$scope.saveClassGroupResource = $resource(
			'user/saveClassGroupRequest/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				save : {
					method : 'PUT',
					isArray : false,
					headers : {
						'Authorization' : getPutAuthToken(
								"user/saveClassGroupRequest/", time,
								nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce,
						'Content-Type' : 'application/json'
					},
					transformRequest : function(data,
							headersGetter) {
						var headers = headersGetter();
						var time = javaRest.get_iso_date();
						var nonce = makeRandomString();
						headers['Content-Type'] = "application/json";
						headers['Authorization'] = getPutAuthToken(
								"user/saveClassGroupRequest/", time,
								nonce);
						headers['x-safeplanet-date'] = time;
						headers['nonce'] = nonce;

						var presenceData = '{"className":"'
								+ data.className
								+ '","groupName":"'
								+ data.groupName
								+ '","uuid":"'
								+ data.uuid
								+ '"}';
						return presenceData;
					}
				}
			});
	$scope.classList=[
		{className: "Pre Primary",class:"Class"},
		{className: "Nursery",class:"Class"},
	    {className: "I",class:"Class"},
	    {className: "II",class:"Class"},
	    {className: "III",class:"Class"},
	    {className: "IV",class:"Class"},
	    {className: "V",class:"Class"},
	    {className: "VI",class:"Class"},
	    {className: "VII",class:"Class"},
	    {className: "VIII",class:"Class"},
	    {className: "IX",class:"Class"},
	    {className: "X",class:"Class"},
	    {className: "XI",class:"Class"},
	    {className: "XII",class:"Class"},
	    
	    
	];

	$scope.showGroupClassModal = function showGroupClassModal() {
		
		$('#classGroupModal').modal('show');
		$scope.successTextAlert = "";
		$scope.disableButton = false;
	};
	$scope.disableButton = false;
	$scope.saveClassGroup = function saveClassGroup() {
         $scope.presenceData = {};
        console.log(JSON.stringify($scope.className));
        var obj="";
        for(var index in $scope.className){
        	obj+=$scope.className[index].className+",";
        }
        $scope.presenceData.className = obj;;
		$scope.presenceData.groupName = $scope.groupName;
		if($scope.uuid==undefined)
			{
			$scope.presenceData.uuid='newState';
			}else{
		$scope.presenceData.uuid = $scope.uuid;
			}
		$scope.disableButton = true;
		$scope.saveClassGroupResource
				.save(
						{
							userId : javaRest.cookie
									.get('userId')
						},
						$scope.presenceData,
						function(resp, headers) {
							$scope.successTextAlert = "Request Saved";
							$scope.showSuccessAlert = true;
							$('#classGroupModal').modal(
									'hide');
							// switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};

							$timeout(
									function() {
										$http.get('user/getClassGroup/' + javaRest.cookie.get('userId')).success(function(classGroupRecord) {
											
											$scope.classGroupRecord = classGroupRecord;
											
										});
										
										$scope.showSuccessAlert = false;
									}, 3000);
						},
						function(err) {
							$scope.successTextAlert = "Failed to send message.";
							$scope.showSuccessAlert = true;

							// switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};
						});
	}
	
});

dashboardApp.controller('parentsVisitorController', function($scope,
		$rootScope, $resource, $timeout, $http,$controller, userService,studentService) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
    var today = new Date();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	// $rootScope.fixLeftSideBar();
	studentService.getStudentClass().$promise.then(function(
			studentClass) {
		$scope.stuClass = studentClass;
	});
	
	$scope.getStudentByClass = function () {
		studentService.getStudentByClass ($scope.className).$promise.then(function(student) {
			$scope.studentList = student;
			
		});
	}
	function callOTPmodal(){
		$scope.userNumber=mbNumber;
		$scope.uuid=uuid;
		$('#otpModal').modal('show');
		let timerOn = true;

		function timer(remaining) {
		  var m = Math.floor(remaining / 60);
		  var s = remaining % 60;
		  
		  m = m < 10 ? '0' + m : m;
		  s = s < 10 ? '0' + s : s;
		  document.getElementById('timer').innerHTML = m + ':' + s;
		  remaining -= 1;
		  
		  if(remaining >= 0 && timerOn) {
		    setTimeout(function() {
		        timer(remaining);
		    }, 1000);
		    $scope.disableResendButton=true;
		    return;
		  }

		  if(!timerOn) {
			
		    return;
		  }
		  
		  $scope.disableResendButton=false;
		  document.getElementById('disableResendButton').disabled=false;
		  document.getElementById('disableOTPButton').disabled=true;
		  console.log($scope.disableResendButton);
		  document.getElementById('timer').innerHTML ="Expired"
			   
		}
         timer(30);
		$scope.successTextAlert = "";
		$scope.disableButton = false;
		
	}
	$scope.getUserByStudent = function (studentName) {
		var obj =$scope.studentList;
		console.log(studentName);
		
		for (var i = 0; i < obj.length; i++) {
			console.log(obj[i]);
				if(obj[i].firstName==studentName){
					$scope.userName=obj[i].parentName;
				}
			
		}
	}
	$scope.resendOTPRequestRes = $resource(
			'user/resendOTPRequest/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				save : {
					method : 'PUT',
					isArray : false,
					headers : {
						'Authorization' : getPutAuthToken(
								"user/resendOTPRequest/", time,
								nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce,
						'Content-Type' : 'application/json'
					},
					transformRequest : function(data,
							headersGetter) {
						var headers = headersGetter();
						var time = javaRest.get_iso_date();
						var nonce = makeRandomString();
						headers['Content-Type'] = "application/json";
						headers['Authorization'] = getPutAuthToken(
								"user/resendOTPRequest/", time,
								nonce);
						headers['x-safeplanet-date'] = time;
						headers['nonce'] = nonce;

						var visitorData = '{"uuid":"'
								+ data.uuid
								+ '","userNumber":"'
								+ data.userNumber
								+ '"}';
						return visitorData;
					}
				}
			});
	

	$scope.disableButton = false;
	$scope.resendOTPRequest = function resendOTPRequest() {
        $scope.visitorData = {};
        $scope.visitorData.uuid = $scope.uuid;
        $scope.visitorData.userNumber = $scope.userNumber;
		$scope.disableButton = true;
		$scope.resendOTPRequestRes
				.save(
						{
							userId : javaRest.cookie
									.get('userId')
						},
						$scope.visitorData,
						function(resp, headers) {
							$scope.successTextAlert =resp.message;
							$scope.showSuccessAlert = true;
							$('#otpModal').modal(
									'show');
					         // switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};

							$timeout(
									function() {
										callOTPmodal();
										$scope.showSuccessAlert = false;
									}, 3000);
						},
						function(err) {
							$scope.successTextAlert = "Failed to Verified.";
							$scope.showSuccessAlert = true;

							// switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};
						});
	}

	$scope.sendOTPRequestRes = $resource(
			'user/sendOTPRequest/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				save : {
					method : 'PUT',
					isArray : false,
					headers : {
						'Authorization' : getPutAuthToken(
								"user/sendOTPRequest/", time,
								nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce,
						'Content-Type' : 'application/json'
					},
					transformRequest : function(data,
							headersGetter) {
						var headers = headersGetter();
						var time = javaRest.get_iso_date();
						var nonce = makeRandomString();
						headers['Content-Type'] = "application/json";
						headers['Authorization'] = getPutAuthToken(
								"user/sendOTPRequest/", time,
								nonce);
						headers['x-safeplanet-date'] = time;
						headers['nonce'] = nonce;

						var visitorData = '{"otpNumber":"'
								+ data.otpNumber
								+ '","userNumber":"'
								+ data.userNumber
								+ '"}';
						return visitorData;
					}
				}
			});
	

	$scope.disableButton = false;
	$scope.sendOTPRequest = function sendOTPRequest() {
        $scope.visitorData = {};
        $scope.visitorData.otpNumber = $scope.otpNumber;
        $scope.visitorData.userNumber = $scope.userNumber;
		$scope.disableButton = true;
		$scope.sendOTPRequestRes
				.save(
						{
							userId : javaRest.cookie
									.get('userId')
						},
						$scope.visitorData,
						function(resp, headers) {
							$scope.successTextAlert = resp.message;
							$scope.showSuccessAlert = true;
							if(resp.message=="OTP no is inccorect"){
							$scope.disableButton = false;
							$('#otpModal').modal(
									'show');
							}else{
								$('#otpModal').modal(
								'hide');
							}
					         // switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};

							$timeout(
									function() {
										
										$scope.showSuccessAlert = false;
									}, 3000);
						},
						function(err) {
							$scope.successTextAlert = "Failed to Verified.";
							$scope.showSuccessAlert = true;

							// switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};
						});
	}

	$scope.showparentsVisitorModal = function showparentsVisitorModal() {
		
		$('#parentsVisitorModal').modal('show');
		$scope.successTextAlert = "";
		$scope.disableButton = false;
	};$scope.sendParentsVisitorRequestResource = $resource(
			'user/sendParentsVisitorRequest/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				save : {
					method : 'PUT',
					isArray : false,
					headers : {
						'Authorization' : getPutAuthToken(
								"user/sendParentsVisitorRequest/", time,
								nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce,
						'Content-Type' : 'application/json'
					},
					transformRequest : function(data,
							headersGetter) {
						var headers = headersGetter();
						var time = javaRest.get_iso_date();
						var nonce = makeRandomString();
						headers['Content-Type'] = "application/json";
						headers['Authorization'] = getPutAuthToken(
								"user/sendParentsVisitorRequest/", time,
								nonce);
						headers['x-safeplanet-date'] = time;
						headers['nonce'] = nonce;

						var visitorData = '{"userName":"'
								+ data.userName
								+ '", "studentName":"'
								+ data.studentName
								+ '", "message":"'
								+ data.message
								+ '", "userNumber":"'
								+ data.userNumber
								+ '"}';
						return visitorData;
					}
				}
			});
	
    var mbNumber;
    var uuid;
	$scope.disableButton = false;
	$scope.sendParentsVisitorRequest = function sendParentsVisitorRequest() {
        $scope.visitorData = {};
        $scope.visitorData.userName = $scope.userName;
		$scope.visitorData.studentName = $scope.studentName;
		$scope.visitorData.userNumber=$scope.visitorNumber;
		$scope.visitorData.message = $scope.purpose;
		console.log($scope.purpose);
		$scope.disableButton = true;
		$scope.sendParentsVisitorRequestResource
				.save(
						{
							userId : javaRest.cookie
									.get('userId')
						},
						$scope.visitorData,
						function(resp, headers) {
							mbNumber=resp.message;
							uuid=resp.uuid;
							$scope.successTextAlert = "OTP Request Sent to your mobile number";
							$scope.showSuccessAlert = true;
							$('#parentsVisitorModal').modal(
									'hide');
					         // switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};

							$timeout(
									function() {
										callOTPmodal();
										$scope.showSuccessAlert = false;
									}, 3000);
						},
						function(err) {
							$scope.successTextAlert = "Failed to send message.";
							$scope.showSuccessAlert = true;

							// switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};
						});
	}

});
dashboardApp.controller('normalVisitorController', function($scope,
		$rootScope, $resource, $timeout, $http,$controller, userService) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	// $rootScope.fixLeftSideBar();
	
	
	var today = new Date();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

});

dashboardApp.controller('makeRequestReportsController', function($scope, $rootScope) {

	// Show reports icons
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}

	$scope.reports = {};
	$scope.reports.reportList = [];

	$scope.reports.reportList.push({
		"link" : "#markLeave.html",
		"linkName" : "Mark Leave",
		"icon" : "img/avatar5.png",
	});
	$scope.reports.reportList.push({
		"link" : "#pickUpRequest.html",
		"linkName" : "PickUp Request",
		"icon" : "icons/pickup.png",
	});
});

dashboardApp.controller('AssignmentController', function($scope, $rootScope,
		$resource, $timeout,$interval,$http,$location ) {

	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

	$scope.assignment=true;
	$scope.assignmentList=false;
	
	angular.extend($scope, {
		india : {
			lat : 28.53,
			lng : 77.38,
			zoom : 15
		},
		layers : {
			// baselayers : {
			// googleRoadmap : {
			// name : 'Google Streets',
			// layerType : 'ROADMAP',
			// type : 'google'
			// }
			// }
			baselayers : {
				osm : {
					name : 'OpenStreetMap',
					url : 'https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}',
					type : 'xyz'
				}
			}
		},
		studentPaths : {}
	});
	

	// vehicle List
	$scope.allVehicleList = $resource(
			'reports/getAllVehicleList/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'Authorization' : getAuthToken(
								"reports/getAllVehicleList/", time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();

	$scope.allVehicleList.$promise.then(function(allVehicleList) {

	});
	
	// Route List
	$scope.allRouteList = $resource(
			'reports/getAllRouteList/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'Authorization' : getAuthToken(
								"reports/getAllVehicleList/", time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();

	$scope.allRouteList.$promise.then(function(allRouteList) {

	});
	
	
	$scope.goToNext = function() {
		$scope.assignment=false;
		$scope.assignmentList=true;
		
	
		
		$scope.getStudentRouteList = $resource(
				'students/getStudentRouteList/:userId/:routeId',
				{
					userId : javaRest.cookie.get('userId'),
					routeId : $scope.routeId
				},
				{
					get : {
						method : 'GET',
						isArray : true,
						headers : {
							'Authorization' : getAuthToken(
									"students/getStudentRouteList/", time, nonce),
							'x-safeplanet-date' : time,
							'nonce' : nonce
						}
					}
				}).get();
		$scope.getStudentRouteList.$promise.then(function(getStudentRouteList) {

		});
		
	}
	
	
	
	
	$scope.selectedRow = function(ai) {
		
	// console.log(ai);
		alert(ai.firstName);
	}
	
	$scope.lattitude;
	$scope.longitude;
	var updateCurrentLocations = function() {
		
		if($scope.vehicleId!=undefined){
		
		var reqLocation1 = {
				url : 'route/getBusCurrentLocation/'
						+ javaRest.cookie.get('userId')+'/'+$scope.vehicleId,
				method : 'GET',
				headers : {
					'Authorization' : getAuthToken(
							"route/getBusCurrentLocation/",
							time, nonce),
					'x-safeplanet-date' : time,
					'nonce' : nonce
				},
				data : {}
			};

			$http(reqLocation1)
					.then(
							function(response) {
								$scope.currentMarkerLocations = response.data;
								var curCount = $scope.currentMarkerLocations.length;
								// var keysObjArr =
								// Object.keys($scope.currentMarkerLocations);
								$scope.markers=[];
								for (var i = 0; i < curCount; i++) {
									/*
									 * $scope.markers[keysObjArr[i]].lat =
									 * $scope.currentMarkerLocations[keysObjArr[i]].lat;
									 * $scope.markers[keysObjArr[i]].lng =
									 * $scope.currentMarkerLocations[keysObjArr[i]].lng;
									 * $scope.markers[keysObjArr[i]].message =
									 * $scope.currentMarkerLocations[keysObjArr[i]].message;
									 */
									$scope.lattitude=response.data[i].lattitude;
									$scope.longitude=response.data[i].longitude;
									
									angular.extend($scope, {
										india : {
											lat : response.data[i].lattitude,
											lng : response.data[i].longitude,
											zoom : 15
										}
									});
									$scope.markers
											.push({
												lat : response.data[i].lattitude,
												lng : response.data[i].longitude,
												// message : "Bus Stop",
												icon : "icons/map-marker.svg"
											});
								}
								// alert('first'+$scope.markers);

							}, function() {
								// alert("Error::");
							});
		}
	}
	
	$interval(updateCurrentLocations, 5000);
	$scope.getStudentRouteList1=[];
	$scope.saveAssignment = function() {
		// alert($scope.getStudentRouteList1);
		// alert('Name Of the Stop---'+$scope.nameofthestop);
		// alert('Pick Time---'+$scope.picktime);
		// alert('Drop Time---'+$scope.droptime);
		// alert('len--'+$scope.getStudentRouteList1.length);
		$scope.studentIdVal=[];
		for (var i=0;i<$scope.getStudentRouteList1.length;i++)
		{
			  
			/*
			 * for(var key in $scope.getStudentRouteList1) {
			 * if($scope.getStudentRouteList1.hasOwnProperty(key)) { alert("Key: " +
			 * key + ", Value: " + $scope.getStudentRouteList1[key]); } }
			 */
			
			$scope.getStudentRouteList1.forEach(function(value, index, array) {
				
				
		    	// alert( v);
		    	 $scope.studentIdVal=value.studentId+'-'+$scope.studentIdVal;
				
			});
			
			/*
			 * $.each($.parseJSON($scope.getStudentRouteList1[i]), function(k,
			 * v) { //alert(k + ' is ' + v); if(k=='studentId'){
			 * saveAssignment(v) // alert( v);
			 * $scope.studentIdVal=v+'-'+$scope.studentIdVal; }
			 * 
			 * });
			 */	   
		} 
		saveAssignment($scope.getStudentRouteList1.map(function(x){return x.studentId;}));
		// alert($scope.studentIdVal);
	     // $scope.broadcastMessageResource = $resource(
		// $scope.addStudentResource = $resource(
	}
	
	
	function saveAssignment(v){
		
		$scope.student = {};
		$scope.student.studentId = v;
		$scope.student.nameofthestop = $scope.nameofthestop;
		$scope.student.picktime = $scope.picktime;
		$scope.student.droptime =$scope.droptime;
		
		var studentData = {routeId:$scope.routeId, "studentId": v, "routeName": $scope.nameofthestop,"dropTime": $scope.droptime.toString(),"pickTime": $scope.picktime.toString(), "longitude": $scope.longitude, "lattitude": $scope.lattitude};
		
		$http.put('user/addUpdateAssigment/'+javaRest.cookie.get('userId'),studentData,{json:true}).then(function(){
			// console.log(resp);
			$scope.successTextAlert = "Updated.";
			$scope.showSuccessAlert = true;

			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};

			$timeout(
					function() {
						$scope.showSuccessAlert = false;
						// $location.path("/student.html");
					}, 10000);
		},function(){
			$scope.successTextAlert = "Error: "
				+ err.data.validationErrors[0].message;
		$scope.showSuccessAlert = true;

		// switch flag
		$scope.switchBool = function(value) {
			$scope[value] = !$scope[value];
		};
		});
	}
	
	$scope.selectedStudent=function(std){
		
		// alert(std);
		 $scope.getStudentRouteList1.push(std);	
		 
		 var index=$scope.getStudentRouteList.indexOf(std)
	      $scope.getStudentRouteList.splice(index,1);  
		
	}
	
$scope.selectedStudent1=function(std){
		
		// alert(std);
		 $scope.getStudentRouteList.push(std);	
		 
		 var index=$scope.getStudentRouteList1.indexOf(std)
	      $scope.getStudentRouteList1.splice(index,1);  
		
	}
	$scope.closeNotification = function() {
		$scope.successTextAlert = undefined;
		$scope.errorTextAlert = undefined;
	}
	 $( function() {
		    $( "#sortable1, #sortable2" ).sortable({
		      connectWith: ".connectedSortable"
		    }).disableSelection();
		  } );
		  
		  $("#sortable2").droppable({
			  drop: function(event, ui) {
			  // alert( "dropped" );
			    // alert(ui.draggable.attr('id'));
			    // var obj=ui.draggable.attr('id');
			    $scope.getStudentRouteList1.push(ui.draggable.attr('id'));			    
			   // alert($scope.getStudentRouteList1);
			 }
			});
});
dashboardApp.controller('manageRouteScheduleController', function($scope,
		$rootScope, $resource, $timeout, $http,$controller, userService,routeService ,$filter) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left')
			|| $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}
	// $rootScope.fixLeftSideBar();
	$scope.absentFromDate = new Date();
	$scope.absentToDate = new Date();
	var today = new Date();
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();

	var fTime = new Date();
	
	 fTime.setHours(6);
	 fTime.setMinutes(0);
	 fTime.setSeconds(0,0)
	 $scope.sTime=fTime;
	 
	 var eTime = new Date();
	 eTime.setHours(14);
	 eTime.setMinutes(0);
	 eTime.setSeconds(0,0)
	 $scope.eTime=eTime;
	 
	 

	$scope.getRouteScheduleDetails = $resource(
			'route/getRouteScheduleDetails/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				get : {
					method : 'GET',
					isArray : true,
					headers : {
						'Authorization' : getAuthToken(
								"route/getRouteScheduleDetails/", time, nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce
					}
				}
			}).get();
 
	$scope.getRouteScheduleDetails.$promise.then(function(
			getRouteScheduleDetails) {
		$scope.getRouteScheduleDetails=getRouteScheduleDetails;
    });
	
	
	userService.getSchoolList().$promise
	.then(function(schoolList) {
		$scope.schoolList = schoolList;
	});
	$scope.getRouteBySchool = function () {
		routeService.getRouteBySchool($scope.schoolId).$promise.then(function(routeList) {
			$scope.routeList = routeList;
		});
	}
	$scope.addRouteScheduleResource = $resource(
			'route/addRouteSchedule/:userId',
			{
				userId : javaRest.cookie.get('userId')
			},
			{
				save : {
					method : 'PUT',
					isArray : false,
					headers : {
						'Authorization' : getPutAuthToken(
								"route/addRouteSchedule/", time,
								nonce),
						'x-safeplanet-date' : time,
						'nonce' : nonce,
						'Content-Type' : 'application/json'
					},
					transformRequest : function(data,
							headersGetter) {
						var headers = headersGetter();
						var time = javaRest.get_iso_date();
						var nonce = makeRandomString();
						headers['Content-Type'] = "application/json";
						headers['Authorization'] = getPutAuthToken(
								"route/addRouteSchedule/", time,
								nonce);
						headers['x-safeplanet-date'] = time;
						headers['nonce'] = nonce;

						var scheduleData = '{"schoolId":"'
								+ data.schoolId
								+ '", "scheduleFlag":"'
								+ data.scheduleFlag
								+ '","scheduleName":"'
								+ data.scheduleName
								+ '","scheduleId":"'
								+ data.scheduleId
								+ '", "routeId":"'
								+ data.routeId
								+ '", "startTime":"'
								+ data.startTime
								+ '","endTime":"'
								+ data.endTime
								+ '"}';
						return scheduleData;
					}
				}
			});
	
$scope.addRouteSchedule = function addRouteSchedule() {
		

		if ($scope.absentFromDate == undefined
				|| $scope.absentFromDate < today) {
			alert("From Date can not be blank or less then today.");
			return;
		} 
       
		$scope.scheduleData = {};
		$scope.scheduleData.schoolId = $scope.schoolId;
		$scope.scheduleData.scheduleFlag =$scope.scheduleFlag;
		if($scope.scheduleId==null){
			$scope.scheduleData.scheduleId=-1;
		}else{
			$scope.scheduleData.scheduleId=$scope.scheduleId;
		}
		
		$scope.scheduleData.routeId = $scope.routeId;
		$scope.scheduleData.scheduleName = $scope.scheduleName;
		$scope.scheduleData.startTime = javaRest
				.get_custom_iso_date($scope.sTime);
		$scope.scheduleData.endTime = javaRest
		.get_custom_iso_date($scope.eTime);
	
		$scope.disableButton = true;
		$scope.addRouteScheduleResource
				.save(
						{
							userId : javaRest.cookie
									.get('userId')
						},
						$scope.scheduleData,
						function(resp, headers) {
							$scope.successTextAlert = "Saved";
							$scope.showSuccessAlert = true;
							$('#addRouteScheduleModal').modal(
									'hide');
							// switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};

							$timeout(
									function() {
										$http.get('route/getRouteScheduleDetails/' + javaRest.cookie.get('userId')).success(function(getRouteScheduleDetails) {
											
											$scope.getRouteScheduleDetails = getRouteScheduleDetails;
											
										});
										
										$scope.showSuccessAlert = false;
									}, 3000);
						},
						function(err) {
							$scope.successTextAlert = "Failed to send message.";
							$scope.showSuccessAlert = true;

							// switch flag
							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};
						});
	}

$scope.editRouteSchedule = function(route) {
	var timeParts= route.startTime.split(':');
	var hour = timeParts[0];
	var min = timeParts[1];
	
	$scope.sTime = new Date(2018,0,1,hour,min);
	
	 timeParts= route.endTime.split(':');
	 hour = timeParts[0];
	 min = timeParts[1];
	 $scope.eTime = new Date(2018,0,1,hour,min);
	 $scope.schoolId =route.schoolId;
	 $scope.scheduleId=route.scheduleId;
	 $scope.scheduleFlag=route.scheduleFlag;
	 $scope.scheduleName=route.scheduleName;
	 $scope.getRouteBySchool();
	 $scope.routeId=route.routeId;
	console.log($scope.scheduleId)
	$('#addRouteScheduleModal').modal('show');
};
	
});
