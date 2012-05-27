package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.beans.AuthBean;
import edu.iastate.music.marching.attendance.beans.PageTemplateBean;
import edu.iastate.music.marching.attendance.controllers.AppDataController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.model.AppData;

public class PageBuilder {

	private static final String JSP_PATH_PRE = "/WEB-INF/";

	private static final String JSP_PATH_POST = ".jsp";

	private String mJSPPath;

	private Map<String, Object> attribute_map;
	private PageTemplateBean mPageBean;

	private AppData mAppData;

	private PageBuilder() {
		attribute_map = new HashMap<String, Object>();
	}

	private PageBuilder(String jsp_simple_path, AppData appData) {
		this();

		// Save parameters
		mJSPPath = JSP_PATH_PRE + jsp_simple_path + JSP_PATH_POST;
		
		mAppData = appData;

		mPageBean = new PageTemplateBean(jsp_simple_path, appData);

		mPageBean.setTitle(appData.getTitle());
	}

	public <T extends Enum<T>> PageBuilder(T page, String jsp_servlet_path) {
		this(jsp_servlet_path + "/" + page.name(), DataTrain.getAndStartTrain().getAppDataController().get());
	}
	
	public <T extends Enum<T>> PageBuilder(T page, String jsp_servlet_path, AppData appData) {
		this(jsp_servlet_path + "/" + page.name(), appData);
	}

	public PageBuilder setPageTitle(String title) {
		mPageBean.setTitle(title + " - " + mAppData.getTitle());

		return this;
	}

	public PageBuilder setAttribute(String name, Object value) {
		attribute_map.put(name, value);

		return this;
	}

	public void passOffToJsp(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Apply all saved attributes
		for (Entry<String, Object> entry : attribute_map.entrySet())
			req.setAttribute(entry.getKey(), entry.getValue());

		// Insert page template data bean
		mPageBean.apply(req);

		// Insert authentication data bean
		AuthBean.getBean(req.getSession()).apply(req);

		// Do actual forward
		req.getRequestDispatcher(mJSPPath).forward(req, resp);
	}

}
