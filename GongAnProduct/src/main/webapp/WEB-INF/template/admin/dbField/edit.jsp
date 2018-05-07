<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="/ruiec-tags" prefix="ruiec" %>
<!DOCTYPE html >
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
        <section>
        	<jsp:include page="../common/leftNav5.jsp"></jsp:include> 
            <div class="mainRight">
                <div class="pancl_pd">
                    <form  action="/admin/dbField/update.shtml" method="POST" id="form1">
                        <div class="tit_menu_list">
                            <a href="/admin/common/main.shtml" class="m_menu_item">
                                <i class="iconfont">&#xe62a;</i>
                                <em>首页</em>
                            </a>
                            <i class="fl split_gt">&gt;</i>
                            <a href="javascript:;" class="m_menu_item refresh_html">
                               <em>数据导入</em>
                            </a>
                            <i class="fl split_gt">&gt;</i>
                            <a href="/admin/db/list.shtml" class="m_menu_item">
                                <em>数据源列表</em>
                            </a>
                            <i class="fl split_gt">&gt;</i>
                            <a href="/admin/dbTable/list.shtml?id=${dbId }" class="m_menu_item">
                                <em>数据导入表列表</em>
                            </a>
                            <i class="fl split_gt">&gt;</i>
                            <a href="/admin/dbField/list.shtml?id=${tid }&dbId=${dbId }" class="m_menu_item">
                                <em>数据导入字段列表</em>
                            </a>
                            <i class="fl split_gt">&gt;</i>
                            <a href="javascript:;" class="m_menu_item refresh_html">
                                <em>编辑数据导入字段</em>
                            </a>
                        </div>
                        <input type="hidden" name="dbId" value="${dbId }" />
                        <input type="hidden" name="dbTable.id" value="${tid }" />
                        <input type="hidden" name="id" value="${dbField.id }" />
                        <div class="mgt20 new-people-box form_box">
                            <dl class="new_peo_list">
                                <dt>对应表名：</dt>
                                <dd>
                                    <div class="fl rule-single-select single-select">
                                        <select class="sel" name="dbShow.id">
                                        <c:forEach var="dbShow" items="${dbShows }">
                                            <option <c:if test="${dbField.dbShow.id==dbShow.id }">selected="selected"</c:if> value="${dbShow.id }"><c:if test="${dbShow.isNeed==1}">*</c:if>${dbShow.name }</option>
                                        </c:forEach>
                                        </select>
                                    </div>
                                </dd>
                            </dl>
                            <dl class="new_peo_list">
                                <dt>导入字段名：</dt>
                                <dd>
                                     <div class="fl left_item">
                                       <input type="text" value="${dbField.fieldName }" class="txt_in" name="fieldName" maxlength="50"/>
                                    </div>
                                </dd>
                            </dl>
                            <dl class="new_peo_list">
                                <dt>字段类型：</dt>
                                <dd>
                                    <div class="fl rule-single-select single-select">
                                        <select class="sel" name="fieldType">
                                            <option value="0">整型</option>
                                            <option <c:if test="${dbField.fieldType==1 }">selected="selected"</c:if> value="1">字符串</option>
                                            <option <c:if test="${dbField.fieldType==2 }">selected="selected"</c:if> value="2">日期</option>
                                        </select>
                                    </div>
                                </dd>
                            </dl>
                            <dl class="new_peo_list">
                                <dt>是否必须：</dt>
                                <dd>
                                    <div class="fl rule-single-select single-select">
                                        <select class="sel" name="isNeed">
                                            <option value="0">否</option>
                                            <option <c:if test="${dbField.isNeed==1 }">selected="selected"</c:if> value="1">是</option>
                                        </select>
                                    </div>
                                </dd>
                            </dl>
                        </div>
                        <div class="state-save sub_btn_box">
                             <input onclick="javascript:history.back(-1);"
					          type="button" value="返回上一页" value="返回上一页" class="back_next" />
                            <input type="button" value="保存" style="color:white;" class="sub_btn"/>
                        </div>
                    </form>
                </div>
            </div>
        </section>
	       <!--js-->
    <script src="${base }/resources/admin/js/jquery-1.9.1.min.js"></script>
    <script src="${base }/resources/admin/js/layer.js"></script>
    <!--表单验证-->
    <script src="${base }/resources/admin/js/jquery.form.js"></script>
    <script src="${base }/resources/admin/js/Validform_v5.3.2/Validform_v5.3.2.js"></script>
    <!--自写-->
    <script src="${base }/resources/admin/js/layout.js"></script>
    <!--拖拽-->
    <script src="${base }/resources/admin/js/gridster/jquery.gridster.min.js"></script>
    <!---下拉框 -->
    <script src="${base }/resources/admin/js/common.js"></script>
    <!--日期-->
    <script src="${base }/resources/admin/js/My97DatePicker/lang/zh-cn.js"></script>
    <script src="${base }/resources/admin/js/My97DatePicker/WdatePicker.js"></script>  
    <script type="text/javascript">
    $('.sub_btn').click(function(){
    	var tid = $('input[name="dbTable.id"]').val();
    	var dbId = $('input[name="dbId"]').val();
	    $.ajax({
			url:'/admin/dbField/update.shtml',
			type:'POST',
			data: $('#form1').serialize(),
			dataType: "json",
			success: function(data) {
				if(data.code==200){
					layer.msg(data.msg, { icon: 1,shade: 0.5});
					setTimeout(function(){
						window.location.href="/admin/dbField/list.shtml?dbId="+dbId+"&id="+tid; 
					}		
					, 1000);
				}else{
					layer.msg(data.msg, { icon: 2,shade: 0.5});
				}
			},
			error : function(data) {  
				layer.msg("数据库修改失败", { icon: 2,shade: 0.5});  
		       } 
		});
	})
    </script>	
</body>
</html>