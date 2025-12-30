package com.art.usermanagement.service.blacklist;

import com.art.usermanagement.dto.request.BlacklistFilterParam;
import com.art.usermanagement.dto.response.BlackListDto;
import com.art.usermanagement.dto.response.PaginationDto;
import com.art.usermanagement.exception.AlreadyExistedException;
import com.art.usermanagement.exception.BlacklistNotFoundException;
import com.art.usermanagement.exception.UserAccountNotFoundException;
import com.art.usermanagement.model.Account;
import com.art.usermanagement.model.Blacklist;
import com.art.usermanagement.repository.account.AccountRepo;
import com.art.usermanagement.repository.blacklist.BlacklistRepo;
import com.art.usermanagement.security.AccountDetails;
import com.art.usermanagement.util.PaginationHelper;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BlacklistServiceImpl implements BlacklistService {

    private BlacklistRepo blacklistRepo;
    private ModelMapper modelMapper;
    private AccountRepo accountRepo;

    public BlacklistServiceImpl(BlacklistRepo blacklistRepo, ModelMapper modelMapper, AccountRepo accountRepo)
    {
        this.blacklistRepo = blacklistRepo;
        this.modelMapper = modelMapper;
        this.accountRepo = accountRepo;
    }

    @Override
    public BlackListDto create(UUID accountId, AccountDetails superUser)
    {
//        I don't have to check account status because once a blacklist record is created, related account will be
//        deleted.
        Account targetAccount = this.accountRepo.findById(accountId).orElseThrow(() -> new UserAccountNotFoundException(
                "Account with id " + accountId + "not found."));

        boolean isExisted = this.blacklistRepo.existsByPhoneNumberOrNrcNumber(targetAccount.getPhoneNumber(),
                targetAccount.getNrcNumber());

        if (isExisted)
            throw new AlreadyExistedException("This phone number or nrc number is already blacklisted.");

        Blacklist blacklist = Blacklist.builder()
                .id(null)
                .phoneNumber(targetAccount.getPhoneNumber())
                .nrcNumber(targetAccount.getNrcNumber())
                .account(superUser.getAccount())
                .build();

        this.blacklistRepo.save(blacklist);
        this.accountRepo.delete(targetAccount);

        return this.modelMapper.map(blacklist, BlackListDto.class);
    }

    @Override
    public BlackListDto getBlackListById(UUID id)
    {
        Blacklist blacklist = this.blacklistRepo.findById(id).orElseThrow(
                () -> new BlacklistNotFoundException("Blacklist record with id " + id + " not found.")
        );

        return this.modelMapper.map(blacklist, BlackListDto.class);
    }

    @Override
    public PaginationDto filter(BlacklistFilterParam param)
    {
        Pageable pageable = getPageable(param);
        Page<Blacklist> blacklists = this.blacklistRepo.filter(pageable);

        List<BlackListDto> blackListDtos = blacklists.stream().map(blacklist -> this.modelMapper.map(blacklist, BlackListDto.class)).toList();

        long totalItems = this.blacklistRepo.count();

        return PaginationHelper.buildPagination(param.getPage(), param.getSize(), totalItems, blackListDtos);
    }

    private static @NonNull Pageable getPageable(BlacklistFilterParam param)
    {
        Sort.Direction sortOrder = param.getOrder().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortOrder, param.getSortBy());
        Pageable pageable = PageRequest.of(param.getPage() - 1, param.getSize(), sort);
        return pageable;
    }

    @Override
    public void delete(UUID blackListId, AccountDetails superUser)
    {
        Blacklist blacklist = this.blacklistRepo.findById(blackListId).orElseThrow(() -> new BlacklistNotFoundException("Blacklist record " +
                "with id " + blackListId + " not found."));

        Account superUserAccount = superUser.getAccount();

        superUserAccount.getBlacklists().remove(blacklist);
        this.blacklistRepo.delete(blacklist); // Actually, this can be ignored.
    }
}
