dashboardApp.controller('teacherViewController', function($scope, $rootScope, $timeout,$http) {
	if (!$(".sidebar-offcanvas").hasClass('collapse-left') || $(".row-offcanvas").hasClass('active')) {
		$("[data-toggle='offcanvas']").click();
		$(".row-offcanvas").removeClass('active');
	}

    $scope.isMorning = true;

    $scope.routes = [];
    $scope.selectedRoute = '';

    $scope.rawStudents = [];
    $scope.students = [];
    $scope.otherStudents = [];

    $http.get('route/findAllRoutesBySchoolId').success(function (routes) {
        $scope.routes = routes;
    });

    $scope.$watch('selectedRoute', function () {
        if (!$scope.selectedRoute) return;
        $scope.students = [];
        $scope.getStudents();
    });

    $scope.$watch('isMorning', function () {
        if($scope.isMorning){
        	$scope.students = angular.copy($scope.rawStudents);
        }else{
        	$scope.students= $scope.rawStudents.filter(function(s){return s.presentFlag=='I'});
        	$scope.otherStudents = $scope.rawStudents.filter(function(s){return s.presentFlag!='I'});
        }
    });
    
    $scope.getStudents = function () {
        $http.get('students/getTeacherStudentListByRouteId/' + $scope.selectedRoute + '/true').success(function (students) {
            $scope.students = students;
            $scope.rawStudents = angular.copy(students);
        });
    }

    $scope.switchBool = function (value) {
        $scope[value] = !$scope[value];
    };

	$scope.markPresence = function markPresence(student, flag) {

		$scope.presenceData = {};
		$scope.presenceData.studentId = student.studentId;
        $scope.presenceData.presentFlag = flag;
        $scope.presenceData.absentFromDate = new Date().toISOString();
        $scope.presenceData.absentToDate = new Date().toISOString();

        $http.put('students/markPresence/' + javaRest.cookie.get('userId'), $scope.presenceData, { json: true })
            .then(function (response) {
                var data = response.data;

                student.presentFlag = flag;
                
                $scope.successTextAlert = "Saved";
                $scope.showSuccessAlert = true;

                $timeout(function () {
                    $scope.showSuccessAlert = false;
                }, 5000);

            }, function () {
                $scope.successTextAlert = "Error";
                $scope.showSuccessAlert = true;
            });
	}
});
