package dao;

import java.util.List;

import domain.Account;

public interface AccountDao{
	List<Account> findAll()throws Exception;

	public void insertAcount(Account Account) throws Exception;
}
