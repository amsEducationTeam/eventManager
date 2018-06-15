package dao;

import java.util.List;

import domain.Account;

public interface AccountDao{
	List<Account> findAll()throws Exception;

	public String insertAcount(List<Account> Account) throws Exception;
}
