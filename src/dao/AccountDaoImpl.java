package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.sql.DataSource;

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
	public void insertAcount(Account account) throws Exception {
		try (Connection con = ds.getConnection()) {

			// アカウントテーブルに各idとpassをINSERTする
			String sqlAcc = "INSERT INTO account(login_id,login_pass, auth_id) VALUES(?,?,?);";
			PreparedStatement stmtAcc = con.prepareStatement(sqlAcc);
			//account
			stmtAcc.setString(1, account.getLoginId());
			stmtAcc.setString(2, account.getLoginPass());
			stmtAcc.setObject(3, account.getAuthId());

			// メンバテーブルのlogin_idにidをINSERTする
			String sqlMem = "UPDATE members SET login_id = ? WHERE member_id = ?;";
			PreparedStatement stmtMem = con.prepareStatement(sqlMem);
			stmtMem.setString(1, account.getLoginId());
			stmtMem.setString(2, account.getMemberId());

			stmtAcc.executeUpdate();
			stmtMem.executeUpdate();
		}
	}

	@Override
	public List<Account> findAll() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}