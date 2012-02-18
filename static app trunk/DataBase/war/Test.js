// <script type="text/javascript" src="jquery.js">
//		</script> 
//		<script type="javascript">
//			
/************************************************************************/
// if the local storage comes up empty, reset count to zero
function initializeID() 
{
	var lastID = localStorage.getItem( 'lastID' );
	if ( lastID == null ) {
		location.reload(true);
		localStorage.setItem( 'lastID',0 );
	}
}


/************************************************************************/
function validate() 
{
	$("#save").validate({
		// using the jQuery Validate plugin here
		invalidHandler: function() {
			alert("Please fill in all of the information.");
		},
		// continue doing stuff if all fields validate
		submitHandler: function(form) {
			// get input field vars
			var fname = $('input[name="fname"]').val();
				lname = $('input[name="lname"]').val();
				email = $('input[name="email"]').val();
				somethingsecret = $('input[name="somethingsecret"]').val();
			// convert the ID to a string so it can be used as a key
			var formSaveID = uniqueID.toString();
			// assign everything to an object for local storage
			var newFormSave = {};
				newFormSave.id = uniqueID;
				newFormSave.fname = fname;
				newFormSave.lname = lname;
				newFormSave.email = email;
				newFormSave.somethingsecret = somethingsecret;
			// turn data into JSON string
			localStorage.setItem( formSaveID, JSON.stringify( newFormSave ) );
			// reload history with new entry; see below
			loadHistory(uniqueID);
			// increment the ID
			uniqueID++;
			// save next available ID (note: bugs/features in iOS
			// demand
			// the key be removed first, and then reset; a straight
			// overwrite using the same key does not work)
			localStorage.removeItem( 'lastID' );
			localStorage.setItem( 'lastID', uniqueID );
			// optional; this part clears all of the values in the
			// form
			$form.find('input[type="text"],input[type="email"]').val("");
			// again optional; this is where you could actually post
			// via Ajax
			return false;
		}
	});
}

/************************************************************************/
function loadHistory(uniqueID) 
{
// clear table and append table header row
$("table#history").html("").append("<tr><th>First Name</th><th>Last Name</th><th>E-mail</th></tr>");
	// for the loop, start at the beginning
	ID = 0;
	// the loop count is the same as the last unique ID;
	// if the last record saved had the ID of 57, then
	// there are 57 records to display
	while (ID<=uniqueID) {
		ID.toString();
		// pull the JSON string for the current ID in the loop
		// and extract data into variables
		var history = localStorage.getItem(ID);
			fname = $.evalJSON(history).fname;
			lname = $.evalJSON(history).lname;
			email = $.evalJSON(history).email;
		// render a row of data for each record
		$("table#history").append('<tr class="' + ID + '"><td>' + fname + '</td><td>' + lname + '</td><td>' + email + '</td></tr>');
		ID++;
	} // ends loop
}

/************************************************************************/
// call the other functions
function functionCaller()
{
	// grab the next available ID from storage
	var lastID = localStorage.getItem( 'lastID' );
	initializeID();
	// before the calling storing function, create a uniqueID
	// variable
	var uniqueID = lastID;
	validate();
	loadHistory(uniqueID);
}


// </script>
