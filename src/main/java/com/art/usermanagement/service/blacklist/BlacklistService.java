package com.art.usermanagement.service.blacklist;

import com.art.usermanagement.dto.response.BlackListDto;
import com.art.usermanagement.security.AccountDetails;

import java.util.UUID;

public interface BlacklistService {
    BlackListDto create(UUID accountId, AccountDetails superUser);

    void delete(UUID blackListId, AccountDetails superUser);
}
