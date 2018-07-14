 <!DOCTYPE html>
    <html ng-app="dashboardApp">
    <head>
      
		<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>

		<meta name="keywords" content="" />
		
		<!-- Custom Theme files -->
	
      <!-- load bootstrap and fontawesome via CDN -->
      <!-- <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" />
      <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.0.0/css/font-awesome.css" />
      
		<link href="css/style2.css" rel='stylesheet' type='text/css' /> -->
		
		<!-- bootstrap 3.0.2 -->
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Ionicons -->
        <link href="css/ionicons.min.css" rel="stylesheet" type="text/css" />
        
         <link href="css/admincss.css" rel="stylesheet" type="text/css" />

 <!-- load angular and angular route -->
 
 <script type="text/javascript">
    var globalConfig = {        
        contextPath : 'safeplanet'       
    };

    
</script>
		
		<script src="js/jquery-1.11.1.min.js"></script>

        <script src="js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        	
		<script src="js/angular/angular.js"></script>
		<script src="js/angular/angular-route.js"></script>
		<script src="js/angular/angular-resource.js"></script>      
		<script src="js/angular/angular-cookies.js"></script>
		<script src="js/angular/angular-sanitize.js"></script>
  
  	  	<script src="js/store.js"></script>
        <script src="js/sha256.js"></script>
        <script src="js/enc-base64-min.js"></script>
        <script src="js/javarest.js"></script>
    	<script src="js/cookie.js"></script>
	    	    	
		<script src="js/app/globals.js"></script>
		<script src="js/homeApp.js"></script>
		<script src="js/app/controllers/appcontrollers.js"></script>
		<script src="js/app/directives/appdirectives.js"></script>
		<script src="js/app/services/appservices.js"></script>
      
        <!-- Bootstrap -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        
        <!-- Admin App -->
        <script src="js/app/app.js" type="text/javascript"></script>
        
         <link rel="stylesheet" href="css/leaflet.css" />
         <!-- <link rel="stylesheet" href="leafletroute/leaflet-routing-machine.css" /> -->
          
       <script src="js/leaflet.js"></script>
       <!-- <script src="leafletroute/leaflet-routing-machine.js"></script> -->
       <script src="leafletroute/angular-leaflet-directive.min.js"></script>
		
		<!-- Google Fonts -->
		<link href='//fonts.googleapis.com/css?family=Roboto:400,100,300,500,700,900' rel='stylesheet' type='text/css'>
		      

 </head>
    <body class="skin-blue" ng-controller="UserController">
        <!-- header logo: style can be found in header.less -->
        <header class="header">
            <!-- <a href="index.html" class="logo">                
                SafePlanet
            </a> -->
            <!-- Header Navbar: style can be found in header.less -->
            <nav class="navbar navbar-static-top" role="navigation">
                <!-- Sidebar toggle button-->
                <a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <div class="navbar-right">
                    <ul class="nav navbar-nav" ng-controller="AlertsController">
                        
                        <!-- Notifications: style can be found in dropdown.less -->
                        <li class="dropdown notifications-menu">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <i class="fa fa-warning"></i>
                                <span class="label label-warning">{{alerts.length}}</span>
                            </a>
                            <ul class="dropdown-menu">
                                <li class="header">You have {{alerts.length}} notifications</li>
                                <li>
                                    <!-- inner menu: contains the actual data -->
                                    <ul class="menu">
                                        <li ng-repeat="alert in alerts">
                                            <a href="#">
                                                <!-- <i class="ion ion-ios7-people info"></i> --> <small style="font-size: 10px; !important; color:#3C8DBC ">{{alert.timeCreated | date:'d/M h:mm a'}}: </small>{{alert.addText}}
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                                <li class="footer"><a href="#">View all</a></li>
                            </ul>
                        </li>
                        <!-- Messages: style can be found in dropdown.less Start here -->
                        
                        <!-- <li class="dropdown messages-menu">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <i class="fa fa-envelope"></i>
                                <span class="label label-danger">9</span>
                            </a>
                            <ul class="dropdown-menu">
                                <li class="header">You have 9 tasks</li>
                                <li>
                                    <ul class="menu">
                                        <li>
                                            <a href="#">
                                                <h3>
                                                    Design some buttons
                                                    <small class="pull-right">20%</small>
                                                </h3>
                                                <div class="progress xs">
                                                    <div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">20% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                                <h3>
                                                    Create a nice theme
                                                    <small class="pull-right">40%</small>
                                                </h3>
                                                <div class="progress xs">
                                                    <div class="progress-bar progress-bar-green" style="width: 40%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">40% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                                <h3>
                                                    Some task I need to do
                                                    <small class="pull-right">60%</small>
                                                </h3>
                                                <div class="progress xs">
                                                    <div class="progress-bar progress-bar-red" style="width: 60%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">60% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                                <h3>
                                                    Make beautiful transitions
                                                    <small class="pull-right">80%</small>
                                                </h3>
                                                <div class="progress xs">
                                                    <div class="progress-bar progress-bar-yellow" style="width: 80%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">80% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                                <li class="footer">
                                    <a href="#">View all</a>
                                </li>
                            </ul>
                        </li> -->
                        
                        <!-- Messages: style can be found in dropdown.less End here -->
                        
                        <!-- User Account: style can be found in dropdown.less -->
                        <li class="dropdown user user-menu">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <i class="glyphicon glyphicon-user"></i>
                                <span>{{userName}} <i class="caret"></i></span>
                            </a>
                            <ul class="dropdown-menu">
                                <!-- User image -->
                                <li class="user-header bg-light-blue">
                                    <img src="img/avatar5.png" class="img-circle" alt="User Image" />
                                    <p>
                                        {{userName}}
                                        <!-- <small>Member since Nov. 2012</small> -->
                                    </p>
                                </li>
                                <!-- Menu Body -->
                                <!-- <li class="user-body">
                                    <div class="col-xs-4 text-center">
                                        <a href="#">Followers</a>
                                    </div>
                                    <div class="col-xs-4 text-center">
                                        <a href="#">Sales</a>
                                    </div>
                                    <div class="col-xs-4 text-center">
                                        <a href="#">Friends</a>
                                    </div>
                                </li> -->
                                <!-- Menu Footer-->
                                <li class="user-footer">
                                    <div class="pull-left">
                                        <a href="#editProfile.html" class="btn btn-default btn-flat">Profile</a>
                                    </div>
                                    <div class="pull-right">
                                        <a href="#" class="btn btn-default btn-flat" ng-click="logout()">Sign out</a>
                                    </div>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </nav>
        </header>
        
 
        <div class="wrapper row-offcanvas row-offcanvas-left">
            <!-- Left side column. contains the logo and sidebar -->
            <aside class="left-side sidebar-offcanvas">
                <!-- sidebar: style can be found in sidebar.less -->
                <section class="sidebar">
                    <!-- Sidebar user panel -->
                    <div class="user-panel">
                        <div class="pull-left image">
                            <img src="img/avatar5.png" class="img-circle" alt="User Image" />
                        </div>
                        <div class="pull-left info">
                            <p>Hello, {{firstName}}</p>

                            <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
                        </div>
                    </div>
                    <!-- search form -->
                    <!-- <form action="#" method="get" class="sidebar-form">
                        <div class="input-group">
                            <input type="text" name="q" class="form-control" placeholder="Search..."/>
                            <span class="input-group-btn">
                                <button type='submit' name='seach' id='search-btn' class="btn btn-flat"><i class="fa fa-search"></i></button>
                            </span>
                        </div>
                    </form> -->
                    <!-- /.search form -->
                    <!-- sidebar menu: : style can be found in sidebar.less -->
                    <ul class="sidebar-menu">
                        <li class="active">
                            <a href="#/home.html">
                                <i class="ion-home"></i> <span>Home</span>
                            </a>
                        </li>
                        <li ng-repeat="link in dashboardLinks.linksList" ng-class="getClass($index)" class="treeview" ng-click="openMenu()">
                            <a href="{{link}}">
                                <i class="{{dashboardLinks.linksClassList[$index]}}"></i> <span>{{dashboardLinks.linksNameList[$index]}}</span>
                                <i ng-class="fa fa-angle-left pull-right" ng-if="dashboardLinks.linksNameList[$index]=='Student Details'" class="fa fa-angle-left pull-right"></i>
                            </a>
                            <ul class="treeview-menu" ng-class="treeview-menu" ng-if="dashboardLinks.linksNameList[$index]=='Student Details'">
                                <li><a href="#addstudent.html"><i class="fa fa-angle-double-right"></i> Add Students</a></li>
                                <li><a href="#student.html"><i class="fa fa-angle-double-right"></i> Student List</a></li>                                
                            </ul>
                        </li>
                        <!-- <li>
                            <a href="#teacher.html">
                                <i class="ion-android-people"></i> <span>Teacher</span>
                            </a>
                        </li>
                        <li>
                            <a href="#student.html">
                                <i class="ion-android-contacts"></i> <span>Student Details</span>
                            </a>
                        </li>
                        <li>
                            <a href="#settings.html">
                                <i class="ion-gear-a"></i> <span>Settings</span>
                            </a>
                        </li>
                        <li>
                            <a href="#busLocation.html">
                                <i class="ion-android-bus"></i> <span>Bus Location</span>
                            </a>
                        </li>
                        <li>
                            <a href="#schoolInfo.html">
                                <i class="ion-university"></i> <span>School Information</span>
                            </a>
                        </li>
                        -->
                        <li>
                            <a href="#editProfile.html">
                                <i class="ion-edit"></i> <span>Profile</span>
                            </a>
                        </li> 
                     </ul>
                   </section>
              </aside>
              
               <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                   <!--  <h1>
                        Home
                        <small>Control panel</small>
                    </h1> -->
                    <ol class="breadcrumb">
                        <li><a href="#welcome.html"><i class="fa fa-home"></i> Home</a></li>
                        <!-- <li class="active">Dashboard</li> -->
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <div class="row" style="margin:0 !important;" ng-view>
                        <!-- <div class="col-lg-3 col-xs-6" style="padding-bottom: 15px; height: 104px  !important;" ng-repeat="link in dashboardLinks.linksList">
                            <a href="#{{link}}">
                            	<img src="{{dashboardLinks.iconsList[$index]}}" class="img-rounded" alt="{{dashboardLinks.linksNameList[$index]}}" style="width:60px; height:60px;">
                            	<br><div style="color: #428BCA; max-width: 60px; font-size: 10px; text-align: center; font-weight: bold;">{{dashboardLinks.linksNameList[$index]}}</div>
                            </a>
                        </div> --><!-- ./col -->
                        
                        <!-- <div class="col-lg-3 col-xs-6" style="padding-bottom: 15px; height: 104px  !important;">
                            <img src="icons/Teacher_Icon_128.png" class="img-rounded" alt="Teacher" style="width:60px; height:60px;">
                            <br><div style="color: #428BCA; max-width: 60px; font-size: 10px; text-align: center; font-weight: bold;">Teacher</div>
                        </div>./col
                        
                        <div class="col-lg-3 col-xs-6" style="padding-bottom: 15px; height: 104px  !important;">
                            <img src="img/avatar5.png" class="img-rounded" alt="Profile" style="width:60px; height:60px;"> 
                            <br><div style="color: #428BCA; max-width: 60px; font-size: 10px; text-align: center; font-weight: bold;">Student Details</div>
                        </div>./col
                        
                        <div class="col-lg-3 col-xs-6" style="padding-bottom: 15px; height: 104px  !important; ">
                            <img src="icons/Setting_Icon_128.png" class="img-rounded" alt="Settings" style="width:60px; height:60px;">
                            <br><div style="color: #428BCA; max-width: 60px; font-size: 10px; text-align: center; font-weight: bold;">Settings</div>
                        </div>./col
                        
                        <div class="col-lg-3 col-xs-6" style="padding-bottom: 15px; height: 104px  !important;">
                         	<img src="icons/bus.png" class="img-rounded" alt="Bus" style="width:60px; height:60px;">
                         	<br><div style="color: #428BCA; max-width: 60px; font-size: 10px; text-align: center; font-weight: bold;">Bus Location</div>
                        </div>./col
                        <div class="col-lg-3 col-xs-6" style="padding-bottom: 15px; height: 104px  !important;">
                         	<img src="icons/School_Icon_128.png" class="img-rounded" alt="School" style="width:60px; height:60px;">
                         	<br><div style="color: #428BCA; max-width: 60px; font-size: 10px; text-align: center; font-weight: bold;">School Information</div>
                        </div> --><!-- ./col -->
                        
                        <!-- <div class="col-lg-3 col-xs-6" style="padding-bottom: 15px;">
                         	<img src="icons/map-128.png" class="img-rounded" alt="Map" style="width:80px; height:80px;">
                        </div> --><!-- ./col -->
                        
                    </div><!-- /.row -->
                                                                                                  
           </section>
         </aside>
         
         </div>
                                                                      
        
    </body>


</html>