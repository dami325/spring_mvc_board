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
<script src="<%=request.getContextPath() %>/resources/js/jquery-3.6.1.js"></script>
<script type="text/javascript">
	let pageNum = 1;
	
	$(function() {
		// 게시물 목록 조회를 수행할 load_list() 메서드 호출
		load_list();
		
		// 무한스크롤 기능 구현
		// window 객체에서 scroll 이 동작할 때 기능을 수행하기 위해 scroll() 함수 호출
		$(window).scroll(function() {
			
			// 1. window 객체와 document 객체를 활용하여 특정 값 가져오기
			let scrollTop = $(window).scrollTop(); // 스크롤바의 현재 위치
			let windowHeight = $(window).height(); // 문서가 표시되는 창의 높이
			let documentHeight = $(document).height(); // 문서 전체 높이
	//			$("#valueArea").html("scrollTop : " + scrollTop + ", windowHeight : " + windowHeight + ", documentHeight : " + documentHeight + "<br>");
			
			// 2. 스크롤바 현재 위치값 + 창의 높이 + x 가 문서 전체 높이 이상이면
			//    다음 페이지를 로딩하여 목록에 추가
			// => 이 때 x 는 문서 마지막으로부터 여유 공간으로 둘 스크롤바 아래쪽 남은 공간
			//    (단, 스크롤바 움직일 때 값이 1 씩 변하지 않고 큰 값으로 변화가 일어나므로 함수 호출이 여러번 일어남)
			//    (따라서, x 값을 1로 주면 바닥에 닿을 때 다음 페이지 로딩됨)
			if(scrollTop + windowHeight + 1 >= documentHeight) {
				// 다음 페이지 로딩을 위해 load_movie_list() 함수 호출
				// => 이 때, 함수 호출 전 페이지번호를 1 증가시킨 후 호출 필요
				pageNum++;
				load_list();
			}
		});
	});
	
	function load_list() {
		$.ajax({
			type: "GET",
			url: "BoardListJson.bo?pageNum=" + pageNum,
			dataType: "json"
		})
		.done(function(boardList) { // 요청 성공 시
	//			alert(boardList);
// 			$("#listForm > table").append(JSON.stringify(boardList));

			// JSONArray 객체를 통해 배열 형태로 전달받은 JSON 객체를 반복하면서 객체 하나씩 꺼내기
			for(let board of boardList) {
// 				$("#listForm > table").append("<tr><td colspan='5'>" + JSON.stringify(board) + "</td></tr>");
				
				let space = "";
				
				// board_re_lev 값이 0보다 크면 
				// 제목열 해당 값만큼 공백(&nbsp;) 추가 후 공백 뒤에 답글 아이콘 이미지 추가 
				if(board.board_re_lev > 0) {
					for(let i = 0; i < board.board_re_lev; i++) {
						space += "&nbsp;&nbsp;";
					}
					
					space += "<img src='<%=request.getContextPath() %>/resources/images/re.gif'>&nbsp;";
				}
				
				// <tr><td> 태그를 사용하여 각 게시물을 파싱하여 하나씩 테이블에 출력(board.xxx)
				let result = "<tr height='100'>"
								+ "<td>" + board.board_num + "</td>"
								+ "<td class='td_subject'>" 
										+ space 
										+ "<a href='BoardDetail.bo?board_num=" + board.board_num + "'>" 
												+ board.board_subject 
										+ "</a>";
								+ "</td>"	
								+ "<td>" + board.board_name + "</td>"	
								+ "<td>" + board.board_date + "</td>"	
								+ "<td>" + board.board_readcount + "</td>"	
							+ "</tr>";	
				
				$("#listForm > table").append(result);
			}
			
		})
		.fail(function() { // 요청 실패 시
			$("#listForm > table").html("요청 실패!");
		});
			
	}
</script>

</head>
<body>
	<header>
		<!-- Login, Join 링크 표시 영역(inc/top.jsp 페이지 삽입) -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<!-- 게시판 리스트 -->
	<section id="listForm">
		<h2>게시판 글 목록</h2>
		<table>
			<tr id="tr_top">
				<td width="100px">번호</td>
				<td>제목</td>
				<td width="150px">작성자</td>
				<td width="150px">날짜</td>
				<td width="100px">조회수</td>
			</tr>
			<!-- AJAX 를 통해 조회 결과를 표시할 위치 -->
		</table>
	</section>
	<section id="buttonArea">
		<!-- 검색 기능 구현을 위한 form 태그 -->
		<form action="BoardList.bo" method="get">
			<select name="searchType">
				<option value="subject">제목</option>
				<option value="content">내용</option>
				<option value="subject_content">제목&내용</option>
				<option value="name">작성자</option>
			</select>
			<input type="text" name="keyword">
			<input type="submit" value="검색">
			<input type="button" value="글쓰기" onclick="location.href='BoardWriteForm.bo'" />
		</form>
	</section>
</body>
</html>















