package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

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

	public void insert(List<Depart> department, int datacounter) throws Exception {

		try (Connection con = ds.getConnection()){
			//必ず決まっているデータのスタート地点をセット（テーブルによって量、数値が変わります）
			int data1 = 2;
			int data2 = 2;
			//Connection conn = null;
			try {
				//DB接続
				//オートコミットを切る
				con.setAutoCommit(false);


				//departmentテーブルに部署名とフロア情報を挿入
				for (int i = 0; i < datacounter; i++) {
					String sql = "insert into department (dep_id,department,floor)  values (null,?,?)";
					PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setObject(1, department.get(data1).getDepartment());
					stmt.setObject(2, department.get(data1).getFloor());
					System.out.println(stmt);
					stmt.executeUpdate();
					//次のデータの位置にインデックスを変える（テーブルによって量、数値が変わります）
					data1++;
				}

				//membersテーブルにpositionタイプ（役職情報）を挿入
				for (int i = 0; i < datacounter; i++) {
					String sql = "update Members set position_type =1 where member_id=?;";
					PreparedStatement stmt = con.prepareStatement(sql);

					stmt.setObject(1,department.get(data2).getPosition_type());
					System.out.println(stmt);
					stmt.executeUpdate();
					//次のデータの位置にインデックスを変える（テーブルによって量、数値が変わります）
					data2++;
				}
				//エラーがなければコミットする
				con.commit();
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
