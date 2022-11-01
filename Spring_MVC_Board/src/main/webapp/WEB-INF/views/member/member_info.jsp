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
	function changeDomain(domain) {
		// 선택된 도메인 값을 email2 의 value 값으로 변경
		document.joinForm.email2.value = domain;
		
		// 단, 선택된 도메인이 "직접입력" 이 아닐 경우 email2 입력창 잠금(readOnly 속성 적용)
		// 아니면, email2 입력창 잠금 해제
		if(domain == "") { // 직접입력 선택 시(document.joinForm.selectDomain[0].selected 동일)
			document.joinForm.email2.readOnly = false; // 입력창 잠금 해제
			document.joinForm.email2.focus(); // 입력창 포커스 요청
		} else {
			document.joinForm.email2.readOnly = true; // 입력창 잠금
		}
	} 
	
	function confirmLeave(id) {
		// confirm() 함수를 사용하여 "삭제하시겠습니까?" 메세지로 확인받아 result 변수에 저장 후
		// result 변수값이 true 일 경우 MemberDelete.me 서블릿 주소 요청(파라미터로 id 전달)
		let result = confirm("탈퇴하시겠습니까?\n탈퇴 후에는 복구가 불가능합니다.");
		
		if(result) {
			location.href="MemberDelete.me?id=" + id;
		}
	}
</script>
</head>
<body>
	<!-- 세션 아이디가 null 일 경우 메인페이지로 돌려보내기 -->
	<c:if test="${sessionScope.sId eq null }">
		<script>
			alert("잘못된 접근입니다!");
			location.href = "./";
		</script>
	</c:if>
	<header>
		<!-- Login, Join 링크 표시 영역(inc/top.jsp 페이지 삽입) -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<article>
		<h1>회원 정보 조회</h1>
		<form action="MemberModify.me" method="post" name="joinForm">
			<table border="1">
				<tr>
					<td>이름</td>
					<td><input type="text" name="name" value="${member.name }" required="required" size="20"></td>
				</tr>
				<tr>
					<td>성별</td>
					<td>
	<%-- 					<input type="radio" name="gender" value="남" <%if(member.getGender().equals("남")) {%>checked="checked" <%} %>>남&nbsp;&nbsp; --%>
						<c:choose>
							<c:when test="${member.gender eq '남' }">
								<input type="radio" name="gender" value="남" checked="checked">남&nbsp;&nbsp;
								<input type="radio" name="gender" value="여">여
							</c:when>
							<c:otherwise>
								<input type="radio" name="gender" value="남">남&nbsp;&nbsp;
								<input type="radio" name="gender" value="여" checked="checked">여
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td>E-Mail</td>
					<td>
						<input type="text" name="email" value="${member.email }" required="required" size="10">
					</td>
				</tr>
				<tr>
					<td>아이디</td>
					<td>
						<input type="text" name="id" value="${member.id }" required="required" size="20" placeholder="4-16자리 영문자,숫자 조합" readonly="readonly">
						<span id="checkIdResult"><!-- 자바스크립트에 의해 메세지가 표시될 공간 --></span>
					</td>
				</tr>
				<tr>
					<td>기존 패스워드</td>
					<td>
						<input type="password" name="passwd" required="required" size="20">
					</td>
				</tr>
				<tr>
					<td>새 패스워드</td>
					<td>
						<input type="password" name="newPasswd" size="20" placeholder="8-20자리 영문자,숫자,특수문자 조합">
						<span id="checkPasswdResult"><!-- 자바스크립트에 의해 메세지가 표시될 공간 --></span>
					</td>
				</tr>
				<tr>
					<td>새 패스워드 확인</td>
					<td>
						<input type="password" name="newPasswd2" size="20" placeholder="8-20자리 영문자,숫자,특수문자 조합">
						<span id="checkPasswdResult2"><!-- 자바스크립트에 의해 메세지가 표시될 공간 --></span>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="회원정보수정">
						<input type="button" value="취소" onclick="history.back()">
						<!-- 탈퇴 버튼 클릭 시 확인을 통해 "MemberDelete.me" 로 이동(id 파라미터 필요) -->
						<input type="button" value="탈퇴" onclick="confirmLeave('${member.id}')">
					</td>
				</tr>
			</table>
		</form>
	</article>
</body>
</html>




