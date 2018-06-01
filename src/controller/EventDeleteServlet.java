package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AttendDao;
import dao.DaoFactory;
import dao.EventsDao;
import domain.Events;

/**
 * Servlet implementation class EventDeleteServlet
 */
@WebServlet("/eventDelete")
public class EventDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int eventId = Integer.parseInt(request.getParameter("info"));
//			EventsDao eventsDao1 = DaoFactory.createEventsDao();
//			List<Events> eventsList = eventsDao1.findAll();
		try {
			EventsDao eventsDao = DaoFactory.createEventsDao();
			AttendDao attendDao = DaoFactory.createAttendDao();
			Events event = new Events();
			event.setId(eventId);
			attendDao.deleteByEventId(event);
			eventsDao.delete(event);
			request.getRequestDispatcher("view/eventdelDone.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		//request.getRequestDispatcher("view/eventdelDone.jsp").forward(request, response);

	}

}
