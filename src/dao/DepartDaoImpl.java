package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.mysql.jdbc.Statement;

import domain.Depart;
import domain.Events;
import domain.Members;

public class DepartDaoImpl implements DepartDao {
	private DataSource ds;

	public DepartDaoImpl(DataSource ds) {
		this.ds = ds;
	}


	@Override
	public List<Depart> findAttends(Integer dep_id) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public String insert(List<Depart> department, int datacounter) throws Exception {

		try (Connection con = ds.getConnection()){
			//必ず決まっているデータのスタート地点をセット（テーブルによって量、数値が変わります）
			int data1 = 2;
			//int data2 = 2;
			//Connection conn = null;
			try {
				//DB接続
				//オートコミットを切る
				con.setAutoCommit(false);


				//departmentテーブルに部署名とフロア情報を挿入
				//membersテーブルにpositionタイプ（役職情報）を挿入
				for (int i = 0; i < datacounter; i++) {
					String sql = "insert into department (dep_id,department,floor)  values (null,?,?)";
					PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setObject(1, department.get(data1).getDepartment());
					stmt.setObject(2, department.get(data1).getFloor());
					System.out.println(stmt);
					stmt.executeUpdate();

					//member_idを検索して結果をResultSetにセットする
					String sql1 = "select member_id from members where member_id = ?";
					PreparedStatement stmt1 = con.prepareStatement(sql1);
					stmt1.setObject(1, department.get(data1).getPosition_type());
					ResultSet rs = stmt1.executeQuery();

					//検索結果がnullならロールバック
					if(rs==null) {
						con.rollback();
						return "302";
					}else {

					//membersテーブルにpositionタイプ（役職情報）を挿入
					String sql2 = "update Members set position_type =1 where member_id=?;";
					PreparedStatement stmt2 = con.prepareStatement(sql2);
					stmt2.setObject(1,department.get(data1).getPosition_type());
					System.out.println(stmt2);
					stmt2.executeUpdate();
					}
					//次のデータの位置にインデックスを変える（テーブルによって量、数値が変わります）
					data1++;
				}

				//エラーがなければコミットする
				con.commit();
				return "100";
			}
			//挿入時にエラーが発生したらロールバックしてエラー文を表示
			catch (Exception e) {
				System.out.println("error1");
				e.printStackTrace();
					con.rollback();
					return "300";

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

	public static boolean checkDataBassExit(String str, String tableName, String calamName) throws Exception {
		boolean result=false;
		int count = 0;

		try(Connection conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/eventdb2?useUnicode=true&characterEncoding=utf8&useSSL=false", "root",
				"rootpass")) {
			Class.forName("com.mysql.jdbc.Driver");
			String sql=	" SELECT "
					+  " COUNT(*) FROM ";
			sql += tableName + " WHERE ";
			sql += tableName + "." + calamName;
			sql += " = '" + str + "';";
			Statement stmt=(Statement)conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				count = Integer.parseInt(rs.getString("count(*)"));
			}
			if(count > 0) {
				result = true;
			}
		} catch (Exception e) {
			result = false;
			throw  new Exception(e);
		}
		return result;
	}

	@Override
	public void delete(String member_Id, int event_Id) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void deleteByUserId(Members user) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void deleteByEventId(Events event) throws Exception {
		// TODO 自動生成されたメソッド・スタブ

	}



}
