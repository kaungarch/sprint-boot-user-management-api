package com.art.usermanagement.service.account;

import com.art.usermanagement.dto.request.AccountSearchQueryParam;
import com.art.usermanagement.dto.request.RegistrationRequest;
import com.art.usermanagement.dto.response.AccountDetailsDto;
import com.art.usermanagement.dto.response.AccountDto;
import com.art.usermanagement.dto.response.AccountSummaryDto;
import com.art.usermanagement.dto.response.PaginationDto;
import com.art.usermanagement.model.Account;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountSummaryDto register(RegistrationRequest request); // user

    AccountDetailsDto approveAccount(UUID id); // super user
    AccountDetailsDto resetApprovedAccount(UUID id); // super user
    AccountDetailsDto rejectAccount(UUID id); // super user
    AccountDetailsDto resetRejectedAccount(UUID id); // super user
    AccountDetailsDto blockAccount(UUID id); // super user
    AccountDetailsDto activeAccount(UUID id); // super user

    // filter methods
    PaginationDto filterAccounts(AccountSearchQueryParam param); // super user

    // helper method for security
    Account loadAccountByPhoneNumber(String phoneNumber);
    boolean isBlacklisted(String phoneNumber, String nrcNumber);

    AccountDto findById(UUID id);


}
