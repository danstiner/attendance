<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<html>
<head>
	<jsp:include page="/WEB-INF/template/head.jsp" />
</head>
<body>
	<jsp:include page="/WEB-INF/template/header.jsp" />

	<h1>${pagetemplate.title}</h1>
	<br />
		<c:if test="${not empty error_messages}">
			<p class="notify-msg error">
				<strong>Error:</strong>
				<c:forEach items="${error_messages}" var="error_message">
					<c:out value="${error_message}" />
					<br/>
				</c:forEach>
			</p>
		</c:if>
		
		

	<form action="./setranks" method="POST">
		<table class="gray full-width">
			<!-- start headers -->
			<thead>
				<tr>
					<th>Last Name</th>
					<th>First name</th>
					<th>Section</th>
					<th>University ID</th>
					<th>Rank</th>
					<!-- headers are now done -->
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${students }" var="student">
					<tr>
						<td>${student.firstName }</td>
						<td>${student.lastName }</td>
						<td>${student.section.displayName }</td>
						<td>${student.universityID }</td>
						<td><input type="text" name=${student.id } value=${student.rank }></input></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br/>
		<input type="submit" value="Submit Ranks" />
	</form></form>

	<jsp:include page="/WEB-INF/template/footer.jsp" />
</body>
</html>