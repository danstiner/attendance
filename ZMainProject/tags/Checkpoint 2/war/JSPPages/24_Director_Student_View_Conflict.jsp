<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
	<head>
		<title>@10Dance</title>
	</head>

	<body>
	
	<!--*********************Page Trail*****************************-->

	<!--TODO: need to connected to specific page-->
	<h3>
		<li>
			<a href="http://www.iastate.edu" title="PageTrail_Home">Home</a> 
			>
			<a href="http://www.iastate.edu" title="PageTrail_Student">Student</a>
			>
			<a href="http://www.iastate.edu" title="PageTrail_AbsenceApprovalForm_Class_Conflict">AbsenceApprovalForm(Class Conflict)</a>

		<!--HELP BUTTON-->	
		<a class="addthis_button"><img
        src="http://icons.iconarchive.com/icons/deleket/button/24/Button-Help-icon.png"
        width="16" height="16" border="0" alt="Share" /></a>
		</li>
	</h3>	

	<!--*********************info*****************************-->
	
		<h1>NetID, Student Name</h1>
		
		<!--*********************Pick a day div*****************************-->
		<div>
			<table>
				<tr><td>Date Requested:</td> </tr>
				
				<tr><td>
					<img src="Calendar.jpg" alt="Calendar" width="100" height="100" />	
				</td></tr>							
			</table>
		</div>

		<!--*********************Class info div*****************************-->
		</br></br>
		<div>
		<table>
			<tr><td> What is the recurrance of the class?</td></tr>
		<!--Monday ~ Sunday -->
		<tr><td> 
			<input type="checkbox" name="Monday" value="Monday"> Monday
			<input type="checkbox" name="Tuesday" value="Tuesday" checked > Tuesday
			<input type="checkbox" name="Wednesday" value="Wednesday">Wednesday<br>
		</td></tr>
		
		<tr><td> 
			<input type="checkbox" name = "Thursday" value="Thursday" checked>Thursday
			<input type="checkbox" name = "Friday" value="Friday">Friday<br>
		</td></tr>
		
	<!--start date -->		
		<tr>
			<td>Start Date: <input type= "text" name="StartDate" value = "02/31"id="StartDate"/></td>
			
		<tr>
		
		<tr>
			<td>End Date:
			<input type ="text" name="EndDate"  value = "05/31"id="EndDate"/></td>
		</tr>
		
		<!--until/ start at/ completely miss-->
			<tr><td>
				<input type="radio" name="TimeMiss" value="Until"/>Util<br>
			</td></tr>
			<tr><td>
				<input type="radio" name="TimeMiss"  value="StartAt"/>Start At
				<input type= "text" name="StartDate" id="StartDate"/>
			</td></tr>
			
			<tr><td>
				<input type="radio" name="TimeMiss" value="CompletelyMiss" checked  />Completely Miss<br>
			</td></tr>	
		</table>
		</div>
		
	<h1>*******************</h1>
	<!--*********************drop down bar div*****************************-->

		<table>
			<tr>					
				<td><label for="CourseDept">Course Dept:</label></td>
				<td><input type= "text" name="CourseDept" id="CourseDept" value = "ENGLISH"/></td>
			</tr>
			
			<tr>
				<td><label for="CourseNum">Course#:</label></td>
				<td><input type= "text" name="CourseNum" id="CourseNum"  value = "Eng314"/></td>	
			</tr>
			<tr>
				<td><label for="Section">Section:</label></td>
				<td><input type= "text" name="Section" id="Section" value = "A"/></td>	
			</tr>
			<tr>
				<td><label for="Bulding">Bulding:</label></td>
				<td><input type= "text" name="Bulding" id="Bulding"  value = "Ross Hall"/></td>	
			</tr>
			
			<tr>
				<td><label for="Comments">Comments(Option):</label></td>
				<td><input type= "text" name="Comments" id="Comments"  value = "i dont like english!!!	"/></td>	
			</tr>

		</table>
	<!--*********************End Button*****************************-->

				<h2>
					<button type="Back">Back</button>
					<button type="Submit">Pre</button>
					<button type="Submit">Approve</button>
					<button type="Submit">Deny and Mesage</button>
					<button type="Submit">Next</button>
				<h2>
		</form>		
	</body>
	



</html>