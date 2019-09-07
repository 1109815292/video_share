package com.juheshi.video.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * * Created with IntelliJ IDEA. * User: tsaowe * Date: 12-11-28 * Time: ����10:33
 */
public class Listener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		String webroot = servletContextEvent.getServletContext().getRealPath(
				"/");
		System.setProperty("webapp.root", webroot);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}
}