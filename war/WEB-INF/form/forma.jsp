<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="date" class="java.util.Date" />

<fmt:formatDate var="year" value="${date}" pattern="yyyy" />

<html>
<head>
<jsp:include page="/WEB-INF/template/head.jsp" />
<script>
	function goahead() {
		<c:if test="${late}">
			return confirm("It's past the deadline to submit this form. If you think you have a valid excuse, you can go ahead and submit. The form will be marked as denied without notifying the director.");
		</c:if>
		<c:if test="${not late}">
			return true;
		</c:if>
	}
</script>
</head>

<body>

	<jsp:include page="/WEB-INF/template/header.jsp" />

	<h1>Performance Absence Request | Form A</h1>

	<p>
		This form includes all performances through any post-season activity
		ending January 30, ${year+1}, and it must be submitted by
		<fmt:formatDate value="${cutoff}"
			pattern="hh:mm a 'on' E, MMMMM d, yyyy" />
		. Documentation must be submitted to the director for all absences
		(doctor's note, obituary, wedding program, etc.).
	</p>

	<form method="post" accept-charset="utf-8">

		<dl class="block-layout">

			<dt>
				<label class='required'>Date of the absence:</label>
			</dt>
			<dd>
				<input autofocus id='startMonth' size='5' type='number'
					name='StartMonth' min='01' max='12' placeholder='MM'
					value='<c:out value="${empty StartMonth ? '' : StartMonth+1}" />' />
				/ <input id='startDay' size='5' type='number' name='StartDay'
					min='01' max='31' step='1' placeholder='DD'
					value='<c:out value="${StartDay}" />' /> / <input id='startYear'
					size='5' type='number' name='StartYear' min='${year}'
					max='${year+1}' step='1' placeholder='YYYY'
					value='<c:out value="${StartYear}" />' />
			</dd>

			<dt>
				<label class='required'>Reasons:</label>
			</dt>
			<dd>
				<textarea rows="6" cols="50" id="reason" name="Reason"
					wrap="physical">
					<c:out value="${Reason}" />
				</textarea>
				<br /> Please be specific and be thorough.
			</dd>

		</dl>

		<input type="submit" value="Save Info" name="SaveInfo"
			onclick="return goahead()" />
	</form>

	<jsp:include page="/WEB-INF/template/footer.jsp" />
</body>

</html>