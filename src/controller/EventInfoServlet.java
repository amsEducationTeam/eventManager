package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AttendDao;
import dao.DaoFactory;
import dao.EventsDao;
import domain.Attend;
import domain.Events;

/**
 * Servlet implementation class EventInfoServlet
 */
@WebServlet()// /eventinfo
public class EventInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		try {
			int infoId = Integer.parseInt(request.getParameter("info"));
			request.getSession().setAttribute("findEventId", infoId);
			} catch(Exception e){

			}
			int findEventId=(int)request.getSession().getAttribute("findEventId");

		try {
			EventsDao eventsDao = DaoFactory.createEventsDao();
			Events event = eventsDao.findById(findEventId);
			request.setAttribute("event",event);
			AttendDao attendDao = DaoFactory.createAttendDao();
			List<Attend> attendList = attendDao.findAttends(findEventId);
			request.setAttribute("attendList",attendList);
            request.getRequestDispatcher("view/eventinfo.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
