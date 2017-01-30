<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>

<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Meta, title, CSS, favicons, etc. -->
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>openSem Server Administration</title>

        <!-- Bootstrap -->
        <link href="<c:url value='/vendors/bootstrap/dist/css/bootstrap.min.css'/>" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="<c:url value='/vendors/font-awesome/css/font-awesome.min.css'/>" rel="stylesheet">
        <!-- NProgress -->
        <link href="<c:url value='/vendors/nprogress/nprogress.css'/>" rel="stylesheet">
        <!-- iCheck -->
        <link href="<c:url value='/vendors/iCheck/skins/flat/green.css'/>" rel="stylesheet">
        <!-- Datatables -->
        <link href="<c:url value='/vendors/datatables.net-bs/css/dataTables.bootstrap.min.css'/>" rel="stylesheet">
        <link href="<c:url value='/vendors/datatables.net-buttons-bs/css/buttons.bootstrap.min.css'/>" rel="stylesheet">
        <link href="<c:url value='/vendors/datatables.net-fixedheader-bs/css/fixedHeader.bootstrap.min.css'/>" rel="stylesheet">
        <link href="<c:url value='/vendors/datatables.net-responsive-bs/css/responsive.bootstrap.min.css'/>" rel="stylesheet">
        <link href="<c:url value='/vendors/datatables.net-scroller-bs/css/scroller.bootstrap.min.css'/>" rel="stylesheet">

        <!-- Custom Theme Style -->
        <link href="<c:url value='/css/custom.min.css'/>" rel="stylesheet">
    </head>

    <body class="nav-md">
        <div class="container body">
            <div class="main_container">
                <div class="col-md-3 left_col">
                    <div class="left_col scroll-view">
                        <div class="navbar nav_title" style="border: 0;">
                            <a href="<c:url value='/controller/administration'/>" class="site_title"><i class="fa fa-paw"></i> <span>openSem Server</span></a>
                        </div>

                        <div class="clearfix"></div>



                        <br />

                        <!-- sidebar menu -->
                        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
                            <div class="menu_section">
                                
                            </div>
                        </div>
                        <!-- /sidebar menu -->

                        <!-- /menu footer buttons -->
                        <div class="sidebar-footer hidden-small">
                        </div>
                        <!-- /menu footer buttons -->
                    </div>
                </div>

                <!-- top navigation -->
                <div class="top_nav">
                    <div class="nav_menu">
                        <nav>
                            <div class="nav toggle">
                                <a id="menu_toggle"><i class="fa fa-bars"></i></a>
                            </div>

                            <ul class="nav navbar-nav navbar-right">
                                <li class="">
                                    <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                                        Login necessario
                                        <span class=" fa fa-angle-down"></span>
                                    </a>
                                     
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
                <!-- /top navigation -->

                <!-- page content -->
                <div class="right_col" role="main">
                    <div class="">


                        <div class="clearfix"></div>

                        
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                            <h2>Inserisci password</h2>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <p class="text-muted font-13 m-b-30">
                                            <b style="color:red"><form:errors path="*"/></b>
                                            <c:url var="addAction" value="/controller/administration/doLogin"></c:url>
                                            <form:form action="${addAction}" commandName="SemServerLogin" class="form-horizontal form-label-left" method="POST">
                                            <p class="text" ><font color="red"><b>
                                                        <form:errors path=""/>
                                                        ${SemServerLogin.msg}
                                                        
                                                    </b></font>
                                                        <font color="green"><b>
                                                        
                                                        <br>La password di default &egrave; "administrator"
                                                    </b></font>
                                            </p>



                                            
                                            <div class="form-group">
                                                <form:label path="pwd" class="control-label col-md-3 col-sm-3 col-xs-12">
                                                    Inserire password
                                                </form:label>
                                                <div class="col-md-6 col-sm-6 col-xs-12">
                                                    
                                                    <form:password path="pwd" class="form-control col-md-7 col-xs-12"/>
                                                    <form:errors path="pwd" />
                                                </div>
                                                
                                            </div>

                                            
                                                <input type="submit" value="Login" class="btn btn-success"/> <br><br>

                                            
                                        </form:form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    
                </div>
                <!-- /page content -->

                <!-- footer content -->
                <footer>
                    <div class="pull-right">
                        TheSemProject openSem Server
                    </div>
                    <div class="clearfix"></div>
                </footer>
                <!-- /footer content -->
            </div>
        </div>

        <!-- jQuery -->
        <script src="<c:url value='/vendors/jquery/dist/jquery.min.js'/>"></script>
        <!-- Bootstrap -->
        <script src="<c:url value='/vendors/bootstrap/dist/js/bootstrap.min.js'/>"></script>
        <!-- FastClick -->
        <script src="<c:url value='/vendors/fastclick/lib/fastclick.js'/>"></script>
        <!-- NProgress -->
        <script src="<c:url value='/vendors/nprogress/nprogress.js'/>"></script>
        <!-- iCheck -->
        <script src="<c:url value='/vendors/iCheck/icheck.min.js'/>"></script>
        <!-- Datatables -->
        <script src="<c:url value='/vendors/datatables.net/js/jquery.dataTables.min.js'/>"></script>
        <script src="<c:url value='/vendors/datatables.net-bs/js/dataTables.bootstrap.min.js'/>"></script>
        <script src="<c:url value='/vendors/datatables.net-buttons/js/dataTables.buttons.min.js'/>"></script>
        <script src="<c:url value='/vendors/datatables.net-buttons-bs/js/buttons.bootstrap.min.js'/>"></script>
        <script src="<c:url value='/vendors/datatables.net-buttons/js/buttons.flash.min.js'/>"></script>
        <script src="<c:url value='/vendors/datatables.net-buttons/js/buttons.html5.min.js'/>"></script>
        <script src="<c:url value='/vendors/datatables.net-buttons/js/buttons.print.min.js'/>"></script>
        <script src="<c:url value='/vendors/datatables.net-fixedheader/js/dataTables.fixedHeader.min.js'/>"></script>
        <script src="<c:url value='/vendors/datatables.net-keytable/js/dataTables.keyTable.min.js'/>"></script>
        <script src="<c:url value='/vendors/datatables.net-responsive/js/dataTables.responsive.min.js'/>"></script>
        <script src="<c:url value='/vendors/datatables.net-responsive-bs/js/responsive.bootstrap.js'/>"></script>
        <script src="<c:url value='/vendors/datatables.net-scroller/js/datatables.scroller.min.js'/>"></script>
        <script src="<c:url value='/vendors/jszip/dist/jszip.min.js'/>"></script>
        <script src="<c:url value='/vendors/pdfmake/pdfmake.min.js'/>"></script>
        <script src="<c:url value='/vendors/pdfmake/vfs_fonts.js'/>"></script>

        <!-- Custom Theme Scripts -->
        <script src="../js/custom.min.js"></script>

        <!-- Datatables -->
        <script>
            $(document).ready(function () {
                var handleDataTableButtons = function () {
                    if ($("#datatable-buttons").length) {
                        $("#datatable-buttons").DataTable({
                            dom: "Bfrtip",
                            buttons: [
                                {
                                    extend: "copy",
                                    className: "btn-sm"
                                },
                                {
                                    extend: "csv",
                                    className: "btn-sm"
                                },
                                {
                                    extend: "excel",
                                    className: "btn-sm"
                                },
                                {
                                    extend: "pdfHtml5",
                                    className: "btn-sm"
                                },
                                {
                                    extend: "print",
                                    className: "btn-sm"
                                },
                            ],
                            responsive: true
                        });
                    }
                };

                TableManageButtons = function () {
                    "use strict";
                    return {
                        init: function () {
                            handleDataTableButtons();
                        }
                    };
                }();

                $('#datatable').dataTable();

                $('#datatable-keytable').DataTable({
                    keys: true
                });

                $('#datatable-responsive').DataTable();

                $('#datatable-scroller').DataTable({
                    ajax: "js/datatables/json/scroller-demo.json",
                    deferRender: true,
                    scrollY: 380,
                    scrollCollapse: true,
                    scroller: true
                });

                $('#datatable-fixed-header').DataTable({
                    fixedHeader: true
                });

                var $datatable = $('#datatable-checkbox');

                $datatable.dataTable({
                    'order': [[1, 'asc']],
                    'columnDefs': [
                        {orderable: false, targets: [0]}
                    ]
                });
                $datatable.on('draw.dt', function () {
                    $('input').iCheck({
                        checkboxClass: 'icheckbox_flat-green'
                    });
                });

                TableManageButtons.init();
            });
        </script>
        <!-- /Datatables -->
    </body>
</html>






