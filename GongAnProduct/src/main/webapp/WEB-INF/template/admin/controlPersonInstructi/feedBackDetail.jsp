<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/ruiec-tags" prefix="ruiec" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  	<title><ruiec:title/></title>
    <link rel="stylesheet" href="${base }/resources/admin/css/layer.css" >
    <link rel="stylesheet" href="${base }/resources/admin/css/common.css">
    <link rel="stylesheet" href="${base }/resources/admin/css/style.css">
    <link rel="stylesheet" href="${base }/resources/admin/css/index.css">
    <link rel="stylesheet" href="${base }/resources/admin/css/quote.css">
</head>
<body class="bodyCss">
        <!--头部-->
        <jsp:include page="../common/header.jsp"></jsp:include> 
        <!---->
        <input type="hidden" name="id" value="${id }" id="datailId" />
        <section>
        	<jsp:include page="../common/leftNav2.jsp"></jsp:include> 
            <div class="mainRight">
                <div class="pancl_pd">
                    <div class="new-people-box early_warning_box">
                        <form>
                            <div class="tit_menu_list">
                                <a href="/admin/common/main.shtml" class="m_menu_item">
                                    <i class="iconfont"></i>
                                    <em>首页</em>
                                </a>
                                <i class="fl split_gt">&gt;</i>
	                            <a href="/admin/controlPersonAlarm/list.shtml" class="m_menu_item">
	                                <em>重点人预警</em>
	                            </a>
	                            <i class="fl split_gt">&gt;</i>
	                            <a href="/admin/controlPersonInstructi/receivedInstruction.shtml" class="m_menu_item">
	                                <em>待签收预警</em>
	                            </a>
                                <i class="fl split_gt">&gt;</i>
                                <a href="javascript:;" class="m_menu_item refresh_html">
                                    <em>反馈详情</em>
                                </a>
                            </div>
                        </form>
                    </div>
                    <div class="during"> 
                         <!-- <div class="during_dl">
                         	<div class="feedback">
	                         	<span class="fl feed_fl"></span>
	                         	<span class="fl feedback_fl"><a href="javascript:void(0)" class="#icon-xiala">再次反馈<i class="iconfont">&#xe618</i></a></span>
	                         </div>
	                         <div class="feekback_dl">
	                         	<dl>
	                         		<dt>目标状态:</dt>
	                         		<dd>未发现</dd>
	                         	</dl>
	                         	<dl>
	                         		<dt>温控状态:</dt>
	                         		<dd>稳控</dd>
	                         	</dl>
	                         	<dl>
	                         		<dt>反馈内容:</dt>
	                         		<dd>
	                         			<span>反馈内容我也不知道，你们自己填写真实信息就oK.反馈内容我也不知道，你们自己填写真实信息就oK.反
											馈内容我也不知道，你们自己填写真实信息就oK.馈内容我也不知道，你们自己填写真实信息就oK.馈内容
											我也不知道，你们自己填写真实信息就oK.</span>
	                         		</dd>
	                         	</dl>
	                         </div>
                         </div>
                         <div class="during_dl">
                         	<div class="feedback">
	                         	<span class="fl feed_fl"></span>
	                         	<span class="fl feedback_fl"><a href="javascript:void(0)" class="#icon-xiala">三次反馈<i class="iconfont">&#xe618</i></a></span>
	                         </div>
                         </div>
                     -->
                    </div>
                </div>
            </div>
        </section>
    <!--js-->
    <script src="${base }/resources/admin/js/jquery-1.9.1.min.js"></script> 	
    <!--自写-->
    <script src="${base }/resources/admin/js/layout.js"></script> 
    <!---下拉框 -->
    <script src="${base }/resources/admin/js/common.js"></script> 
    <script type="text/javascript">
		$(function(){
			var _url = "/admin/controlPersonInstructi/feedBackDetail.shtml";
			var datailId = $("#datailId").val();
			FeedbackDetail(_url,datailId)	
		})
	</script>
</body>
</html>