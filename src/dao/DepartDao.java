package dao;

import java.util.List;

import domain.Depart;
import domain.Events;
import domain.Members;

public interface DepartDao{
	List<Depart> findAttends(Integer dep_id)throws Exception;
	void insert(List<Depart> department, int datacounter) throws Exception;
	void delete(String member_Id, int event_Id) throws Exception;
	void deleteByUserId(Members user)throws Exception;
	void deleteByEventId(Events event) throws Exception;
}