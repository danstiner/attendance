<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>


	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Export Grades</h1>
		
		<p>
			<a href="?type=csv">All grades in Excel format</a>
		</p>
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>