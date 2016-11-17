<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ include file="/resources/common/taglibs.jsp" %>
        <!DOCTYPE html>
        <!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
        <html>
        <head>
		    <%@ include file="/resources/common/required_style.jsp" %>
            <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
            <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
            <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
           
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8"  />
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <title>
                <sitemesh:write property='title' />
            </title>
            <!-- Tell the browser to be responsive to screen width -->
            <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
            <sitemesh:write property='head' />
        </head>
        <!--
BODY TAG OPTIONS:
=================
Apply one or more of the following classes to get the
desired effect
|---------------------------------------------------------|
| SKINS         | skin-blue                               |
|               | skin-black                              |
|               | skin-purple                             |
|               | skin-yellow                             |
|               | skin-red                                |
|               | skin-green                              |
|---------------------------------------------------------|
|LAYOUT OPTIONS | fixed                                   |
|               | layout-boxed                            |
|               | layout-top-nav                          |
|               | sidebar-collapse                        |
|               | sidebar-mini                            |
|---------------------------------------------------------|
-->

        <body class="hold-transition skin-blue sidebar-mini">
            <div class="wrapper">
                <!-- Main Header -->
                <header class="main-header">
                    <!-- Logo -->
                    <a href="index2.html" class="logo">
                        <!-- mini logo for sidebar mini 50x50 pixels -->
                        <span class="logo-mini"><b>乐活</b>后台</span>
                        <!-- logo for regular state and mobile devices -->
                        <span class="logo-lg"><b>乐活集</b>后台系统</span>
                    </a>
                    <!-- Header Navbar -->
                    <nav class="navbar navbar-static-top" role="navigation">
                        <!-- Sidebar toggle button-->
                        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                            <span class="sr-only">Toggle navigation</span>
                        </a>
                        <!-- Navbar Right Menu -->
                        <div class="navbar-custom-menu">
                            <ul class="nav navbar-nav">
                                <!-- Messages: style can be found in dropdown.less-->
                                <li class="dropdown messages-menu">
                                    <!-- Menu toggle button -->
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                        <i class="fa fa-envelope-o"></i>
                                        <span class="label label-success">4</span>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li class="header">您有四条信息</li>
                                        <li>
                                            <!-- inner menu: contains the messages -->
                                            <ul class="menu">
                                                <li>
                                                    <!-- start message -->
                                                    <a href="#">
                                                        <div class="pull-left">
                                                            <!-- User Image -->
                                                            <img src="${resources_static}/AdminLTE-2.3.7/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
                                                        </div>
                                                        <!-- Message title and timestamp -->
                                                        <h4>
                        Support Team
                        <small><i class="fa fa-clock-o"></i> 5 mins</small>
                      </h4>
                                                        <!-- The message -->
                                                        <p>信息展示...</p>
                                                    </a>
                                                </li>
                                                <!-- end message -->
                                            </ul>
                                            <!-- /.menu -->
                                        </li>
                                        <li class="footer"><a href="#">查看所有信息</a></li>
                                    </ul>
                                </li>
                                <!-- /.messages-menu -->
                                <!-- Notifications Menu -->
                                <li class="dropdown notifications-menu">
                                    <!-- Menu toggle button -->
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                        <i class="fa fa-bell-o"></i>
                                        <span class="label label-warning">10</span>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li class="header">您有10个通知</li>
                                        <li>
                                            <!-- Inner Menu: contains the notifications -->
                                            <ul class="menu">
                                                <li>
                                                    <!-- start notification -->
                                                    <a href="#">
                                                        <i class="fa fa-users text-aqua"></i>今天有5位新成员加入
                                                    </a>
                                                </li>
                                                <!-- end notification -->
                                            </ul>
                                        </li>
                                        <li class="footer"><a href="#">查看所有</a></li>
                                    </ul>
                                </li>
                                <!-- Tasks Menu -->
                                <li class="dropdown tasks-menu">
                                    <!-- Menu Toggle Button -->
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                        <i class="fa fa-flag-o"></i>
                                        <span class="label label-danger">9</span>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li class="header">您有9个任务</li>
                                        <li>
                                            <!-- Inner menu: contains the tasks -->
                                            <ul class="menu">
                                                <li>
                                                    <!-- Task item -->
                                                    <a href="#">
                                                        <!-- Task title and progress text -->
                                                        <h3>
                        设计按钮
                        <small class="pull-right">20%</small>
                      </h3>
                                                        <!-- The progress bar -->
                                                        <div class="progress xs">
                                                            <!-- Change the css width attribute to simulate progress -->
                                                            <div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                                <span class="sr-only">20% 完成度</span>
                                                            </div>
                                                        </div>
                                                    </a>
                                                </li>
                                                <!-- end task item -->
                                            </ul>
                                        </li>
                                        <li class="footer">
                                            <a href="#">查看所有任务</a>
                                        </li>
                                    </ul>
                                </li>
                                <!-- User Account Menu -->
                                <li class="dropdown user user-menu">
                                    <!-- Menu Toggle Button -->
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                        <!-- The user image in the navbar-->
                                       
                                        <!-- hidden-xs hides the username on small devices so only the image appears. -->
                                        <span class="hidden-xs"> ${ADMINUSER.userName==null?'':ADMINUSER.userName}   </span>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <!-- The user image in the menu -->
                                        <li class="user-header">
                                            <c:choose>
												 <c:when test="${ADMINUSER.avatar == null }">
			                                        <img src=" ${resources_static}/AdminLTE-2.3.7/dist/img/avatar5.png" class="img-circle" alt="User Image" />
												 </c:when>
												 <c:when test="${ADMINUSER.avatar == '' }">
			                                        <img src=" ${resources_static}/AdminLTE-2.3.7/dist/img/avatar5.png" class="img-circle" alt="User Image" />
												 </c:when>
												 <c:otherwise>
			                                        <img src=" ${ADMINUSER.avatar }" class="img-circle" alt="User Image" />
												 </c:otherwise>
											</c:choose>
										
                                            <p>
                                                王辉阳 - Java开发
                                                <small>Member since Nov. 2012</small>
                                            </p>
                                        </li>
                                        <!-- Menu Body -->
                                        <li class="user-body">
                                            <div class="row">
                                                <div class="col-xs-4 text-center">
                                                    <a href="#">关注者</a>
                                                </div>
                                                <div class="col-xs-4 text-center">
                                                    <a href="#">业绩</a>
                                                </div>
                                                <div class="col-xs-4 text-center">
                                                    <a href="#">朋友</a>
                                                </div>
                                            </div>
                                            <!-- /.row -->
                                        </li>
                                        <!-- Menu Footer-->
                                        <li class="user-footer">
                                            <div class="pull-left"   id="rightAboveSelf">
                                                <a href="${ctx }/adminUsers/updateSelfPage" class="btn btn-default btn-flat">个人信息</a>
                                            </div>
                                            <div class="pull-right">
                                                <a href="${ctx}/admin/logout" class="btn btn-default btn-flat">注销</a>
                                            </div>
                                        </li>
                                    </ul>
                                </li>
                                <!-- Control Sidebar Toggle Button -->
                                <li>
                                    <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
                                </li>
                            </ul>
                        </div>
                    </nav>
                </header>
                <!-- Left side column. contains the logo and sidebar -->
                <aside class="main-sidebar">
                    <!-- sidebar: style can be found in sidebar.less -->
                    <section class="sidebar">
                        <!-- Sidebar user panel (optional) -->
                        <div class="user-panel">
                            <div class="pull-left image">
                                      <c:choose>
										 <c:when test="${ADMINUSER.avatar == null }">
	                                        <img src=" ${resources_static}/AdminLTE-2.3.7/dist/img/avatar5.png" class="img-circle" alt="User Image" />
										 </c:when>
										 <c:when test="${ADMINUSER.avatar == '' }">
	                                        <img src=" ${resources_static}/AdminLTE-2.3.7/dist/img/avatar5.png" class="img-circle" alt="User Image" />
										 </c:when>
										 <c:otherwise>
	                                        <img src=" ${ADMINUSER.avatar }" class="img-circle" alt="User Image" />
										 </c:otherwise>
										</c:choose>
                            </div>
                            <div class="pull-left info">
                                <p>${ADMINUSER_NAME==null ? "" : ADMINUSER_NAME}</p>
                                <!-- Status -->
                                <!-- 
                                <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
                                 -->
                            </div>
                        </div>
                        <!-- search form (Optional) -->
                        <form action="#" method="get" class="sidebar-form">
                            <div class="input-group">
                                <input type="text" name="q" class="form-control" placeholder="Search...">
                                <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                </button>
              </span>
                            </div>
                        </form>
                        <!-- /.search form -->
                        <!-- Sidebar Menu -->
                        <ul class="sidebar-menu">
                            <li class="header">菜单</li>
                            <!-- Optionally, you can add icons to the links -->
                            <li role="index"  class="active"><a href="${ctx }/admin/index"><i class="fa fa-link"></i> <span>主页</span></a></li>
                            <li  role="updatePwdPage"><a href="${ctx }/adminUsers/updatePwdPage"><i class="fa fa-link"></i> <span>修改密码</span></a></li>
                            <li  role="updateSelfPage"><a href="${ctx }/adminUsers/updateSelfPage"><i class="fa fa-link"></i> <span>个人信息</span></a></li>
                            <li class="treeview"   >
						           <a href="#">
						            <i class="fa fa-wrench"></i>
						            <span>系统管理</span>
						            <span class="pull-right-container">
						              <i class="fa fa-angle-left pull-right"></i>
						            </span>
						          </a>
						          <ul class="treeview-menu">
	                            <shiro:hasPermission name="admin:adminUsers:read">
                                    <li role="adminUsers"><a href="${ctx}/adminUsers/listPage"><i class="fa fa-circle-o"> </i> <span>用户管理</span></a></li>
	                            </shiro:hasPermission>  
	                            <shiro:hasPermission name="admin:roles:read">
                                    <li role="roles"><a href="${ctx}/roles/listPage"><i class="fa fa-circle-o"> </i>角色管理</a></li>
                                 </shiro:hasPermission>
	                            <shiro:hasPermission name="admin:permissions:read">
                                    <li role="permissions"><a href="${ctx}/permissions/listPage"><i class="fa fa-circle-o"> </i>权限管理</a></li>
                                 </shiro:hasPermission>
						          </ul>
						        </li>
						        
						        <li class="treeview"   >
						           <a href="#">
						            <i class="fa fa-wrench"></i>
						            <span>测试</span>
						            <span class="pull-right-container">
						              <i class="fa fa-angle-left pull-right"></i>
						            </span>
						          </a>
						          <ul class="treeview-menu">
                                    <li role="tempTest"><a href="${ctx}/tempTest/upload2QiniuPage"><i class="fa fa-circle-o"> </i> <span>大文件上传</span></a></li>
						          </ul>
						        </li>
						        
                        </ul>
                        <!-- /.sidebar-menu -->
                    </section>
                    <!-- /.sidebar -->
                </aside>
                <!-- Content Wrapper. Contains page content -->
                <div class="content-wrapper">
                    <!-- Content Header (Page header) -->
                    <section class="content-header">
                        <h1>
        <sitemesh:write property='title'/>
        <!-- 单个页面副标题
        <small>Optional description</small>
         -->
      </h1>
                        <ol class="breadcrumb">
                            <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
                            <li class="active">Here</li>
                        </ol>
                    </section>
                    <!-- Main content -->
                    <section class="content">
                        <!-- Your Page Content Here -->
                        <sitemesh:write property="body" />
                    </section>
                    <!-- /.content -->
                </div>
                <!-- /.content-wrapper -->
                <!-- Main Footer -->
                <footer class="main-footer">
                    <!-- To the right -->
                    <div class="pull-right hidden-xs">
                    <!-- 
                        some info
                     -->
                    </div>
                    <!-- Default to the left -->
                    <strong>Copyright &copy; 2016 <a href="#">乐活集科技有限公司</a>.</strong> All rights reserved.
                </footer>
                <!-- Control Sidebar -->
                <aside class="control-sidebar control-sidebar-dark">
                    <!-- Create the tabs -->
                    <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
                        <li class="active"><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
                        <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
                    </ul>
                    <!-- Tab panes -->
                    <div class="tab-content">
                        <!-- Home tab content -->
                        <div class="tab-pane active" id="control-sidebar-home-tab">
                            <h3 class="control-sidebar-heading">Recent Activity</h3>
                            <ul class="control-sidebar-menu">
                                <li>
                                    <a href="javascript::;">
                                        <i class="menu-icon fa fa-birthday-cake bg-red"></i>
                                        <div class="menu-info">
                                            <h4 class="control-sidebar-subheading">Langdon's Birthday</h4>
                                            <p>Will be 23 on April 24th</p>
                                        </div>
                                    </a>
                                </li>
                            </ul>
                            <!-- /.control-sidebar-menu -->
                            <h3 class="control-sidebar-heading">Tasks Progress</h3>
                            <ul class="control-sidebar-menu">
                                <li>
                                    <a href="javascript::;">
                                        <h4 class="control-sidebar-subheading">
                Custom Template Design
                <span class="pull-right-container">
                  <span class="label label-danger pull-right">70%</span>
                </span>
              </h4>
                                        <div class="progress progress-xxs">
                                            <div class="progress-bar progress-bar-danger" style="width: 70%"></div>
                                        </div>
                                    </a>
                                </li>
                            </ul>
                            <!-- /.control-sidebar-menu -->
                        </div>
                        <!-- /.tab-pane -->
                        <!-- Stats tab content -->
                        <div class="tab-pane" id="control-sidebar-stats-tab">Stats Tab Content</div>
                        <!-- /.tab-pane -->
                        <!-- Settings tab content -->
                        <div class="tab-pane" id="control-sidebar-settings-tab">
                            <form method="post">
                                <h3 class="control-sidebar-heading">General Settings</h3>
                                <div class="form-group">
                                    <label class="control-sidebar-subheading">
                                        Report panel usage
                                        <input type="checkbox" class="pull-right" checked>
                                    </label>
                                    <p>
                                        Some information about this general settings option
                                    </p>
                                </div>
                                <!-- /.form-group -->
                            </form>
                        </div>
                        <!-- /.tab-pane -->
                    </div>
                </aside>
                <!-- /.control-sidebar -->
                <!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
                <div class="control-sidebar-bg"></div>
            </div>
            <!-- ./wrapper -->
            <script>  
				$(function(){
							//菜单保持选中状态
							//TODO 第一次登陆要清除cookie
							//TODO 注销也要清除cookie
					
				           if( $.cookie("curMenu") ){
				        	   var curMenu = $("li[role="+$.cookie("curMenu")+"]");
				        	   if(curMenu){
									  //移除其他选中项
									  $(curMenu).parents(".sidebar-menu").first().find("li.active").removeClass("active");
									 //增加active和父ul的active，移除其他active
									  if(!$(curMenu).hasClass("active")){
											$(curMenu).addClass("active");
						        	   }
									  var parentMenu = $(curMenu).parents("li").filter(".treeview");
						        	  if( parentMenu.length &&  !parentMenu.hasClass("active") && parentMenu.addClass("active")){ 
						        	  }
				        	   }
				           }
							
						  //点击：点击没有连接跳转，单点父菜单，都不放入cookie
				           $(".sidebar-menu").filter("ul").find("li").click(function(){
				        	   if( $("a",$(this)) && $("a",$(this)).attr("href") &&  $("a",$(this)).attr("href")!="#" ){
				        		   //js创建的cookie默认当前页面可访问，设置路径其他页面则可访问
				        		   $.cookie('curMenu', $(this).attr("role"),{ expires: 1, path: '/' });// 1天有效期
				        	   }
				           });
						  
						  // 右上角个人信息对应左侧菜单
						  $("#rightAboveSelf").bind("click" , function(){
				        		   $.cookie('curMenu', "updateSelfPage",{ expires: 1, path: '/' });
						  })
				})
				</script>
        </body>

        </html>
