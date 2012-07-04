<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
		<h1>Message Inbox</h1>
		<br/>
		<c:choose>
		<c:when test="${empty unresolved}">
			<p><i>No unresolved messages.</i></p>
		</c:when>
		<c:otherwise>
			<h2>Unresolved Messages</h2>
			<c:set var="threads" value="${unresolved}" scope="request"/>
			<jsp:include page="/WEB-INF/common/threadtable.jsp" />
		</c:otherwise>
		</c:choose>
		
		<c:choose>
		<c:when test="${empty resolved}">
			<p><i>No resolved messages.</i></p>
		</c:when>
		<c:otherwise>
			<h2>Resolved Messages</h2>
			<c:set var="threads" value="${resolved}" scope="request" />
			<jsp:include page="/WEB-INF/common/threadtable.jsp" />
		</c:otherwise>
		</c:choose>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>