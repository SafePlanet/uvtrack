dashboardApp.controller('changePasswordController', function($scope, $rootScope, $resource, $interval, $timeout, $location, $http) {
	$scope.textChanged = function() {
		$scope.showErrorAlert = undefined;
		$scope.showSuccessAlert = undefined;
	}

	$scope.changePassword = function changePassword() {
		$scope.showErrorAlert = undefined;
		$scope.showSuccessAlert = undefined;
		$scope.successTextAlert = undefined;
		if (angular.isUndefined($scope.oPassword)) {
			$scope.showErrorAlert = "Please enter old password.";
			return;
		}

		if (angular.isUndefined($scope.password) || angular.isUndefined($scope.cPassword)) {
			$scope.showErrorAlert = "Please enter both passwords.";
			return;
		}

		if ($scope.password.length < 8) {
			$scope.showErrorAlert = "Password should be at least 8 letters long.";
			return;
		}

		if ($scope.password != $scope.cPassword) {
			$scope.showErrorAlert = "Passwords do not match.";
			return;
		}

		var postData = {
			oPassword : $scope.oPassword,
			password : $scope.password,
			cPassword : $scope.cPassword
		};

		$http.put('user/changePassword/' + javaRest.cookie.get('userId'), postData, {
			json : true
		}).then(function(response) {
			var data = response.data;
			if (data.isSuccess) {
				$scope.showSuccessAlert = true;
				$scope.successTextAlert = data.message;
			} else {
				$scope.showFailedAlert = true;
				$scope.showErrorAlert = data.message;
			}

			$scope.password = undefined;
			$scope.cPassword = undefined;

			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};

			$timeout(function() {
				$scope.showSuccessAlert = false;
			}, 10000);

		}, function() {
			$scope.successTextAlert = "Error: in saving the password.";
			$scope.showFailedAlert = true;
			$scope.password = undefined;
			$scope.cPassword = undefined;

			// switch flag
			$scope.switchBool = function(value) {
				$scope[value] = !$scope[value];
			};

			$timeout(function() {
				$scope.failedTextAlert = false;
			}, 3000);
		});
	};
});