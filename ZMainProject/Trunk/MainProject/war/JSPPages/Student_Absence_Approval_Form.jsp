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
			<a href="http://www.iastate.edu" title="PageTrail_AbsenceApprovalForm_Rehearsal_Performance">AbsenceApprovalForm(Rehearsal/Performance)</a>

		<!--HELP BUTTON-->	
		<a class="addthis_button"><img
        src="http://icons.iconarchive.com/icons/deleket/button/24/Button-Help-icon.png"
        width="16" height="16" border="0" alt="Share" /></a>
		</li>
	</h3>	

	<!--*********************info*****************************-->
	
		<h1>NetID, Student Name</h1>
		
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
				<td><label for="Additional_Option">Additional(Option):</label></td>
				<td><input type= "Additional" name="Additional" id="Additional"/></td>	
			</tr>
			
			<tr>
				<td><label for="UploadDocument">UploadDocument(Optional):</label></td>
				<td><input type= "UploadDocument" name="UploadDocument" id="UploadDocument"/></td>
				<td><button type="Browse">Browse</button></td>				
			</tr>

		</table>
	<!--*********************End Button*****************************-->

				<h2>
					<button type="Back">Back</button>
					<button type="Submit">Submit</button>
				<h2>
		</form>		
	</body>
	



</html>