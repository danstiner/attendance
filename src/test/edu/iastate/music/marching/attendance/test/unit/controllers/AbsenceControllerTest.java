package edu.iastate.music.marching.attendance.test.unit.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.AbsenceController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.EventController;
import edu.iastate.music.marching.attendance.controllers.FormController;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.util.Users;

@SuppressWarnings("deprecation")
public class AbsenceControllerTest extends AbstractTest {

	@Test
	public void testCreateAbsence() {
		/*
		 * Test to see if adding an absence actually works
		 */
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0400");
		Date eventEnd = makeDate("2012-06-16 0600");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);
		train.getAbsenceController().createOrUpdateAbsence(student, eventStart,
				eventEnd);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getStart().equals(eventStart));
		assertTrue(absence.getEnd().equals(eventEnd));
		assertTrue(absence.getType() == Absence.Type.Absence);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateAbsenceFail() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0400");
		Date eventEnd = makeDate("2012-06-16 0300");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);
		train.getAbsenceController().createOrUpdateAbsence(student, eventStart,
				eventEnd);
	}

	@Test
	public void testAbsenceVsAbsenceSameTime() {
		/*
		 * When we have two absences at the same time only one of them should be
		 * added
		 */
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0500");
		Date eventEnd = makeDate("2012-06-16 0700");

		Date contesterStart = makeDate("2012-06-16 0500");
		Date contesterEnd = makeDate("2012-06-16 0700");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		train.getAbsenceController().createOrUpdateAbsence(student, eventStart,
				eventEnd);
		train.getAbsenceController().createOrUpdateAbsence(student,
				contesterStart, contesterEnd);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getStart().equals(eventStart));
		assertTrue(absence.getEnd().equals(eventEnd));
		assertTrue(absence.getType() == Absence.Type.Absence);
	}

	@Test
	public void testAbsenceVsAbsenceDiffTime() {
		/*
		 * When we have two absences at different times for different events
		 * they should both remain
		 */
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0500");
		Date eventEnd = makeDate("2012-06-16 0700");

		Date contesterStart = makeDate("2012-06-16 0800");
		Date contesterEnd = makeDate("2012-06-16 0900");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);
		train.getEventController().createOrUpdate(Event.Type.Performance,
				contesterStart, contesterEnd);

		train.getAbsenceController().createOrUpdateAbsence(student, eventStart,
				eventEnd);
		train.getAbsenceController().createOrUpdateAbsence(student,
				contesterStart, contesterEnd);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student);

		assertEquals(2, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getStart().equals(eventStart)
				|| absence.getStart().equals(contesterStart));
		assertTrue(absence.getEnd().equals(eventEnd)
				|| absence.getEnd().equals(contesterEnd));
		assertTrue(absence.getType() == Absence.Type.Absence);

		absence = studentAbsences.get(1);

		assertTrue(absence.getStart().equals(eventStart)
				|| absence.getStart().equals(contesterStart));
		assertTrue(absence.getEnd().equals(eventEnd)
				|| absence.getEnd().equals(contesterEnd));
		assertTrue(absence.getType() == Absence.Type.Absence);
	}

	@Test
	public void testAbsenceVsTardySameEvent() {
		/*
		 * If we have an absence and a tardy added where the tardy happens
		 * during the absence, the absence should be removed and the tardy
		 * should remain. This test adds the absence first and the tardy second
		 */
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0500");
		Date eventEnd = makeDate("2012-06-16 0700");
		Date tardy = makeDate("2012-06-16 0515");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		train.getAbsenceController().createOrUpdateAbsence(student, eventStart,
				eventEnd);
		train.getAbsenceController().createOrUpdateTardy(student, tardy);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getStart().equals(tardy));
		assertTrue(absence.getType() == Absence.Type.Tardy);
	}

	@Test
	public void testAbsenceVsTardyDiffEvent() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date event1Start = makeDate("2012-06-16 0500");
		Date event1End = makeDate("2012-06-16 0700");

		Date event2Start = makeDate("2012-07-16 0500");
		Date event2End = makeDate("2012-07-16 0700");

		Date tardyDate = makeDate("2012-07-16 0515");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				event1Start, event1End);
		train.getEventController().createOrUpdate(Event.Type.Performance,
				event2Start, event2End);

		train.getAbsenceController().createOrUpdateAbsence(student,
				event1Start, event1End);
		train.getAbsenceController().createOrUpdateTardy(student, tardyDate);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student);

		assertEquals(2, studentAbsences.size());
		Absence absence = (studentAbsences.get(0).getType() == Absence.Type.Absence) ? studentAbsences
				.get(0) : studentAbsences.get(1);
		Absence tardy = (studentAbsences.get(0).getType() == Absence.Type.Tardy) ? studentAbsences
				.get(0) : studentAbsences.get(1);

		assertTrue(absence.getStart().equals(event1Start));
		assertTrue(absence.getEnd().equals(event1End));

		assertTrue(tardy.getStart().equals(tardyDate));
	}

	@Test
	public void testAbsenceVsEarlySameEvent() {
		/*
		 * If we have an absence and an early checkout added during the same
		 * interval, the absence should be removed and the early checkout should
		 * remain
		 */
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0500");
		Date eventEnd = makeDate("2012-06-16 0700");
		Date early = makeDate("2012-06-16 0515");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		train.getAbsenceController().createOrUpdateAbsence(student, eventStart,
				eventEnd);
		train.getAbsenceController()
				.createOrUpdateEarlyCheckout(student, early);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getStart().equals(early));
		assertTrue(absence.getType() == Absence.Type.EarlyCheckOut);
	}

	@Test
	public void testAbsenceVsEarlyDiffEvent() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date event1Start = makeDate("2012-06-16 0500");
		Date event1End = makeDate("2012-06-16 0700");

		Date event2Start = makeDate("2012-07-16 0500");
		Date event2End = makeDate("2012-07-16 0700");

		Date earlyDate = makeDate("2012-07-16 0515");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				event1Start, event1End);
		train.getEventController().createOrUpdate(Event.Type.Performance,
				event2Start, event2End);

		train.getAbsenceController().createOrUpdateAbsence(student,
				event1Start, event1End);
		train.getAbsenceController().createOrUpdateEarlyCheckout(student,
				earlyDate);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student);

		assertEquals(2, studentAbsences.size());
		Absence absence = (studentAbsences.get(0).getType() == Absence.Type.Absence) ? studentAbsences
				.get(0) : studentAbsences.get(1);
		Absence early = (studentAbsences.get(0).getType() == Absence.Type.EarlyCheckOut) ? studentAbsences
				.get(0) : studentAbsences.get(1);

		assertTrue(absence.getStart().equals(event1Start));
		assertTrue(absence.getEnd().equals(event1End));

		assertTrue(early.getStart().equals(earlyDate));

	}

	@Test
	public void testTardyVsAbsence() {
		/*
		 * This test is the same idea as the testAbsenceVsTardy method only it
		 * adds the tardy first
		 */
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0500");
		Date eventEnd = makeDate("2012-06-16 0700");
		Date tardy = makeDate("2012-06-16 0515");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		train.getAbsenceController().createOrUpdateTardy(student, tardy);
		train.getAbsenceController().createOrUpdateAbsence(student, eventStart,
				eventEnd);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getStart().equals(tardy));
		assertTrue(absence.getType() == Absence.Type.Tardy);
	}

	@Test
	public void testTardyVsTardySameEvent() {
		/*
		 * If we have two tardies added for the same event we need to take the
		 * later tardy. This method checks adding the earlier tardy before the
		 * later tardy and vice versa
		 */
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student1 = Users.createStudent(uc, "student1", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);
		User student2 = Users.createStudent(uc, "student2", "123456782",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0500");
		Date eventEnd = makeDate("2012-06-16 0700");

		Date tardy = makeDate("2012-06-16 0515");
		Date tardyLate = makeDate("2012-06-16 0520");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);
		// Adding the early tardy before the late tardy
		train.getAbsenceController().createOrUpdateTardy(student1, tardy);
		train.getAbsenceController().createOrUpdateTardy(student1, tardyLate);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student1);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getStart().equals(tardyLate));
		assertTrue(absence.getType() == Absence.Type.Tardy);

		// Adding the late tardy before the early tardy
		train.getAbsenceController().createOrUpdateTardy(student2, tardyLate);
		train.getAbsenceController().createOrUpdateTardy(student2, tardy);

		List<Absence> student2Absences = train.getAbsenceController().get(
				student1);

		assertEquals(1, studentAbsences.size());
		Absence absence2 = student2Absences.get(0);

		assertTrue(absence2.getStart().equals(tardyLate));
		assertTrue(absence2.getType() == Absence.Type.Tardy);
	}

	@Test
	public void testTardyVsTardyDiffEvent() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student1 = Users.createStudent(uc, "student1", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);
		;

		Date event1Start = makeDate("2012-06-16 0500");
		Date event1End = makeDate("2012-06-16 0700");

		Date event2Start = makeDate("2012-06-16 0800");
		Date event2End = makeDate("2012-06-16 0900");

		Date tardy = makeDate("2012-06-16 0515");
		Date otherTardy = makeDate("2012-06-16 0820");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				event1Start, event1End);
		train.getEventController().createOrUpdate(Event.Type.Performance,
				event2Start, event2End);

		train.getAbsenceController().createOrUpdateTardy(student1, tardy);
		train.getAbsenceController().createOrUpdateTardy(student1, otherTardy);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student1);

		assertEquals(2, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getStart().equals(tardy)
				|| absence.getStart().equals(otherTardy));
		assertTrue(absence.getType() == Absence.Type.Tardy);

		absence = studentAbsences.get(1);

		assertTrue(absence.getStart().equals(tardy)
				|| absence.getStart().equals(otherTardy));
		assertTrue(absence.getType() == Absence.Type.Tardy);
	}

	@Test
	public void testTardyVsEarly() {
		/*
		 * This method tests these four cases: Case 1: don't overlap, both
		 * should remain. a: Adding Tardy first b: Adding EarlyCheckout first
		 * 
		 * Case 2: do overlap, become an absence a: Adding Tardy first b: Adding
		 * EarlyCheckout first
		 */

		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User nonOverTardyFirst = Users.createStudent(uc, "student1",
				"123456780", "First", "last", 2, "major", User.Section.AltoSax);
		User nonOverEarlyFirst = Users.createStudent(uc, "student2",
				"123456789", "First", "last", 2, "major", User.Section.AltoSax);

		User overlapTardyFirst = Users.createStudent(uc, "student3",
				"123456781", "First", "last", 2, "major", User.Section.AltoSax);
		User overlapEarlyFirst = Users.createStudent(uc, "student4",
				"123456782", "First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0500");
		Date eventEnd = makeDate("2012-06-16 0700");

		Date tardyDate = makeDate("2012-06-16 0515");
		Date earlyNon = makeDate("2012-06-16 0650");
		Date earlyOverlap = makeDate("2012-06-16 0510");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);
		// Case 1.a
		train.getAbsenceController().createOrUpdateTardy(nonOverTardyFirst,
				tardyDate);
		train.getAbsenceController().createOrUpdateEarlyCheckout(
				nonOverTardyFirst, earlyNon);

		List<Absence> nonOverTardyFirstAbsences = train.getAbsenceController()
				.get(nonOverTardyFirst);

		assertEquals(2, nonOverTardyFirstAbsences.size());
		Absence tardy = (nonOverTardyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ? nonOverTardyFirstAbsences
				.get(0) : nonOverTardyFirstAbsences.get(1);
		Absence earlyCheckOut = (nonOverTardyFirstAbsences.get(0).getType() == Absence.Type.EarlyCheckOut) ? nonOverTardyFirstAbsences
				.get(0) : nonOverTardyFirstAbsences.get(1);

		assertTrue(tardy.getType() == Absence.Type.Tardy);
		assertTrue(tardy.getStart().equals(tardyDate));

		assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
		assertTrue(earlyCheckOut.getStart().equals(earlyNon));

		// Case 1.b
		train.getAbsenceController().createOrUpdateEarlyCheckout(
				nonOverEarlyFirst, earlyNon);
		train.getAbsenceController().createOrUpdateTardy(nonOverEarlyFirst,
				tardyDate);

		List<Absence> nonOverEarlyFirstAbsences = train.getAbsenceController()
				.get(nonOverEarlyFirst);

		assertEquals(2, nonOverEarlyFirstAbsences.size());
		tardy = (nonOverEarlyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ? nonOverEarlyFirstAbsences
				.get(0) : nonOverEarlyFirstAbsences.get(1);
		earlyCheckOut = (nonOverEarlyFirstAbsences.get(0).getType() == Absence.Type.EarlyCheckOut) ? nonOverEarlyFirstAbsences
				.get(0) : nonOverEarlyFirstAbsences.get(1);

		assertTrue(tardy.getType() == Absence.Type.Tardy);
		assertTrue(tardy.getStart().equals(tardyDate));

		assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
		assertTrue(earlyCheckOut.getStart().equals(earlyNon));

		// Case 2.a
		train.getAbsenceController().createOrUpdateTardy(overlapTardyFirst,
				tardyDate);
		train.getAbsenceController().createOrUpdateEarlyCheckout(
				overlapTardyFirst, earlyOverlap);

		List<Absence> overlapTardyFirstAbsences = train.getAbsenceController()
				.get(overlapTardyFirst);

		assertEquals(1, overlapTardyFirstAbsences.size());
		Absence createdAbsence = overlapTardyFirstAbsences.get(0);

		assertTrue(createdAbsence.getType() == Absence.Type.Absence);
		assertEquals(eventStart, createdAbsence.getStart());
		assertEquals(eventEnd, createdAbsence.getEnd());

		// Case 2.b
		train.getAbsenceController().createOrUpdateEarlyCheckout(
				overlapEarlyFirst, earlyOverlap);
		train.getAbsenceController().createOrUpdateTardy(overlapEarlyFirst,
				tardyDate);

		List<Absence> overlapEarlyFirstAbsences = train.getAbsenceController()
				.get(overlapEarlyFirst);

		assertEquals(1, overlapEarlyFirstAbsences.size());
		createdAbsence = overlapEarlyFirstAbsences.get(0);

		assertTrue(createdAbsence.getType() == Absence.Type.Absence);
		assertEquals(eventStart, createdAbsence.getStart());
		assertEquals(eventEnd, createdAbsence.getEnd());
	}

	@Test
	public void testEarlyVsAbsence() {
		/*
		 * Same as the testAbsenceVsEarly method only this adds the Early
		 * checkout before the absence
		 */
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0500");
		Date eventEnd = makeDate("2012-06-16 0700");
		Date early = makeDate("2012-06-16 0515");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		train.getAbsenceController()
				.createOrUpdateEarlyCheckout(student, early);
		train.getAbsenceController().createOrUpdateAbsence(student, eventStart,
				eventEnd);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getStart().equals(early));
		assertTrue(absence.getType() == Absence.Type.EarlyCheckOut);
	}

	@Test
	public void testEarlyVsEarlySameEvent() {
		/*
		 * If we have two early checkouts added for the same time zone we need
		 * to take the earlier of the two and remove the later. This method
		 * checks adding the later checkout before the earlier checkout and vice
		 * versa
		 */

		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student1 = Users.createStudent(uc, "student1", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);
		User student2 = Users.createStudent(uc, "student2", "123456780",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0500");
		Date eventEnd = makeDate("2012-06-16 0700");

		Date early = makeDate("2012-06-16 0550");
		Date earlyEarlier = makeDate("2012-06-16 0545");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);
		// Adding the early checkout before the earlier checkout
		train.getAbsenceController().createOrUpdateEarlyCheckout(student1,
				early);
		train.getAbsenceController().createOrUpdateEarlyCheckout(student1,
				earlyEarlier);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student1);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getStart().equals(earlyEarlier));
		assertTrue(absence.getType() == Absence.Type.EarlyCheckOut);

		// Adding the earlier early before the early
		train.getAbsenceController().createOrUpdateEarlyCheckout(student2,
				earlyEarlier);
		train.getAbsenceController().createOrUpdateEarlyCheckout(student2,
				early);

		List<Absence> student2Absences = train.getAbsenceController().get(
				student1);

		assertEquals(1, studentAbsences.size());
		Absence absence2 = student2Absences.get(0);

		assertTrue(absence2.getStart().equals(earlyEarlier));
		assertTrue(absence2.getType() == Absence.Type.EarlyCheckOut);
	}

	@Test
	public void testEarlyVsEarlyDiffEvent() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		User student1 = Users.createStudent(uc, "student1", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date event1Start = makeDate("2012-06-16 0500");
		Date event1End = makeDate("2012-06-16 0700");

		Date event2Start = makeDate("2012-06-16 0800");
		Date event2End = makeDate("2012-06-16 0900");

		Date early = makeDate("2012-06-16 0550");
		Date otherEarly = makeDate("2012-06-16 0845");

		train.getEventController().createOrUpdate(Event.Type.Performance,
				event1Start, event1End);
		train.getEventController().createOrUpdate(Event.Type.Performance,
				event2Start, event2End);

		train.getAbsenceController().createOrUpdateEarlyCheckout(student1,
				early);
		train.getAbsenceController().createOrUpdateEarlyCheckout(student1,
				otherEarly);

		List<Absence> studentAbsences = train.getAbsenceController().get(
				student1);

		assertEquals(2, studentAbsences.size());

		Absence absence = studentAbsences.get(0);
		assertTrue(absence.getStart().equals(early)
				|| absence.getStart().equals(otherEarly));
		assertTrue(absence.getType() == Absence.Type.EarlyCheckOut);

		absence = studentAbsences.get(0);
		assertTrue(absence.getStart().equals(early)
				|| absence.getStart().equals(otherEarly));
		assertTrue(absence.getType() == Absence.Type.EarlyCheckOut);
	}

	
	@Test
	public void testApproveAbsence() {
		DataTrain train = getDataTrain();
		
		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "student1", "123456789", "First", "last", 2, "major", User.Section.AltoSax);

		Date start = makeDate("2012-06-16 0500");
		Date end = makeDate("2012-06-16 0700");
		
		train.getEventController().createOrUpdate(Event.Type.Performance, start, end);
		Absence abs = train.getAbsenceController().createOrUpdateAbsence(student, start, end);
		abs.setStatus(Absence.Status.Approved);
		train.getAbsenceController().updateAbsence(abs);
		
		List<Absence> studentAbs = train.getAbsenceController().get(student);
		assertEquals(1, studentAbs.size());
		
		Absence absence = studentAbs.get(0);
		assertTrue(absence.getStart().equals(start));
		assertTrue(absence.getEnd().equals(end));
		assertTrue(absence.getType() == Absence.Type.Absence);
		assertTrue(absence.getStatus() == Absence.Status.Approved);		
	}
	
	@Test
	public void testApprovedAbsenceDominatesAbsence() {
		DataTrain train = getDataTrain();
		
		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "student", "123456789", "First", "last", 2, "major", User.Section.AltoSax);
		User student1 = Users.createStudent(uc, "student1", "123456780", "First", "last", 2, "major", User.Section.AltoSax);

		
		Date start = makeDate("2012-06-16 0500");
		Date end = makeDate("2012-06-16 0600");		
		
		//Approved saved first
		train.getEventController().createOrUpdate(Event.Type.Performance, start, end);
		Absence abs = train.getAbsenceController().createOrUpdateAbsence(student, start, end);
		abs.setStatus(Absence.Status.Approved);
		train.getAbsenceController().updateAbsence(abs);
		
		train.getAbsenceController().createOrUpdateAbsence(student, start, end);
		
		List<Absence> studentAbs = train.getAbsenceController().get(student);
		assertEquals(1, studentAbs.size());
		
		Absence absence = studentAbs.get(0);
		assertTrue(absence.getStart().equals(start));
		assertTrue(absence.getEnd().equals(end));
		assertTrue(absence.getType() == Absence.Type.Absence);
		assertTrue(absence.getStatus() == Absence.Status.Approved);	
		
		//Approved saved second
		train.getAbsenceController().createOrUpdateAbsence(student1, start, end);
		abs = train.getAbsenceController().createOrUpdateAbsence(student1, start, end);
		abs.setStatus(Absence.Status.Approved);
		train.getAbsenceController().updateAbsence(abs);
		
		studentAbs = train.getAbsenceController().get(student);
		assertEquals(1, studentAbs.size());
		
		absence = studentAbs.get(0);
		assertTrue(absence.getStart().equals(start));
		assertTrue(absence.getEnd().equals(end));
		assertTrue(absence.getType() == Absence.Type.Absence);
		assertTrue(absence.getStatus() == Absence.Status.Approved);
	}
	
	@Test
	public void testApprovedAbsenceDominatesTardy() {
		DataTrain train = getDataTrain();
		
		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "student", "123456789", "First", "last", 2, "major", User.Section.AltoSax);
		User student1 = Users.createStudent(uc, "student1", "123456780", "First", "last", 2, "major", User.Section.AltoSax);

		
		Date start = makeDate("2012-06-16 0500");
		Date tardyStart = makeDate("2012-06-16 0515");
		Date end = makeDate("2012-06-16 0600");		
		
		//Approved saved first
		train.getEventController().createOrUpdate(Event.Type.Performance, start, end);
		Absence abs = train.getAbsenceController().createOrUpdateAbsence(student, start, end);
		abs.setStatus(Absence.Status.Approved);
		train.getAbsenceController().updateAbsence(abs);
		
		train.getAbsenceController().createOrUpdateTardy(student, tardyStart);
		
		List<Absence> studentAbs = train.getAbsenceController().get(student);
		assertEquals(1, studentAbs.size());
		
		Absence absence = studentAbs.get(0);
		assertTrue(absence.getStart().equals(start));
		assertTrue(absence.getEnd().equals(end));
		assertTrue(absence.getType() == Absence.Type.Absence);
		assertTrue(absence.getStatus() == Absence.Status.Approved);	
		
		//Approved saved second
		train.getAbsenceController().createOrUpdateTardy(student, tardyStart);
		abs = train.getAbsenceController().createOrUpdateAbsence(student1, start, end);
		abs.setStatus(Absence.Status.Approved);
		train.getAbsenceController().updateAbsence(abs);
		
		studentAbs = train.getAbsenceController().get(student);
		assertEquals(1, studentAbs.size());
		
		absence = studentAbs.get(0);
		assertTrue(absence.getStart().equals(start));
		assertTrue(absence.getEnd().equals(end));
		assertTrue(absence.getType() == Absence.Type.Absence);
		assertTrue(absence.getStatus() == Absence.Status.Approved);
	}
	
	@Test
	public void testApprovedAbsenceDominatesEarly() {
		DataTrain train = getDataTrain();
		
		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "student", "123456789", "First", "last", 2, "major", User.Section.AltoSax);
		User student1 = Users.createStudent(uc, "student1", "123456780", "First", "last", 2, "major", User.Section.AltoSax);

		
		Date start = makeDate("2012-06-16 0500");
		Date tardyStart = makeDate("2012-06-16 0515");
		Date end = makeDate("2012-06-16 0600");		
		
		//Approved saved first
		train.getEventController().createOrUpdate(Event.Type.Performance, start, end);
		Absence abs = train.getAbsenceController().createOrUpdateAbsence(student, start, end);
		abs.setStatus(Absence.Status.Approved);
		train.getAbsenceController().updateAbsence(abs);
		
		train.getAbsenceController().createOrUpdateEarlyCheckout(student, tardyStart);
		
		List<Absence> studentAbs = train.getAbsenceController().get(student);
		assertEquals(1, studentAbs.size());
		
		Absence absence = studentAbs.get(0);
		assertTrue(absence.getStart().equals(start));
		assertTrue(absence.getEnd().equals(end));
		assertTrue(absence.getType() == Absence.Type.Absence);
		assertTrue(absence.getStatus() == Absence.Status.Approved);	
		
		//Approved saved second
		train.getAbsenceController().createOrUpdateEarlyCheckout(student, tardyStart);
		abs = train.getAbsenceController().createOrUpdateAbsence(student1, start, end);
		abs.setStatus(Absence.Status.Approved);
		train.getAbsenceController().updateAbsence(abs);
		
		studentAbs = train.getAbsenceController().get(student);
		assertEquals(1, studentAbs.size());
		
		absence = studentAbs.get(0);
		assertTrue(absence.getStart().equals(start));
		assertTrue(absence.getEnd().equals(end));
		assertTrue(absence.getType() == Absence.Type.Absence);
		assertTrue(absence.getStatus() == Absence.Status.Approved);
	}
	
	@Test
	public void testApprovedTardyVsAbsence() {
		/*
		 * This test is the same idea as the testAbsenceVsTardy method only it
		 * adds the tardy first
		 */
		DataTrain train = getDataTrain();
		
		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();

		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0500");
		Date eventEnd = makeDate("2012-06-16 0700");
		Date tardyDate = makeDate("2012-06-16 0515");

		ec.createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		Absence tardy = ac.createOrUpdateTardy(student, tardyDate);
		tardy.setStatus(Absence.Status.Approved);
		ac.updateAbsence(tardy);
		
		ac.createOrUpdateAbsence(student, eventStart,
				eventEnd);

		List<Absence> studentAbsences = ac.get(
				student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getStart().equals(tardyDate));
		assertTrue(absence.getType() == Absence.Type.Tardy);
		assertTrue(absence.getStatus() == Absence.Status.Approved);
	}

	
	@Test
	public void testApprovedTardyDominatesTardy() {
		DataTrain train = getDataTrain();
		
		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "student", "123456789", "First", "last", 2, "major", User.Section.AltoSax);
		User student1 = Users.createStudent(uc, "student1", "123456780", "First", "last", 2, "major", User.Section.AltoSax);

		
		Date start = makeDate("2012-06-16 0500");
		Date end = makeDate("2012-06-16 0600");		
		
		//Approved saved first
		train.getEventController().createOrUpdate(Event.Type.Performance, start, end);
		Absence abs = train.getAbsenceController().createOrUpdateTardy(student, start);
		abs.setStatus(Absence.Status.Approved);
		train.getAbsenceController().updateAbsence(abs);
		
		train.getAbsenceController().createOrUpdateTardy(student, start);
		
		List<Absence> studentAbs = train.getAbsenceController().get(student);
		assertEquals(1, studentAbs.size());
		
		Absence absence = studentAbs.get(0);
		assertTrue(absence.getStart().equals(start));
		assertTrue(absence.getType() == Absence.Type.Tardy);
		assertTrue(absence.getStatus() == Absence.Status.Approved);	
		
		//Approved saved second
		train.getAbsenceController().createOrUpdateTardy(student1, start);
		abs = train.getAbsenceController().createOrUpdateTardy(student1, start);
		abs.setStatus(Absence.Status.Approved);
		train.getAbsenceController().updateAbsence(abs);
		
		studentAbs = train.getAbsenceController().get(student);
		assertEquals(1, studentAbs.size());
		
		absence = studentAbs.get(0);
		assertTrue(absence.getStart().equals(start));
		assertTrue(absence.getType() == Absence.Type.Tardy);
		assertTrue(absence.getStatus() == Absence.Status.Approved);
	}
	
	@Test
	public void testApprovedTardyVsEarly() {
		/*
		 * This method tests these four cases: 
		 * Case 1: don't overlap, both should remain. 
		 * 		a: Adding Tardy first 
		 * 		b: Adding EarlyCheckout first
		 * 
		 * Case 2: do overlap, become an absence 
		 * 		a: Adding Tardy first 
		 * 		b: Adding EarlyCheckout first
		 */

		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		
		User nonOverTardyFirst = Users.createStudent(uc, "student1",
				"123456780", "First", "last", 2, "major", User.Section.AltoSax);
		User nonOverEarlyFirst = Users.createStudent(uc, "student2",
				"123456789", "First", "last", 2, "major", User.Section.AltoSax);

		User overlapTardyFirst = Users.createStudent(uc, "student3",
				"123456781", "First", "last", 2, "major", User.Section.AltoSax);
		User overlapEarlyFirst = Users.createStudent(uc, "student4",
				"123456782", "First", "last", 2, "major", User.Section.AltoSax);

		Date eventStart = makeDate("2012-06-16 0500");
		Date eventEnd = makeDate("2012-06-16 0700");

		Date tardyDate = makeDate("2012-06-16 0515");
		Date earlyNon = makeDate("2012-06-16 0650");
		Date earlyOverlap = makeDate("2012-06-16 0510");

		ec.createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);
		// Case 1.a
		Absence t = ac.createOrUpdateTardy(nonOverTardyFirst, tardyDate);
		t.setStatus(Absence.Status.Approved);
		ac.updateAbsence(t);
		ac.createOrUpdateEarlyCheckout(nonOverTardyFirst, earlyNon);

		List<Absence> nonOverTardyFirstAbsences = ac.get(nonOverTardyFirst);

		assertEquals(2, nonOverTardyFirstAbsences.size());
		Absence tardy = (nonOverTardyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ? nonOverTardyFirstAbsences
				.get(0) : nonOverTardyFirstAbsences.get(1);
		Absence earlyCheckOut = (nonOverTardyFirstAbsences.get(0).getType() == Absence.Type.EarlyCheckOut) ? nonOverTardyFirstAbsences
				.get(0) : nonOverTardyFirstAbsences.get(1);

		assertTrue(tardy.getType() == Absence.Type.Tardy);
		assertTrue(tardy.getStatus() == Absence.Status.Approved);
		assertTrue(tardy.getStart().equals(tardyDate));

		assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
		assertTrue(earlyCheckOut.getStatus() == Absence.Status.Pending);
		assertTrue(earlyCheckOut.getStart().equals(earlyNon));

		// Case 1.b
		ac.createOrUpdateEarlyCheckout(nonOverEarlyFirst, earlyNon);
		t = ac.createOrUpdateTardy(nonOverEarlyFirst, tardyDate);
		t.setStatus(Absence.Status.Approved);
		ac.updateAbsence(t);

		List<Absence> nonOverEarlyFirstAbsences = train.getAbsenceController()
				.get(nonOverEarlyFirst);

		assertEquals(2, nonOverEarlyFirstAbsences.size());
		tardy = (nonOverEarlyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ? nonOverEarlyFirstAbsences
				.get(0) : nonOverEarlyFirstAbsences.get(1);
		earlyCheckOut = (nonOverEarlyFirstAbsences.get(0).getType() == Absence.Type.EarlyCheckOut) ? nonOverEarlyFirstAbsences
				.get(0) : nonOverEarlyFirstAbsences.get(1);

		assertTrue(tardy.getType() == Absence.Type.Tardy);
		assertTrue(tardy.getStatus() == Absence.Status.Approved);
		assertTrue(tardy.getStart().equals(tardyDate));

		assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
		assertTrue(earlyCheckOut.getStatus() == Absence.Status.Pending);
		assertTrue(earlyCheckOut.getStart().equals(earlyNon));

		// Case 2.a
		t = ac.createOrUpdateTardy(overlapTardyFirst, tardyDate);
		t.setStatus(Absence.Status.Approved);
		ac.updateAbsence(t);
		
		ac.createOrUpdateEarlyCheckout(overlapTardyFirst, earlyOverlap);

		List<Absence> overlapTardyFirstAbsences = train.getAbsenceController().get(overlapTardyFirst);

		assertEquals(1, overlapTardyFirstAbsences.size());
		Absence createdAbsence = overlapTardyFirstAbsences.get(0);

		assertTrue(createdAbsence.getType() == Absence.Type.Absence);
		//TODO assertTrue(createdAbsence.getStatus() == Absence.Status.?);
		assertEquals(eventStart, createdAbsence.getStart());
		assertEquals(eventEnd, createdAbsence.getEnd());

		// Case 2.b
		ac.createOrUpdateEarlyCheckout(overlapEarlyFirst, earlyOverlap);
		ac.createOrUpdateTardy(overlapEarlyFirst, tardyDate);

		List<Absence> overlapEarlyFirstAbsences = train.getAbsenceController().get(overlapEarlyFirst);

		assertEquals(1, overlapEarlyFirstAbsences.size());
		createdAbsence = overlapEarlyFirstAbsences.get(0);

		assertTrue(createdAbsence.getType() == Absence.Type.Absence);
		//TODO assertTrue(createdAbsence.getStatus() == Absence.Status.?);
		assertEquals(eventStart, createdAbsence.getStart());
		assertEquals(eventEnd, createdAbsence.getEnd());
	}

	
	@Test
	public void testApprovedEarlyDominatesEarly() {
		DataTrain train = getDataTrain();
		
		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "student", "123456789", "First", "last", 2, "major", User.Section.AltoSax);
		User student1 = Users.createStudent(uc, "student1", "123456780", "First", "last", 2, "major", User.Section.AltoSax);

		
		Date start = makeDate("2012-06-16 0500");
		Date checkout = makeDate("2012-06-16 0550");
		Date end = makeDate("2012-06-16 0600");		
		
		//Approved saved first
		Event e = train.getEventController().createOrUpdate(Event.Type.Performance, start, end);
		Absence abs = train.getAbsenceController().createOrUpdateEarlyCheckout(student, checkout);
		abs.setStatus(Absence.Status.Approved);
		train.getAbsenceController().updateAbsence(abs);
		System.out.println(e);
		train.getAbsenceController().createOrUpdateEarlyCheckout(student, checkout);
		
		List<Absence> studentAbs = train.getAbsenceController().get(student);
		assertEquals(1, studentAbs.size());
		
		Absence absence = studentAbs.get(0);
		assertTrue(absence.getStart().equals(checkout));
		assertTrue(absence.getType() == Absence.Type.EarlyCheckOut);
		assertTrue(absence.getStatus() == Absence.Status.Approved);	
		
		//Approved saved second
		train.getAbsenceController().createOrUpdateTardy(student1, checkout);
		abs = train.getAbsenceController().createOrUpdateTardy(student1, checkout);
		abs.setStatus(Absence.Status.Approved);
		train.getAbsenceController().updateAbsence(abs);
		
		studentAbs = train.getAbsenceController().get(student);
		assertEquals(1, studentAbs.size());
		
		absence = studentAbs.get(0);
		assertTrue(absence.getStart().equals(checkout));
		assertTrue(absence.getType() == Absence.Type.EarlyCheckOut);
		assertTrue(absence.getStatus() == Absence.Status.Approved);
	}
	
	private Date makeDate(String sDate) {
		// Private method to make dates out of strings following the format I
		// always use
		try {
			return new SimpleDateFormat("yyyy-MM-dd HHmm").parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Add a form A, approve it, add an absence, check that it is approved.
	 */
	@Test
	public void testAutoApproveWithFormA() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");
		form.setStatus(Form.Status.Approved);
		fc.update(form);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);
		assertEquals(Absence.Status.Approved, a.getStatus());
	}
}
