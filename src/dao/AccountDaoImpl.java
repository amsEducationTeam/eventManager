package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
	// list化
	public void insertAcount(Account account) throws Exception {
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

				// レコードがあれば更新
				// members
				// メンバテーブルのlogin_idにidをINSERTする
				String sqlMem = "UPDATE members SET login_id = ? WHERE member_id = ?;";
				PreparedStatement stmtMem = con.prepareStatement(sqlMem);
				stmtMem.setString(1, account.getLoginId());
				stmtMem.setString(2, account.getMemberId());
				stmtMem.executeUpdate();
			} //挿入時にエラーが発生したらロールバックしてエラー文を表示
			catch (Exception e) {
				System.out.println("error1");
				e.printStackTrace();
					con.rollback();
					return;

			} finally {
				try {
					if (con != null) {
						con.close();
						System.out.println("切断しました");
					}
				} catch (SQLException e) {
					System.out.println("error2");
				}
			}
		}
	}

	@Override
	public List<Account> findAll() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}