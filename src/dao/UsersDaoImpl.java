package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

import domain.Users;

public class UsersDaoImpl implements UsersDao {
	private DataSource ds;

	public UsersDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * ユーザー一覧
	 * ResultSetをUsersクラスにセットします
	 * 5行分とってくる
	 * @return users
	 */
	@Override
	public List<Users> findAll(int page) throws Exception {
		List<Users> userList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {

			String sql = "SELECT "
					+ " users.id "
					+ " from users "
					+ " ORDER BY id asc "
					+ " LIMIT ?,5";

			PreparedStatement stms = con.prepareStatement(sql);
			stms.setObject(1, page);
			ResultSet rs = stms.executeQuery();

			while (rs.next()) {
				userList.add(mapToUsers(rs));
			}
		}
		return userList;
	}

	/**
	 * ユーザー一覧
	 * ResultSetをUsersクラスにセットします
	 * 5行分とってきたやつをjoinして表示を整える
	 * @return users
	 */
	@Override
	public List<Users> findfive(List<Users> hoge) throws Exception {
		List<Users> userList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {

			for (Users fuga : hoge) {

				String sql = "SELECT "
						+ " users.id, users.name, groups.name as groupName "
						+ " from users join groups "
						+ " on users.group_id = groups.id "
						+ " where users.id=?";

				PreparedStatement stms = con.prepareStatement(sql);
				stms.setObject(1, fuga.getId());
				ResultSet rs = stms.executeQuery();

				if (rs.next()) {

					userList.add(mapToUsers2(rs));
					;
				}
			}

		}
		return userList;
	}

	/**
	 * findAllメソッドから呼び出し
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Users mapToUsers(ResultSet rs) throws SQLException {

		domain.Users users = new Users();
		users.setId((Integer) rs.getObject("id"));
		return users;
	}

	/**
	 * findAfiveメソッドから呼び出し
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Users mapToUsers2(ResultSet rs) throws SQLException {

		domain.Users users = new Users();
		users.setId((Integer) rs.getObject("id"));
		//users.setLoginId((rs.getString("login_id")));
		users.setName(rs.getString("name"));
		//users.setGroup((Integer)rs.getObject("group_id"));
		users.setGroupName(rs.getString("groupName"));
		//users.setCreated(rs.getTimestamp("created"));
		return users;
	}

	/**
	 * findByIdメソッドから呼び出される
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Users mapToUser(ResultSet rs) throws SQLException {

		domain.Users users = new Users();
		users.setId((Integer) rs.getObject("id"));
		users.setLoginId((rs.getString("login_id")));
		users.setName(rs.getString("name"));
		//users.setGroup((Integer)rs.getObject("group_id"));
		users.setGroupName(rs.getString("groupName"));
		//users.setCreated(rs.getTimestamp("created"));
		return users;
	}

	/**
	 * ログイン時に処理
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Users mapToLogin(ResultSet rs) throws SQLException {

		domain.Users users = new Users();
		users.setId((Integer) rs.getObject("id"));
		users.setName(rs.getString("name"));
		users.setTypeId((Integer) rs.getObject("type_id"));
		return users;
	}

	/**
	 * ユーザー詳細時に処理
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	@Override
	public Users findById(Integer id) throws Exception {
		Users user = null;
		try (Connection con = ds.getConnection()) {
			String sql = "SELECT "
					+ " users.id,login_id, users.name, groups.name as groupName "
					+ " from users join groups "
					+ " on users.group_id = groups.id WHERE users.id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, id, Types.INTEGER);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				user = mapToUser(rs);
			}
			//user=mapToUsers(rs);
		}

		return user;

	}

	/**
	 * ユーザ情報の入力
	 * @param user
	 */
	@Override
	public void insert(Users user) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "INSERT INTO users"
					+ " (id, login_id, login_pass, name, type_id, group_id, created) "
					+ "values(null,?,?,?,1,?,now())";// id=null(auto), type_id = 1(デフォルトで一般ユーザー)
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, user.getLoginId());
			stmt.setString(2, user.getLoginPass());
			stmt.setString(3, user.getName());
			stmt.setObject(4, user.getGroup(), Types.INTEGER);
			stmt.executeUpdate();
		}
	}

	/**
	 * ログイン時に処理
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	@Override
	public Users findByLoginIdAndLoginPass(String loginId, String loginPass) throws Exception {
		Users user = null;
		try (Connection con = ds.getConnection()) {
			String sql = "SELECT * "
					+ "FROM users WHERE login_id=?";
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

	/**
	 * ユーザ情報更新処理
	 * @param Users
	 */
	@Override
	public void update(Users Users) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "Update users set "
					+ " name=?, login_id=?, "
					+ " login_pass=?, group_id=? "
					+ " WHERE id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(5, Users.getId());
			stmt.setString(1, Users.getName());
			stmt.setObject(2, Users.getLoginId());
			stmt.setObject(3, Users.getLoginPass());
			stmt.setObject(4, Users.getGroup());

			stmt.executeUpdate();
		}
	}
	/**
	 * パスワードの変更なしの場合のユーザ情報の更新
	 */
	@Override
	public void updateWhithoutPass(Users Users) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "Update users set "
					+ " name=?, login_id=?, group_id=? "
					+ " WHERE id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(4, Users.getId());
			stmt.setString(1, Users.getName());
			stmt.setObject(2, Users.getLoginId());
			stmt.setObject(3, Users.getGroup());

			stmt.executeUpdate();
		}
	}

	/**
	 * ユーザー情報の削除
	 * @return users
	 */
	@Override
	public void delete(Users Users) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "DELETE "
					+ "FROM users WHERE id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setObject(1, Users.getId(), Types.INTEGER);
			stmt.executeUpdate();
		}
	}

	/**
	 * @return count
	 */
	public double countAll() throws Exception {
		List<Users> userList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {

			String sql = "SELECT "
					+ " users.id "
					+ " from users";

			PreparedStatement stms = con.prepareStatement(sql);
			ResultSet rs = stms.executeQuery();

			while (rs.next()) {
				userList.add(mapToUsers(rs));
			}
		}
		double count = userList.size();
		return count;
	}

	/**
	 * login_idを検索して検索結果が１件以上ならfalseを返す
	 *  return の初期値 true
	 * @return check
	 */
	@Override
	public boolean CheckLoginId(String loginId) throws Exception {
		boolean check = true;
		int count = 0;
		try(Connection con=ds.getConnection()) {
			String sql="SELECT count(*) from users"
					+ " WHERE users.login_id=?";
			PreparedStatement stmt=con.prepareStatement(sql);
			stmt.setString(1, loginId);
			ResultSet rs =stmt.executeQuery();
			while(rs.next()) {
				count = Integer.parseInt(rs.getString("count(*)"));
			}
			if(count > 0) {
				check = false;
			}
		return check;
		}
	}


//	public void countAll(int count) throws Exception {
//		try (Connection con = ds.getConnection()) {
//			String sql = "SELECT COUNT( id ) FROM users";
//			PreparedStatement stmt = con.prepareStatement(sql);
//			ResultSet rs = stmt.executeQuery();
//
//			while (rs.next()) {
//				count = mapToCount(rs);
//			}
//
//		}
//
//		return count;
//
//	}
//
//	private Users mapToCount(ResultSet rs) throws SQLException {
//
//		domain.Users users = new Users();
//		users.setCount((Integer) rs.getObject("id"));
//		return users;
//	}

	@Override
	public List<Users> findAll() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}