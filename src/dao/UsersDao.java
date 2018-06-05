package dao;

import java.util.List;

import domain.Users;


public interface UsersDao{
	List<Users> findAll()throws Exception;
	Users findById(String id) throws Exception;
	void insert(Users user)throws Exception;
	void update(Users user)throws Exception;
	void delete(Users user)throws Exception;
	Users findByLoginIdAndLoginPass(String loginId,String loginPass) throws Exception;
	List<Users> findAll(int page) throws Exception;
	List<Users> findfive(List<Users> userList) throws Exception;
	double countAll() throws Exception;
	boolean CheckLoginId(String loginId) throws Exception;
	void updateWhithoutPass(Users Users) throws Exception;

	public Users login(String loginId, String loginPass)throws Exception;
	public void insertacount(Users user) throws Exception;
	public void updateaccount(Users Users) throws Exception;
	public void updateAccountWhithoutPass(Users Users) throws Exception;


}

