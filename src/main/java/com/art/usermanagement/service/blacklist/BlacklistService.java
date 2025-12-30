package com.art.usermanagement.service.blacklist;

import com.art.usermanagement.dto.request.BlacklistFilterParam;
import com.art.usermanagement.dto.response.BlackListDto;
import com.art.usermanagement.dto.response.PaginationDto;
import com.art.usermanagement.security.AccountDetails;

import java.util.UUID;

public interface BlacklistService {
    BlackListDto create(UUID accountId, AccountDetails superUser);

    BlackListDto getBlackListById(UUID id);

    PaginationDto filter(BlacklistFilterParam param);

    void delete(UUID blackListId, AccountDetails superUser);
}
