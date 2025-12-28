package com.art.usermanagement.security;

import com.art.usermanagement.model.Account;
import com.art.usermanagement.model.AccountStatus;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AccountDetails implements UserDetails {

    private Account account;

    public AccountDetails(Account account)
    {
        this.account = account;
    }

    public static AccountDetails of(Account account)
    {
        return new AccountDetails(account);
    }

    public UUID getAccountId()
    {
        return this.account.getId();
    }

    public AccountStatus getAccountStatus()
    {
        return this.account.getStatus();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return List.of(new SimpleGrantedAuthority(this.account.getRole().toString()));
    }

    @Override
    public @Nullable String getPassword()
    {
        return this.account.getPassword();
    }

    @Override
    public String getUsername()
    {
        return this.account.getPhoneNumber();
    }

    public String getNrcNumber()
    {
        return this.account.getNrcNumber();
    }

    public Account getAccount()
    {
        return this.account;
    }
}
