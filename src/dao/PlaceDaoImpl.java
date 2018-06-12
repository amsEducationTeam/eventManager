package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import domain.Place;

public class PlaceDaoImpl implements PlaceDao {
	private DataSource ds;

	public PlaceDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	public void insert(List<Place> place, int count)throws Exception{
		try(Connection con=ds.getConnection()){
			String sql="INSERT INTO place"
					+ "(place,capa,equ_mic,equ_whitebord,equ_projector, admin_id,locking_time) "
					+ "VALUES(?,?,?,?,?,?,?);";
			for(int i=0;i<count;i++) {
			Timestamp LockTime = new Timestamp(place.get(i).getLocking_time().getTime());
			PreparedStatement stmt=con.prepareStatement(sql);
			stmt.setString(1, place.get(i).getPlace());
			stmt.setObject(2, place.get(i).getCapa());
			stmt.setObject(3, place.get(i).getEqu_mic());
			stmt.setObject(4, place.get(i).getEqu_whitebord());
			stmt.setObject(5, place.get(i).getEqu_projector());
			stmt.setObject(6, place.get(i).getAdmin_id());
			stmt.setTimestamp(7, LockTime);
			stmt.executeUpdate();
			}

		}
	}

	@Override
	public void findMember() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		try(Connection con=ds.getConnection()){
			
			Map<Integer,String> nameMap=new HashMap<Integer,String>();
			String sql="SELECT member_id FROM eventdb2.members;";
			
			PreparedStatement stmt=con.prepareStatement(sql);
			ResultSet rs=stmt.executeQuery();
			
			int i=1;
			if(rs.next()) {
				nameMap.put(i, rs.getString("member_id"));
				i++;
			}
		}

	}




}
