<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>Administrative Data Export/Import</h1>
		
		<p>For backups and restores</p>
		
		<p>
			<a href="export/data.json">Export entire database</a> (Useful as a backup)
		</p>
		
		<br/>
		
		<p>
			<a href="export/data.json">Try upgrading database</a> <br/>
			Database is at version: <c:out value="${AppDatastoreVersion}" /><br/>
			Code expects version: <c:out value="${ObjectDatastoreVersion}" />
		</p>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>