<%@ page contentType="text/html; charset=UTF-8" language="java" %>



<!DOCTYPE html>

<html>
	<head>
		<title>@10Dance</title>
	</head>
	<!-- <form> name=form1 action="MainProjectServlet" method="GET"</form> -->
	<body>
		<h1>ISU Varsity Marching Band / Spring 2012</h1>
		<form action ="/login" method ="post" accept-charset="uft-8">
				<table>
					<tr>
					
						<td><label for="User Name">UserName</label></td>
						<td><input type= "text" name="User Name" id="User Name"/></td>
					</tr>
					
					<tr>
						<td><label for="Password">Password</label></td>
						<td><input type= "password" name="Password" id="Password"/></td>	
					</tr>
				</table>
				
				<input type="submit" value="Register" name="Register"/>
				<input type="submit" value="Login" name ="Login"/>
		</form>		
	</body>
	



</html>