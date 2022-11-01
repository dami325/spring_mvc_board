<%@page import="com.itwillbs.mvc_board.vo.MemberVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="<%=request.getContextPath() %>/resources/css/top.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/resources/css/default.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function confirmDelete(id) {
		// confirm() 함수를 사용하여 "삭제하시겠습니까?" 메세지로 확인받아 result 변수에 저장 후
		// result 변수값이 true 일 경우 MemberDelete.me 서블릿 주소 요청(파라미터로 id 전달)
		let result = confirm("삭제하시겠습니까?");
		
		if(result) {
			location.href="MemberDelete.me?id=" + id;
		}
	}
</script>
</head>
<body>
	<!-- 세션 아이디가 null 일 경우 메인페이지로 돌려보내기 -->
	<c:if test="${sessionScope.sId eq null or sessionScope.sId ne'admin'}">
		<script>
			alert("잘못된 접근입니다!");
			location.href = "./";
		</script>
	</c:if>
	<header>
		<!-- Login, Join 링크 표시 영역(inc/top.jsp 페이지 삽입) -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<h1>관리자 메인페이지</h1>
	<!-- 번호, 이름, 아이디, 이메일, 가입일 출력 -->
	<table border="1">
		<tr>
			<th width="50">번호</th>
			<th width="100">이름</th>
			<th width="100">아이디</th>
			<th width="200">이메일</th>
			<th width="150">가입일</th>
			<th width="150"></th>
		</tr>
		<!-- 반복문을 통해 회원 목록 출력 -->
		<c:forEach var="member" items="${memberList }">
			<tr>
				<td>${member.idx }</td>
				<td>${member.name }</td>
				<td>${member.id }</td>
				<td>${member.email }</td>
				<td>${member.date }</td>
				<td>
					<!-- 상세 정보 버튼 클릭 시 MemberInfo.me 서블릿 요청(id 파라미터 필요)-->
					<input type="button" value="상세정보" onclick="location.href='MemberInfo.me?id=${member.id}'">
					<!-- 삭제 버튼 클릭 시 MemberDelete.me 서블릿 요청(id 파라미터 필요) -->
					<input type="button" value="삭제" onclick="confirmDelete('${member.id}')">
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>















