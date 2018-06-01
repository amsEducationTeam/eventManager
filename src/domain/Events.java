package domain;

import java.util.Date;

public class Events {

	private int id;
	private String title;
	private Date start;
	private Date end;
	private String place;
	private String groups_name;
	private String detail;
	private String users_name;
	private int registered_by;
	private Date created;
	private int group_id;
	private int user_id;


	public int getRegistered_by() {
		return registered_by;
	}
	public void setRegistered_by(int registered_by) {
		this.registered_by = registered_by;
	}
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}

	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}



	public String getGroups_name() {
		return groups_name;
	}
	public void setGroups_name(String groups_name) {
		this.groups_name = groups_name;
	}

	public String getUsers_name() {
		return users_name;
	}
	public void setUsers_name(String users_name) {
		this.users_name = users_name;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

}