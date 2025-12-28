package com.art.usermanagement.controller.account;

import com.art.usermanagement.dto.request.RegistrationRequest;
import com.art.usermanagement.dto.response.AccountDetailsDto;
import com.art.usermanagement.dto.response.AccountSummaryDto;
import com.art.usermanagement.dto.response.ApiResponse;
import com.art.usermanagement.service.account.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("${apiPrefix}/accounts")
public class AccountController {

    @Value("apiPrefix")
    private String apiPrefix;

    private AccountService accountService;

    public AccountController(AccountService accountService)
    {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AccountSummaryDto>> register(@RequestBody @Validated RegistrationRequest registrationRequest)
    {
        AccountSummaryDto registeredAccount = this.accountService.register(registrationRequest);

        ApiResponse<AccountSummaryDto> apiResponse = ApiResponse.<AccountSummaryDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Account registered successfully")
                .data(registeredAccount)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();

        URI URI = ServletUriComponentsBuilder.fromPath("/accounts/{id}").build(registeredAccount.getId());
//        URI uri = URI.create(apiPrefix + "/accounts/" + registeredAccount.getId());
        return ResponseEntity.created(URI).body(apiResponse);
    }
}
