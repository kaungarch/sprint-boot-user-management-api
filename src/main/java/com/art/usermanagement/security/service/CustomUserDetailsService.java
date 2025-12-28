package com.art.usermanagement.security.service;

import com.art.usermanagement.security.AccountDetails;
import com.art.usermanagement.service.account.AccountService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    private AccountService accountService;

    public CustomUserDetailsService(AccountService accountService)
    {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return new AccountDetails(this.accountService.loadAccountByPhoneNumber(username));
    }
}
