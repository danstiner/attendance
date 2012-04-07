<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>
	<%
	String netID = (String) session.getAttribute("user");
	User user = null;
	
	if (netID == null || netID.equals("")) 
	{
		response.sendRedirect("/JSPPages/logout.jsp");
		return;
	}
	else
	{
		user = DatabaseUtil.getUser(netID);
		if (!user.getType().equalsIgnoreCase("Director")) {
			response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	<script>
		function viewForms() {
			var div = document.getElementById("formsDiv")
			if (div.style.display == "none") {
				div.style.display = "block";
			} else {
				div.style.display = "none";
			}
		}
		function pullAttendance() {
			
		}
		function editInformation() {
			
		}
	</script>
	<body>
<!--*********************Page Trail*****************************-->
	
			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/Student_Page.jsp" title="PageTrail_director">Director</a>
			
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

<!--*********************info*****************************-->

	<!--*********************User Info*****************************-->	
		<br/><br/>
		<div>
		<table>
			<tr><td>NetID:</td> <td><%= user.getNetID() %></td></tr>
		</table>
		</div>
----------------------------------
<p>
		<!--********************* Button *****************************-->
		<input type ="submit" onClick="viewForms();"  value = "Forms">
			<div id="formsDiv" style="display: none">
				<p><a href="/JSPPages/Student_Form_A_Performance_Absence_Request.jsp">Form A - Performance Absence Request</a></p>
				<p><a href="/JSPPages/Student_Form_B_Class_Conflict_Request.jsp">Form B - Class Conflict Request</a></p>
				<p><a href="/JSPPages/Student_Form_C_Rehearsal_Excuse.jsp">Form C - Request for Excuse from Rehearsal</a></p>
				<p><a href="/JSPPages/Student_Form_D_Time_Worked.jsp">Form D - Time Worked</a></p>
			</div>
		<input type ="submit" onClick="window.location = '/JSPPages/Student_View_Attendance.jsp';"  value = "View Attendance">
		<br/>
		<input type ="submit" onClick="window.location = '/JSPPages/Student_Edit_Info.jsp';"  value = "Edit my information">
		
<!--*****************************************the powerful stuff*************************************************-->		
		
		
	</body>

</html>