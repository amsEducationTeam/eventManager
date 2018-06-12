package dao;

import java.util.List;

import domain.Attend;
import domain.Events;
import domain.Members;

/**
 * AttendDaoInterface
 * @author
 *
 */
public interface AttendDao{
	List<Attend> findAttends(Integer event_Id)throws Exception;
	void deleteByMemberId(Members user)throws Exception;
	void deleteByEventId(Events event) throws Exception;
	void insert(String member_Id, int event_Id) throws Exception;
	void delete(String member_Id, int event_Id) throws Exception;
}

