package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

			String sql = "SELECT"
					+ " members.member_id "
					+ "FROM members ORDER BY member_id "
					+ "ASC LIMIT ? , 5;";

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
						+ "members.member_id, members.name, department.department "
						+ "FROM members JOIN department "
						+ "ON members.dep_id = department.dep_id "
						+ "WHERE members.member_id = ?;";


				PreparedStatement stms = con.prepareStatement(sql);
				stms.setObject(1, fuga.getMember_id());
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
		users.setMember_id(rs.getString("member_id"));
		return users;
	}

	/**
	 * findAfiveメソッドから呼び出し
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Users mapToUsers2(ResultSet rs) throws SQLException {

		domain.Users users = new Users();
		users.setMember_id(rs.getString("member_id"));
		users.setName(rs.getString("name"));
		users.setDepartment(rs.getString("department"));

		return users;
	}

	/**
	 * findByIdメソッドから呼び出される
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Users mapToUser(ResultSet rs) throws SQLException {

		domain.Users users = new Users();
		users.setMember_id(rs.getString("member_id"));
		users.setLogin_id((rs.getString("login_id")));
		users.setName(rs.getString("name"));
		users.setKana(rs.getString("kana"));
		users.setDepartment(rs.getString("department"));
		users.setAddress(rs.getString("address"));
		users.setTel(rs.getString("tel"));
		users.setBirthday(rs.getTimestamp("birthday"));
		users.setPosition_type((Integer)rs.getInt("position_type"));
		users.setHired(rs.getTimestamp("hired"));

		return users;
	}

	/**
	 * ログイン時に処理
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	private Users mapToLogin(ResultSet rs) throws SQLException {

		domain.Users users = new Users();
		users.setMember_id(rs.getString("id"));
		users.setName(rs.getString("name"));

		users.setLogin_id(rs.getString("login_id"));
		users.setLogin_pass(rs.getString("login_pass"));
		users.setAuth_id((Integer) rs.getObject("auth_id"));
		return users;
	}


	/**
	 * ユーザー詳細時に処理
	 * ResultSetをUsersクラスにセットします
	 * @return users
	 */
	@Override
	public Users findById(String member_id) throws Exception {
		Users user = null;
		try (Connection con = ds.getConnection()) {
			String sql = "SELECT "
					+ "members.member_id, members.login_id, members.name, members.kana, department.department,"
					+ "members.address,members.tel,members,birthday,members.position_type,members.hired"
					+ " FROM members "
					+ "JOIN department ON members.dep_id = department.dep_id "
					+ "WHERE members.member_id = ?;";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, member_id);
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
	//書き換え必須
	@Override
	public void insert(Users user) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "INSERT INTO members(member_id,name,kana,birthday,address,tel,hired,dep_id,position_type,login_id)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?);";

			//accountも編集

			Timestamp Birth = new Timestamp(user.getBirthday().getTime());
			Timestamp  Hire= new Timestamp(user.getHired().getTime());

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setString(1, user.getMember_id());
			stmt.setString(2, user.getName());
			stmt.setString(3, user.getKana());
			stmt.setTimestamp(4,Birth);
			stmt.setString(5, user.getAddress());
			stmt.setString(6, user.getTel());
			stmt.setTimestamp(7, Hire);
			stmt.setObject(8, user.getDep_id(),Types.INTEGER);
			stmt.setObject(9, 0);
			stmt.setString(10, user.getLogin_id());

			//account
			stmt.setString(11, user.getLogin_id());
			stmt.setString(12, user.getLogin_pass());
			stmt.setObject(13, user.getAuth_id(),Types.INTEGER);

			stmt.executeUpdate();
		}
	}

	@Override
	public void insertacount(Users user) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "INSERT INTO account(login_id,login_pass,auth_id)VALUES(?,?,?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			//account
			stmt.setString(1, user.getLogin_id());
			stmt.setString(2, user.getLogin_pass());
			stmt.setObject(3, user.getAuth_id(),Types.INTEGER);

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
			String sql1 = "SELECT  * FROM members WHERE login_id = ?;";


			PreparedStatement stmt1 = con.prepareStatement(sql1);


			stmt1.setString(1, loginId);

			ResultSet rs1 = stmt1.executeQuery();


			if (rs1.next()) {

					user = mapToLogin(rs1);

			}

		}
		return user;
	}

	@Override
	public Users login(String loginId, String loginPass)throws Exception {
		Users user = null;
		try (Connection con = ds.getConnection()) {
			String sql2="SELECT*FROM account WHERE login_id=?";
			PreparedStatement stmt2 = con.prepareStatement(sql2);
			stmt2.setString(1, loginId);
			ResultSet rs2 = stmt2.executeQuery();

			if (rs2.next()) {
				if (BCrypt.checkpw(loginPass, rs2.getString("login_pass"))) {

				user = mapToLogin(rs2);
				}
			}
			return user;
		}
	}
	/**
	 * ユーザ情報更新処理
	 * @param Users
	 */
	@Override
	public void update(Users Users) throws Exception {

		try (Connection con = ds.getConnection()) {
			String sql ="UPDATE members SET member_id=?,name =?,kana=?,dep_id=?,address=?,tel=?,birthday=?,position_type=?,login_id = ? WHERE member_id = ?;";

			Timestamp Birth = new Timestamp(Users.getBirthday().getTime());

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setString(1, Users.getMember_id());
			stmt.setString(2, Users.getName());
			stmt.setString(3, Users.getKana());
			stmt.setObject(4, Users.getDep_id(),Types.INTEGER);
			stmt.setString(5, Users.getAddress());
			stmt.setString(6, Users.getTel());
			stmt.setTimestamp(7,Birth);
			stmt.setObject(8, Users.getPosition_type(),Types.INTEGER);
			stmt.setString(9, Users.getLogin_id());
			stmt.setString(10, Users.getOldmember_id());


			stmt.executeUpdate();
		}
	}

	public void updateaccount(Users Users) throws Exception {

		try (Connection con = ds.getConnection()) {
			String sql= "UPDATE account SET login_id=?,login_pass=?,auth_id=? WHERE login_id=?;";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, Users.getLogin_id());
			stmt.setString(2, Users.getLogin_pass());
			stmt.setObject(3, Users.getAuth_id(),Types.INTEGER);
			stmt.setString(4, Users.getOldlogin_id());

			stmt.executeUpdate();
		}
	}

	/**
	 * パスワードの変更なしの場合のユーザ情報の更新
	 */
	@Override
	public void updateWhithoutPass(Users Users) throws Exception {
		try (Connection con = ds.getConnection()) {
			String sql = "UPDATE members SET member_id=?,name = ?,kana=?,dep_id=?,address=?,tel=?,birthday=?,position_type=?,login_id = ? WHERE member_id = ?;";

			Timestamp Birth = new Timestamp(Users.getBirthday().getTime());
			//アカウントの扱い
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, Users.getMember_id());
			stmt.setString(1, Users.getName());
			stmt.setString(2, Users.getKana());
			stmt.setObject(3, Users.getDep_id(),Types.INTEGER);
			stmt.setString(4, Users.getAddress());
			stmt.setString(5, Users.getTel());
			stmt.setTimestamp(6,Birth);
			stmt.setObject(7, Users.getPosition_type(),Types.INTEGER);
			stmt.setString(8, Users.getLogin_id());
			stmt.setString(10, Users.getOldmember_id());

			stmt.executeUpdate();
		}
	}

	public void updateAccountWhithoutPass(Users Users) throws Exception{

		try (Connection con = ds.getConnection()) {
			String sql= "UPDATE account SET login_id=?,auth_id=? WHERE login_id=?;";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, Users.getLogin_id());
			stmt.setObject(2, Users.getAuth_id(),Types.INTEGER);
			stmt.setString(3, Users.getOldlogin_id());

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
			String sql ="DELETE "
					+ "FROM members "
					+ "WHERE login_id = ?;";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, Users.getLogin_id());
			stmt.executeUpdate();
		}
	}
	public void deleteAccount(Users Users) throws Exception{

		try (Connection con = ds.getConnection()) {
			String sql ="DELETE "
					+ "FROM account "
					+ "WHERE login_id = ?;";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, Users.getLogin_id());
			stmt.executeUpdate();
		}
	}


	/**
	 * @return count
	 */
	public double countAll() throws Exception {
		List<Users> userList = new ArrayList<>();

		try (Connection con = ds.getConnection()) {

			String sql ="SELECT "
					+ "members.member_id "
					+ "FROM members;";

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
			String sql=	"SELECT "
					+ "COUNT(*) FROM members "
					+ "WHERE members.login_id = ?;";
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


	@Override
	public List<Users> findAll() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}