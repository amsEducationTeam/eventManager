package dao;

import java.util.List;

import domain.Members;


public interface MembersDao{
	List<Members> findAll()throws Exception;
	Members findById(String id) throws Exception;
	void insert(Members member)throws Exception;
	void update(Members member)throws Exception;
	void delete(Members member)throws Exception;
	Members findByLoginIdAndLoginPass(String loginId,String loginPass) throws Exception;
	List<Members> findAll(int page) throws Exception;
	List<Members> findfive(List<Members> userList) throws Exception;
	double countAll() throws Exception;
	boolean CheckLoginId(String loginId) throws Exception;
	void updateWhithoutPass(Members Members) throws Exception;

	public Members login(String loginId, String loginPass)throws Exception;
	public void insertacount(Members member) throws Exception;
	public void updateaccount(Members Members) throws Exception;
	public void updateAccountWhithoutPass(Members Members) throws Exception;
	void deleteAccount(Members Members) throws Exception;


}

