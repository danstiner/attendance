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

	/**
	 * 
	 * @param pathInfo
	 * @param pageEnum
	 * @return
	 */
	protected static <P extends Enum<P>> P parsePathInfo(String pathInfo,
			Class<P> pageEnum) {

		if (pathInfo == null || "/".equals(pathInfo)) {
			// Accessing index, return null if no index value is declared in the
			// enum
			try {
				return Enum.valueOf(pageEnum, "index");
			} catch (IllegalArgumentException e) {
				return null;
			}
		} else {
			// Normal access to a page of the servlet
			String[] path_parts = pathInfo.split("/");

			// TODO https://github.com/curtisullerich/attendance/issues/121
			//What if more than two parts

			String path = path_parts[1];

			try {
				return Enum.valueOf(pageEnum, path);
			} catch (IllegalArgumentException e) {
				return null;
			}

		}

	}

	protected <P extends Enum<P>> String urlFromPage(P page, String jsp_path) {

		return pageToUrl(page, jsp_path);

	}

	protected static <P extends Enum<P>> String pageToUrl(P path,
			String jsp_path) {

		String pagename = path.name();

		if ("index".equals(pagename))
			return "/" + jsp_path;
		else
			return "/" + jsp_path + "/" + pagename;

	}

	protected static boolean isLoggedIn(HttpServletRequest req,
			HttpServletResponse resp, User.Type... allowed_types)
			throws IOException {
		if (!AuthController.isLoggedIn(req.getSession(), allowed_types)) {
			// User of correct type is not logged in
			return false;
		}
		return true;
	}

	protected static void show404(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {
		ErrorServlet.showError(req, resp, 404);
	}
}
