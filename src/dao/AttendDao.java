package dao;

import java.util.List;

import domain.Attend;
import domain.Events;
import domain.Users;

/**
 * AttendDaoInterface
 * @author
 *
 */
public interface AttendDao{
	List<Attend> findAttends(Integer id)throws Exception;
	Users findById(Integer id) throws Exception;
	void insert(Events event)throws Exception;
	void update(Users user)throws Exception;
	Users findByLoginIdAndLoginPass(String loginId,String loginPass) throws Exception;
	void insert(int userId,int eventId) throws Exception;
	void delete(int userId, int eventId) throws Exception;
	void deleteByUserId(Users user)throws Exception;
	void deleteByEventId(Events event) throws Exception;
}
