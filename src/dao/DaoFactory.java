package dao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DaoFactory {
	public static UsersDao createUsersDao() {
		return new UsersDaoImpl(getDataSource());

	}
	public static EventsDao createEventsDao() {
		return new EventsDaoImpl(getDataSource());
	}
	public static AttendDao createAttendDao() {
		return new AttendDaoImpl(getDataSource());
	}
	private static DataSource getDataSource() {
		InitialContext ctx=null;
		DataSource ds=null;
		try {
			ctx=new InitialContext();
			ds=(DataSource)ctx.lookup("java:comp/env/jdbc/eventdb");
		} catch (NamingException e) {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException el) {
					throw new RuntimeException(el);
				}
			}
			throw new RuntimeException(e);
		  }
		return ds;
	}

}