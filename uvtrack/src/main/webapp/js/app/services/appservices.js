'use strict';
var dashboardApp = angular.module("dashboardApp");

dashboardApp.service("settingsService", [ "$resource", function($resource) {

    var time = javaRest.get_iso_date();
    var nonce = makeRandomString();

    this.getStudentDetails = function() {
	return $resource('user/getStudentDetails/:userId', {
	    userId : javaRest.cookie.get('userId')
	}, {
	    get : {
		method : 'GET',
		isArray : true,
		headers : {
		    'Authorization' : getAuthToken("user/student/", time, nonce),
		    'x-safeplanet-date' : time,
		    'nonce' : nonce
		}
	    }
	}).get();
    };
    this.getUserNotificationSettings = function() {
	return $resource('user/getAlertSettings/:userId', {
	    userId : javaRest.cookie.get('userId')
	}, {
	    get : {
		method : 'GET',
		isArray : true,
		headers : {
		    'Authorization' : getAuthToken("user/getAlertSettings/", time, nonce),
		    'x-safeplanet-date' : time,
		    'nonce' : nonce
		}

	    }
	}).get();
    };

    this.updateNotificationSettings = function() {
	return $resource('user/updateAlertSettings/:userId', {
	    userId : javaRest.cookie.get('userId')
	}, {
	    save : {
		method : 'PUT',
		isArray : true,
		headers : {
		    'Authorization' : getPutAuthToken("user/updateAlertSettings/", time, nonce),
		    'x-safeplanet-date' : time,
		    'nonce' : nonce,
		    'Content-Type' : 'application/json'
		},
		transformRequest : function(data, headersGetter) {
		    var headers = headersGetter();
		    var time = javaRest.get_iso_date();
		    var nonce = makeRandomString();
		    headers['Content-Type'] = "application/json";
		    headers['Authorization'] = getPutAuthToken("user/updateAlertSettings/", time, nonce);
		    headers['x-safeplanet-date'] = time;
		    headers['nonce'] = nonce;
		    /*
		     * var alertConfigData = '{"studentId":"'+data.studentId+'", "presentFlag":"'+data.presentFlag+ '"}'; return alertConfigData;
		     */
		    return JSON.stringify(data);
		}
	    }
	});
    }

} ]);

dashboardApp.service("homeService", [ "$resource", function($resource) {

    var time = javaRest.get_iso_date();
    var nonce = makeRandomString();

    this.getUserDashboard = function() {
	return $resource('user/getUserDashboard/:userId', {
	    userId : javaRest.cookie.get('userId')
	}, {
	    get : {
		method : 'GET',
		isArray : false,
		headers : {
		    'Authorization' : getAuthToken("user/getUserDashboard/", time, nonce),
		    'x-safeplanet-date' : time,
		    'nonce' : nonce
		}
	    }
	}).get();
    }

} ]);

dashboardApp.service("routeService", [ "$resource", function($resource) {

    var time = javaRest.get_iso_date();
    var nonce = makeRandomString();

    this.findAllRouteList = function() {
	return $resource('route/findAllRouteList/:userId', {
	    userId : javaRest.cookie.get('userId')
	}, {
	    get : {
		method : 'GET',
		isArray : true,
		headers : {
		    'Authorization' : getAuthToken("route/findAllRouteList/", time, nonce),
		    'x-safeplanet-date' : time,
		    'nonce' : nonce
		}
	    }
	}).get();
    }, this.getRouteBySchool = function(schoolId) {
    	return $resource('route/getRouteBySchool/:schoolId', {
    	    schoolId : schoolId
    	}, {
    	    get : {
    		method : 'GET',
    		isArray : true,
    		headers : {
    		    'Authorization' : getAuthToken("route/getRouteBySchool/", time, nonce),
    		    'x-safeplanet-date' : time,
    		    'nonce' : nonce
    		}
    	    }
    	}).get();
        }
    
    
    ,this.getScheduleByRoute = function (routeId) {
        return $resource('route/getScheduleByRoute/:userId/:routeId', {
            userId: javaRest.cookie.get('userId'),
            routeId: routeId
        }, {
            get: {
                method: 'GET',
                isArray: true,
                headers: {
                    'Authorization': getAuthToken("route/getScheduleByRoute/", time, nonce),
                    'x-safeplanet-date': time,
                    'nonce': nonce
                }
            }
        }).get();
    }
    ,this.getStudentByRoute = function (routeId) {
        return $resource('route/getStudentByRoute/:userId/:routeId', {
            userId: javaRest.cookie.get('userId'),
            routeId: routeId
        }, {
            get: {
                method: 'GET',
                isArray: true,
                headers: {
                    'Authorization': getAuthToken("route/getStudentByRoute/", time, nonce),
                    'x-safeplanet-date': time,
                    'nonce': nonce
                }
            }
        }).get();
    },
    this.getScheduleByFleet = function (vehicleId) {
        return $resource('route/getScheduleByFleet/:userId/:fleetId', {
            userId: javaRest.cookie.get('userId'),
            fleetId: vehicleId
        }, {
            get: {
                method: 'GET',
                isArray: true,
                headers: {
                    'Authorization': getAuthToken("route/getScheduleByFleet/", time, nonce),
                    'x-safeplanet-date': time,
                    'nonce': nonce
                }
            }
        }).get();
    }, this.getSpeedByFleet = function (vehicleId) {
        return $resource('route/getSpeedByFleet/:userId/:fleetId', {
            userId: javaRest.cookie.get('userId'),
            fleetId: vehicleId
        }, {
            get: {
                method: 'GET',
                isArray: false,
                headers: {
                    'Authorization': getAuthToken("route/getSpeedByFleet/", time, nonce),
                    'x-safeplanet-date': time,
                    'nonce': nonce
                }
            }
        }).get();
    }, this.getUnassignedDevice = function() {
		return $resource('user/getUnassignedDevice/:userId', {
		    userId : javaRest.cookie.get('userId')
		}, {
		    get : {
			method : 'GET',
			isArray : true,
			headers : {
			    'Authorization' : getAuthToken("user/getUnassignedDevice/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce
			}
		    }
		}).get();
    }
    ,  this.getUnassignedFleet = function() {
		return $resource('route/getUnassignedFleet/:userId', {
		    userId : javaRest.cookie.get('userId')
		}, {
		    get : {
			method : 'GET',
			isArray : true,
			headers : {
			    'Authorization' : getAuthToken("route/getUnassignedFleet/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce
			}
		    }
		}).get();
    } , this.getCityByState = function(stateId) {
		return $resource('states/getCityList/:userId/:stateId', {
		    userId : javaRest.cookie.get('userId'),
		    stateId: stateId
		}, {
		    get : {
			method : 'GET',
			isArray : true,
			headers : {
			    'Authorization' : getAuthToken("states/getCityList/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce
			}
		    }
		}).get();
    },this.findAllWayPointListforRoute = function(routeId) {
    	return $resource('route/findAllWayPointListforRoute/:userId/:routeId', {
    	    userId : javaRest.cookie.get('userId'),
    	    routeId: routeId
    	}, {
    	    get : {
    		method : 'GET',
    		isArray : true,
    		headers : {
    		    'Authorization' : getAuthToken("route/findAllWayPointListforRoute/", time, nonce),
    		    'x-safeplanet-date' : time,
    		    'nonce' : nonce
    		}
    	    }
    	}).get();
        }
    ,this.getScheduleByWayPoint = function (wayPointId) {
        return $resource('route/getScheduleByWayPoint/:userId/:id', {
            userId: javaRest.cookie.get('userId'),
            id: wayPointId
        }, {
            get: {
                method: 'GET',
                isArray: true,
                headers: {
                    'Authorization': getAuthToken("route/getScheduleByWayPoint/", time, nonce),
                    'x-safeplanet-date': time,
                    'nonce': nonce
                }
            }
        }).get();
    }
} ]);

dashboardApp.service("studentService", [
	"$resource",
	function($resource) {

	    var time = javaRest.get_iso_date();
	    var nonce = makeRandomString();

	    this.addStudent = function() {
		return $resource('user/addUpdateStudent/:userId', {
		    userId : javaRest.cookie.get('userId')
		}, {
		    save : {
			method : 'PUT',
			isArray : false,
			headers : {
			    'Authorization' : getPutAuthToken("user/addUpdateStudent/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce,
			    'Content-Type' : 'application/json'
			},
			transformRequest : function(data, headersGetter) {
			    var headers = headersGetter();
			    var time = javaRest.get_iso_date();
			    var nonce = makeRandomString();
			    headers['Content-Type'] = "application/json";
			    headers['Authorization'] = getPutAuthToken("user/addUpdateStudent/", time, nonce);
			    headers['x-safeplanet-date'] = time;
			    headers['nonce'] = nonce;

			    return JSON.stringify(data);
			}
		    }
		});
	    }, 
	   
		    
		    this.absentReasons = function() {
		return $resource('user/absentReasons/:userId', {
		    userId : javaRest.cookie.get('userId')
		}, {
		    get : {
			method : 'GET',
			isArray : true,
			headers : {
			    'Authorization' : getAuthToken("user/absentReasons/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce
			}
		    }
		}).get();
	    }, this.studentDetailList = function() {
		return $resource('user/getStudentDetails/:userId', {
		    userId : javaRest.cookie.get('userId')
		}, {
		    get : {
			method : 'GET',
			isArray : true,
			headers : {
			    'Authorization' : getAuthToken("user/student/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce
			}
		    }
		}).get();
	    }, this.getStates = function() {
		return $resource('states/getStates/:userId', {
		    userId : javaRest.cookie.get('userId')
		}, {
		    get : {
			method : 'GET',
			isArray : true,
			headers : {
			    'Authorization' : getAuthToken("states/getStates/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce
			}
		    }
		}).get();
	    }, this.getStudentByClass = function (className) {
	        return $resource('students/getStudentByClass/:userId/:className', {
	            userId: javaRest.cookie.get('userId'),
	            className: className
	        }, {
	            get: {
	                method: 'GET',
	                isArray: true,
	                headers: {
	                    'Authorization': getAuthToken("students/getStudentByClass/", time, nonce),
	                    'x-safeplanet-date': time,
	                    'nonce': nonce
	                }
	            }
	        }).get();
	    }, this.sendAbsentMessage = function() {
		return $resource('user/absentMessage/:userId', {
		    userId : javaRest.cookie.get('userId')
		}, {
		    save : {
			method : 'PUT',
			isArray : false,
			headers : {
			    'Authorization' : getPutAuthToken("user/absentMessage/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce,
			    'Content-Type' : 'application/json'
			},
			transformRequest : function(data, headersGetter) {
			    var headers = headersGetter();
			    var time = javaRest.get_iso_date();
			    var nonce = makeRandomString();
			    headers['Content-Type'] = "application/json";
			    headers['Authorization'] = getPutAuthToken("user/absentMessage/", time, nonce);
			    headers['x-safeplanet-date'] = time;
			    headers['nonce'] = nonce;

			    var presenceData = '{"studentId":"' + data.studentId + '", "presentFlag":"' + data.presentFlag + '", "absentReasonId":"' + data.absentReasonId + '", "message":"'
				    + data.message + '"}';
			    return presenceData;
			}
		    }
		});
	    }, this.getStudentClass = function() {
		return $resource('students/getStudentClass/:userId', {
		    userId : javaRest.cookie.get('userId')
		}, {
		    get : {
			method : 'GET',
			isArray : true,
			headers : {
			    'Authorization' : getAuthToken("students/getStudentClass/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce
			}
		    }
		}).get();
	    }, this.getStudentSection = function() {
		return $resource('students/getStudentSection/:userId', {
		    userId : javaRest.cookie.get('userId')
		}, {
		    get : {
			method : 'GET',
			isArray : true,
			headers : {
			    'Authorization' : getAuthToken("students/getStudentSection/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce
			}
		    }
		}).get();
	    }, this.getStudentsInSchool = function() {
		return $resource('students/getStudentsInSchool/:userId', {
		    userId : javaRest.cookie.get('userId')
		}, {
		    get : {
			method : 'GET',
			isArray : true,
			headers : {
			    'Authorization' : getAuthToken("students/getStudentsInSchool/", time, nonce),
			    'x-safeplanet-date' : time,
			    'nonce' : nonce
			}
		    }
		}).get();
	    }, this.getUnassignedSchool = function() {
			return $resource('students/getUnassignedSchool/:userId', {
			    userId : javaRest.cookie.get('userId')
			}, {
			    get : {
				method : 'GET',
				isArray : true,
				headers : {
				    'Authorization' : getAuthToken("students/getUnassignedSchool/", time, nonce),
				    'x-safeplanet-date' : time,
				    'nonce' : nonce
				}
			    }
			}).get();
	    },this.getStudentsByWayPoint = function(wayPointId) {
			return $resource('students/getStudentsByWayPoint/:userId/:id', {
			    userId : javaRest.cookie.get('userId'),
			    id : wayPointId
			}, {
			    get : {
				method : 'GET',
				isArray : true,
				headers : {
				    'Authorization' : getAuthToken("students/getStudentsByWayPoint/", time, nonce),
				    'x-safeplanet-date' : time,
				    'nonce' : nonce
				}
			    }
			}).get();
		    }

	} ]);

dashboardApp.service("userService", [ "$resource", function($resource) {
    var time = javaRest.get_iso_date();
    var nonce = makeRandomString();
    this.getUserDetails = function(userUUID) {
	return $resource('route/getUser/:loginId/:userId', {
	    loginId : javaRest.cookie.get('userId'),
	    userId : userUUID
	}, {
	    get : {
		method : 'GET',
		isArray : false,
		headers : {
		    'Authorization' : getAuthToken("route/getUser/", time, nonce),
		    'x-safeplanet-date' : time,
		    'nonce' : nonce
		}
	    }
	}).get();
    },this.getSchoolList = function(userUUID) {
    	return $resource('route/getSchoolList/:loginId/:userId', {
    	    loginId : javaRest.cookie.get('userId'),
    	    userId : userUUID
    	}, {
    	    get : {
    		method : 'GET',
    		isArray : true,
    		headers : {
    		    'Authorization' : getAuthToken("route/getSchoolList/", time, nonce),
    		    'x-safeplanet-date' : time,
    		    'nonce' : nonce
    		}
    	    }
    	}).get();
        }
    ,
    this.setUser = function (user) {
        localStorage.setItem('user', JSON.stringify(user || {}));
    }

    this.getUser = function () {
        return JSON.parse(localStorage["user"] || {});
    }
} ]);