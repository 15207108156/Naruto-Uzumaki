<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>后台管理系统</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="multipart/form-data; charset=utf-8" />
    <link href="/WebBackAPI/admin/css/jquery.lineProgressbar.css" rel="stylesheet">
    <link href="/WebBackAPI/admin/static/css/site.css" rel="stylesheet">
    <link href="/WebBackAPI/admin/static/plugins/webuploader/webuploader.css" rel="stylesheet">
</head>

<body class="hold-transition sidebar-mini">
<div class="wrapper">
    <nav class="main-header navbar navbar-expand bg-white navbar-light border-bottom" id="topSidebar"></nav>
    <aside class="main-sidebar sidebar-dark-primary elevation-4" id="leftSidebar" data-selectindex="4"></aside>

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
                                        <div class="col-lg-4 btncaozuo">
                                            <button class="btn btn-primary btn-sm" id="venueSave">认领场馆</button>
                                            <button class="btn btn-primary btn-sm" id="venueImport">导入场馆</button>
                                            <button class="btn btn-primary btn-sm" id="venueLog">场馆日志</button>
                                            <button class="btn btn-primary btn-sm" id="venueCheck">待审核场地(机构添加)</button>
                                        </div>
                                    </div>
                                </h3>
                            </div>
                            <div class="card-body table-responsive p-0">
                                <table id="tableList">
                                </table>
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

<!-- jQuery -->
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
    <script src="/WebBackAPI/admin/static/plugins/webuploader/webuploader.js"></script>
    <script src="/WebBackAPI/admin/static/js/jquery.lineProgressbar.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=ITcG0S4URK9aokGSOhTNnSXCO9o7fK8D"></script>
<script>
	var map=null;
	var mapmarker=null;
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
                $("#search").unbind().on("click", function () {
                    $.reload(self.obj);
                });
            },
            tableList: function () {
                var self = this;
                self.obj = $.tableObject({
                    tableId: 'tableList',
                    tableOption: {
                        url: '/WebBackAPI/admin/venue/list',
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
                                {field: 'city', title: '城市', sort: true},
                                {field: 'district', title: '区县', sort: true},
                                {field: 'name', title: '场馆', sort: true},
                                {field: 'type', title: '类型', sort: true},
                                {field: 'trainteam', title: '机构入驻', sort: true},
                                {field: 'lnglat', title: '坐标', sort: true},
                                {field: 'tel', title: '电话', sort: true},
                                {field: 'venueError', title: '报错', sort: true},
                                {field: 'venuelogSum', title: '日志', sort: true},
                                {field: 'showflag', title: '状态', sort: true},
                 				{field: 'lng', title: 'lng', hide:true},
                 				{field: 'lat', title: 'lat', hide:true},
                            ]
                        ]
                    },
                    menuItem: {
                        item1: {
                            name: "编辑", callback: function (key, opt) {
                            	$.showAjaxContent("编辑", "35%", "/WebBackAPI/admin/venue/update/view", $(this).find("td").eq(0).attr('title'));
                            	var mapFlag = $(this).find("td").eq(6).attr('title');
                            	var lng = $(this).find("td").eq(11).attr('title');
                            	var lat = $(this).find("td").eq(12).attr('title');
                            	$("#citymapDiv").removeClass("hide");
                            	layer.open({
                             	  title: "位置", 
                				  type: 1,
                				  anim: 1,
                				  shade: 0.3,
                				  closeBtn: 0,
                				  offset: '20px',
                				  title: false,
                				  move: false,
                	              shadeClose : true,
                				  resize: false,
                				  area: ['30%', '50%'],
                				  content: $('#citymap'),
                                  cancel: function (index, layero) {
                                	  layer.closeAll();
                                      $(".contextMenuDialog").addClass("hide");
                          			  $.reload(self.obj);
                                  },
                                  end: function () {
                                	  layer.closeAll();
                                      $(".contextMenuDialog").addClass("hide");
                          			  $.reload(self.obj);
                                  },
                				  success: function(){
                					    map = new BMap.Map("citymap");    // 创建Map实例
	                					map.centerAndZoom(new BMap.Point(114.056386, 22.592976), 15);  // 初始化地图,设置中心点坐标和地图级别
                						//添加地图类型控件
                						map.addControl(new BMap.MapTypeControl({
                							mapTypes:[
                						        BMAP_NORMAL_MAP,
                						        BMAP_HYBRID_MAP
                						    ]}));	  
                						map.setCurrentCity("深圳");          // 设置地图显示的城市 此项是必须设置的
                						map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
                						
                						if(mapmarker != null){
                	   				        map.removeOverlay(mapmarker);
                	   					}
                						if(mapFlag == "是"){
	               	   			        	var point = new BMap.Point(lng, lat);
	               	   			      		map.centerAndZoom(point, 16);
	               	   			      		var marker = new BMap.Marker(point);// 创建标注
	               	   			      		mapmarker = marker;
	               	   			      		map.addOverlay(marker);// 将标注添加到地图中
                                     	} else {
                	   			        	layer.msg("暂无地址信息");
                                     	}
                				   }
                				});
                            }
                        },
                        item2: {
                            name: "日志", callback: function (key, opt) {
                                $.showContentMenu(key, opt)
                                $.tableObject({
                                    tableId: 'tablevenuelog',
                                    tableOption: {
                                        url: '/WebBackAPI/admin/venue/venueloglist?venueid='+$(this).find("td").eq(0).attr('title'),
                                        page: false,
                                        height: $("#tablevenuelog").parents(".layui-layer-content").height() - 30,
                                        where: {},
                                        cols: [
                                            [
                                                { field: 'createtime', title: '时间', sort: true },
                                                { field: 'manager', title: '操作人', sort: true },
                                                { field: 'content', title: '内容', sort: true },
                                            ]
                                        ]
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }
        tableObj.init();

        $("#venueSave").on("click", function () {
            var obj = $("#button1");
            obj.removeClass("hide");
            layer.open({
                type: 1,
                area: ['100%', '50%'],
                offset: 'b',
                title: '认领入驻',
                resize: true,
                anim: 1,
                shadeClose : true,
                content: obj,
                cancel: function (index, layero) {
                    $(".contextMenuDialog").addClass("hide");
        			$.reload(tableObj.obj);
                },
                end: function () {
                    $(".contextMenuDialog").addClass("hide");
        			$.reload(tableObj.obj);
	        	}
            });
			venueEnterTableInit(0);
        });
        

        function venueEnterTableInit(checkFlag){
        	var tableVenueEnter = {
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
                            tableId: 'tableAllVenueEnter',
                            tableOption: {
                                url: '/WebBackAPI/admin/venue/venueEnter/list?checkFlag='+checkFlag,
                                page: true,
                                height: 410,
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
    	                                {field: 'createTime', title: '申请时间', sort: true},
    	                                {field: 'appnickname', title: '操作人', sort: true},
    	                                {field: 'phone', title: '绑定', sort: true},
    	                                {field: 'title', title: '场馆名', sort: true},
    	                                {field: 'address', title: '城市', sort: true},
    	                                {field: 'mainName', title: '负责人', sort: true},
    	                                {field: 'mainPhone', title: '负责人电话', sort: true},
    	                                {field: 'ballType', title: '类别', sort: true},
    	                                {field: 'checkFlag', title: '审核状态', sort: true},
    	                                {field: 'rname', title: '审核人', sort: true},
    	                                {field: 'checkTime', title: '审核时间', sort: true},
    	                                {field: 'content', title: '意见', edit : "text"},
    	                                {fixed: 'right', title: '操作', align:'center', toolbar: '#venueEnterBar'}
                                    ]
                                ]
                            }
                        });
                    }
                }
        		tableVenueEnter.init();
                layui.table.on('tool(tableAllVenueEnter)', function(obj) {
    	   			var data = obj.data;
    	   			var layEvent = obj.event;
    	   			var tr = obj.tr;
    	   			var content = tr.find("td").eq(12).find("div").text();
    	   		   	
    	   			// 判断意见是否填写
    	   			if (content != '') {
    	   				var check = 0;
           				if (layEvent == 'yes') {
           					layer.msg("通过审核");
           					check = 1;
           				} else if (layEvent == 'no') {
           					layer.msg("无效场馆");
           					check = 2;
           				}
           				$.ajax({
           					type : "POST", //提交方式  
           					url : "/WebBackAPI/admin/venue/venueEnter/check",//路径  
           					data : {
           						id : data.id,
           						check : check,
    	   						content : content
           					},//数据，这里使用的是Json格式进行传输  
           					dataType : "json",
           					success : function(result) {//返回数据根据结果进行相应的处理  
           	
           					}
           				});
           				$.reload(tableVenueEnter.obj);
    	   			} else {
    	   				layer.msg("请填写意见后操作!");
    	   			}
                });
        	}

		$("#selectVenueEnterType").on("change", function () {
			venueEnterTableInit($("#selectVenueEnterType").val());
        });
        
        
        $("#venueCheck").on("click", function () {
            var obj = $("#button4");
            obj.removeClass("hide");
            layer.open({
                type: 1,
                area: ['100%', '52%'],
                offset: 'b',
                title: '待审核场地',
                resize: true,
                anim: 1,
                shadeClose : true,
                content: obj,
	            cancel: function (index, layero) {
                    $(".contextMenuDialog").addClass("hide");
                    layer.closeAll(); 
	                obj.css("display","none");
	    			$.reload(tableObj.obj);
	            },
	            end: function (index, layero) {
                    $(".contextMenuDialog").addClass("hide");
                    layer.closeAll(); 
	                obj.css("display","none");
	    			$.reload(tableObj.obj);
	            }
            });
            
            var tableLog = {
    	            obj: null,
    	            init: function () {
    	                var self = this;
    	                self.tableList();
    	                self.search();
    	            },
                    search: function () {
                        var self = this;
                        $("#btnSearch4").unbind().on("click", function () {
                            $.reload(self.obj);
                        });
                    },
                    tableList: function () {
                        var self = this;
                        self.obj = $.tableObject({
                            tableId: 'tableVenueCheck',
                            tableOption: {
                                url: '/WebBackAPI/admin/venue/venueCheckList',
                                page: true,
                                height: 410,
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
                         				{field: 'lng', title: 'lng', hide:true},
                         				{field: 'lat', title: 'lat', hide:true},
                                        {field: 'createTime', title: '时间', sort: true},
                                        {field: 'trainTeam', title: '上传机构', sort: true},
                                        {field: 'title', title: '场馆名', sort: true},
                                        {field: 'type', title: '类型', sort: true},
                                        {field: 'content', title: '描述', sort: true},
                                        {fixed: 'right', title: '操作', align:'center', toolbar: '#venueCheckBar'}
                                    ]
                                ]
                            }
                        });
                    }
                }
            	tableLog.init();

            	$("#citymapDiv").removeClass("hide");
            	layer.open({
             	  title: "位置", 
				  type: 1,
				  anim: 1,
				  shade: 0,
				  closeBtn: 0,
				  offset: 't',
				  move: false,
	              shadeClose : true,
				  resize: false,
				  area: ['30%', '45%'],
				  content: $('#citymap'),
				  success: function(){
					    map = new BMap.Map("citymap");    // 创建Map实例
						map.centerAndZoom(new BMap.Point(114.056386,22.592976), 20);  // 初始化地图,设置中心点坐标和地图级别
						//添加地图类型控件
						map.addControl(new BMap.MapTypeControl({
							mapTypes:[
						        BMAP_NORMAL_MAP,
						        BMAP_HYBRID_MAP
						    ]}));	  
						map.setCurrentCity("深圳");          // 设置地图显示的城市 此项是必须设置的
						map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
				   }
				});
            	layui.table.on('tool(tableVenueCheck)', function(obj) {
		   			var data = obj.data;
		   			var layEvent = obj.event;
		   			var tr = obj.tr;
		   			
	   				var check = 0;
	   				if (layEvent == 'yes') {
	   					layer.msg("通过审核");
	   					check = 1;
	   				} else if (layEvent == 'no') {
	   					layer.msg("无效场馆");
	   					check = 2;
	   				}
	   				$.ajax({
	   					type : "POST", //提交方式  
	   					url : "/WebBackAPI/admin/venue/venueCheck",//路径  
	   					data : {
	   						id : data.id,
	   						check : check
	   					},//数据，这里使用的是Json格式进行传输  
	   					dataType : "json",
	   					success : function(result) {//返回数据根据结果进行相应的处理  
	   	
	   					}
	   				});
	   				$.reload(tableLog.obj);
	            });
   				//监听行单击事件
   				layui.table.on('row(tableVenueCheck)', function(obj){
   					if(mapmarker != null){
   				        map.removeOverlay(mapmarker);
   					}
   					
   			        if(obj.data.lng != '' && obj.data.lat != ''){
   			        	var point = new BMap.Point(obj.data.lng, obj.data.lat);
   			      		map.centerAndZoom(point, 16);
   			      		var marker = new BMap.Marker(point);// 创建标注
   			      		mapmarker = marker;
   			      		map.addOverlay(marker);// 将标注添加到地图中
   			        }else{
   			        	layer.msg("暂无地址信息");
   			        }
   				});
        });
        
        $("#venueLog").on("click", function () {
            var obj = $("#button3");
            obj.removeClass("hide");
            layer.open({
                type: 1,
                area: ['100%', '500px'],
                offset: 'b',
                title: '场馆日志',
                resize: true,
                anim: 1,
                shadeClose : true,
                content: obj,
                cancel: function (index, layero) {
                    $(".contextMenuDialog").addClass("hide");
                }
            });

            var tableLog = {
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
                        tableId: 'tableAllLog',
                        tableOption: {
                            url: '/WebBackAPI/admin/venue/venuelogAllList',
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
                                    {field: 'createtime', title: '时间', sort: true},
                                    {field: 'manager', title: '操作人', sort: true},
                                    {field: 'name', title: '场馆', sort: true},
                                    {field: 'venueno', title: '场馆编号', sort: true},
                                    {field: 'content', title: '内容', sort: true},
                                ]
                            ]
                        }
                    });
                }
            }
        	tableLog.init();
        });
        
        
        $("#venueImport").on("click", function () {
            var obj = $("#ExcelUpload");
            obj.css("display","block");
            layer.open({
                type: 1,
                area: ['100%', '500px'],
                offset: 'b',
                title: '导入场馆',
                resize: true,
                anim: 1,
                shadeClose : true,
                maxmin: false,
			    resize: false,
			    shade: true,
                content: obj,
                cancel: function (index, layero) {
                    obj.css("display","none");
                },
                end: function (index, layero) {
                    obj.css("display","none");
                }
            });
        });
		//绑定页面的上传的JS
		var Myuploader = WebUploader.create({
          auto: true,   
          swf: '/WebBackAPI/admin/static/plugins/webuploader/Uploader.swf',
          server: "/WebBackAPI/admin/venue/importExcel", 
          pick: '#picker'
      	});
    		var interval = null;
  			var current = 0;
  			//上传前触发
  			Myuploader.on('uploadStart', function (file){ 
				// 初始化
				$('.proggress').animate({width:'0%'}); 
				$('.percentCount').html('0%'); 
        		$('#datamsg').html("");
  				
  				interval = setInterval(increment,1000);
  			})
  			$('#progressbar1').LineProgressbar({
				percentage: 0,
				fillBackgroundColor: '#1abc9c'
			});
  			function increment(){
  				$.ajax({  
  			        type : "POST",  //提交方式  
  			        url : "/WebBackAPI/admin/common/redisupload?redisname=venue",//路径  
  			        data : {},//数据，这里使用的是Json格式进行传输  
  			        dataType:"json",
  			        success : function(result) {//返回数据根据结果进行相应的处理  
  			            if ( result.code == 200 ) {  
  							if(result.data.current == 100) { 
  								$.ajax({  
  	  	  		  			        type : "POST",  //提交方式  
  	  	  		  			        url : "/WebBackAPI/admin/common/deleteRedisupload?redisname=venue",//路径  
  	  	  		  			        dataType:"json",
  	  	  		  			        success : function(result) {}
  	  	  		  			    });
  								clearInterval(interval); 
  							}
  							$('.proggress').animate({width:result.data.current+'%'}); 
  							$('.percentCount').html(result.data.current+'%'); 
  							$('#dataPage').html(result.data.page); 
  			            } else {
  	  			        	$.ajax({  
  	  		  			        type : "POST",  //提交方式  
  	  		  			        url : "/WebBackAPI/admin/common/deleteRedisupload?redisname=venue",//路径  
  	  		  			        dataType:"json",
  	  		  			        success : function(result) {}
  	  		  			    }); 
  			            	layer.msg("上传失败");
  							$('.proggress').animate({width:'100%'}); 
  							$('.percentCount').html('100%'); 
  			            }  
  			        },
  			        error: function(){
  			        	$.ajax({  
  		  			        type : "POST",  //提交方式  
  		  			        url : "/WebBackAPI/admin/common/deleteRedisupload?redisname=venue",//路径  
  		  			        dataType:"json",
  		  			        success : function(result) {}
  		  			    }); 
  			        	clearInterval(interval);
  			        }
  			    }); 
  			}
    		  //上传成功
    		  Myuploader.on('uploadSuccess', function (file, data){ 
    			if( file != "" && file != null && file != undefined ){
                   var _format =file.ext; 
                   var _name =file.name; 
    	                if( _format == "xls" || _format == "xlsx" || _format == "xlt" || _format == "xlsm" || _format == "XLS" || _format == "XLSX" || _format == "XLT" || _format == "XLSM"  ){
    	                	if(data.code == 200){
    	                		var msg = "</br></br>";
    	                		msg += "导入总数量:" + data.data.count + "</br>";
    	                		msg += "导入成功数量:" + data.data.succeed + "</br>";
    	                		msg += "导入失败数量:" + data.data.error + "</br>";
    	                		msg += "导入成功日志:</br>" + data.data.succeedlist;
    	                		msg += "导入失败日志:</br>" + data.data.errorlist;
    	                		
    	                		$('#datamsg').html(msg);
    		                	var index = layer.alert(data.msg, {
    	    						skin: 'layui-layer-molv', //样式类名
    	    						closeBtn: 0,
    	    						anim: 4, //动画类型
    	    						yes:function(){
    	                                layer.close(index); 
    								}
    	    					});
                            }else{
                            	layer.alert(data.msg, {
                                    skin: 'layui-layer-molv', //样式类名
                                    closeBtn: 0,
                                    anim: 4 //动画类型
                                });
                                layer.closeAll('loading'); 
                                return;
                            }
    	              	}else{
    	              		layer.alert("请上传xls,xlsx,xlt,xlsm格式的文件！", {
    							skin: 'layui-layer-molv', //样式类名
    							closeBtn: 0,
    							anim: 4 //动画类型
    						});
    		              	layer.closeAll('loading'); 
    	              		return;
    	              	};  
                   }; 
    		  });
		//文件上传失败，显示上传出错。
        Myuploader.on('uploadError', function (file) {
            console.log(file, "error", arguments);
            if (typeof com != "undefined") {
                layer.msg("文件上传失败");
            }
        }); 
        /* var obj = $("#ExcelUpload");
        obj.css("visibility","hidden"); */
    });
</script>

<div id="citymapDiv" class="contextMenuDialog otherDialog hide">
    <div class="row">
        <div class="col-12" id="citymap" style="width:100%;height: 100%;">
        </div>
    </div>
</div>


<script type="text/html" id="venueEnterBar">
  <!-- 这里同样支持 laytpl 语法，如： -->
  {{# if(d.checkFlag == '待核'){ }}
    <a class="layui-btn layui-btn-xs" lay-event="yes" style="color: #fff;" id="yes">通过</a>
    <a class="layui-btn layui-btn-xs" lay-event="no" style="color: #fff;" id="no">无效</a>
  {{# } }}
</script>

<script type="text/html" id="venueCheckBar">
  <!-- 这里同样支持 laytpl 语法，如： -->
    <a class="layui-btn layui-btn-xs" lay-event="yes" style="color: #fff;" id="yes">通过</a>
    <a class="layui-btn layui-btn-xs" lay-event="no" style="color: #fff;" id="no">不通过</a>
</script>

<div class="contextMenuDialog hide" id="button1">
	<div class="card-tools" style="padding-bottom: 10px;">
		<div class="input-group input-group-sm" style="width: 150px;">
			审核状态: <select class="form-control float-right" id="selectVenueEnterType"">
				<option value="0">待核</option>
				<option value="1">通过</option>
				<option value="2">无效</option>
			</select>
		</div>
	</div>
	<div class="row">
		<table id="tableAllVenueEnter"></table>
	</div>
</div>

<div class="contextMenuDialog hide" id="button3">
    <div class="card-body">
			<div class="card-tools" style="padding-bottom:  10px;">
				<div class="input-group input-group-sm" style="width: 350px;">
					<select class="form-control float-right" id="selectType">
						<option value="0">场馆名</option>
						<option value="1">场馆编号</option>
						<option value="2">内容</option>
					</select> 
					<input class="form-control float-right" id="keyword" name="table_search" type="text" placeholder>

					<div class="input-group-append">
						<button class="btn btn-default" id="btnSearch" type="submit">
							<i class="fa fa-search"></i> 搜索
						</button>
					</div>
				</div>
			</div>
			<div class="row">
            <table id="tableAllLog"></table>
        </div>
    </div>
</div>

	<div class="contextMenuDialog hide" id="button4">
		<div class="card card-secondary">
			<div class="card-body table-responsive p-0">
				<table id="tableList">
				</table>
			</div>
		</div>
		<div class="card-body table-responsive p-0">
			<table id="tableVenueCheck">
			</table>
		</div>
	</div>

	<div class="contextMenuDialog hide" id="item1">
    <div class="card-body">
        <div class="row">
            <table id="tablemanager"></table>
        </div>
    </div>
</div>
<div class="contextMenuDialog hide" id="item2">
    <div class="card-body">
        <div class="row">
            <table id="tablevenuelog"></table>
        </div>
    </div>
</div>

<div id="ExcelUpload">
	<div id="uploader" class="wu-example">
    <!--用来存放文件信息-->
	<div id="thelist" class="uploader-list"></div>
	    <div class="btns">
	        <div id="picker">选择文件</div>
	    </div>
	</div>
	<div class="float-left mr-1">
           <a class="form-control btn btn-primary" 
           href="https://ekeae-image.oss-cn-shenzhen.aliyuncs.com/ekeResource%2Fdownload%2F%E5%9C%BA%E9%A6%86%E6%A8%A1%E6%9D%BF.xlsx" download="场馆模板.xlsx">Excel规范模板下载</a>
       </div>
       <br>
       <br>
	<div id="dataPage">0/0</div>
   	<div id="progressbar1"style="width:50%;"></div>
	<div id="uploader_btn"></div>
	<div id="datamsg"></div>
</div>

<div class="contextMenuDialog hide" id="dataContent">
    <div class="card-body">
        <div class="row">
            <div class="input-group col-lg-3">
                <div class="custom-file">
                    <input class="custom-file-input" id="exampleInputFile" type="file">
                    <label class="custom-file-label" for="exampleInputFile">选择Excel文件</label>
                </div>
            </div>
            <div class="col-lg-3">
                <input class="btn btn-primary" type="button" value="开始">
            </div>
            <div class="col-lg-6">
                <div class="callout callout-info clearfix">
                    <h5 class="float-left">
                        <i class="fa fa-info"></i>备注：</h5>
                    <span class="float-left">excel命名规范：表名=sheet1，城市列名=城市，小区列名=小区</span>
                </div>
            </div>
        </div>
        <br>
        <div class="row">
            <div class="col-lg-7">
                <div class="row">
                    <table id="tabledata"></table>
                </div>
            </div>
            <div class="col-lg-5">
                <textarea class="p-md-2" id="progress" name="progress" style="width: 100%;" rows="10"></textarea>
            </div>
        </div>
    </div>
</div>
</body>

</html>