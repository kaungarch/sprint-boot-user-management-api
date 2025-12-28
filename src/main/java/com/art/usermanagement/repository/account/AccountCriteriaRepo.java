package com.art.usermanagement.repository.account;

import com.art.usermanagement.dto.request.AccountSearchQueryParam;
import com.art.usermanagement.dto.response.AccountSummaryDto;
import com.art.usermanagement.model.Account;

import java.util.List;

public interface AccountCriteriaRepo {
    List<Account> search(AccountSearchQueryParam param);

    long getCount(AccountSearchQueryParam param);
}
