package com.art.usermanagement.controller.blacklist;

import com.art.usermanagement.dto.response.ApiResponse;
import com.art.usermanagement.dto.response.BlackListDto;
import com.art.usermanagement.security.AccountDetails;
import com.art.usermanagement.service.blacklist.BlacklistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("${apiPrefix}/blacklists")
public class BlacklistController {

    private BlacklistService blacklistService;

    public BlacklistController(BlacklistService blacklistService)
    {
        this.blacklistService = blacklistService;
    }

    @PostMapping("/{accountId}")
    @PreAuthorize("hasAuthority('SUPER_USER')")
    public ResponseEntity<ApiResponse<BlackListDto>> createBlacklist(
            @PathVariable UUID accountId,
            @AuthenticationPrincipal AccountDetails accountDetails)
    {
        BlackListDto blackListDto = this.blacklistService.create(accountId, accountDetails);
        ApiResponse<BlackListDto> apiResponse = ApiResponse.<BlackListDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Black listed successfully")
                .data(blackListDto)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        URI uri = ServletUriComponentsBuilder.fromPath("/blacklists/{id}").build(blackListDto.getId());
        return ResponseEntity.created(uri).body(apiResponse);
    }

    @DeleteMapping("/{blacklistId}")
    @PreAuthorize("hasAuthority('SUPER_USER')")
    public ResponseEntity<ApiResponse<Void>> removeBlacklist(
            @PathVariable UUID blacklistId,
            @AuthenticationPrincipal AccountDetails accountDetails)
    {
        this.blacklistService.delete(blacklistId, accountDetails);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Removed blacklist record successfully")
                .timestamp(LocalDateTime.now())
                .data(null)
                .errors(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
