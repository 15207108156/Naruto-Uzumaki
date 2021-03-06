<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<style type="text/css">
	body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
	</style>
	<script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=${key}"></script>
	<title>地图展示</title>
</head>
<body>
	<div id="allmap"></div>
</body>
	<script type="text/javascript">
	var lng = 114.038174;
	var lat = 22.669265;
		// 百度地图API功能
		var map = new BMap.Map("allmap");    // 创建Map实例
		map.centerAndZoom(new BMap.Point(lng, lat), 20);  // 初始化地图,设置中心点坐标和地图级别
		//添加地图类型控件
		map.addControl(new BMap.MapTypeControl({
			mapTypes:[
	            BMAP_NORMAL_MAP,
	            BMAP_HYBRID_MAP
	        ]}));	  
		map.setCurrentCity("深圳");          // 设置地图显示的城市 此项是必须设置的
		map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
		
		var removeMarker = function(e,ee,marker){
			map.removeOverlay(marker);
		}

		var pt = new BMap.Point(lng, lat);
		var myIcon = new BMap.Icon("/admin/static/image/img1.png", new BMap.Size(40,40));
		var marker = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
		map.addOverlay(marker); // 将标注添加到地图中
		 
	</script>
</html>