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
			<a href="http://www.iastate.edu" title="PageTrail_Director">Director</a>
			>
			<a href="http://www.iastate.edu" title="PageTrail_ViewAndEditStudentList">View and Edit Student List</a>
			>
			<a href="http://www.iastate.edu" title="PageTrail_AbsenceForm">Absence Form</a>

		<!--HELP BUTTON-->	
		<a class="addthis_button"><img
        src="http://icons.iconarchive.com/icons/deleket/button/24/Button-Help-icon.png"
        width="16" height="16" border="0" alt="Share" /></a>
		</li>
	</h3>	

	<!--*********************info*****************************-->
	
		<h1>NetID, Student Name-Absence 1</h1>
		
		<input type="checkbox" name="Monday" checked >Monday</input></br>
		<input type="checkbox" name="Tuesday"  >Tuesday</input></br>
		<input type="checkbox" name="Wednesday"  >Wednesday</input></br>
		<input type="checkbox" name="Thursday"  >Thursday</input></br>
		<input type="checkbox" name="Friday"  >Friday</input></br>

		
		
		<!--*********************calander div*****************************-->
		<div>
			<table>
				<tr><td>Date Requested:</td> </tr>
				
				<tr><td>
					<img src="Calendar.jpg" alt="Calendar" width="100" height="100" />	
				</td></tr>							
			</table>
		</div>

		<!--*********************calander div*****************************-->
		</br></br>
		
		<div>
		<table>
			<tr><td> Will this be a Recurring Absence or Tardy?</td></tr>
		
		<!--drop down button-->
			<tr><td>
				<select>
					<option>Yes</option>
					<option>No</option>
				</select>
			</td></tr>
			
			<tr><td>How ofen?</td></tr>
			
		<!--drop down button-->
			<tr>
				<td>
					<select>
						<option>Daily</option>
						<option>Weekly</option>
						<option>Monthly</option>
					</select>
				</td>
			</tr>
		</table>
		</div>
	
	<!--*********************drop down bar div*****************************-->

		<table>
			<tr>					
				<td><label for="Reason">Reason:</label></td>
				<td><input type= "Reason" name="Reason" id="Reason"/></td>
			</tr>
			
			<tr>
				<td><label for="Additional_Option">Additional Info:</label></td>
				<td><input type= "Additional" name="Additional" id="Additional"/></td>	
			</tr>
			
			<tr>
				<td><button type="Upload">View Uploaded Document</button></td>				
			</tr>

		</table>
	<!--*********************End Button*****************************-->

				<h2>
					<button type="Back">Back</button>
					<button type="Pre">Pre</button>
					<button type="Approve">Approve</button>
					<button type="Deny">Deny and Message</button>
					<button type="Next">Next</button>
				<h2>
		</form>		
	</body>
	



</html>