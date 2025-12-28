package com.art.usermanagement.service.account;

import com.art.usermanagement.dto.request.AccountSearchQueryParam;
import com.art.usermanagement.dto.request.RegistrationRequest;
import com.art.usermanagement.dto.response.AccountDetailsDto;
import com.art.usermanagement.dto.response.AccountDto;
import com.art.usermanagement.dto.response.AccountSummaryDto;
import com.art.usermanagement.dto.response.PaginationDto;
import com.art.usermanagement.exception.*;
import com.art.usermanagement.model.Account;
import com.art.usermanagement.model.UserRole;
import com.art.usermanagement.model.AccountStatus;
import com.art.usermanagement.repository.account.AccountCriteriaRepo;
import com.art.usermanagement.repository.account.AccountRepo;
import com.art.usermanagement.repository.blacklist.BlacklistRepo;
import com.art.usermanagement.security.AccountDetails;
import com.art.usermanagement.util.PaginationHelper;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepo accountRepo;
    private AccountCriteriaRepo accountCriteriaRepo;
    private BlacklistRepo blacklistRepo;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepo accountRepo, AccountCriteriaRepo accountCriteriaRepo, BlacklistRepo blacklistRepo, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder)
    {
        this.accountRepo = accountRepo;
        this.accountCriteriaRepo = accountCriteriaRepo;
        this.blacklistRepo = blacklistRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isBlacklisted(String phoneNumber, String nrcNumber)
    {
        return this.blacklistRepo.existsByPhoneNumberOrNrcNumber(phoneNumber, nrcNumber);
    }

    @Override
    public AccountSummaryDto register(RegistrationRequest request)
    {
        // check details in blacklist table. If existed, throw blacklisted account exception. If not, continue.
        boolean isBlacklisted = this.isBlacklisted(request.phoneNumber(), request.nrcNumber());

        if (isBlacklisted)
            throw new BlacklistedAccountException("This account is blacklisted.");

        // Search details in account table. If no record is found, process registration and return dto. exit method.
        Optional<Account> accountOptional = this.accountRepo.findFirstByPhoneNumberOrNrcNumber(request.phoneNumber(),
                request.nrcNumber());

        if (accountOptional.isEmpty()) {
            Account newUserAccount = getNewUserAccount(request);
            Account savedAccount = this.accountRepo.save(newUserAccount);
            return this.modelMapper.map(savedAccount, AccountSummaryDto.class);
        }

        // If a record is found and status is not rejected, throw already existed exception or blocked credential
        // exception and AlreadyRegisteredException
        Account foundAccount = accountOptional.get();
        if (foundAccount.getStatus() == AccountStatus.APPROVED) {
            throw new AccountAlreadyExistedException("This phone number or nrc number already exists.");
        }

        if (foundAccount.getStatus() == AccountStatus.BLOCKED) {
            throw new BlockedCredentialException("This phone number or nrc number can't never be used to register.");
        }

        if (foundAccount.getStatus() == AccountStatus.REGISTERED) {
            throw new AlreadyExistedException("This phone number or nrc number already exists.");
        }

        // If found record's status is rejected, delete old record and process re-registration. exit method.
        this.accountRepo.delete(foundAccount);
        this.accountRepo.flush();
        Account newUserAccount = this.accountRepo.save(getNewUserAccount(request));
        return this.modelMapper.map(newUserAccount, AccountSummaryDto.class);
    }

    private Account getNewUserAccount(RegistrationRequest request)
    {
        return Account.builder()
                .id(null)
                .name(request.name())
                .password(this.passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .nrcNumber(request.nrcNumber())
                .status(AccountStatus.REGISTERED)
                .role(UserRole.USER)
                .build();
    }

    @Override
    public AccountDetailsDto approveAccount(UUID id)
    {
        Account account = this.accountRepo.findByIdAndStatus(id, AccountStatus.REGISTERED)
                .orElseThrow(() -> new UserAccountNotFoundException("Account registration with id " + id + " not " +
                        "found."));
        account.setStatus(AccountStatus.APPROVED);
        Account approvedAccount = this.accountRepo.save(account);
        return this.modelMapper.map(approvedAccount, AccountDetailsDto.class);
    }

    @Override
    public AccountDetailsDto resetApprovedAccount(UUID id)
    {
        Account approvedAccount = this.accountRepo.findByIdAndStatus(id, AccountStatus.APPROVED).orElseThrow(() -> new UserAccountNotFoundException("Account with id " + id + " not found."));
//        unapprove account
        approvedAccount.setStatus(AccountStatus.REGISTERED);
        Account unapprovedAccount = this.accountRepo.save(approvedAccount);
        return this.modelMapper.map(unapprovedAccount, AccountDetailsDto.class);
    }

    @Override
    public AccountDetailsDto rejectAccount(UUID id)
    {
        Account account = this.accountRepo.findByIdAndStatus(id, AccountStatus.REGISTERED)
                .orElseThrow(() -> new UserAccountNotFoundException("Account registration with id " + id + " not " +
                        "found."));
        account.setStatus(AccountStatus.REJECTED);
        Account rejectedAccount = this.accountRepo.save(account);
        return this.modelMapper.map(rejectedAccount, AccountDetailsDto.class);
    }

    @Override
    public AccountDetailsDto resetRejectedAccount(UUID id)
    {
        Account rejectedAccount = this.accountRepo.findByIdAndStatus(id, AccountStatus.REJECTED).orElseThrow(() -> new UserAccountNotFoundException("Account registration with id " + id + " not found."));
//        unreject account
        rejectedAccount.setStatus(AccountStatus.REGISTERED);
        Account unrejectedAccount = this.accountRepo.save(rejectedAccount);
        return this.modelMapper.map(unrejectedAccount, AccountDetailsDto.class);
    }

    @Override
    public AccountDetailsDto blockAccount(UUID id)
    {
        Account account = this.accountRepo.findByIdAndStatus(id, AccountStatus.APPROVED)
                .orElseThrow(() -> new UserAccountNotFoundException("Account with id " + id + " not found."));

        account.setStatus(AccountStatus.BLOCKED);
        Account blockedAccount = this.accountRepo.save(account);
        return this.modelMapper.map(blockedAccount, AccountDetailsDto.class);
    }

    @Override
    public AccountDetailsDto activeAccount(UUID id)
    {
        Account blockedAccount = this.accountRepo.findByIdAndStatus(id, AccountStatus.BLOCKED).orElseThrow(() -> new UserAccountNotFoundException("Account with id " + id + " not found."));
//        unblock account
        blockedAccount.setStatus(AccountStatus.APPROVED);
        Account unblockedAccount = this.accountRepo.save(blockedAccount);
        return this.modelMapper.map(unblockedAccount, AccountDetailsDto.class);
    }

    @Override
    public Account loadAccountByPhoneNumber(String phoneNumber)
    {
        return this.accountRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Account with phone number " + phoneNumber + " " +
                        "not found."));
    }

    @Override
    public AccountDto findById(UUID id)
    {
//        TODO: I have to check the current whether he is super user or user. If he is super user, he can view any
//         profile. But he is a user, he can only view his profile. So, I didn't use id check at controller method
//         level using PreAuthorize with expression language.
        Account foundAccount = this.accountRepo.findById(id)
                .orElseThrow(() -> new UserAccountNotFoundException("Account with id " + id + " not found."));

        AccountDetails accountDetails =
                (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isSuperUser = accountDetails.getAuthorities().contains(new SimpleGrantedAuthority("SUPER_USER"));

        if (isSuperUser) {
            return this.modelMapper.map(foundAccount, AccountDetailsDto.class);
        }

        boolean isOwnAccount = id.equals(accountDetails.getAccountId());
        if (!isOwnAccount)
            throw new AccessDeniedException("You can only view your own account.");

        return this.modelMapper.map(foundAccount, AccountSummaryDto.class);
    }

    @Override
    public PaginationDto filterAccounts(AccountSearchQueryParam param)
    {
        List<AccountDetailsDto> accountDetailsDtoList = this.accountCriteriaRepo.search(param).stream().map(account -> this.modelMapper.map(account,
                AccountDetailsDto.class)).toList();

        long totalItems = this.accountCriteriaRepo.getCount(param);
        return PaginationHelper.buildPagination(param.getPage(), param.getSize(), totalItems, accountDetailsDtoList);
    }
}
