package com.art.usermanagement.IntegrationTest.blacklist;

import com.art.usermanagement.config.UtilConfig;
import com.art.usermanagement.dto.response.BlackListDto;
import com.art.usermanagement.exception.BlacklistNotFoundException;
import com.art.usermanagement.exception.UserAccountNotFoundException;
import com.art.usermanagement.model.Account;
import com.art.usermanagement.model.Blacklist;
import com.art.usermanagement.repository.account.AccountCriteriaRepoImpl;
import com.art.usermanagement.repository.account.AccountRepo;
import com.art.usermanagement.repository.blacklist.BlacklistRepo;
import com.art.usermanagement.security.AccountDetails;
import com.art.usermanagement.service.blacklist.BlacklistService;
import com.art.usermanagement.service.blacklist.BlacklistServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({AccountCriteriaRepoImpl.class, AccountCriteriaRepoImpl.class, BlacklistServiceImpl.class, ModelMapper.class,
        UtilConfig.class})
public class BlacklistServiceTest {

    @Autowired
    private BlacklistService blacklistService;
    @Autowired
    private BlacklistRepo blacklistRepo;
    @Autowired
    private AccountRepo accountRepo;

    private UUID EXISTED_ACCOUNT_ID = UUID.fromString("c2f3d4e5-2222-4f00-9abc-123456789002");
    private UUID SUPER_USER_ID = UUID.fromString("b1e2c3d4-1111-4f00-9abc-123456789001");
    private UUID FAKE_UUID = UUID.fromString("3f9b6c54-1e2a-4d8f-9b27-4e7b8cdfe123");


    private AccountDetails getSuperUser()
    {
        Account account = this.accountRepo.findById(SUPER_USER_ID).orElseThrow(() -> new UserAccountNotFoundException("Account with id " + SUPER_USER_ID + " not found."));
        return AccountDetails.of(account);
    }

    @Test
    public void testBlacklistingAnAccountSuccessfully()
    {
        BlackListDto blackListDto = this.blacklistService.create(EXISTED_ACCOUNT_ID, getSuperUser());
        assertThat(blackListDto).isNotNull();
        assertThat(blackListDto.getBlackListedBy()).isNotNull();

        Optional<Account> accountOptional = this.accountRepo.findById(EXISTED_ACCOUNT_ID);
        assertThat(accountOptional.isEmpty()).isTrue();
    }

    @Test
    public void testFailedBlacklistingDueToAccountNotFound()
    {
        Executable executable = () -> this.blacklistService.create(FAKE_UUID, getSuperUser());
        assertThrows(UserAccountNotFoundException.class, executable);
    }

    @Test
    public void testRemoveBlackListRecordSuccessfully()
    {
        BlackListDto blackListDto = this.blacklistService.create(EXISTED_ACCOUNT_ID, getSuperUser());
        this.blacklistService.delete(blackListDto.getId(), getSuperUser());
        Optional<Blacklist> blacklistOptional = this.blacklistRepo.findById(blackListDto.getId());

        assertThat(blacklistOptional.isEmpty()).isTrue();

        Account account = this.accountRepo.findById(getSuperUser().getAccountId()).orElseThrow(() -> new UserAccountNotFoundException(
                "Account with id " + getSuperUser().getAccountId() + " not found."));
        assertThat(account.getBlacklists().contains(blackListDto)).isFalse();
    }

    @Test
    public void testFailedRemovingBlacklistDueToNotFound()
    {
        Executable executable = () -> this.blacklistService.delete(FAKE_UUID, getSuperUser());
        assertThrows(BlacklistNotFoundException.class, executable);
    }

}
