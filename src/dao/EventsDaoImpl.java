package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import domain.Events;

public class EventsDaoImpl implements EventsDao {
	private DataSource ds;

	public EventsDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * @see dao.EventsDao#findAll()
	 */
	@Override
	public List<Events> findAll(int event_page) throws Exception {
		List<Events> eventsList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {

			String sql = "SELECT "
					+ " Events.ID "
					+ " from events"
					+ " LIMIT ?,5";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, event_page);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				eventsList.add(mapToEvents(rs));
			}
		}
		return eventsList;
	}

	/**
	 * ユーザー一覧
	 * ResultSetをUsersクラスにセットします
	 * 5行分とってきたやつをjoinして表示を整える
	 * @return users
	 */
	@Override
	public List<Events> findfive(List<Events> hoge,int id) throws Exception {
		List<Events> eventsList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {

			for (Events fuga : hoge) {

				String sql = "select Events.ID,Events.title,Events.start,Events.end,Events.place,atn.user_id, Groups.name AS groups_name,Events.group_id,Events.detail,Users.name as users_name,Events.created\r\n"
						+
						"from events left join (select * from attends where USER_ID= ?) as atn on events.id = atn.EVENT_ID JOIN Groups ON Events.group_id=groups.id \r\n"
						+
						"left Join users on events.registered_by=users.id "
						+
						 "where events.id=?";
				PreparedStatement stms = con.prepareStatement(sql);
				stms.setObject(1, id);
				stms.setObject(2, fuga.getId());
				ResultSet rs = stms.executeQuery();

				if (rs.next()) {

					eventsList.add(mapToEvents2(rs));
					;
				}
			}

		}
		return eventsList;
	}

	@Override
	public List<Events> findToday(int event_page) throws Exception {
		List<Events> eventsList = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		String today = String.format("%04d", year) + "-" + String.format("%02d", month) + "-"
				+ String.format("%02d", day);

		try (Connection con = ds.getConnection()) {

			String sql = "SELECT "
					+ " Events.ID "
					+ " from events"
					+ " WHERE SUBSTRING( `start`, 1, 10 ) = ?"
					+ " LIMIT ?,5";


			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, today);
			stmt.setInt(2, event_page);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				eventsList.add(mapToEvents(rs));
			}
		}
		return eventsList;
	}

/**
 * 今日のイベント用のpagenation
 */

	/**
	 * findAllメソッドから呼び出し
	 */
	private Events mapToEvents(ResultSet rs) throws SQLException {
		Events events = new Events();
		events.setId((Integer) rs.getObject("id"));
		return events;
	}

	/**
	 * findfiveメソッドから呼び出し
	 */
	private Events mapToEvents2(ResultSet rs) throws SQLException {
	Events events = new Events();
	events.setId((Integer) rs.getObject("id"));
	events.setTitle(rs.getString("title"));
	events.setStart(rs.getTimestamp("start"));
	events.setEnd(rs.getTimestamp("end"));
	events.setPlace(rs.getString("place"));
	events.setGroups_name(rs.getString("groups_name"));
	events.setGroup_id((Integer) rs.getObject("group_id"));
	events.setDetail(rs.getString("detail"));
	events.setUsers_name(rs.getString("users_name"));
	events.setCreated(rs.getTimestamp("created"));
	try {
		events.setUser_id((Integer) rs.getObject("user_id"));
	} catch (NullPointerException e) {

	}
	return events;
}

	private Events mapToEventInfo(ResultSet rs) throws SQLException {
		Events events = new Events();
		events.setId((Integer) rs.getObject("id"));
		events.setTitle(rs.getString("title"));
		events.setStart(rs.getTimestamp("start"));
		events.setEnd(rs.getTimestamp("end"));
		events.setPlace(rs.getString("place"));
		events.setGroups_name(rs.getString("groups_name"));
		events.setGroup_id((Integer) rs.getObject("group_id"));
		events.setDetail(rs.getString("detail"));
		events.setUsers_name(rs.getString("users_name"));
		events.setCreated(rs.getTimestamp("created"));
		return events;
	}

	@Override
	public Events findById(Integer id) throws Exception {
		Events events = new Events();

		try (Connection con = ds.getConnection()) {
			String sql = "SELECT"
					+ " Events.ID,Events.title,Events.start,Events.end,Events.place,"
					+ " Groups.NAME AS groups_name,Events.group_id,Events.detail,Users.name as users_name,Events.created"
					+ " FROM Events JOIN Groups"
					+ " ON Events.group_id=groups.id"
					+ " Join users on events.registered_by=users.id where events.id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				events = mapToEventInfo(rs);
			}
		}
		return events;
	}

	@Override
	public void insert(Events events) throws Exception {
		Timestamp castStart = new Timestamp(events.getStart().getTime());
		Timestamp castEnd = new Timestamp(events.getEnd().getTime());
		try (Connection con = ds.getConnection()) {
			String sql = "INSERT INTO events "
					+ " (title,start,end,place,group_id,detail,registered_by,created) "
					+ "VALUES(?,?,?,?,?,?,?,NOW())";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, events.getTitle());
			stmt.setTimestamp(2, castStart);
			stmt.setTimestamp(3, castEnd);
			stmt.setString(4, events.getPlace());
			stmt.setInt(5, events.getGroup_id());
			stmt.setString(6, events.getDetail());
			stmt.setInt(7, events.getRegistered_by());
			stmt.executeUpdate();
		}
	}

	@Override
	public void update(Events events) throws Exception {
		Timestamp castStart = new Timestamp(events.getStart().getTime());
		Timestamp castEnd = new Timestamp(events.getEnd().getTime());
		try (Connection con = ds.getConnection()) {
			String sql = "update events "
					+ " set title=?,start=?,end=?,place=?,group_id=?,detail=?"
					+ " where id =? ";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, events.getTitle());
			stmt.setTimestamp(2, castStart);
			stmt.setTimestamp(3, castEnd);
			stmt.setString(4, events.getPlace());
			stmt.setInt(5, events.getGroup_id());
			stmt.setString(6, events.getDetail());
			stmt.setInt(7, events.getId());
			stmt.executeUpdate();
		}

	}

	@Override
	public void delete(Events events) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "DELETE "
					+ "FROM events WHERE id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, events.getId(), Types.INTEGER);
			stmt.executeUpdate();
		}

	}

	/*
	 * pagenationで使用する
	 */
	public double countAll() throws Exception {
		List<Events> eventsList = new ArrayList<>();
		try (Connection con = ds.getConnection()) {

			String sql = "SELECT "
					+ " events.id "
					+ " from events";

			PreparedStatement stms = con.prepareStatement(sql);
			ResultSet rs = stms.executeQuery();

			while (rs.next()) {
				eventsList.add(mapToEvents(rs));
			}
		}
		double count = eventsList.size();
		return count;
	}

	/*
	 * Todayのpagenationで使用する
	 */
	public double countAllToday() throws Exception {
		List<Events> eventsList = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		String today = String.format("%04d", year) + "-" + String.format("%02d", month) + "-"
				+ String.format("%02d", day);

		try (Connection con = ds.getConnection()) {

			String sql = "SELECT "
					+ " events.id "
					+ " from events"
					+ " WHERE SUBSTRING( `start`, 1, 10 ) = ?";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, today);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				eventsList.add(mapToEvents(rs));
			}
		}
		double count = eventsList.size();
		return count;
	}

	@Override
	public List<Events> findAll() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<Events> findToday() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
