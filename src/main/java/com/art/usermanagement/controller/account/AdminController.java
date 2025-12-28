package com.art.usermanagement.controller.account;

import com.art.usermanagement.dto.request.AccountSearchQueryParam;
import com.art.usermanagement.dto.response.AccountDetailsDto;
import com.art.usermanagement.dto.response.AccountDto;
import com.art.usermanagement.dto.response.ApiResponse;
import com.art.usermanagement.dto.response.PaginationDto;
import com.art.usermanagement.service.account.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "${apiPrefix}/accounts")
public class AdminController {

    private AccountService accountService;

    public AdminController(AccountService accountService)
    {
        this.accountService = accountService;
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('SUPER_USER')")
    public ResponseEntity<ApiResponse<AccountDetailsDto>> approve(@PathVariable UUID id)
    {
        AccountDetailsDto approvedAccount = this.accountService.approveAccount(id);
        ApiResponse<AccountDetailsDto> apiResponse = ApiResponse.<AccountDetailsDto>builder()
                .status(HttpStatus.OK.value())
                .message("Account status updated")
                .data(approvedAccount)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}/cancel-approval")
    @PreAuthorize("hasAuthority('SUPER_USER')")
    public ResponseEntity<ApiResponse<AccountDetailsDto>> cancelApproval(@PathVariable UUID id)
    {
        AccountDetailsDto updatedAccount = this.accountService.resetApprovedAccount(id);
        ApiResponse<AccountDetailsDto> apiResponse = ApiResponse.<AccountDetailsDto>builder()
                .status(HttpStatus.OK.value())
                .message("Account status updated")
                .data(updatedAccount)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}/cancel-rejection")
    @PreAuthorize("hasAuthority('SUPER_USER')")
    public ResponseEntity<ApiResponse<AccountDetailsDto>> cancelRejection(@PathVariable UUID id)
    {
        AccountDetailsDto updatedAccount = this.accountService.resetRejectedAccount(id);
        ApiResponse<AccountDetailsDto> apiResponse = ApiResponse.<AccountDetailsDto>builder()
                .status(HttpStatus.OK.value())
                .message("Account status updated")
                .data(updatedAccount)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}/unblock")
    @PreAuthorize("hasAuthority('SUPER_USER')")
    public ResponseEntity<ApiResponse<AccountDetailsDto>> unblock(@PathVariable UUID id)
    {
        AccountDetailsDto updatedAccount = this.accountService.activeAccount(id);
        ApiResponse<AccountDetailsDto> apiResponse = ApiResponse.<AccountDetailsDto>builder()
                .status(HttpStatus.OK.value())
                .message("Account status updated")
                .data(updatedAccount)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('SUPER_USER')")
    public ResponseEntity<ApiResponse<AccountDetailsDto>> reject(@PathVariable UUID id)
    {
        AccountDetailsDto rejectedAccount = this.accountService.rejectAccount(id);
        ApiResponse<AccountDetailsDto> apiResponse = ApiResponse.<AccountDetailsDto>builder()
                .status(HttpStatus.OK.value())
                .message("Account status updated")
                .data(rejectedAccount)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}/block")
    @PreAuthorize("hasAuthority('SUPER_USER')")
    public ResponseEntity<ApiResponse<AccountDetailsDto>> block(@PathVariable UUID id)
    {
        AccountDetailsDto blockedAccount = this.accountService.blockAccount(id);
        ApiResponse<AccountDetailsDto> apiResponse = ApiResponse.<AccountDetailsDto>builder()
                .status(HttpStatus.OK.value())
                .message("Account status updated")
                .data(blockedAccount)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_USER', 'USER')")
    public ResponseEntity<ApiResponse<AccountDto>> getAccount(@PathVariable UUID id)
    {
//        TODO: we need to make sure that signed in user id and id from path variable are the same because we just
//         need to compare it at the method level and super_user can also access this method
        AccountDto account = this.accountService.findById(id);

        ApiResponse<AccountDto> apiResponse = ApiResponse.<AccountDto>builder()
                .status(HttpStatus.OK.value())
                .message("Account retrieved successfully")
                .data(account)
                .errors(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_USER')")
    public ResponseEntity<ApiResponse<PaginationDto>> filterAccounts(
            @Validated @ModelAttribute AccountSearchQueryParam param)
    {
        System.out.println(param);
        PaginationDto allAccounts = this.accountService.filterAccounts(param);

        ApiResponse<PaginationDto> apiResponse = ApiResponse.<PaginationDto>builder()
                .data(allAccounts)
                .errors(null)
                .status(HttpStatus.OK.value())
                .message("Account list")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
