<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
	<c:when test="${page.pageCount > 1 }">
		<%-- <span class="fr search_page" >
			共${page.pageCount }页
		   	到<input type="text" class="search_num" id="inputPageNumber" value="${page.pageNumber }" />页
		    <a id="pageTo" class="search_submit">确定</a>
		</span> --%>
		<span class="page_all">
	    	<a class="${page.pageNumber > 1 ? 'pageable':'nullity'} firstPage">首页</a>
	    	<%-- <c:if test="${page.pageNumber > 1 }">
	    		<a class="pageable prev" val="${page.pageNumber-1 }">＜</a>
	    	</c:if> --%>
	    	<a class="pageable prev" val="${page.pageNumber - 1 }">＜</a>
	    	<span>${page.pageNumber }</span>
			<%-- <c:if test="${page.pageNumber-1 > 4 }">
				<a>...</a>
			</c:if> --%>
			<%-- <c:forEach var="num" begin="${page.pageNumber-2 < 1 ? 1 : page.pageNumber-2 }" step="1" end="${page.pageNumber+2 > page.pageCount ? page.pageCount : page.pageNumber+2 }">
				<c:choose>
					<c:when test="${num eq page.pageNumber }">
						<a class="nullity">${num }</a>
					</c:when>
					<c:otherwise>
						<a class="pageable" val="${num }">${num }</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>  --%>
			<%-- <c:if test="${page.pageNumber+4 < page.pageCount }">
				<a>...</a>
			</c:if>  --%>
			<a class="pageable next" val="${page.pageNumber + 1 }">＞</a>
			<%-- <c:if test="${page.pageCount gt page.pageNumber }">
				<a class="pageable next" val="${page.pageNumber+1 }">＞</a>
			</c:if> --%>
			<a class="${page.pageNumber ne page.pageCount ? 'pageable':'nullity'} lastPage" val="${page.pageCount }" style="border-right: 0px;">尾页</a>
		</span>
	</c:when>
	<%-- <c:when test="${page.totalCount < 1}">
		<div style="margin: auto; text-align:center; width:100%; display:block;">暂无记录</div>
	</c:when> --%>
</c:choose>
