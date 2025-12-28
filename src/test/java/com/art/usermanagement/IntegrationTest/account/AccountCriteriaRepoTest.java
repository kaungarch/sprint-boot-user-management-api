package com.art.usermanagement.IntegrationTest.account;

import com.art.usermanagement.dto.request.AccountSearchQueryParam;
import com.art.usermanagement.model.Account;
import com.art.usermanagement.repository.account.AccountCriteriaRepo;
import com.art.usermanagement.repository.account.AccountCriteriaRepoImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({AccountCriteriaRepoImpl.class})
public class AccountCriteriaRepoTest {
    @Autowired
    private AccountCriteriaRepo accountCriteriaRepo;

    @Test
    public void testWithDefaultParams()
    {
        AccountSearchQueryParam DEFAULT_PARAM = new AccountSearchQueryParam();
        List<Account> accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        System.out.println(accountList.size());
        assertEquals(10, accountList.size());
    }

    @Test
    public void testFilterRole()
    {
        AccountSearchQueryParam DEFAULT_PARAM = new AccountSearchQueryParam();
        DEFAULT_PARAM.setRole("USER");
        List<Account> accountList = new ArrayList<>();
        accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        assertEquals(9, accountList.size());

        DEFAULT_PARAM.setRole("SUPER_USER");
        accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        assertEquals(1, accountList.size());
    }

    @Test
    public void testFilterRegisteredUserList()
    {
        AccountSearchQueryParam DEFAULT_PARAM = new AccountSearchQueryParam();
        DEFAULT_PARAM.setStatus("REGISTERED");
        List<Account> accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        assertEquals(3, accountList.size());
    }

    @Test
    public void testFilterApprovedUserList()
    {
        AccountSearchQueryParam DEFAULT_PARAM = new AccountSearchQueryParam();
        DEFAULT_PARAM.setStatus("APPROVED");
        List<Account> accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        assertEquals(4, accountList.size());
    }

    @Test
    public void testFilterRejectedUserList()
    {
        AccountSearchQueryParam DEFAULT_PARAM = new AccountSearchQueryParam();
        DEFAULT_PARAM.setStatus("REJECTED");
        List<Account> accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        assertEquals(2, accountList.size());
    }

    @Test
    public void testFilterBlockedUserList()
    {
        AccountSearchQueryParam DEFAULT_PARAM = new AccountSearchQueryParam();
        DEFAULT_PARAM.setStatus("BLOCKED");
        List<Account> accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        assertEquals(1, accountList.size());
    }

    @Test
    public void testSortByName()
    {
        AccountSearchQueryParam DEFAULT_PARAM = new AccountSearchQueryParam();
        DEFAULT_PARAM.setSortBy("name");
        List<Account> accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        Account firstAccount = accountList.getFirst();
        assertEquals("Aung Myint", firstAccount.getName());

        DEFAULT_PARAM.setOrder("desc");
        accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        firstAccount = accountList.getFirst();
        assertEquals("Zaw Min", firstAccount.getName());

    }

    @Test
    public void testSortByPhoneNumber()
    {
        AccountSearchQueryParam DEFAULT_PARAM = new AccountSearchQueryParam();

        DEFAULT_PARAM.setSortBy("phoneNumber");
        List<Account> accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        for (Account account : accountList)
            System.out.println(account.getPhoneNumber());
    }

    @Test
    public void testSortByNrcNumber()
    {
        AccountSearchQueryParam DEFAULT_PARAM = new AccountSearchQueryParam();
        DEFAULT_PARAM.setSortBy("nrcNumber");
        DEFAULT_PARAM.setOrder("desc");
        List<Account> accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        for (Account account : accountList)
            System.out.println(account.getNrcNumber());
    }

    @Test
    public void testPageAndLimit()
    {
        AccountSearchQueryParam DEFAULT_PARAM = new AccountSearchQueryParam();
        DEFAULT_PARAM.setSize(5);
        List<Account> accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        assertEquals(5, accountList.size());
        for (Account account : accountList)
            System.out.println(account.getName());

        DEFAULT_PARAM.setPage(2);
        accountList = this.accountCriteriaRepo.search(DEFAULT_PARAM);
        for (Account account : accountList)
            System.out.println(account.getName());
    }
}
