package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

import domain.Account;

public class AccountDaoImpl implements AccountDao {

	private DataSource ds;

	public AccountDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * アカウントデータ(memberId,loginId,loginPass,authId)をDBのテーブルaccount,membersにインサートする
	 * @param Account
	 */
	@Override
	public String insertAcount(Account account) throws Exception {
		try (Connection con = ds.getConnection()) {
			try {
				//オートコミットを切る
				con.setAutoCommit(false);

				// アカウントテーブルに各idとpassをINSERTする
				String sqlAcc = "INSERT INTO account(login_id,login_pass, auth_id) VALUES(?,?,?);";
				PreparedStatement stmtAcc = con.prepareStatement(sqlAcc);
				//account
				stmtAcc.setString(1, account.getLoginId());
				String hashPass = BCrypt.hashpw(account.getLoginPass(), BCrypt.gensalt());
				stmtAcc.setString(2, hashPass);
				stmtAcc.setObject(3, account.getAuthId());
				stmtAcc.executeUpdate();

				// members.member_idがあるかチェック

				String memCheck = "SELECT COUNT(*) from members where member_id=?";
				PreparedStatement stmtMC = con.prepareStatement(memCheck);
				stmtMC.setString(1, account.getMemberId());
				ResultSet rs = stmtMC.executeQuery();
				int count=0;
				while(rs.next()) {
					count=Integer.parseInt(rs.getString("count(*)"));
				}

				if(count==1) {
					// members
					// メンバテーブルのlogin_idにidをINSERTする
					String sqlMem = "UPDATE members SET login_id = ? WHERE member_id = ?;";
					PreparedStatement stmtMem = con.prepareStatement(sqlMem);
					stmtMem.setString(1, account.getLoginId());
					stmtMem.setString(2, account.getMemberId());
					stmtMem.executeUpdate();
				}else {
					con.rollback();
//					System.out.println("error1");
					return "903";
				}
				con.commit();
			} //挿入時にエラーが発生したらロールバックしてエラー文を表示
			catch (Exception e) {
				System.out.println("error2");
				e.printStackTrace();
					con.rollback();
					return "302";

			} finally {
				try {
					if (con != null) {
						con.close();

					}
				} catch (SQLException e) {
//					System.out.println("error3");
					return "904";
				}
			}
		}
		return "100";
	}

	@Override
	public List<Account> findAll() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}