package com.poly.service;

import java.util.List;

import com.poly.entity.Account;

public interface AccountService {

	List<Account> findAll();

	Account create(Account account);

	Account update(Account account);

	void deleteById(String username);

	Account findById(String id);

}
