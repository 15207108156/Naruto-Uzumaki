<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>小易运维</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="icon" href="/WebBackAPI/admin/static/image/logo.png" type="image/x-icon"/>
    <link href="/WebBackAPI/admin/static/css/site.css" rel="stylesheet">
</head>

<body class="hold-transition sidebar-mini">
    <div class="wrapper">
        <nav class="main-header navbar navbar-expand bg-white navbar-light border-bottom" id="topSidebar"></nav>
        <aside class="main-sidebar sidebar-dark-primary elevation-4" id="leftSidebar" data-selectindex="10"></aside>

        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper pt-3">

            <!-- Main content -->
            <section class="content">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="card card-secondary">
                                <div class="card-header">
                            		<h3 class="card-title">
                                        <div class="row">
											<c:if test="${btn413 == 1}">
												<div class="col-lg-4 btncaozuo">
													<button class="btn btn-primary btn-sm mr-2" id="applyStaff">加入申请</button>
												</div>
											</c:if>
										</div>
                                    </h3>
                                </div>

                                <div class="card-body table-responsive p-0">
                                    <table id="tableList"></table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /.row -->

                </div>
                <!-- /.container-fluid -->
            </section>
            <!-- /.content -->
        </div>


        <!-- Control Sidebar -->
        <aside class="control-sidebar control-sidebar-dark">
            <!-- Control sidebar content goes here -->
        </aside>
        <!-- /.control-sidebar -->
    </div>
    <script src="/WebBackAPI/admin/static/plugins/jquery/jquery.min.js"></script>
    <script src="/WebBackAPI/admin/static/js/layout.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/jQueryUI/jquery-ui.min.js"></script>
    <script>
        $.widget.bridge('uibutton', $.ui.button)
    </script>
    <script src="/WebBackAPI/admin/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="/WebBackAPI/admin/static/js/raphael-min.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/morris/morris.min.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/sparkline/jquery.sparkline.min.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/knob/jquery.knob.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/moment/moment.min.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/daterangepicker/daterangepicker.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/datepicker/bootstrap-datepicker.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/slimScroll/jquery.slimscroll.min.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/fastclick/fastclick.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/js/pages/dashboard.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/js/demo.js"></script>
    <script src="/WebBackAPI/admin/static/js/echarts.min.js"></script>
    <script src="/WebBackAPI/admin/static/js/jqBootstrapValidation-1.3.7.min.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/layui/layui.all.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/jQuery-contextMenu/jquery.contextMenu.min.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/bootstrap-table/bootstrap-table.min.js"></script>
    <script src="/WebBackAPI/admin/static/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="/WebBackAPI/admin/static/js/jq-ext.js"></script>
    <script>
        $(function () {
            var tableObj = {
                obj: null,
                init: function () {
                    var self = this;
                    self.tableList();
                    self.search();
                },
                search: function () {
                    var self = this;
                    $("#btnSearch").unbind().on("click", function () {
                        $.reload(self.obj);
                    });
                },
                tableList: function () {
                    var self = this;
                    self.obj = $.tableObject({
                        tableId: 'tableList',
                        tableOption: {
                            url: '/WebBackAPI/admin/staff/list',
                            page: true,
                            height: $(window).height() - 150,
                            where: {
                                selectType: function () {
                                    return $("#selectType").val()
                                },
                                keyword: function () {
                                    return $("#keyword").val()
                                }
                            },
                            cols: [
                                [
                     				{field: 'id', title: 'id', hide:true},
                                    { field: 'staffno', title: '编号', sort: true },
                                    { field: 'nickname', title: '昵称', sort: true },
                                    { field: 'name', title: '姓名', sort: true },
                                    { field: 'position', title: '岗位', sort: true },
                                    { field: 'power', title: '权级', sort: true },
                                    { field: 'statusFlag', title: '状态', sort: true },
                                    { field: 'loginchange', title: '最近登录', sort: true },
                                ]
                            ]
                        },
                        menuItem: {

    						<c:if test="${btn411 == 1}">
                            item2: {
                                name: "变更", callback: function (key, opt) {
                               		layer.open({
                                        type: 2,
                                        area: [$(document).width() + 'px', '236px'],
                                        offset: 'b',
                                        title: "变更",
                                        resize: true,
                                        anim: 1,
                                        content: "/WebBackAPI/admin/staff/editStaff?id="+$(this).find("td").eq(0).attr('title'),
                                        maxmin: false,
                                        shadeClose: true,
                                        cancel: function (index, layero) {
                                            layer.close(index);
                                        }
                                    });
                                }
                            },
                            </c:if>
    						<c:if test="${btn412 == 1}">
                            item3: {
                                name: "删除", callback: function (key, opt) {
                                	var staffId = $(this).find("td").eq(0).attr('title');
                                	var name = $(this).find("td").eq(3).attr('title') == undefined ? "" : $(this).find("td").eq(3).attr('title');
                                	layer.open({
                                        title: '删除确认框',
                                        content: "确定删除"+name+"吗?",
                                        btn: ['是', '否'],
                                        yes: function (index) {
                                        	$.ajax({  
                                                type : "POST",  //提交方式  
                                                url : "/WebBackAPI/admin/staff/delStaff",//路径  
                                                data : {staffId: staffId},//数据，这里使用的是Json格式进行传输  
                                                dataType:"json",
                                                success : function(result) {//返回数据根据结果进行相应的处理 
                                                	layer.close(index);
                                                	$.reload(self.obj);
                                                }
                                            });
                                        }
                                    });
                                }
                            },
                            </c:if>

                        }
                    });
                }
            }
            tableObj.init();
            $("#applyStaff").on("click", function () {
            	var obj = $("#button1");
                obj.removeClass("hide");
                layer.open({
                    type: 1,
                    area: ['100%', '500px'],
                    offset: 'b',
                    title: '加入申请',
                    resize: true,
                    anim: 1,
                    shadeClose : true,
                    content: obj,
                    cancel: function (index, layero) {
                        $(".contextMenuDialog").addClass("hide");
    	            },
    	        	end: function () {
                        $(".contextMenuDialog").addClass("hide");
    	        	}
                });
                applyStaffTableInit(0);
            });
            function applyStaffTableInit(checkFlag){
				var applyStaffTable = {
    	            obj: null,
    	            init: function () {
    	                var self = this;
    	                self.tableList();
    	                self.search();
    	            },
                    search: function () {
                        var self = this;
                        $("#btnSearch").unbind().on("click", function () {
                            $.reload(self.obj);
                        });
                    },
                    tableList: function () {
                        var self = this;
                        self.obj = $.tableObject({
                            tableId: 'applyStaffTable',
                            tableOption: {
                                url: '/WebBackAPI/admin/staff/staffApply/list?checkFlag='+checkFlag,
                                page: true,
                                height: 370,
                                where: {
                                    selectType: function () {
                                        return $("#selectType").val()
                                    },
                                    keyword: function () {
                                        return $("#keyword").val()
                                    }
                                },
                                cols: [
                                    [
                         				{field: 'id', title: 'id', hide:true},
                                        {field: 'applyTime', title: '申请时间', sort: true},
                                        {field: 'name', title: '姓名', sort: true},
                                        {field: 'tel', title: '手机号', sort: true},
                                        {field: 'nickname', title: '微信昵称', sort: true},
                                        {field: 'checkFlag', title: '审核状态', sort: true},
                                        {field: 'checkName', title: '审核人', sort: true},
                                        {field: 'checkTime', title: '审核时间', sort: true},
                                        {field: 'checkContent', title: '审核意见', edit : "text"},
                                        {fixed: 'right', title: '操作', align:'center', toolbar: '#staffApplyBar'}
                                    ]
                                ]
                            }
                        });
                    }
                }
				applyStaffTable.init();
				layui.table.on('tool(applyStaffTable)', function(obj) {
		   			var data = obj.data;
		   			var layEvent = obj.event;
		   			var tr = obj.tr;
		   			var content = tr.find("td").eq(8).find("div").text();
		   			
		   			
		   			var selectStatusFlag = $("#selectStatusFlag").val();
		   			var selectPosition = $("#selectPosition").val();
		   			var selectPower = $("#selectPower").val();
		   	
		   			// 判断意见是否填写
		   			if (content != '') {
		   				var check = 0;
		   				if (layEvent == 'yes') {
		   					layer.msg("通过审核");
		   					check = 1;
		   				} else if (layEvent == 'no') {
		   					layer.msg("无效人员");
		   					check = 2;
		   				}
		   				$.ajax({
		   					type : "POST", //提交方式  
		   					url : "/WebBackAPI/admin/staff/checkStaffApply",//路径  
		   					data : {
		   						id : data.id,
		   						checkFlag : check,
		   						content : content,
		   						selectStatusFlag : selectStatusFlag,
		   						selectPosition : selectPosition,
		   						selectPower : selectPower
		   					},//数据，这里使用的是Json格式进行传输  
		   					dataType : "json",
		   					success : function(result) {
		   						//返回数据根据结果进行相应的处理  
		   					}
		   				});
		   				$.reload(applyStaffTable.obj);
		   			} else {
		   				layer.msg("请填写意见后操作!");
		   			}
		   		});
            }
    		$("#selectType").on("change", function () {
    			applyStaffTableInit($("#selectType").val());
            });
        });
    </script>
    
<script type="text/html" id="staffApplyBar">
  <!-- 这里同样支持 laytpl 语法，如： -->
  {{# if(d.checkFlag == '待核'){ }}
    <a class="layui-btn layui-btn-xs" lay-event="yes" style="color: #fff;" id="yes">通过</a>
    <a class="layui-btn layui-btn-xs" lay-event="no" style="color: #fff;" id="no">无效</a>
  {{# } }}
</script>
<div class="contextMenuDialog hide" id="button1">
    <div class="card-body">
		<div class="card-tools" style="padding-bottom: 10px;">
			<div class="input-group input-group-sm" style="width: 50%">
				审核状态: <select class="form-control float-right" id="selectType">
					<option value="0">待核</option>
					<option value="1">通过</option>
					<option value="2">无效</option>
				</select>
				状态: <select class="form-control float-right" id="selectStatusFlag">
					<option value="正常">正常</option>
					<option value="冻结">冻结</option>
					<option value="异常">异常</option>
				</select>
				职务: <select class="form-control float-right" id="selectPosition">
					<option value="经理">经理</option>
					<option value="职员">职员</option>
					<option value="临时">临时</option>
				</select>
				权限: <select class="form-control float-right" id="selectPower">
					<option value="1">一级</option>
					<option value="2">二级</option>
					<option value="3">三级</option>
					<option value="4">四级</option>
					<option value="5">五级</option>
					<option value="6">六级</option>
					<option value="7">七级</option>
				</select>
			</div>
		</div>
		<div class="row">
			<table id="applyStaffTable"></table>
		</div>
	</div>
</div>
</body>
</html>