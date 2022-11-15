<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<link href="<%=request.getContextPath() %>/resources/css/top.css" rel="stylesheet" type="text/css">
<style type="text/css">
	#registForm {
		width: 500px;
		height: 610px;
		border: 1px solid red;
		margin: auto;
	}
	
	h1 {
		text-align: center;
	}
	
	table {
		margin: auto;
		width: 450px;
	}
	
	.td_left {
		width: 150px;
		background: orange;
	}
	
	.td_right {
		width: 300px;
		background: skyblue;
	}
	
	#commandCell {
		text-align: center;
	}
</style>
</head>
<body>
	<header>
		<!-- Login, Join 링크 표시 영역(inc/top.jsp 페이지 삽입) -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<!-- 게시판 등록 -->
	<section id="writeForm">
		<h1>게시판 글 등록</h1>
		<form action="BoardWritePro.bo" method="post" name="boardForm" enctype="multipart/form-data">
			<table>
				<tr>
					<td class="td_left"><label for="board_name">글쓴이</label></td>
					<td class="td_right"><input type="text" name="board_name" required="required" /></td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_pass">비밀번호</label></td>
					<td class="td_right"><input type="password" name="board_pass" required="required" /></td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_subject">제목</label></td>
					<td class="td_right"><input type="text" name="board_subject" required="required" /></td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_content">내용</label></td>
					<td class="td_right">
						<textarea id="board_content" name="board_content" cols="40" rows="15" required="required"></textarea>
					</td>
				</tr>
				<tr>
					<td class="td_left"><label for="file">파일</label></td>
					<td class="td_right">
						<!-- 
						복수개의 파일 업로드 시 name 속성값을 같게 할 경우
						동일한 파라미터명으로 복수개의 값이 각각 전달됨
						=> 컨트롤러에서 각각을 배열로 처리 가능 
						-->
						<!-- 복수개의 파일을 각각의 입력폼으로 처리할 경우 -->
						<input type="file" name="files" required="required" />
						<input type="file" name="files" required="required" />
						<input type="file" name="files" required="required" />
						
						<!-- 복수개의 파일을 하나의 입력폼으로 처리할 경우(multiple 속성 필요) -->
<!-- 						<input type="file" name="files" required="required" multiple="multiple" /> -->
					</td>
				</tr>
			</table>
			<section id="commandCell">
				<input type="submit" value="등록">&nbsp;&nbsp;
				<input type="reset" value="다시쓰기">
			</section>
		</form>
	</section>
	
</body>
</html>