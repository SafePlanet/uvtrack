dashboardApp
		.controller(
				'positionReportController',
				function($scope, $rootScope, $resource, $timeout, $controller,
						leafletData, $location, $http, $interval,$filter) {
                    var map;
					var time = javaRest.get_iso_date();
					var nonce = makeRandomString();

					var myMovingMarker;
					$scope.ticked = false;

					$scope.back = function() {
						$location.path("/reports.html");
					}
					$scope.date=new Date();
					
					var currDate=new Date();
					
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
				    
					angular.extend(this, $controller('BaseReportController', {
						$scope : $scope
					}));
					$scope.positionReportList = {};

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
										positionReportPath : {
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

					$scope.wayPoints = [];
					// $scope.buses = [];
					$scope.markers = [];
					// $scope.selectedBus = {};
					// $scope.collapsed = false;
					$scope.busy = true;
					// $scope.isInitialLoad = true;
					// $scope.lastUpdate = true;
					$scope.city;
					$scope.postcode;
					$rootScope.positionReportList;
					// $scope.data1 = [];
					// var isInitialLoad= true;
					// $scope.selectedBus = [];

					$scope.selectVehicle = function(){
						var v = $scope.allVehicleList.filter(function(x) {
							return x.id == $scope.vehicleId
						})[0];
						var routeUuid = v.route.routeUuId;
						$http.get('route/findAllWayPointsByRouteUUID/'+ routeUuid).success(function(waypoints) {
							
							if(waypoints.length != 0 ){
								
								$scope.mySwitch = false;
							}
							else{
								$scope.mySwitch = true;
							}
							
						});
						
						
						var v = $scope.allVehicleList.filter(function(x) {
							return x.id == $scope.vehicleId
						})[0];
						var vehicleId = v.id;
						
						$http.get('route/getGeofenceByVehicle/'+ vehicleId).success(function(data) {
						
							if(data.length != 0){
								$scope.mySwitch = false;
							}
							else{
								
								$scope.mySwitch = true;
								}
						
					   });
					
					}
					
					
					$scope.onCategoryChange = function() {
                     if(!$scope.ticked)return;
						var v = $scope.allVehicleList.filter(function(x) {
							return x.id == $scope.vehicleId
						})[0];
						var routeUuid = v.route.routeUuId;
						$http
								.get(
										'route/findAllWayPointsByRouteUUID/'
												+ routeUuid)
								.success(
										function(waypoints) {
											$scope.wayPoints = waypoints;
											$scope.result = angular
													.equals(waypoints);
											if ($scope.result != true) {
												$scope.markers = [];
												$scope.busy = false;
												$scope.busName = false;
												// $scope.buses = data;
												var busarray = waypoints;
												$scope.markers = waypoints
														.map(function(x) {
															return {

																"lat" : x.lattitude,
																"lng" : x.longitude,
																 "icon": 'https://maps.gstatic.com/mapfiles/ms2/micons/blue.png',
																"message" : (x.name + '<br/> ')
															}
                                                    });
															var latlng = L.latLng(($scope.markers[0]||{}).lat||0, ($scope.markers[0]||{}).lng||0);

                                                            map.panTo(latlng);
												
													for(var i=0;i<waypoints.length; i++){
													   if(waypoints[i].studentNames != ""){
														   var marker;
															marker = new L.marker(
																	[
																		waypoints[i].lattitude,
																		waypoints[i].longitude ],
																	{
																		icon : L
																				.divIcon({
																					html : '<div class="l"><img src="js/images/marker-icon.png"  style=" margin:0px;position: relative;-webkit-transform: rotate('
																							+ 'deg); -moz-transform:rotate('
																							+ 'deg);"data-id="'+ waypoints[i].wayPointId +'" /></div>'
																				})
																	})
																	.addTo(map);
													   }
													   else
														   {
														   
														   var marker;
															marker = new L.marker(
																	[
																		waypoints[i].lattitude,
																		waypoints[i].longitude ],
																	{
																		icon : L
																				.divIcon({
																					html : '<div class="l"><img src="images/pin.png"  style=" width: 31px;height: 42px;margin:0px;position: relative;-webkit-transform: rotate('
																							+ 'deg); -moz-transform:rotate('
																							+ 'deg);"data-id="'+ waypoints[i].wayPointId +'" /></div>'
																				})
																	})
																	.addTo(map);
														   }
														
													}
													
  												
											}
										});
						
						
						   ////=======================Geofence=================//
						
												   
						
					}

                    $('body').on('click', '.l', function (e) {
                        var id = $(this).find('img').attr("data-id");
                        var wayPoints = $scope.wayPoints;
                        $scope.$apply(function () {
                            $scope.selectedWaypoint = $filter('filter')(wayPoints, { wayPointId: id })[0];
                            //console.log($scope.selectedWaypoint);
                        });
                    });

                    //

                    //map = new L.map('map', {
                    //    center: [28.6139, 77.2090],
                    //    zoom: 13
                    //});
                 
                    map = L.map('map').setView(
                        [28.6139, 77.2090], 14);
                    var mapLink = '';
                    L.tileLayer('http://{s}.googleapis.com/vt?lyrs=m@174225136&src=apiv3&hl=en-US&x={x}&y={y}&z={z}&s=Galile&style=api%7Csmartmaps', {
					       // attribution: 'Niteesh Mishra',
				        maxZoom: 22,
				        subdomains: ['mt0', 'mt1']
				    }).addTo(map);
                    
                   //=======================Geofence Function======================//
                    
                    $scope.geofenceReport = function(){
                    	if ($scope.vehicleId == undefined
								|| $scope.vehicleId == "") {
							bootbox.alert("<b>Please select Vehicle.</b>");
							return;
						}
                    	  if(!$scope.tickedgeofence)return;
							var v = $scope.allVehicleList.filter(function(x) {
								
								return x.id == $scope.vehicleId
							})[0];

							
							var vehicleId = v.id;
							
							$http
							.get('route/getGeofenceByVehicle/'+ vehicleId)
							.success(function(data) {
							var geoFenceData=	data.latLongList.map(function(x){
									return [x.latitude,x.longitude];
								});
								
						
								var FlyJSONP=function(e){var c,l,i,j,m,g,n,o,k;l=function(a,b,c){a.addEventListener?a.addEventListener(b,c,!1):a.attachEvent?a.attachEvent("on"+b,c):a["on"+b]=c};i=function(a,b){c.log("Garbage collecting!");b.parentNode.removeChild(b);e[a]=void 0;try{delete e[a]}catch(p){}};j=function(a,b){var c="",d,f;for(d in a)a.hasOwnProperty(d)&&(d=b?encodeURIComponent(d):d,f=b?encodeURIComponent(a[d]):a[d],c+=d+"="+f+"&");return c.replace(/&$/,"")};m=function(){for(var a="",a=[],b=0,b=0;b<32;b+=1)a[b]="0123456789ABCDEF".substr(Math.floor(Math.random()*
										16),1);a[12]="4";a[16]="0123456789ABCDEF".substr(a[16]&3|8,1);return a="flyjsonp_"+a.join("")};g=function(a,b){c.log(b);typeof a!=="undefined"&&a(b)};n=function(a,b){c.log("GET success");typeof a!=="undefined"&&a(b);c.log(b)};o=function(a,b){c.log("POST success");typeof a!=="undefined"&&a(b);c.log(b)};k=function(a){c.log("Request complete");typeof a!=="undefined"&&a()};c={options:{debug:!1}};c.init=function(a){var b;c.log("Initializing!");for(b in a)a.hasOwnProperty(b)&&(c.options[b]=a[b]);c.log("Initialization options");
										c.log(c.options);return!0};c.log=function(a){e.console&&c.options.debug&&e.console.log(a)};c.get=function(a){var a=a||{},b=a.url,p=a.callbackParameter||"callback",d=a.parameters||{},f=e.document.createElement("script"),h=m(),q="?";if(!b)throw Error("URL must be specified!");d[p]=h;b.indexOf("?")>=0&&(q="&");b+=q+j(d,!0);e[h]=function(b){typeof b==="undefined"?g(a.error,"Invalid JSON data returned"):a.httpMethod==="post"?(b=b.query.results,!b||!b.postresult?g(a.error,"Invalid JSON data returned"):
										(b=b.postresult.json?b.postresult.json:b.postresult,o(a.success,b))):n(a.success,b);i(h,f);k(a.complete)};c.log("Getting JSONP data");f.setAttribute("src",b);e.document.getElementsByTagName("head")[0].appendChild(f);l(f,"error",function(){i(h,f);k(a.complete);g(a.error,"Error while trying to access the URL")})};c.post=function(a){var a=a||{},b=a.url,e=a.parameters||{},d={};if(!b)throw Error("URL must be specified!");b="http://query.yahooapis.com/v1/public/yql?q="+encodeURIComponent('select * from jsonpost where url="'+
										b+'" and postdata="'+j(e,!1)+'"')+"&format=json&env="+encodeURIComponent("store://datatables.org/alltableswithkeys");d.url=b;d.httpMethod="post";if(typeof a.success!=="undefined")d.success=a.success;if(typeof a.error!=="undefined")d.error=a.error;if(typeof a.complete!=="undefined")d.complete=a.complete;c.get(d)};return c}(this);
								
								var app = {
									    history: [], // auto
									    history2: [], // manual

									    geodataID: 'geofence-data', // localStorage
									    watchID: null, // watchPosition

									    pt: [], // center
									    pts: [], // polygon

									    map: null,

									    layer1: null,
									    layer2: null,
									    marker: null,
									    polygon: null,
									    polyline: null,
									    //rectangle: null,

									    lastStatus: '',
									    startTime: 0,
									    endTime: 0,

									    dist: 0.0, // total distance travelled

									    accuracyThreshold: 600, // meters

									    serverAlert: false,
									    serverUrl: 'http://ings.ca/post.php'
									};

									app.init = function () {
									    console.info('app.init:');
									    console.log('Leaflet ' + L.version);

									    FlyJSONP.init({
									        debug: false
									    });

									    var txt = 'DateTime,Latitude,Longitude,Accuracy,Heading,Speed,Distance,Altitude,Geofence' + "\n";

									    localStorage.setItem(app.geodataID, txt);

									    if(app.pts[0])
									    app.pt = app.pts[0];
									  
									    app.pts = geoFenceData;
									    app.map = map;
									    var latlng = L.latLng((app.pts[0]||{}), (app.pts[0]||{}));
                                        map.panTo(latlng);

									    L.tileLayer('http://{s}.googleapis.com/vt?lyrs=m@174225136&src=apiv3&hl=en-US&x={x}&y={y}&z={z}&s=Galile&style=api%7Csmartmaps', {
									       // attribution: 'Niteesh Mishra',
									        maxZoom: 22,
									        subdomains: ['mt0', 'mt1']
									    }).addTo(app.map);

									    L.control.scale().addTo(app.map);

									    app.updateZoom();

									    app.map.on('zoomend', app.updateZoom);

									    app.map.on('move', function () {
									        //crosshair.setLatLng(app.map.getCenter());
									    });

									    app.polygon = L.polygon(app.pts, {
									        color: '#FF0000',
									        opacity: 0.6,
									        fillOpacity: 0.2
									    });


									    //app.layer1 = L.layerGroup([app.polygon, app.rectangle]);
									    app.layer1 = L.layerGroup([app.polygon]);

									    app.layer1.addTo(app.map);

									    app.history2.push({lat: app.pt[0], lng: app.pt[1]});

									    // Add listeners


									    var touch = ('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch;
									    var evt = (touch) ? 'touchstart' : 'click';
									    console.log('touch =', touch, 'evt =', evt);

									};

									app.updateFence = function (ev) {
									    console.info('app.updateFence:');

									    if (ev.target.tagName != 'A') {
									        return;
									    }

									    var pt, pts;
									    if (ev.target.dataset['center'] && ev.target.dataset['points']) {
									        pt = JSON.parse(ev.target.dataset['center']);
									        pts = JSON.parse(ev.target.dataset['points']);
									    }
									    else {
									        pt = JSON.parse(document.getElementById('custom-center').value);
									        pts = JSON.parse(document.getElementById('custom-points').value);
									    }

									    app.polygon.setLatLngs(pts);
									    app.map.panTo(pt);

									    app.map.fitBounds(app.polygon.getBounds());
									    app.lastStatus = '';

									    app.history2 = [];
									    app.history2.push({lat: pt[0], lng: pt[1]});
									};

									app.clearHistory = function () {
									    console.info('app.clearHistory:');

									    app.history = [];
									    app.history2 = [];
									    app.lastStatus = '';

									    if (app.layer2) {
									        app.layer2.clearLayers();
									    }
									};

									
									app.checkGeoFence = function (lat, lng, timestamp) {
									    console.info('app.checkGeoFence:');

									    var res;

									    // use "contains" method -- not accurate -- returns bounding box of polygon
									    //res = app.polygon.getBounds().contains(L.latLng(lat, lng));

									    // use "leafletPip" library -- accurate
									    var gjLayer = L.geoJson(app.polygon.toGeoJSON());
									    res = leafletPip.pointInLayer([lng, lat], gjLayer);

									    return status;
									};

									app.handleMove = function (coords) {
									    console.info('app.handleMove:');

									    var timestamp = new Date();
									    var timestr = app.leftPad(timestamp.getHours(), 2) + ':' + app.leftPad(timestamp.getMinutes(), 2) + ':' + app.leftPad(timestamp.getSeconds(), 2);
									    var ts = timestamp.getFullYear() + '/' + app.leftPad(timestamp.getMonth() + 1, 2) + '/' + app.leftPad(timestamp.getDate(), 2) + ' ' + app.leftPad(timestamp.getHours(), 2) + ':' + app.leftPad(timestamp.getMinutes(), 2) + ':' + app.leftPad(timestamp.getSeconds(), 2);

									    var status = app.checkGeoFence(coords.lat, coords.lng, timestamp);
									    var statusColor = (status == 'inside') ? 'green' : 'red';

									    app.lastStatus = status;

									    if (app.history2.length > 0) {
									        var d = app.calculateDistance(app.history2[app.history2.length - 1].lat, app.history2[app.history2.length - 1].lng, coords.lat, coords.lng);
									        app.dist += parseFloat(d);
									    }

									    if (app.history2.length === 1) {
									        var lastItem = app.history2[app.history2.length - 1];
									        var latlngs = [L.latLng(lastItem.lat, lastItem.lng), L.latLng(coords.lat, coords.lng)];

									        app.polyline = L.polyline(latlngs, {
									            color: '#0000FF',
									            opacity: 0.8
									        });

									        app.layer2 = L.layerGroup([app.polyline]);
									        app.layer2.addTo(app.map);
									    }
									    else if (app.history2.length > 1) {
									        app.polyline.addLatLng(L.latLng(coords.lat, coords.lng), {
									            color: '#0000FF',
									            opacity: 0.8
									        });

									        app.layer2 = L.layerGroup([app.polyline]);
									        app.layer2.addTo(app.map);
									    }

									    app.history2.push({
									        lat: coords.lat,
									        lng: coords.lng
									    });

									};

									app.handleWatch = function (position) {
									    console.info('app.handleWatch:');

									    var txt = '';
									    var log = '';
									    var timestamp = new Date();
									    var timestr = app.leftPad(timestamp.getHours(), 2) + ':' + app.leftPad(timestamp.getMinutes(), 2) + ':' + app.leftPad(timestamp.getSeconds(), 2);

									    var coords = position.coords;

									    if (coords.accuracy > app.accuracyThreshold) {
									        console.warn('Poor accuracy!', coords.accuracy);
									        log += '<span data-lat="' + coords.latitude + '" data-lng="' + coords.longitude + '" style="color: red; text-decoration: line-through;">' + timestr + ': ' + coords.latitude.toFixed(6) + ', ' + coords.longitude.toFixed(6) + ' (' + coords.accuracy + ')' + '</span><br>';
									        return;
									    }

									    var status = app.checkGeoFence(coords.latitude, coords.longitude, timestamp);

									    app.lastStatus = status;

									    if (app.history.length === 1) {
									        var lastItem = app.history[app.history.length - 1].coords;
									        var latlngs = [L.latLng(lastItem.latitude, lastItem.longitude), L.latLng(coords.latitude, coords.longitude)];

									        app.polyline = L.polyline(latlngs, {
									            color: '#0000FF',
									            opacity: 0.8
									        });

									        app.layer2 = L.layerGroup([app.polyline]);
									        app.layer2.addTo(app.map);
									    }
									    else if (app.history.length > 1) {
									        app.polyline.addLatLng(L.latLng(coords.latitude, coords.longitude), {
									            color: '#0000FF',
									            opacity: 0.8
									        });

									        app.layer2 = L.layerGroup([app.polyline]);
									        app.layer2.addTo(app.map);
									    }

									    app.marker.setLatLng([coords.latitude, coords.longitude]);

									    app.map.panTo([coords.latitude, coords.longitude]);

									    if (app.history.length > 0) {
									        var d = app.calculateDistance(app.history[app.history.length - 1].coords.latitude, app.history[app.history.length - 1].coords.longitude, coords.latitude, coords.longitude);
									        app.dist += parseFloat(d);
									    }

									    var ts = timestamp.getFullYear() + '/' + app.leftPad(timestamp.getMonth() + 1, 2) + '/' + app.leftPad(timestamp.getDate(), 2) + ' ' + app.leftPad(timestamp.getHours(), 2) + ':' + app.leftPad(timestamp.getMinutes(), 2) + ':' + app.leftPad(timestamp.getSeconds(), 2);

									    var lat = coords.latitude.toFixed(6);
									    var lng = coords.longitude.toFixed(6);
									    var accuracy = (coords.accuracy) ? coords.accuracy : ''; // accuracy is in meters
									    var heading = (coords.heading) ? coords.heading.toFixed(0) : '';
									    var speed = (coords.speed) ? (coords.speed * 3.6).toFixed(0) : 0; // speed is m/s, multiply by 3.6 for km/h or 2.23693629 for mph
									    var speed_km = (coords.speed) ? speed + ' km/h' : '';
									    var distance = (app.dist) ? app.dist.toFixed(2) : 0;
									    var distance_km = (app.dist) ? distance + ' km' : '';
									    var altitude = (coords.altitude) ? coords.altitude.toFixed(0) : '';

									    var elapsedTime = app.msToTime(timestamp - app.startTime);

									    document.getElementById('stat_timestamp').innerHTML = ts;
									    document.getElementById('stat_latitude').innerHTML = lat;
									    document.getElementById('stat_longitude').innerHTML = lng;
									    document.getElementById('stat_speed').innerHTML = speed_km;
									    document.getElementById('stat_distance').innerHTML = distance;
									    document.getElementById('stat_altitude').innerHTML = altitude;
									    document.getElementById('stat_heading').innerHTML = heading;
									    document.getElementById('stat_accuracy').innerHTML = accuracy;
									    document.getElementById('stat_elapsed').innerHTML = elapsedTime;

									    // Adjust zoom level based on speed
									    if (speed === 0) {
									        // do nothing
									    }
									    else if (speed > 0 && speed < 15) {
									        app.map.setZoom(18); // ~30m
									    }
									    else if (speed >= 15 && speed < 30) {
									        app.map.setZoom(17); // ~50m
									    }
									    else if (speed >= 30 && speed < 60) {
									        app.map.setZoom(16); // ~100m
									    }
									    else if (speed >= 60 && speed < 90) {
									        app.map.setZoom(15); // ~300m
									    }
									    else if (speed >= 90 && speed < 120) {
									        app.map.setZoom(14); // ~500m
									    }
									    else if (speed >= 120 && speed < 150) {
									        app.map.setZoom(13); // ~1km
									    }
									    else if (speed >= 150 && speed < 180) {
									        app.map.setZoom(12); // ~2km
									    }
									    else if (speed >= 180 && speed < 260) {
									        app.map.setZoom(11); // ~5km
									    }
									    else {
									        app.map.setZoom(10); // ~10km
									    }

									    log += '<span data-lat="' + lat + '" data-lng="' + lng + '">' + timestr + ': ' + lat + ', ' + lng + ' (' + accuracy + ')' + '</span><br>';

									    document.getElementById('log').innerHTML += log;

									    //txt = timestamp.getTime() + ',' + coords.latitude + ',' + coords.longitude + ',' + coords.accuracy + ',' + coords.heading + ',' + coords.speed + ',' + app.dist + ',' + coords.altitude  + ',' + status + "\n";
									    txt = timestamp.getTime() + ',' + lat + ',' + lng + ',' + accuracy + ',' + heading + ',' + speed + ',' + distance + ',' + altitude  + ',' + status + "\n";
									    app.appendToStorage(app.geodataID, txt);

									    app.marker.setPopupContent('<b>' + app.marker.getLatLng().lat.toFixed(6) + ', ' + app.marker.getLatLng().lng.toFixed(6) + '</b><br>');

									    app.history.push(position);
									};

									app.toggleWatch = function (ev) {
									    console.info('app.toggleWatch:');

									    app.history = [];
									    app.history2 = [];
									    app.dist = 0.0;

									    var res;

									    if (ev.target.innerText == 'Start') {
									        var timestamp = new Date();
									        var timestr = timestamp.getFullYear() + '' + app.leftPad(timestamp.getMonth() + 1, 2) + '' + app.leftPad(timestamp.getDate(), 2) + '-' + app.leftPad(timestamp.getHours(), 2) + '' + app.leftPad(timestamp.getMinutes(), 2) + '' + app.leftPad(timestamp.getSeconds(), 2);

									        app.startTime = timestamp;

									        if (window.blackberry && community && community.preventsleep) {
									            res = community.preventsleep.setPreventSleep(true);
									            console.log(res);
									            document.getElementById('stat_screen').innerHTML = 'on';
									        }
									        else {
									            document.getElementById('stat_screen').innerHTML = '';
									        }

									        app.watchID = navigator.geolocation.watchPosition(app.handleWatch, function (err) {
									            console.warn('watchPosition error:', err);
									        }, {
									            enableHighAccuracy: true,
									            maximumAge: 1500,
									            timeout: 3000
									        });

									        ev.target.innerText = 'Stop';
									    }
									    else {
									        if (window.blackberry && community && community.preventsleep) {
									            res = community.preventsleep.setPreventSleep(false);
									            console.log(res);
									            document.getElementById('stat_screen').innerHTML = 'timeout';
									        }

									        navigator.geolocation.clearWatch(app.watchID);

									        ev.target.innerText = 'Start';
									    }
									};

									app.updateZoom = function () {
									    console.info('app.updateZoom:');
									   // document.getElementById('stat_zoom').innerHTML = app.map.getZoom() + ' / ' + app.map.getMaxZoom();
									};

									//----------------------------------------------------------------------------

									Number.prototype.toRad = function () {
									    return this * Math.PI / 180;
									};

									app.calculateDistance = function (lat1, lng1, lat2, lng2) {
									    //console.info('app.calculateDistance:');
									    //console.log('lat1=', lat1, 'lng1=', lng1, 'lat2=', lat2, 'lng2=', lng2);

									    var R = 6371; // radius of the Earth in km
									    var dLat = (lat2 - lat1).toRad();
									    var dLng = (lng2 - lng1).toRad();
									    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
									        Math.cos(lat1.toRad()) * Math.cos(lat2.toRad()) *
									        Math.sin(dLng / 2) * Math.sin(dLng / 2);
									    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
									    var d = R * c;

									    //console.log(d);
									    return d;
									};

									app.msToTime = function (duration) {
									    var milliseconds = parseInt((duration % 1000) / 100, 10),
									        seconds = parseInt((duration / 1000) % 60, 10),
									        minutes = parseInt((duration / (1000 * 60)) % 60, 10),
									        hours = parseInt((duration / (1000 * 60 * 60)) % 24, 10);

									    hours = (hours < 10) ? "0" + hours : hours;
									    minutes = (minutes < 10) ? "0" + minutes : minutes;
									    seconds = (seconds < 10) ? "0" + seconds : seconds;

									    return hours + ":" + minutes + ":" + seconds + "." + milliseconds;
									};

									app.appendToStorage = function (name, data) {
									    console.info('app.appendToStorage:');

									    try {
									        var item = localStorage.getItem(name);
									        if (item === null) {
									            item = "";
									        }

									        localStorage.setItem(name, item + data);
									    }
									    catch (ex) {
									        console.warn(ex.message);
									        for (var p in ex) {
									            console.log("\t" + p + ': ' + ex[p]);
									        }
									    }
									};

									app.leftPad = function (value, padding) {
									    //console.info('app.leftPad:');

									    var zeroes = "0";

									    for (var i = 0; i < padding; i++) {
									        zeroes += "0";
									    }

									    return (zeroes + value).slice(padding * -1);
									};
								
								
								
									app.init();
								
	});
							//last-line
					
						
						//====================================================//

                    }
                    
                    //======================End ===================================//
                    
                    
                    
                    
				$scope.positionTimeShowmarker = function(){
					/////
					//$("#map").html("");
					//$("#preMap").empty();

                    $("#map").empty();
					//$(
	    //     		"<div id=\"map\" style=\"width:1170px;height: 370px;\"></div>")
					//.appendTo("#preMap");
					var temp =$rootScope.positionReportList;
					
					if (temp.positionReportList.length > 0) {
						 //map = L.map('map').setView(
							//	[ $scope.centerLat,
							//			$scope.centerLon ], 14);
						//var mapLink = '<a href="http://openstreetmap.org">OpenStreetMap</a>';
						//L
						//		.tileLayer(
						//				'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
						//				{
						//					attribution : '&copy; '
						//							+ mapLink
						//							+ ' Contributors',
						//					maxZoom : map
						//							.getMaxZoom(),
						//				}).addTo(map);
						// 22-$scope.zoom

						// create custom icon
						var firefoxIcon = L.icon({
							iconUrl : 'icons/arrow.svg',
							iconSize : [ 21, 30 ], // size of
						// the icon
						});

						var latlngs = Array();

						angular
								.forEach(
										temp.positionReportList,
										function(value, index) {
											if (index == 0) {

												map
														.setView(
																[
																		$scope.centerLat,
																		$scope.centerLon ],
																14); // setting
												// the
												// map
												// view
												// u
												// can
												// comment
												// it

											}
											// setTimeout(function(){
											var pointA;
											if (index == 0) {
												pointA = new L.LatLng(
														(temp.positionReportList[index].latitude),
														(temp.positionReportList[index].longitude));
											} else {
												pointA = new L.LatLng(
														(temp.positionReportList[index - 1].latitude),
														(temp.positionReportList[index - 1].longitude));
											}

											var pointB = new L.LatLng(
													(temp.positionReportList[index].latitude),
													(temp.positionReportList[index].longitude));
											var pointList = [
													pointA,
													pointB ];

											var firstpolyline = new L.Polyline(
													pointList,
													{
														color : 'red',
														weight : 3,
														opacity : 0.5,
														smoothFactor : 1
													});
											firstpolyline
													.addTo(map);
											// alert(value.latitude);
											if (temp.positionReportList[index].course < 300) {
												marker = new L.marker(
														[
															temp.positionReportList[index].latitude,
															temp.positionReportList[index].longitude ],
														{
															icon : L
																	.divIcon({
																		html : '<img src="icons/arrow.svg" style=" margin:-21px;position: relative;-webkit-transform: rotate('
																				+ temp.positionReportList[index].course
																				+ 'deg); -moz-transform:rotate('
																				+ temp.positionReportList[index].course
																				+ 'deg);" />'
																	})
														})
														.bindPopup(
																'<strong>Time:</strong>'
																		+ value.fixTime
																		+ '<br><strong>Speed:</strong>'
																		+ value.speed)
														.addTo(
																map);
											} else {
												marker = new L.marker(
														[
															temp.positionReportList[index].latitude,
															temp.positionReportList[index].longitude ],
														{
															icon : L
																	.divIcon({
																		html : '<img src="icons/arrow.svg" style=" margin:0px;position: relative;-webkit-transform: rotate('
																				+ temp.positionReportList[index].course
																				+ 'deg); -moz-transform:rotate('
																				+ temp.positionReportList[index].course
																				+ 'deg);" />'
																	})
														})
														.bindPopup(
																'<strong>Time:</strong>'
																		+ value.fixTime
																		+ '<br><strong>Speed:</strong>'
																		+ value.speed)
														.addTo(
																map);
											}
											// }, 60);
										})

					} else {
						// alert('new m');

						// map = L.map('map').setView(
						//		[ 20.5937, 78.9629 ], 14);
						//var mapLink = '<a href="http://openstreetmap.org">OpenStreetMap</a>';
						//L
						//		.tileLayer(
						//				'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
						//				{
						//					attribution : '&copy; '
						//							+ mapLink
						//							+ ' Contributors',
						//					maxZoom : 18,
						//				}).addTo(map);

					}
					/////////////////////////
					/*var temp =$rootScope.positionReportList;
					var index = 0;
					
					for(var i=0;i<temp.positionReportList.length;i++){
				if (temp.positionReportList[i].course < 300) {
					marker = new L.marker(
							[
								temp.positionReportList[i].latitude,
								temp.positionReportList[i].longitude ],
							{
								icon : L
										.divIcon({
											html : '<img src="icons/arrow.svg" style=" margin:-21px;position: relative;-webkit-transform: rotate('
													+ temp.positionReportList[i].course
													+ 'deg); -moz-transform:rotate('
													+ temp.positionReportList[i].course
													+ 'deg);" />'
										})
							})
							.bindPopup(
									'<strong>Time:</strong>'
											+ value.fixTime
											+ '<br><strong>Speed:</strong>'
											+ value.speed)
							.addTo(
									map);
				} else {
					marker = new L.marker(
							[
								temp.positionReportList[i].latitude,
								temp.positionReportList[i].longitude ],
							{
								icon : L
										.divIcon({
											html : '<img src="icons/arrow.svg" style=" margin:0px;position: relative;-webkit-transform: rotate('
													+ $rootScope.positionReportList[i].course
													+ 'deg); -moz-transform:rotate('
													+ $rootScope.positionReportList[i].course
													+ 'deg);" />'
										})
							})
							.bindPopup(
									'<strong>Time:</strong>'
											+ value.fixTime
											+ '<br><strong>Speed:</strong>'
											+ value.speed)
							.addTo(
									map);
				}
			}*/
					
				}
					

					$scope.showWayPointsSchoolName = function() {
						var shoolName = $scope.wayPoints.filter(function(x) {
							return x.id == $scope.wayPointId
						})[0];
						// $scope.
					}

                    $scope.loadPositionReport = function () {
                    	 $scope.geofenceReport();
						// $("#map").html("");
						// $("#preMap").empty();
                        $("#map").html("");
                        $("#preMap").empty();
                        $("<div id=\"map\" style=\"width:1170px;height: 370px;\"></div>")
                        .appendTo("#preMap");
                       map = L.map('map').setView(
                        [28.6139, 77.2090], 14);
                       var mapLink = '<a href="http://openstreetmap.org">OpenStreetMap</a>';
                       L
                           .tileLayer(
                           'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                           {
                               attribution: '&copy; '
                               + mapLink
                               + ' Contributors',
                               maxZoom: 18,
                           }).addTo(map);
                        
						
						
						
						// alert($scope.fromDate);
						// alert(fromDateSbstr);
						if ($scope.date > currDate) {
							bootbox.alert("<b>Don't select Future Date.</b>");
							return;
						} else if ($scope.toTime == undefined) {
							bootbox.alert("<b>Please select To Date.</b>");
							return;
						} else if ($scope.toTime < $scope.fromTime) {
							bootbox
									.alert("<b>From Time should not be grater than To Time</b>");
							return;
						} else if ($scope.vehicleId == undefined
								|| $scope.vehicleId == "") {
							bootbox.alert("<b>Please select Vehicle.</b>");
							return;
						} 

						var request = $resource(
								'reports/getPositionSummary/:userId',
								{
									userId : javaRest.cookie.get('userId')
								},
								{
									put : {
										method : 'PUT',
										isArray : false,
										headers : {
											'Authorization' : getPutAuthToken(
													"reports/getPositionSummary/",
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
													"reports/getPositionSummary/",
													time, nonce);
											headers['x-safeplanet-date'] = time;
											headers['nonce'] = nonce;

											var fromDateVar = new Date();
											var fromTimeVar = new Date();
											var toTimeVar = new Date();
											var vehicleIdVar = "-1";

											fromDateVar = $scope.date;
											toTimeVar = $scope.toTime;
											fromTimeVar = $scope.fromTime;
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
													+ ":00" + '", "vehicleId":'
													+ vehicleIdVar + '}';

											return filterData;
										}
									}
								}).put();

                        request.$promise
								.then(function(positionReportList) {
									$scope.positionReportList = positionReportList.positionReportList;
									$scope.positionReportPath.p1.latlngs = positionReportList.positionReportPath;

									$scope.markers.Startlat = positionReportList.startLat;
									$scope.markers.Startlng = positionReportList.startLng;

									$scope.markers.Endlat = positionReportList.endLat;
									$scope.markers.Endlng = positionReportList.endLng;

									$scope.zoom = positionReportList.zoom;

									var cLatLong = positionReportList.cLatLong
											.split(',');

									$scope.centerLat = cLatLong[0];
									$scope.centerLon = cLatLong[1];

                                    $rootScope.positionReportList = $scope.positionReportList;
                                    ///////////////////////////////////////////////

                                    //$("#map").html("");
                                    //$("#preMap").empty();
                                    //$(
                                    //    "<div id=\"map\" style=\"width:1170px;height: 370px;\"></div>")
                                    //    .appendTo("#preMap");

                                    var marker;

                                    //alert($scope.positionReportList.length);

                                    if ($scope.positionReportList.length > 0) {
                                        //try {
                                        //    map = L.map('map').setView(
                                        //        [$scope.centerLat,
                                        //        $scope.centerLon], 14);
                                        //    var mapLink = '<a href="http://openstreetmap.org">OpenStreetMap</a>';
                                        //    L
                                        //        .tileLayer(
                                        //        'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                                        //        {
                                        //            attribution: '&copy; '
                                        //            + mapLink
                                        //            + ' Contributors',
                                        //            maxZoom: map
                                        //                .getMaxZoom(),
                                        //        }).addTo(map);
                                        //}
                                        //catch (e) {

                                        //}

                                        // 22-$scope.zoom

                                        // create custom icon
                                        var firefoxIcon = L.icon({
                                            iconUrl: 'icons/arrow.svg',
                                            iconSize: [21, 30], // size of
                                            // the icon
                                        });

                                        var latlngs = Array();
                                        

                                        angular
                                            .forEach(
                                            $scope.positionReportList,
                                            function (value, index) {
                                                if (index == 0) {

                                                    map
                                                        .setView(
                                                        [
                                                            $scope.centerLat,
                                                            $scope.centerLon],
                                                        14); // setting
                                                    // the
                                                    // map
                                                    // view
                                                    // u
                                                    // can
                                                    // comment
                                                    // it

                                                }
                                                // setTimeout(function(){
                                                var pointA;
                                                if (index == 0) {
                                                    pointA = new L.LatLng(
                                                        ($scope.positionReportList[index].latitude),
                                                        ($scope.positionReportList[index].longitude));
                                                } else {
                                                    pointA = new L.LatLng(
                                                        ($scope.positionReportList[index - 1].latitude),
                                                        ($scope.positionReportList[index - 1].longitude));
                                                }

                                                var pointB = new L.LatLng(
                                                    ($scope.positionReportList[index].latitude),
                                                    ($scope.positionReportList[index].longitude));
                                                var pointList = [
                                                    pointA,
                                                    pointB];

                                                var firstpolyline = new L.Polyline(
                                                    pointList,
                                                    {
                                                        color: 'red',
                                                        weight: 3,
                                                        opacity: 0.5,
                                                        smoothFactor: 1
                                                    });
                                                firstpolyline
                                                    .addTo(map);
                                                // alert(value.latitude);
                                                if ($scope.positionReportList[index].course < 300) {
                                                    marker = new L.marker(
                                                        [
                                                            $scope.positionReportList[index].latitude,
                                                            $scope.positionReportList[index].longitude],
                                                        {
                                                            icon: L
                                                                .divIcon({
                                                                    html: '<img src="icons/arrow.svg" style=" margin:-21px;position: relative;-webkit-transform: rotate('
                                                                    + $scope.positionReportList[index].course
                                                                    + 'deg); -moz-transform:rotate('
                                                                    + $scope.positionReportList[index].course
                                                                    + 'deg);" />'
                                                                })
                                                        })
                                                        .bindPopup(
                                                        '<strong>Time:</strong>'
                                                        + value.fixTime
                                                        + '<br><strong>Speed:</strong>'
                                                        + value.speed)
                                                        .addTo(
                                                        map);
                                                } else {
                                                    try {
                                                        marker = new L.marker(
                                                            [
                                                                $scope.positionReportList[index].latitude,
                                                                $scope.positionReportList[index].longitude],
                                                            {
                                                                icon: L
                                                                    .divIcon({
                                                                        html: '<img src="icons/arrow.svg" style=" margin:0px;position: relative;-webkit-transform: rotate('
                                                                        + $scope.positionReportList[index].course
                                                                        + 'deg); -moz-transform:rotate('
                                                                        + $scope.positionReportList[index].course
                                                                        + 'deg);" />'
                                                                    })
                                                            })
                                                            .bindPopup(
                                                            '<strong>Time:</strong>'
                                                            + value.fixTime
                                                            + '<br><strong>Speed:</strong>'
                                                            + value.speed)
                                                            .addTo(
                                                            map);
                                                    }
                                                    catch (e) {

                                                    }

                                                }
                                                // }, 60);
                                            })

                                    } else {
                                        // alert('new m');
                                        //try {
                                        //    map = L.map('map').setView(
                                        //        [20.5937, 78.9629], 14);
                                        //    var mapLink = '<a href="http://openstreetmap.org">OpenStreetMap</a>';
                                        //    L
                                        //        .tileLayer(
                                        //        'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                                        //        {
                                        //            attribution: '&copy; '
                                        //            + mapLink
                                        //            + ' Contributors',
                                        //            maxZoom: 18,
                                        //        }).addTo(map);
                                        //}
                                        //catch (e) {

                                        //}


                                    }



                                    $scope.onCategoryChange();
                                   
								});

						
					}

					//$scope.loadEmptyMarker = function() {

					//	$("#map").html("");
					//	$("#preMap").empty();
					//	$(
					//			"<div id=\"map\" style=\"width:1170px;height: 380px;\"></div>")
					//			.appendTo("#preMap");

					//	 map = L.map('map')
					//			.setView([ 20.5937, 78.9629 ], 14);
					//	var mapLink = '<a href="http://openstreetmap.org">OpenStreetMap</a>';
					//	L
					//			.tileLayer(
					//					'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
					//					{
					//						attribution : '&copy; ' + mapLink
					//								+ ' Contributors',
					//						maxZoom : 18,
					//					}).addTo(map);
					//}

					//$scope.playMarkerAnimation = function() {

					//	$("#map").html("");
					//	$("#preMap").empty();
					//	$(
					//			"<div id=\"map\" style=\"width:1170px;height: 380px;\"></div>")
					//			.appendTo("#preMap");
					//	// }

					//	setTimeout(
					//			function() {
					//				var marker;

					//				 map = L.map('map').setView(
					//						[ $scope.centerLat,
					//								$scope.centerLon ], 14);
					//				var mapLink = '<a href="http://openstreetmap.org">OpenStreetMap</a>';
					//				L
					//						.tileLayer(
					//								'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
					//								{
					//									attribution : '&copy; '
					//											+ mapLink
					//											+ ' Contributors',
					//									maxZoom : map
					//											.getMaxZoom(),
					//								}).addTo(map);
					//				// 22-$scope.zoom

					//				// create custom icon
					//				var firefoxIcon = L.icon({
					//					iconUrl : 'icons/arrow.svg',
					//					iconSize : [ 21, 30 ], // size of the
					//				// icon
					//				});

					//				var latlngs = Array();

					//				angular
					//						.forEach(
					//								$scope.positionReportList,
					//								function(value, index) {
					//									if (index == 0) {

					//										map
					//												.setView(
					//														[
					//																$scope.centerLat,
					//																$scope.centerLon ],
					//														14); // setting
					//										// the
					//										// map
					//										// view
					//										// u
					//										// can
					//										// comment
					//										// it

					//									}
					//									setTimeout(
					//											function() {
					//												var pointA;
					//												if (index == 0) {
					//													pointA = new L.LatLng(
					//															($scope.positionReportList[index].latitude),
					//															($scope.positionReportList[index].longitude));
					//												} else {
					//													pointA = new L.LatLng(
					//															($scope.positionReportList[index - 1].latitude),
					//															($scope.positionReportList[index - 1].longitude));
					//												}

					//												var pointB = new L.LatLng(
					//														($scope.positionReportList[index].latitude),
					//														($scope.positionReportList[index].longitude));
					//												var pointList = [
					//														pointA,
					//														pointB ];

					//												var firstpolyline = new L.Polyline(
					//														pointList,
					//														{
					//															color : 'red',
					//															weight : 3,
					//															opacity : 0.5,
					//															smoothFactor : 1
					//														});
					//												firstpolyline
					//														.addTo(map);
					//												// alert(value.latitude);
					//												if ($scope.positionReportList[index].course < 300) {
					//													marker = new L.marker(
					//															[
					//																	$scope.positionReportList[index].latitude,
					//																	$scope.positionReportList[index].longitude ],
					//															{
					//																icon : L
					//																		.divIcon({
					//																			html : '<img src="icons/arrow.svg" style=" margin:-21px;position: relative;-webkit-transform: rotate('
					//																					+ $scope.positionReportList[index].course
					//																					+ 'deg); -moz-transform:rotate('
					//																					+ $scope.positionReportList[index].course
					//																					+ 'deg);" />'
					//																		})
					//															})
					//															.bindPopup(
					//																	'<strong>Time:</strong>'
					//																			+ value.fixTime
					//																			+ '<br><strong>Speed:</strong>'
					//																			+ value.speed)
					//															.addTo(
					//																	map);
					//												} else {
					//													marker = new L.marker(
					//															[
					//																	$scope.positionReportList[index].latitude,
					//																	$scope.positionReportList[index].longitude ],
					//															{
					//																icon : L
					//																		.divIcon({
					//																			html : '<img src="icons/arrow.svg" style=" margin:0px;position: relative;-webkit-transform: rotate('
					//																					+ $scope.positionReportList[index].course
					//																					+ 'deg); -moz-transform:rotate('
					//																					+ $scope.positionReportList[index].course
					//																					+ 'deg);" />'
					//																		})
					//															})
					//															.bindPopup(
					//																	'<strong>Time:</strong>'
					//																			+ value.fixTime
					//																			+ '<br><strong>Speed:</strong>'
					//																			+ value.speed)
					//															.addTo(
					//																	map);
					//												}
					//											}, 60);
					//								})

					//			}, 2000);
                        

					//}
                    
                   

					$scope.downloadPositionReport = function() {
						$scope.downloadPositionReportData = $resource(
								'reports/downloadReport/:userId/:reportName',
								{
									userId : javaRest.cookie.get('userId'),
									reportName : 'positionReport'
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

						$scope.downloadPositionReportData.$promise
								.then(function(downloadpositionReportData) {
									window
											.open("uploads/reports/positionSummaryReport_"
													+ javaRest.cookie
															.get('userId')
													+ ".xls");
								});
					}
				});
