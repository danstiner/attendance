package edu.iastate.music.marching.attendance.test.unit.controllers;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class AuthControllerTest extends AbstractTest{

	@Test
	public void testValidGoogleUsers() {
		DataTrain train = getDataTrain();
		//Maybe this should be in a ValidationUtil class?
		String validIaStateEmail = "bmaxwell@iastate.edu";
		String nonValidRealEmail = "bmaxwell921@gmail.com";
		String nonValidMadeUpEmail = "lkajslkfdjasdf@lkasdlkfj.com";
		
		assertTrue(ValidationUtil.validPrimaryEmail(new Email(validIaStateEmail), train));
		assertFalse(ValidationUtil.validPrimaryEmail(new Email(nonValidRealEmail), train));
		assertFalse(ValidationUtil.validPrimaryEmail(new Email(nonValidMadeUpEmail), train));
	}
	
}
