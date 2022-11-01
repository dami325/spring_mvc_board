<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<link href="<%=request.getContextPath() %>/resources/css/top.css" rel="stylesheet" type="text/css">
<style type="text/css">
	#modifyForm {
		width: 500px;
		height: 610px;
		border: 1p solid red;
		margin: auto;
	}
	
	h2 {
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
	
	#board_content {
		resize: none;
	}
	
</style>
</head>
<body>
	<header>
		<!-- Login, Join 링크 표시 영역(inc/top.jsp 페이지 삽입) -->
		<jsp:include page="<%=request.getContextPath() %>/WEB-INF/inc/top.jsp"></jsp:include>
	</header>
	<!-- 게시판 수정하기 -->
	<section id="modifyForm">
		<h2>글 수정하기</h2>
		<form action="BoardModifyPro.bo" method="post" name="modifyForm" enctype="multipart/form-data">
			<input type="hidden" name="board_num" value="${param.board_num }" />
			<input type="hidden" name="pageNum" value="${param.pageNum }" />
			<!-- 파일 수정 시 기존 파일 삭제를 위해 기존 실제 파일명을 파라미터로 전달 필요 -->
			<input type="hidden" name="board_real_file" value="${board.board_real_file }" />
			<table>
				<tr>
					<td class="td_left"><label for="board_name">글쓴이</label></td>
					<td class="td_right">
						<input type="text" name="board_name" required="required" value="${board.board_name }" />
					</td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_pass">비밀번호</label></td>
					<td class="td_right">
						<input type="password" name="board_pass" required="required" />
					</td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_subject">제목</label></td>
					<td class="td_right">
						<input type="text" name="board_subject" required="required" value="${board.board_subject }" />
					</td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_content">내용</label></td>
					<td class="td_right">
						<textarea id="board_content" name="board_content" cols="40" rows="15" 
									required="required">${board.board_content }</textarea>
					</td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_file">파일 첨부</label></td>
					<td class="td_right">
						<!-- 파일 수정할 경우에만 선택하도록 required 속성 제거 -->
						<input type="file" name="board_file" /><br>
						(기존 파일 : ${board.board_file })
					</td>
				</tr>
			</table>
			<section id="commandCell">
				<input type="submit" value="수정">&nbsp;&nbsp;
				<input type="button" value="뒤로" onclick="history.back()">
			</section>
		</form>
	</section>
</body>
</html>















