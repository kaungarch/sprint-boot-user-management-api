package com.art.usermanagement.IntegrationTest.auth;

import com.art.usermanagement.controller.security.jwt.JwtServiceImpl;
import com.art.usermanagement.dto.request.AccountSignInRequest;
import com.art.usermanagement.dto.request.RegistrationRequest;
import com.art.usermanagement.dto.response.AccountDetailsDto;
import com.art.usermanagement.dto.response.AccountSummaryDto;
import com.art.usermanagement.dto.response.AuthResponse;
import com.art.usermanagement.exception.AuthenticationFailedException;
import com.art.usermanagement.repository.account.AccountCriteriaRepoImpl;
import com.art.usermanagement.service.account.AccountService;
import com.art.usermanagement.service.account.AccountServiceImpl;
import com.art.usermanagement.service.auth.AuthService;
import com.art.usermanagement.service.auth.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.sql.init.mode=never"
})
@Import({AccountServiceImpl.class, AccountCriteriaRepoImpl.class, JwtServiceImpl.class,
        BCryptPasswordEncoder.class, ModelMapper.class, AuthServiceImpl.class})
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AccountService accountService;

    private AccountSignInRequest getSignInRequest(String phoneNumber, String password)
    {
        return AccountSignInRequest.builder()
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
    }

    private RegistrationRequest getAccountRegistrationRequest()
    {
        return new RegistrationRequest("Alice", "aLice@12345", "959123456789", "12/KaTaNa(N)123456");
    }

    @Test
    public void testSuccessfulSignIn()
    {
        AccountSummaryDto registered = this.accountService.register(this.getAccountRegistrationRequest());
//        approved by admin
        AccountDetailsDto approvedAccount = this.accountService.approveAccount(registered.getId());
//        try to sign in
        AuthResponse authResponse = this.authService.signIn(this.getSignInRequest(this.getAccountRegistrationRequest().phoneNumber(),
                this.getAccountRegistrationRequest().password()));

        assertThat(authResponse).isNotNull();
        assertThat(authResponse.getAccessToken()).isNotEmpty();
        assertThat(authResponse.getRefreshToken()).isNotEmpty();
    }

    @Test
    public void testSignInFailedDueToAccountNotApproved()
    {
        AccountSummaryDto registered = this.accountService.register(this.getAccountRegistrationRequest());
        Executable executable = () -> this.authService.signIn(this.getSignInRequest(registered.getPhoneNumber(),
                this.getAccountRegistrationRequest().password()));
        assertThrows(AuthenticationFailedException.class, executable);
    }

    @Test
    public void testSignInFailedDueToAccountNotFound()
    {
        Executable executable = () -> this.authService.signIn(this.getSignInRequest("959123456789", "12/KaTaNa(N)123456" +
                "aLice@12345"));

        assertThrows(UsernameNotFoundException.class, executable);
    }

}
