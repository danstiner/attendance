<%@ page contentType="text/html; charset=UTF-8" language="java"%>



<!DOCTYPE html>

<html>
<head>
<script src="/MobileApp/sha.js"/></script>
<script>
	
	/**
 	 *
 	 * Hashes the password for sending to the server.
	 * @author Curtis
	 *
	 */
	function hashData() {
		var str = Sha1.hash(document.getElementById("Password").value);
		str = str.toUpperCase();
		document.getElementById("Hashed Password").value = str;

		var str2 = Sha1.hash(document.getElementById("Re-Enter Password").value);
		str2 = str2.toUpperCase();
		document.getElementById("Re-Enter Hashed Password").value = str2;
		
		var str3 = Sha1.hash(document.getElementById("UniversityID").value);
		str3 = str3.toUpperCase();
		document.getElementById("Hashed UniversityID").value = str3;
		
		return true;
	}
	
</script>

<title>@10Dance</title>
</head>
<!-- <form> name=form1 action="MainProjectServlet" method="GET"</form> -->
<body>
	<h1>ISU Varsity Marching Band / Spring 2012</h1>
	<h2>Registration</h2>
	<form action="/register" method="post" onSubmit="return hashData();" accept-charset="uft-8">
		<table>
			<tr>
				<td><label for="FirstName">FirstName</label></td>
				<td><input type="text" name="FirstName" id="FirstName" /></td>
			</tr>

			<tr>
				<td><label for="LastName">LastName</label></td>
				<td><input type="text" name="LastName" id="LastName" /></td>
			</tr>
			
			<tr>
				<td><label for="NetID">NetID</label></td>
				<td><input type="text" name="NetID" id="NetID" /></td>
			</tr>
			
			<tr>
				<td><label for="UniversityID">UniversityID</label></td>
				<td><input type="text" name="UniversityID" id="UniversityID" /></td>
			</tr>

			<tr>
				<td><label for="Password">Password</label></td>
				<td><input type="password" name="Password" id="Password" /></td>
			</tr>

			<tr>
				<td><label for="Re-Enter Password">Re-Enter Password</label></td>
				<td><input type="password" name="Re-Enter Password" 
					id="Re-Enter Password" /></td>
			</tr>
			
			<tr>
				<td><input type="password" name="Re-Enter Hashed Password" id="Re-Enter Hashed Password" hidden=true/>
				<input type="password" name="Hashed Password" id="Hashed Password" hidden=true/>
				<input type="text" name="Hashed UniversityID" id="Hashed UniversityID" hidden=true/></td>
			</tr>
			
		</table>
		<input type="submit" value="Register" name="Register" />
	</form>
	
</body>

</html>