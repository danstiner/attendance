package edu.iastate.music.marching.attendance.test.servlets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.servlets.DirectorServlet;
import edu.iastate.music.marching.attendance.servlets.MobileAppDataServlet;
import edu.iastate.music.marching.attendance.testlib.AbstractDatastoreTest;
import edu.iastate.music.marching.attendance.testlib.ServletMocks;
import edu.iastate.music.marching.attendance.testlib.Users;

public class ServletAccessTest extends AbstractDatastoreTest {

	@Test
	public void DirectorServlet_Student_ClassList_Test()
			throws ServletException, IOException, InstantiationException,
			IllegalAccessException {
		DataTrain train = getDataTrain();
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		
		ServletMocks.setUserSession(req, Users.createDefaultStudent(train.users()));
		ServletMocks.setPage(req, MobileAppDataServlet.Page.index);

		ServletOutputStream sos = mock(ServletOutputStream.class);

		when(resp.getOutputStream()).thenReturn(sos);

		ServletMocks.doGet(MobileAppDataServlet.class, req, resp);

		// Verify login error is written to the servlet output stream
		verify(sos).print("{\"error\":\"login\"}");
	}

	@Test
	public void MobileDataServlet_TA_ClassList_Test() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		DataTrain train = getDataTrain();
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setUserSession(req, Users.createDefaultTA(train.users()));
		ServletMocks.setPage(req, MobileAppDataServlet.Page.index);

		ServletOutputStream sos = mock(ServletOutputStream.class);

		when(resp.getOutputStream()).thenReturn(sos);

		ServletMocks.doGet(MobileAppDataServlet.class, req, resp);
		
		// Verify success code and data for this one TA is written out
		verify(sos).print("{\"error\":\"success\",\"data\":\"TA\\u0026split\\u0026defaultta\\u0026split\\u0026Johnny\\u0026split\\u0026Doe\\u0026split\\u0026\\u0026split\\u0026null\\u0026newline\\u0026\"}");
	}

	@Test
	public void MobileDataServlet_Director_ClassList_Test()
			throws ServletException, IOException, InstantiationException,
			IllegalAccessException {
		DataTrain train = getDataTrain();
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setUserSession(req, Users.createDefaultDirector(train.users()));
		ServletMocks.setPage(req, MobileAppDataServlet.Page.index);

		ServletOutputStream sos = mock(ServletOutputStream.class);

		when(resp.getOutputStream()).thenReturn(sos);

		ServletMocks.doGet(MobileAppDataServlet.class, req, resp);
		
		// Verify success message is written, don't worry about data as no students exist
		verify(sos).print("{\"error\":\"success\",\"data\":\"\"}");
	}

	@Test
	public void DirectorServlet_Student_Test() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		DataTrain train = getDataTrain();
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setUserSession(req, Users.createDefaultStudent(train.users()));

		ServletMocks.doGet(DirectorServlet.class, req, resp);

		verifyUnauthorizedRedirect(req, resp, "director/index");
	}

	@Test
	public void DirectorServlet_TA_Test() throws ServletException, IOException,
			InstantiationException, IllegalAccessException {
		DataTrain train = getDataTrain();
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setUserSession(req, Users.createDefaultTA(train.users()));

		ServletMocks.doGet(DirectorServlet.class, req, resp);

		verifyUnauthorizedRedirect(req, resp, "director/index");
	}

	@Test
	public void DirectorServlet_TA_Test_NoExist() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		DataTrain train = getDataTrain();
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setUserSession(req, Users.createDefaultTA(train.users()));

		ServletMocks.doGet(DirectorServlet.class, req, resp);

		verifyUnauthorizedRedirect(req, resp, "director/nonexistant");
	}

	@Test
	public void DirectorServlet_DirectorIndex_Test() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		DataTrain train = getDataTrain();
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setUserSession(req, Users.createDefaultDirector(train.users()));

		when(req.getPathInfo()).thenReturn("/index");

		RequestDispatcher dispatcher = setupForwardTo(req, resp,
				"/WEB-INF/director/index.jsp");

		ServletMocks.doGet(DirectorServlet.class, req, resp);

		verifyForwardTo(dispatcher, req, resp);
	}

	@Test
	public void DirectorServlet_DirectorNoExist_Test() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		DataTrain train = getDataTrain();
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setUserSession(req, Users.createDefaultDirector(train.users()));

		when(req.getPathInfo()).thenReturn("/nonexistant");

		RequestDispatcher dispatcher = setupErrorRedirect(req, resp, 404);

		ServletMocks.doGet(DirectorServlet.class, req, resp);

		verify(dispatcher).forward(req, resp);
	}

	private void verifyUnauthorizedRedirect(HttpServletRequest req,
			HttpServletResponse resp, String returnUrl)
			throws ServletException, IOException {
		verify(resp).sendRedirect("/error/unauthorized");
	}

	private RequestDispatcher setupErrorRedirect(HttpServletRequest req,
			HttpServletResponse resp, int code) throws ServletException,
			IOException {
		RequestDispatcher dispatcher = mock(RequestDispatcher.class);
		when(req.getRequestDispatcher("/WEB-INF/error/index.jsp")).thenReturn(
				dispatcher);

		return dispatcher;
	}

	private RequestDispatcher setupForwardTo(HttpServletRequest req,
			HttpServletResponse resp, String jsp_path) throws ServletException,
			IOException {
		RequestDispatcher dispatcher = mock(RequestDispatcher.class);
		when(req.getRequestDispatcher(jsp_path)).thenReturn(dispatcher);
		return dispatcher;
	}

	private void verifyForwardTo(RequestDispatcher dispatcher,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		verify(dispatcher).forward(req, resp);
	}
}
