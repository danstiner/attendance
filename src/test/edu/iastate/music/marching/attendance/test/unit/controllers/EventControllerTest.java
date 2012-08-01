package edu.iastate.music.marching.attendance.test.unit.controllers;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.AbsenceController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.EventController;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.util.Users;

public class EventControllerTest extends AbstractTest {

	@Test
	public void testCreateEvent() throws ParseException {
		// Arrange
		DataTrain train = getDataTrain();

		Date eventStart = null;
		Date eventEnd = null;

		eventStart = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0500");
		eventEnd = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0700");

		// Act
		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		// Assert
		List<Event> events = train.getEventController().getAll();

		assertEquals(1, events.size());

		Event e = events.get(0);

		assertEquals(eventStart, e.getStart());
		assertEquals(eventEnd, e.getEnd());
		assertEquals(Event.Type.Performance, e.getType());
	}

	@Test
	public void testCreateDuplicateEvents() throws ParseException {

		// Arrange
		DataTrain train = getDataTrain();

		Date eventStart = null;
		Date eventEnd = null;

		eventStart = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0500");
		eventEnd = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0700");

		// Act
		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);
		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		// Assert
		List<Event> events = train.getEventController().getAll();

		assertEquals(1, events.size());

		Event e = events.get(0);

		assertEquals(eventStart, e.getStart());
		assertEquals(eventEnd, e.getEnd());
		assertEquals(Event.Type.Performance, e.getType());
	}

	@Test
	public void testAutomaticLinking() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();

		User s1 = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		Calendar start = Calendar.getInstance();
		start.set(2012, 9, 18, 16, 30);
		Calendar end = Calendar.getInstance();
		end.set(2012, 9, 18, 17, 50);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 9, 18, 16, 40);
		Absence a1 = ac.createOrUpdateEarlyCheckout(s1, tardy.getTime());

		uc.update(s1);

		// there's a tardy, but it's not linked
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		Event e1 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		// now that there's a matching event, it should link
		assertEquals(User.Grade.Aminus, uc.get(s1.getId()).getGrade());

		tardy.set(2012, 9, 18, 16, 35);
		ac.createOrUpdateTardy(s1, tardy.getTime());
		uc.update(s1);
		assertEquals(User.Grade.Bplus, uc.get(s1.getId()).getGrade());
	}

	@Test
	public void testAutomaticAbsenceLinking() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();

		User s1 = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		Calendar start = Calendar.getInstance();
		start.set(2012, 9, 18, 16, 30);
		Calendar end = Calendar.getInstance();
		end.set(2012, 9, 18, 17, 50);

		// we test the deprecated method because the mobile app uses it, and you
		// can't create an absence otherwise without an event
		ac.createOrUpdateAbsence(s1, start.getTime(), end.getTime());
		uc.update(s1);

		assertEquals(1, ac.get(s1).size());

		// there's an absence, but it's not linked
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(), end.getTime());
		// now that there's a matching event, it should link
		assertEquals(User.Grade.B, uc.get(s1.getId()).getGrade());

		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 1);
		ac.createOrUpdateAbsence(s1, start.getTime(), end.getTime());

		assertEquals(2, ac.get(s1).size());

		// duplicate. But we can't exclude it automatically because it could
		// always be a performance OR rehearsal....
		ac.createOrUpdateAbsence(s1, start.getTime(), end.getTime());
		assertEquals(3, ac.get(s1).size());

		ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());

		// now the absences are excluded
		assertEquals(2, ac.get(s1).size());
		// TODO there's gotta be a better way to verify that they linked
		// correctly
		assertEquals(User.Grade.D, uc.get(s1.getId()).getGrade());
		start.add(Calendar.DATE, 17);
		end.add(Calendar.DATE, 17);
		ac.createOrUpdateAbsence(s1, start.getTime(), end.getTime());
		assertEquals(3, ac.get(s1).size());

		Absence a = ac.createOrUpdateAbsence(
				s1,
				ec.createOrUpdate(Event.Type.Performance, start.getTime(),
						end.getTime()));
		assertEquals(3, ac.get(s1).size());

		// TODO there's gotta be a better way to verify that they linked
		// correctly
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());
	}

}
