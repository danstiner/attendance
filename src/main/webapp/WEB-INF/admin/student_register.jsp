<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="date" class="java.util.Date" />

<!DOCTYPE html>

<html>
  <head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
  </head>
  <body>
  	<jsp:include page="/WEB-INF/template/header.jsp" />
	
  	
	<h1><c:out value="${pagetemplate.siteTitle}" /> <fmt:formatDate value="${date}" pattern="yyyy" /></h1>
	<h2>Registration</h2>
	
	<form method="post" accept-charset="utf-8">
		<dl class="block-layout">
			<dt><label></label></dt>
				
			<dt><label class="required" for="PrimaryEmail">School Email</label></dt>
			<dd>
				<input type="text" name="PrimaryEmail" value="<c:out value="${PrimaryEmail}" />" />
			</dd>
			
			<dt><label class="required" for="FirstName">First Name</label></dt>
			<dd><input type="text" name="FirstName" value="<c:out value="${FirstName}" />" /></dd>
			
			<dt><label class="required" for="LastName">Last Name</label></dt>
			<dd><input type="text" name="LastName" value="<c:out value="${LastName}" />" /></dd>
			
			<dt><label class="required" for="UniversityID">University ID</label></dt>
			<dd><input type="text" name="UniversityID" value="<c:out value="${UniversityID}" />" /></dd>
			
			<dt><label for="SecondEmail">Secondary Email</label></dt>
			<dd>
				<input type="email" name="SecondEmail" value="<c:out value="${SecondEmail}" />" />
				<br/>
				Optional. If a google email is entered, you will be able to login with that or your CyMail email.
			</dd>
			
			<dt><label class="required" for="Section">Section</label></dt>
			<dd>
				<select name="Section" id="Section">
					<option value="">(Select One)</option>
					<c:forEach items="${sections}" var="s" varStatus="loop">
				        <option value="<c:out value="${s.value}" />"
				        	${Section==s.value ? 'selected="true"' : ''}
				        	><c:out value="${s.displayName}" /></option>
				    </c:forEach>
				</select>
			</dd>
			
			<dt><label class="required" for="Year">Years in band</label></dt>
			<dd>
				<select name="Year" id="Year"  >
					<option value="">(Choose)</option>
					<c:forEach var="i" begin="1" end="10" step="1" varStatus="loop">
				        <option ${Year==i ? 'selected="true"' : ''}><c:out value="${i}" /></option>
				    </c:forEach>
				</select>
			</dd>
			
			<dt><label class="required" for="Major">Major</label></dt>
			<dd><input type="text" name="Major" value="<c:out value="${Major}" />" /></dd>
		</dl>

		<input type="submit" value="Register" name="Register" />
		<input type="button" value="Back" name="Back" onclick="window.location='/'"/>
	</form>
	<jsp:include page="/WEB-INF/template/footer.jsp" />
</body>

</html>