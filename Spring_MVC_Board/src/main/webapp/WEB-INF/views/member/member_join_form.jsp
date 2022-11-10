<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="<%=request.getContextPath() %>/resources/css/top.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/resources/css/default.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath() %>/resources/js/jquery-3.6.1.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#id').blur(function(){
// 			alert("포커스해제");
			$.ajax({
				url:'MemberIdCheck.me',
				data:{'id':$('#id').val()},
				success:function(result){
					$('#checkIdResult').html(result).css('color','blue');
				}
			});
		});
	});
</script>
<script type="text/javascript">
	// 입력 항목들에 대한 정규표현식 검사 결과를 저장할 변수 선언
	// => 각 항목 체크 완료 시 해당 값을 true 로 변경하고, 실패 시 false 로 변경
	var checkNameResult = false, checkIdResult = false, checkPasswdResult = false;

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
	
	// -------------- 정규표현식을 활용한 입력값 검증 -----------------
	// 1. 이름에 대한 입력값 검증 : 한글 2글자 ~ 10글자
	// => 이름이 변경되면 검증 수행하여 패턴과 일치하지 않으면 경고메세지 출력 및 입력값 선택
	function checkName(name) {
		let regex = /^[가-힣]{2,10}$/;
		if(!regex.exec(name)) {
			alert("이름이 올바르지 않습니다!");
			$("#name").select();
			checkNameResult = false;
		} else {
			checkNameResult = true;
		}
	}
	
	// 2. 아이디에 대한 입력값 검증 : 4 ~ 16자리 영문자, 숫자, 특수문자(-_.) 조합
	function checkId(id) {
		let regex = /^[\w-.]{4,16}$/;
		if(!regex.exec(id)) {
			$("#checkIdResult").html("4 ~ 16자리 영문자, 숫자, 특수문자(-_.) 필수!");
			$("#checkIdResult").css("color", "red");
			checkIdResult = false;
		} else {
			$("#checkIdResult").html("사용 가능한 아이디!");
			$("#checkIdResult").css("color", "green");
			checkIdResult = true;
		}
	}
	
	// 3. 패스워드에 대한 입력값 검증 : 8 ~ 20자리 영문자, 숫자, 특수문자(!@#$%^&*) 조합
	function checkPasswd(passwd) {
		let regex = /^[A-Za-z0-9!@#$%^&*]{8,20}$/;
		if(!regex.exec(passwd)) {
			$("#checkPasswdResult").html("8 ~ 20자리 영문자, 숫자, 특수문자(!@#$%^&*) 필수!");
			$("#checkPasswdResult").css("color", "red");
			checkPasswdResult = false;
		} else {
// 			$("#checkPasswdResult").html("사용 가능한 패스워드!");
// 			$("#checkPasswdResult").css("color", "green");

			// 패스워드 복잡도 검사 추가 => 패스워드 부분 검사
			// 영문 대문자, 소문자, 숫자, 특수문자 조합에 대한 점수 계산 후 
			// 안전, 보통, 위험, 사용불가의 4등급으로 분류하여 결과 출력
			// => 각각의 검사 패턴을 별도로 생성해야함
			let upperCaseRegex = /[A-Z]/; // 대문자
			let lowerCaseRegex = /[a-z]/; // 소문자
			let numRegex = /[0-9]/; // 숫자
			let specialCharRegex = /[!@#$%^&*]/; // 특수문자
			
			let count = 0; // 부분 검사 항목에 대한 점수를 계산하기 위한 변수
			// => 각 검사 항목이 포함되어 있으면 count 값을 1 증가시키기
			// => 주의! 각 항목에 대한 검사는 if 문을 각각 적용해야함! (else if 사용 금지!)
			if(upperCaseRegex.exec(passwd)) {
				count++;
			} 

			if(lowerCaseRegex.exec(passwd)) {
				count++;
			} 
			
			if(numRegex.exec(passwd)) {
				count++;
			} 
			
			if(specialCharRegex.exec(passwd)) {
				count++;
			} 
			
			// 부분 검사 결과에 대한 출력
			// => 4점 : "안전" 출력(blue)
			// => 3점 : "보통" 출력(green)
			// => 2점 : "위험" 출력(orange)
			// => 1점 이하 : "사용 불가능한 패스워드" 출력(red)
			switch(count) {
				case 4 : 
					$("#checkPasswdResult").html("안전");
					$("#checkPasswdResult").css("color", "blue");
					checkPasswdResult = true;
					break;
				case 3 : 
					$("#checkPasswdResult").html("보통");
					$("#checkPasswdResult").css("color", "green");
					checkPasswdResult = true;
					break;
				case 2 : 
					$("#checkPasswdResult").html("위험");
					$("#checkPasswdResult").css("color", "orange");
					checkPasswdResult = true;
					break;
				default :
					$("#checkPasswdResult").html("사용 불가능한 패스워드");
					$("#checkPasswdResult").css("color", "red");
					checkPasswdResult = false;
			}
		}
	}
	
	function checkForm() {
		// 모든 항목이 입력되어 있고(required 속성으로 적용 가능)
		// 이름, 아이디, 패스워드 입력 항목 체크가 정상일 경우에만 회원가입 수행
		// => 아닐 경우 각 항목에 대한 경고 메세지 출력 및 회원가입 작업 중단
		// => 단, 각 체크 항목에 대한 판별을 위해 전역변수로 체크할 변수 선언 필요
// 		if(!checkNameResult) {
// 			alert("이름 항목 입력값 체크 필수!");
// 			$("#name").select();
// 			return false; // 현재 폼의 submit 동작을 중단하기 위해 false 리턴
// 		} else if(!checkIdResult) {
// 			alert("아이디 항목 입력값 체크 필수!");
// 			$("#id").select();
// 			return false; // 현재 폼의 submit 동작을 중단하기 위해 false 리턴
// 		} else if(!checkPasswdResult) {
// 			alert("패스워드 항목 입력값 체크 필수!");
// 			$("#passwd").select();
// 			return false; // 현재 폼의 submit 동작을 중단하기 위해 false 리턴
// 		}
		
		// 위의 모든 조건이 해당되지 않으면(= 모두 true 이면) submit 동작 수행
		return true;

		// jQuery 를 사용하여 대상 폼 객체에 대해 submit() 메서드를 호출해도 동일한 동작 수행
		// => 단, submit 버튼 대신 일반 button 의 onclick 속성으로 함수 호출
// 		if(!checkNameResult) {
// 			alert("이름 항목 입력값 체크 필수!");
// 			$("#name").select();
// 		} else if(!checkIdResult) {
// 			alert("아이디 항목 입력값 체크 필수!");
// 			$("#id").select();
// 		} else if(!checkPasswdResult) {
// 			alert("패스워드 항목 입력값 체크 필수!");
// 			$("#passwd").select();
// 		} else {
// 			// 모든 체크 항목이 true 일 때 회원가입 수행
// 			$("form").submit();
// 		}
		
	}
</script>
</head>
<body>
	<!-- 세션 아이디가 null 이 아닐 경우 메인페이지로 돌려보내기 -->
	<c:if test="${sessionScope.sId ne null }">
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
		<h1>회원 가입</h1>
		<form action="MemberJoinPro.me" method="post" name="joinForm" onsubmit="return checkForm()">
			<table border="1">
				<tr>
					<td>이름</td>
					<td><input type="text" name="name" id="name" onchange="checkName(this.value)" required="required" size="20"></td>
				</tr>
				<tr>
					<td>성별</td>
					<td>
						<input type="radio" name="gender" value="남">남&nbsp;&nbsp;
						<input type="radio" name="gender" value="여" checked="checked">여
					</td>
				</tr>
				<tr>
					<td>E-Mail</td>
					<td>
						<input type="text" name="email" required="required" size="10">
					</td>
				</tr>
				<tr>
					<td>아이디</td>
					<td>
						<input type="text" name="id" id="id" onchange="checkId(this.value)" required="required" size="20" placeholder="4-16자리 영문자,숫자 조합">
						<span id="checkIdResult"><!-- 자바스크립트에 의해 메세지가 표시될 공간 --></span>
					</td>
				</tr>
				<tr>
					<td>패스워드</td>
					<td>
						<input type="password" name="passwd" id="passwd" onchange="checkPasswd(this.value)" required="required" size="20" placeholder="8-20자리 영문자,숫자,특수문자 조합">
						<span id="checkPasswdResult"><!-- 자바스크립트에 의해 메세지가 표시될 공간 --></span>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="회원가입">
						<input type="button" value="취소" onclick="history.back()">
					</td>
				</tr>
			</table>
		</form>
	</article>
</body>
</html>