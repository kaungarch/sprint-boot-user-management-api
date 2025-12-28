package com.art.usermanagement.IntegrationTest.account;

import com.art.usermanagement.dto.request.RegistrationRequest;
import com.art.usermanagement.dto.response.AccountDetailsDto;
import com.art.usermanagement.dto.response.AccountSummaryDto;
import com.art.usermanagement.exception.AccountAlreadyExistedException;
import com.art.usermanagement.exception.AlreadyExistedException;
import com.art.usermanagement.exception.UserAccountNotFoundException;
import com.art.usermanagement.model.Account;
import com.art.usermanagement.model.UserRole;
import com.art.usermanagement.model.AccountStatus;
import com.art.usermanagement.repository.account.AccountCriteriaRepoImpl;
import com.art.usermanagement.repository.account.AccountRepo;
import com.art.usermanagement.service.account.AccountService;
import com.art.usermanagement.service.account.AccountServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {"spring.sql.init.mode=never", "spring.jpa.hibernate.ddl-auto=create"})
@Import({AccountServiceImpl.class, AccountCriteriaRepoImpl.class, ModelMapper.class, BCryptPasswordEncoder.class})
public class AccountServiceTest {

    public static UUID FAKE_UUID = UUID.fromString("3f9b6c54-1e2a-4d8f-9b27-4e7b8cdfe123");

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepo accountRepo;


    private RegistrationRequest request = new RegistrationRequest("Alice", "aLice@123456", "959123456789", "5/KALAWA(C)" +
            "123456");

    @Test
    public void testSuccessfulRegistration()
    {
        AccountSummaryDto registered = this.accountService.register(this.request);
        assertNotNull(registered);
        assertEquals(this.request.name(), registered.getName());
        assertEquals(this.request.phoneNumber(), registered.getPhoneNumber());
        assertEquals(this.request.nrcNumber(), registered.getNrcNumber());
        assertEquals(UserRole.USER, registered.getRole());
    }

    @Test
    @Transactional
    public void testFailedRegistrationDueToDuplicateCredential()
    {
        AccountSummaryDto firstAttempt = accountService.register(request);
        assertThrows(AlreadyExistedException.class, () -> accountService.register(request), "This phone number or nrc number has already registered.");
    }

    @Test
    public void testSuccessfulAccountApproval()
    {
        AccountSummaryDto registered = this.accountService.register(this.request);
        this.accountService.approveAccount(registered.getId());

        Optional<Account> accountOptional = this.accountRepo.findById(registered.getId());
        assertNotNull(accountOptional.get());
        assertEquals(AccountStatus.APPROVED, accountOptional.get().getStatus());
    }

    @Test
    public void testFailedAccountApprovalDueToAccountNotFound()
    {
        assertThrows(UserAccountNotFoundException.class, () -> this.accountService.approveAccount(FAKE_UUID));
    }

    @Test
    public void testSuccessfulAccountUnapproval()
    {
        AccountSummaryDto registered = this.accountService.register(request);
//        approve that registration
        AccountDetailsDto approvedAccount = this.accountService.approveAccount(registered.getId());
//        unapprove that
        AccountDetailsDto unapprovedAccount = this.accountService.resetApprovedAccount(approvedAccount.getId());
//        assert
        assertThat(unapprovedAccount).isNotNull();
        assertThat(unapprovedAccount.getStatus()).isEqualTo(AccountStatus.REGISTERED);
    }

    @Test
    public void testFailedUnapprovalDueToIdNotFound()
    {
        Executable executable = () -> this.accountService.resetApprovedAccount(FAKE_UUID);
        assertThrows(UserAccountNotFoundException.class, executable);
    }

    @Test
    public void testFailedUnapprovalDueToAccountNotFound()
    {
        AccountSummaryDto registered = this.accountService.register(request);
        Executable executable = () -> this.accountService.resetApprovedAccount(registered.getId());
        assertThrows(UserAccountNotFoundException.class, executable);
    }

    @Test
    public void testFailureWhenApprovingRejectedRegistration()
    {
        AccountSummaryDto registered = this.accountService.register(this.request);
//        admin reject this registration
        this.accountService.rejectAccount(registered.getId());
//        assert that rejected registration can't be approved.
        Executable executable = () -> this.accountService.approveAccount(registered.getId());
        assertThrows(UserAccountNotFoundException.class, executable);
    }

    @Test
    @Transactional
    public void testFailedRegistrationDueToExistingAccount()
    {
        AccountSummaryDto registered = this.accountService.register(this.request);
        this.accountService.approveAccount(registered.getId());

//        try to register with credentials of approved account
        assertThrows(AccountAlreadyExistedException.class, () -> this.accountService.register(this.request));
    }

    @Test
    public void testSuccessfulAccountRejection()
    {
        AccountSummaryDto registered = this.accountService.register(this.request);
//        admin reject this registration.
        this.accountService.rejectAccount(registered.getId());

//        find account by id.
        Optional<Account> rejectedAccount = this.accountRepo.findById(registered.getId());
        assertNotNull(rejectedAccount.get());
//        assert the registered account is rejected.
        assertEquals(AccountStatus.REJECTED, rejectedAccount.get().getStatus());
    }

    @Test
    public void testFailedAccountRejectionDueToAccountNotFound()
    {
        assertThrows(UserAccountNotFoundException.class, () -> this.accountService.rejectAccount(FAKE_UUID));
    }

    @Test
    public void testFailedAccountRejectionWhenTryingToRejectApprovedAccount()
    {
        AccountSummaryDto registered = this.accountService.register(this.request);
//        approve this registration
        this.accountService.approveAccount(registered.getId());
//        assert exception throw
        Executable executable = () -> this.accountService.rejectAccount(registered.getId());
        assertThrows(UserAccountNotFoundException.class, executable);
    }

    @Test
    public void testSuccessfulAccountRejectionCancellation()
    {
        AccountSummaryDto registered = this.accountService.register(request);
        AccountDetailsDto rejectedAccount = this.accountService.rejectAccount(registered.getId());
//        cancel reject
        AccountDetailsDto unrejectedAccount = this.accountService.resetRejectedAccount(rejectedAccount.getId());

        assertThat(unrejectedAccount).isNotNull();
        assertThat(unrejectedAccount.getStatus()).isEqualTo(AccountStatus.REGISTERED);
    }

    @Test
    public void testFailedAccountRejectionCancellationDueToIdNotFound()
    {
        assertThrows(UserAccountNotFoundException.class, () -> this.accountService.resetRejectedAccount(FAKE_UUID));
    }

    @Test
    public void testFailedAccountRejectionCancellationDueToAccountNotFound()
    {
        AccountSummaryDto registered = this.accountService.register(request);
        assertThrows(UserAccountNotFoundException.class, () -> this.accountService.resetRejectedAccount(registered.getId()));
    }

    @Test
    public void testSuccessfulRegistrationUsingRejectedAccountCredentials()
    {
//        1st attempt to register
        AccountSummaryDto firstAttempt = this.accountService.register(this.request);
//        admin rejected 1st registration
        this.accountService.rejectAccount(firstAttempt.getId());
//        2nd attempt to register using rejected credentials but this will delete old record
        AccountSummaryDto secondAttempt = this.accountService.register(this.request);

//        assert older record is deleted
        List<Account> accountList = this.accountRepo.findAll();
        assertEquals(1, accountList.size());

//        fetch second attempt
        Optional<Account> accountOptional = this.accountRepo.findById(secondAttempt.getId());
        assertNotNull(accountOptional.get());
        assertEquals(AccountStatus.REGISTERED, accountOptional.get().getStatus());

    }

    @Test
    public void testSuccessfulAccountBlocking()
    {
        AccountSummaryDto registered = this.accountService.register(this.request);
//        first, admin will need to approve.
        this.accountService.approveAccount(registered.getId());
//        admin now block this user.
        this.accountService.blockAccount(registered.getId());

        Optional<Account> accountOptional = this.accountRepo.findById(registered.getId());
//        assert that user is now blocked.
        assertNotNull(accountOptional.get());
        assertEquals(AccountStatus.BLOCKED, accountOptional.get().getStatus());
    }

    @Test
    public void testFailedAccountBlockingDueToAccountNotFound()
    {
        assertThrows(UserAccountNotFoundException.class, () -> this.accountService.blockAccount(FAKE_UUID));
    }

    @Test
    public void testFailedAccountBlockingDueToApprovedAccountNotFound()
    {
//        register an account
        AccountSummaryDto registered = this.accountService.register(this.request);
//        try to block this account and assert that it can't be blocked
        assertThrows(UserAccountNotFoundException.class, () -> this.accountService.blockAccount(registered.getId()));
    }

    @Test
    public void testSuccessfulAccountUnblocking()
    {
        AccountSummaryDto registered = this.accountService.register(request);
        AccountDetailsDto approvedAccount = this.accountService.approveAccount(registered.getId());
        AccountDetailsDto blockedAccount = this.accountService.blockAccount(approvedAccount.getId());
//        unblock
        AccountDetailsDto unblockedAccount = this.accountService.activeAccount(blockedAccount.getId());
        assertThat(unblockedAccount).isNotNull();
        assertThat(unblockedAccount.getStatus()).isEqualTo(AccountStatus.APPROVED);
    }

    @Test
    public void testSuccessfulLoadByPhoneNumber()
    {
        AccountSummaryDto registered = this.accountService.register(this.request);
        Account account = this.accountService.loadAccountByPhoneNumber(registered.getPhoneNumber());
        assertNotNull(registered.getPhoneNumber(), account.getPhoneNumber());
    }

    @Test
    public void testFailedLoadByPhoneNumber()
    {
        Executable executable = () -> this.accountService.loadAccountByPhoneNumber("959987654321");
        assertThrows(UsernameNotFoundException.class, executable);
    }
}
