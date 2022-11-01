<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="<%=request.getContextPath() %>/resources/css/top.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/resources/css/default.css" rel="stylesheet" type="text/css">
</head>
<body>
	<header>
		<!-- Login, Join 링크 표시 영역(inc/top.jsp 페이지 삽입) -->
		<jsp:include page="inc/top.jsp"></jsp:include>
	</header>
	<article>
		<h1>MVC_Board</h1>
		<h3><a href="BoardWriteForm.bo">글쓰기</a></h3>
		<h3><a href="BoardList.bo">글목록</a></h3>
	</article>
</body>
</html>















