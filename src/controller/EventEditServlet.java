package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoFactory;
import dao.EventsDao;
import domain.Events;

/**
 * Servlet implementation class EventEditServlet
 */
@WebServlet("/eventedit")
public class EventEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int infoId = Integer.parseInt(request.getParameter("info"));
		try {
			EventsDao eventsDao = DaoFactory.createEventsDao();
			Events event = eventsDao.findById(infoId);
			request.setAttribute("event",event);
            request.getRequestDispatcher("view/eventedit.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int id = Integer.parseInt(request.getParameter("info"));
		String title=request.getParameter("title");
		Date start =null;
		Date end = null;
		try {
			start = sdf.parse(request.getParameter("start"));
			end=sdf.parse(request.getParameter("end"));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String place=request.getParameter("place");
		int group_id=Integer.parseInt(request.getParameter("group_id"));
		String detail=request.getParameter("detail");

		Events event=new Events();
		event.setId(id);
		event.setTitle(title);
		event.setStart(start);
		event.setEnd(end);
		event.setPlace(place);
		event.setGroup_id(group_id);
		event.setDetail(detail);
		event.setRegistered_by((int)request.getSession().getAttribute("id"));

		try {
			EventsDao eventsDao=DaoFactory.createEventsDao();
			eventsDao.update(event);
			request.getRequestDispatcher("view/eventeditDone.jsp").forward(request, response);
			} catch (Exception e) {
			throw new ServletException(e);
			}
	}

}
