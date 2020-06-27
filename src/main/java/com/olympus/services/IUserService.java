package com.olympus.services;

import com.olympus.data.model.User;
import com.olympus.security.AccountCredentialsVO;

public interface IUserService {
	 public User findByUsernameOrEmail(String username, String email);   
	 public User save(AccountCredentialsVO vo);
}
