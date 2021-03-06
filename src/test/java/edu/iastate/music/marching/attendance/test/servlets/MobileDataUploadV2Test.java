package edu.iastate.music.marching.attendance.test.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import edu.iastate.music.marching.attendance.Lang;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.servlets.MobileAppDataServlet;
import edu.iastate.music.marching.attendance.testlib.AbstractDatastoreTest;
import edu.iastate.music.marching.attendance.testlib.ServletMocks;
import edu.iastate.music.marching.attendance.testlib.Config;
import edu.iastate.music.marching.attendance.testlib.Users;
import edu.iastate.music.marching.attendance.util.DateTimeConverter;
import edu.iastate.music.marching.attendance.util.UploadAbsence;
import edu.iastate.music.marching.attendance.util.UploadEvent;

public class MobileDataUploadV2Test extends AbstractDatastoreTest {

	private static final String SIMPLE_ABSENCE_TESTDATA_V2 = "[ { \"absences\": [ { \"type\": \"Absence\", \"netid\": \"ehayles\" }, { \"type\": \"Absence\", \"netid\": \"alusk\" } ], \"type\": \"Rehearsal\", \"startDateTime\": \"2012-05-03T16:30:00.000Z\", \"endDateTime\": \"2012-05-03T17:50:00.000Z\" } ]";
	private static final String SIMPLE_TARDY_TESTDATA_V2 = "[ { \"absences\": [ { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:09:00.000Z\", \"netid\": \"ehayles\" }, { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:16:00.000Z\", \"netid\": \"ehayles\" }, { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:08:00.000Z\", \"netid\": \"jbade\" }, { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:07:00.000Z\", \"netid\": \"ehayles\" }, { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:08:00.000Z\", \"netid\": \"ehayles\" }, { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:08:00.000Z\", \"netid\": \"alusk\" } ], \"type\": \"Rehearsal\", \"startDateTime\": \"2012-05-03T16:30:00.000Z\", \"endDateTime\": \"2012-05-03T17:50:00.000Z\" } ]";
	private static final String SIMPLE_SINGLE_TARDY_STRING = "[{\"absences\":[{\"type\":\"Absence\",\"netid\":\"ehayles\"}],\"type\":\"Rehearsal\",\"startDateTime\":\"2012-05-03T16:30:00.000Z\",\"endDateTime\":\"2012-05-03T17:50:00.000Z\"}]";

	public class DateTimeWrapper {
		public DateTime value;
	}

	@Test
	public void deserializeISO8601DateTime_CorrectDateTime() {

		String str = "{value:\"3141-05-09T02:06:05.035Z\"}";
		DateTime expected = new DateTime(3141, 5, 9, 2, 6, 5, 35,
				DateTimeZone.UTC);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder
				.registerTypeAdapter(DateTime.class, new DateTimeConverter());
		Gson gson = gsonBuilder.create();

		DateTimeWrapper actual = gson.fromJson(str, DateTimeWrapper.class);

		assertNotNull(actual);
		assertEquals(expected, actual.value);
	}

	@Test
	public void testSimpleAbsenceListDeserializing() {
		DateTime start = new DateTime(2012, 5, 3, 16, 30, 0, 0,
				DateTimeZone.UTC);
		DateTime end = new DateTime(2012, 5, 3, 17, 50, 0, 0, DateTimeZone.UTC);
		UploadEvent e = new UploadEvent();
		e.startDateTime = start;
		e.endDateTime = end;
		e.type = Event.Type.Rehearsal;
		UploadAbsence a = new UploadAbsence();
		a.netid = "ehayles";
		a.type = Absence.Type.Absence;
		UploadAbsence a2 = new UploadAbsence();
		a2.netid = "alusk";
		a2.type = Absence.Type.Absence;
		List<UploadAbsence> l = new ArrayList<UploadAbsence>();
		l.add(a);
		l.add(a2);
		e.absences = l;

		Type listType = new TypeToken<List<UploadEvent>>() {
		}.getType();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder
				.registerTypeAdapter(DateTime.class, new DateTimeConverter());
		Gson gson = gsonBuilder.create();

		Reader r = new StringReader(SIMPLE_ABSENCE_TESTDATA_V2);
		List<UploadEvent> result = gson.fromJson(r, listType);
		assertEquals(1, result.size());
		assertEquals(end, result.get(0).endDateTime);
		assertEquals(start, result.get(0).startDateTime);
	}

	@Test
	public void deserializeListOfISO8601DateTime_CorrectDateTime() {

		Type listType = new TypeToken<List<DateTimeWrapper>>() {
		}.getType();
		String str = "[{value:\"3141-05-09T02:06:05.035Z\"}]";
		DateTime expected = new DateTime(3141, 5, 9, 2, 6, 5, 35,
				DateTimeZone.UTC);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder
				.registerTypeAdapter(DateTime.class, new DateTimeConverter());
		Gson gson = gsonBuilder.create();

		List<DateTimeWrapper> actual = gson.fromJson(str, listType);

		assertNotNull(actual);
		assertEquals(1, actual.size());

		assertNotNull(actual.get(0));
		assertEquals(expected, actual.get(0).value);
	}

	@Test
	public void serializeDateTime_CorrectString() {
		String expected = "{\"value\":\"3141-05-09T02:06:05.035Z\"}";
		DateTime time = new DateTime(3141, 5, 9, 2, 6, 5, 35, DateTimeZone.UTC);
		DateTimeWrapper data = new DateTimeWrapper();
		data.value = time;

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder
				.registerTypeAdapter(DateTime.class, new DateTimeConverter());
		Gson gson = gsonBuilder.create();

		String actual = gson.toJson(data, DateTimeWrapper.class);

		assertEquals(expected, actual);
	}

	@Test
	public void simpleAbsenceInsertionThroughServlet_NullStudent()
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		DataTrain train = getDataTrain();
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setPage(req, MobileAppDataServlet.Page.indexv2);
		ServletMocks.setUserSession(req,
				Users.createDefaultTA(train.users()));
		ServletMocks.setPostedContent(req, SIMPLE_ABSENCE_TESTDATA_V2);

		ServletOutputStream os = mock(ServletOutputStream.class);
		when(resp.getOutputStream()).thenReturn(os);

		ServletMocks.doPost(MobileAppDataServlet.class, req, resp);

		verify(os).print(
				"{\"error\":\"exception\",\"message\":\""
						+ Lang.ERROR_ABSENCE_FOR_NULL_USER + "\"}");

		// TODO: Verify insertion lengths
		// When transactional support is re-added, uncomment this
		// assertEquals(0,
		// datastore.find().type(Event.class).returnCount().now()
		// .intValue());
		// assertEquals(0, datastore.find().type(Absence.class).returnCount()
		// .now().intValue());
	}

	@Test
	public void testSimpleAbsenceInsertionThroughController() {

		DataTrain train = getDataTrain();

		User ta = Users.createTA(train.users(), "ehornbuckle", "123456780",
				"first", "last", 2, "major", User.Section.Staff);
		Users.createStudent(train.users(), "ehayles", "123456719", "first",
				"last", 1, "major", User.Section.Baritone);
		Users.createStudent(train.users(), "alusk", "123456782", "first",
				"last", 1, "major", User.Section.Drumline_Bass);

		Reader r = new StringReader(SIMPLE_ABSENCE_TESTDATA_V2);
		train.mobileData().pushMobileDataV2(r, ta);

		simpleAbsenceInsertionVerification();
	}

	@Test
	public void testSimpleAbsenceInsertionThroughServlet()
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		// Arrange
		DataTrain train = getDataTrain();
		Users.createStudent(train.users(), "ehayles", "123456788", "first",
				"last", 1, "major", User.Section.Clarinet);
		Users.createStudent(train.users(), "alusk", "123456782", "first",
				"last", 1, "major", User.Section.TenorSax);

		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setPage(req, MobileAppDataServlet.Page.indexv2);
		ServletMocks.setUserSession(req,
				Users.createDefaultTA(train.users()));
		ServletMocks.setPostedContent(req, SIMPLE_ABSENCE_TESTDATA_V2);
		ServletOutputStream os = mock(ServletOutputStream.class);
		when(resp.getOutputStream()).thenReturn(os);

		// Act
		ServletMocks.doPost(MobileAppDataServlet.class, req, resp);

		// Assert
		verify(os)
				.print("{\"error\":\"success\",\"message\":\"Inserted 1/1 events.\\nInserted 2/2 absences/tardies/early checkouts.\\n\"}");
		simpleAbsenceInsertionVerification();
	}

	@Test
	public void testAttemptedStudentUploadThroughServlet()
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {
		helpTestUploadThroughServlet(true);
	}

	@Test
	public void testAttemptedPublicUploadThroughServlet()
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {
		helpTestUploadThroughServlet(false);
	}

	private void helpTestUploadThroughServlet(boolean setUserSession)
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		DataTrain train = getDataTrain();
		Users.createStudent(train.users(), "ehayles", "123456788", "first",
				"last", 1, "major", User.Section.Clarinet);

		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setPage(req, MobileAppDataServlet.Page.indexv2);
		if (setUserSession) {
			ServletMocks.setUserSession(req,
					Users.createDefaultStudent(train.users()));
		}
		ServletMocks.setPostedContent(req, SIMPLE_SINGLE_TARDY_STRING);
		ServletOutputStream os = mock(ServletOutputStream.class);
		when(resp.getOutputStream()).thenReturn(os);

		ServletMocks.doPost(MobileAppDataServlet.class, req, resp);
		verify(os).print("{\"error\":\"login\"}");
	}

	private void simpleAbsenceInsertionVerification() {
		Event event;
		DataTrain train = getDataTrain();
		DateTimeZone zone = train.appData().get().getTimeZone();

		// Verify insertion lengths
		assertEquals(1, train.events().getCount().intValue());
		assertEquals(2, train.absences().getCount().intValue());

		// Verify event
		List<Event> events = train.events().getAll();
		assertEquals(1, events.size());

		event = events.get(0);

		Calendar cal = Calendar.getInstance(zone.toTimeZone());
		cal.setTimeInMillis(0);

		cal.set(2012, 04, 03, 16, 30, 0);
		assertEquals(cal.getTime(), event.getInterval(zone).getStart().toDate());

		cal.set(2012, 04, 03, 17, 50, 0);
		assertEquals(
				0,
				cal.getTime().compareTo(
						event.getInterval(zone).getEnd().toDate()));

		assertEquals(Event.Type.Rehearsal, event.getType());

		// Verify absences
		List<Absence> absences = train.absences().getAll();
		assertEquals(2, absences.size());

		boolean foundS = false;
		for (Absence a : absences) {

			// Check parent relation
			assertEquals(a.getEvent(), event);

			if (a.getStudent().getPrimaryEmail().getEmail()
					.equals("ehayles@" + Config.getEmailDomain())) {
				assertFalse(
						"There should only be one abscence for user ehayles",
						foundS);
				foundS = true;
				// TODO https://github.com/curtisullerich/attendance/issues/123
				// assert information about inserted absence
			} else if (a.getStudent().getPrimaryEmail().getEmail()
					.equals("alusk@" + Config.getEmailDomain())) {
				// TODO https://github.com/curtisullerich/attendance/issues/123
				// assert information about inserted absence
			} else
				fail("Found an absence we didn't insert");
		}
	}

	@Test
	public void simpleTardyInsertionThroughController() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();

		Users.createStudent(uc, "ehayles", "123456780", "test1", "tester",
				1, "major", User.Section.AltoSax);
		Users.createStudent(uc, "alusk", "123456781", "test1", "tester", 1,
				"major", User.Section.AltoSax);
		Users.createStudent(uc, "jbade", "123456782", "test1", "tester", 1,
				"major", User.Section.AltoSax);
		User ta = Users.createTA(uc, "ehornbuckle", "123456783", "test1",
				"tester", 1, "major", User.Section.AltoSax);

		Reader r = new StringReader(SIMPLE_TARDY_TESTDATA_V2);
		train.mobileData().pushMobileDataV2(r, ta);

		// Verify insertion lengths
		assertEquals(1, train.events().getCount().intValue());

		assertEquals(6, train.absences().getCount().intValue());

		// TODO: https://github.com/curtisullerich/attendance/issues/123
		// Check actual data returned
	}

}
