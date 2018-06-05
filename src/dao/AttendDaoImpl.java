package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

import domain.Attend;
import domain.Events;
import domain.Users;

public class AttendDaoImpl implements UsersDao, AttendDao {
	private DataSource ds;

	public AttendDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	//テーブルattendに新しい参加者を追加するメソッド
	@Override
	public void insert(Users user) throws Exception {
	}


	/**
	 * @param int userId, int eventId
	 */
	@Override
	public void insert(int memberId, int eventId) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "INSERT INTO attends "
					+ " (attends_id, member_id,event_id) "
					+ "values(null,?,?);";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, memberId);
			stmt.setObject(2, eventId);
			stmt.executeUpdate();
		}
	}

	/**
	 * @param int userId, int eventId
	 */
	//テーブルattendから参加者を削除するメソッド
	@Override
	public void delete(int memberId, int eventId) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "delete from attends  where  member_id=? AND event_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, memberId);
			stmt.setObject(2, eventId);
			stmt.executeUpdate();
		}
	}

	/**
	 *@param Integer id
	 *@return List<Attend> attendList
	 */
	@Override
	public List<Attend> findAttends(Integer event_id) throws Exception {
		List<Attend> attendList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {
			String sql = "SELECT "
					+ " members.name, attends.member_id "
					+ " from attends join members "
					+ " on attends.member_id = members.member_id"
					+ " where attends.event_id = ? ";
			PreparedStatement stms = con.prepareStatement(sql);
			stms.setLong(1, event_id);
			ResultSet rs = stms.executeQuery();
			while (rs.next()) {
				attendList.add(mapToAttends(rs));
			}
		}
		return attendList;
	}

	/**
	 *
	 * @param rs
	 * @throws SQLException
	 */
	private Attend mapToAttends(ResultSet rs) throws SQLException {
		Attend users = new Attend();
		users.setMember_name(rs.getString("name"));
		try {
			users.setMember_id((Integer) rs.getObject("user_id"));
		} catch (NullPointerException e) {
		}
		return users;
	}

	/**
	 * ログイン時に処理
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Users mapToLogin(ResultSet rs) throws SQLException {
		domain.Users users = new Users();
		users.setMember_id((String) rs.getObject("member_id"));
		users.setName(rs.getString("name"));
		return users;
	}

	@Override
	public Users findById(Integer id) throws Exception {
		return null;
	}

	@Override
	public Users findByLoginIdAndLoginPass(String loginId, String loginPass) throws Exception {
		Users user = null;
		try (Connection con = ds.getConnection()) {
			String sql = "SELECT * "
					+ "FROM members WHERE login_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, loginId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				if (BCrypt.checkpw(loginPass, rs.getString("login_pass"))) {
					user = mapToLogin(rs);
				}
			}
		}
		return user;
	}

	@Override
	public void update(Users Users) throws Exception {

	}

	/**
	 * userDeleteServletから削除したﾕｰｻﾞｰが参加していたイベント情報を削除します
	 */
	@Override
	public void deleteByUserId(Users user) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "delete from attends  where member_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, user.getMember_id());
			stmt.executeUpdate();
		}
	}

	/**
	 * userDeleteServletから削除したﾕｰｻﾞｰが参加していたイベント情報を削除します
	 */
	@Override
	public void deleteByEventId(Events event) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "delete from attends  where event_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, event.getMember_id());
			stmt.executeUpdate();
		}
	}

	@Override
	public List<Users> findAll() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void insert(Events event) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}

	public List<Users> findfive(List<Users> userList) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public List<Users> findAll(int page) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean CheckLoginId(String loginId) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public double countAll() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void updateWhithoutPass(Users Users) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void delete(Users user) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}

}