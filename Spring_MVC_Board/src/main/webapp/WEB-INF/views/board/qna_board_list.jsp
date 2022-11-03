<%@page import="com.itwillbs.mvc_board.vo.PageInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<link href="<%=request.getContextPath() %>/resources/css/top.css" rel="stylesheet" type="text/css">
<style type="text/css">
	#listForm {
		width: 1024px;
		max-height: 610px;
		margin: auto;
	}
	
	h2 {
		text-align: center;
	}
	
	table {
		margin: auto;
		width: 1024px;
		table-layout: fixed;
	}
	
	#tr_top {
		background: orange;
		text-align: center;
	}
	
	table td {
		text-align: center;
		text-overflow: ellipsis;
		overflow: hidden;
		white-space: nowrap;
	}
	
	.td_left {
		text-align: left;
		padding-left: 10px;
	}
	
	#pageList {
		margin: auto;
		width: 1024px;
		text-align: center;
	}
	
	#emptyArea {
		margin: auto;
		width: 1024px;
		text-align: center;
	}
	
	#buttonArea {
		margin: auto;
		width: 1024px;
		text-align: right;
	}
	
	a {
		text-decoration: none;
	}
</style>
<script src="<%=request.getContextPath()%>/resources/js/jquery-3.6.1.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#btn2').click(function(){
			$.ajax({
				url:'BoardListJson.bo',
				dataType:'json',
				success:function(result){
					$.each(result,function(index,items){
						$('#table2').append('<tr id="tr_top"><td width="100px">'+items.board_num+'</td><td>'+items.board_subject+'</td><td width="150px">'+items.board_name+'</td><td width="150px">'+items.board_date+'</td><td width="100px">'+items.board_readcount+'</td></tr>');
					});
				}
			});
		});
	});
</script>

</head>
<body>
	<header>
		<!-- Login, Join 링크 표시 영역(inc/top.jsp 페이지 삽입) -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<!-- 게시판 리스트 -->
	<section id="listForm">
		<input type="button" value="목록" id="btn2">
		<table id="table2">
			<tr id="tr_top">
				<td width="100px">번호</td>
				<td>제목</td>
				<td width="150px">작성자</td>
				<td width="150px">날짜</td>
				<td width="100px">조회수</td>
			</tr>
		</table>
		<h2>게시판 글 목록</h2>
		<table>
			<tr id="tr_top">
				<td width="100px">번호</td>
				<td>제목</td>
				<td width="150px">작성자</td>
				<td width="150px">날짜</td>
				<td width="100px">조회수</td>
			</tr>
			<!-- JSTL 과 EL 활용하여 글목록 표시 -->
			<c:forEach var="board" items="${boardList }">
				<tr>
					<td>${board.board_num }</td>
					<td class="td_left">
						<%-- =============== 답글 관련 목록 처리 추가 ================== --%>
						<%-- board_re_lev 값이 0보다 크면 반복문을 통해 해당 값만큼 공백(&nbsp;) 추가 --%>
						<c:if test="${board.board_re_lev > 0 }">
							<%-- c:forEach 문을 통해 1부터 board_re_lev 값까지 1씩 증가하면서 공백 추가 --%>
							<c:forEach var="i" begin="1" end="${board.board_re_lev }">
								&nbsp;
							</c:forEach>
							<%-- 답글 제목 앞에 이미지 추가 --%>
							<img src="<%=request.getContextPath()%>/resources/images/re.gif">
						</c:if>
						<%-- ============================================================ --%>
						<a href="BoardDetail.bo?board_num=${board.board_num }&pageNum=${pageInfo.pageNum}" title="${board.board_subject }">
							${board.board_subject }</a>
					</td>
					<td title="${board.board_name }">${board.board_name }</td>
					<td>${board.board_date }</td>
					<td>${board.board_readcount }</td>
				</tr>
			</c:forEach>
		</table>
	</section>
	<section id="buttonArea">
	<!--  검색 기능 구현을 위한 form 태그 -->
		<form action="BoardList.bo" method="get">
			<select name="searchType" >
				<option value="subject">제목</option>
				<option value="content">내용</option>
				<option value="subject_content">제목&내용</option>
				<option value="name">작성자</option>
			</select>
			<input type="text" name="keyword" required="required">
			<input type="submit" value="검색">
			<input type="button" value="글쓰기" onclick="location.href='BoardWriteForm.bo'" />
		</form>
	</section>
	<section id="pageList">
		<!-- 현재 페이지번호가 시작 페이지번호보다 클 때 현재 페이지번호 - 1 값으로 페이지 이동 -->
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${pageInfo.pageNum > pageInfo.startPage }"> --%>
<%-- 				<input type="button" value="이전" onclick="location.href='BoardList.bo?pageNum=${pageInfo.pageNum - 1}'"> --%>
<%-- 			</c:when> --%>
<%-- 			<c:otherwise> --%>
<!-- 				<input type="button" value="이전"> -->
<%-- 			</c:otherwise> --%>
<%-- 		</c:choose> --%>
			<%PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo"); %>
			<input type="button" value="이전" <%if(pageInfo.getPageNum() > pageInfo.getStartPage()) {%>onclick="location.href='BoardList.bo?pageNum=${pageInfo.pageNum - 1}'"<%} %>>
			<!-- 시작페이지(startPage) 부터 끝페이지(endPage) 까지 페이지 번호 표시 -->
			&nbsp;
			<c:forEach var="i" begin="${pageInfo.startPage }" end="${pageInfo.endPage }">
				<!-- 현재 페이지 번호와 i 값이 같을 경우 하이퍼링크 없이 페이지 번호 표시 -->
				<!-- 아니면, pageNum 파라미터를 i 값으로 설정하여 BoardList.bo 서블릿 주소 링크 -->
				<c:choose>
					<c:when test="${i eq pageInfo.pageNum }">${i }</c:when>
					<c:otherwise><a href="BoardList.bo?pageNum=${i }">${i }</a></c:otherwise>
				</c:choose>
				&nbsp;
			</c:forEach>
		<!-- 현재 페이지번호가 끝 페이지번호보다 작을 때 현재 페이지번호 + 1 값으로 페이지 이동 -->
		<input type="button" value="다음" <%if(pageInfo.getPageNum() < pageInfo.getMaxPage()) {%>onclick="location.href='BoardList.bo?pageNum=${pageInfo.pageNum + 1}'"<%} %>>
	</section>
</body>
</html>















