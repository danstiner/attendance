package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.model.User;

public abstract class AbstractBaseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8295424643788812718L;

	interface IPathEnum {

	}

	interface Test extends IPathEnum {

	}

	// protected abstract Class<? extends Enum<?>> getPageEnumType();

	protected <P extends Enum<P>> P pathInfoToPage(HttpServletRequest req,
			HttpServletResponse resp, Class<P> page_enum_class)
			throws IOException, ServletException {

		// // Magic
		// @SuppressWarnings("unchecked")
		// Class<P> p = (Class<P>) getPageEnumType();

		String pathInfo = req.getPathInfo();

		if (pathInfo == null || "/".equals(pathInfo)) {
			// Accessing index, throw a 404 if no index is declared for the
			// servlet
			try {
				return Enum.valueOf(page_enum_class, "index");
			} catch (IllegalArgumentException e) {
				do404(req, resp);
			}
		} else {
			// Normal access to a page of the servlet
			String[] path_parts = req.getPathInfo().split("/");

			// TODO What if more than two parts

			String path = path_parts[1];

			try {
				return Enum.valueOf(page_enum_class, path);
			} catch (IllegalArgumentException e) {
				do404(req, resp);
			}

		}

		return null;
	}

	protected abstract String getJspPath();

	<P extends Enum<P>> String urlFromPage(P path) {

		String jsppath = getJspPath();

		return pageToUrl(path, jsppath);

	}

	static <P extends Enum<P>> String pageToUrl(P path, String jsp_path) {

		String pagename = path.name();

		if ("index".equals(pagename))
			return "/" + jsp_path;
		else
			return "/" + jsp_path + "/" + pagename;

	}

	/**
	 * 
	 * @param servlet
	 * @param page
	 *            Enum value of page to display
	 * @param req
	 * @param resp
	 * @return
	 */
	<T extends Enum<T>> PageBuilder buildPage(T page, HttpServletRequest req,
			HttpServletResponse resp) {
		return AbstractBaseServlet.buildPage(this, page, req, resp);
	}

	/**
	 * 
	 * @param servlet
	 * @param page
	 *            Enum value of page to display
	 * @param req
	 * @param resp
	 * @return
	 */
	static <T extends Enum<T>> PageBuilder buildPage(
			AbstractBaseServlet servlet, T page, HttpServletRequest req,
			HttpServletResponse resp) {
		return new PageBuilder(servlet, page, req, resp);
	}

	static boolean isLoggedIn(HttpServletRequest req,
			HttpServletResponse resp, User.Type... allowed_types)
			throws IOException {
		if (!AuthController.isLoggedIn(req.getSession(), allowed_types)) {
			// User of correct type is not logged in
			resp.sendRedirect(AuthServlet.URL_LOGIN);
			return false;
		}
		return true;
	}

	static void do404(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ErrorServlet.forwardError(req, resp, 404);
	}
}