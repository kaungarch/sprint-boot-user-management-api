package com.art.usermanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackListDto {
    private UUID id;
    private String phoneNumber;
    private String nrcNumber;
    private AccountDetailsDto blackListedBy;
}
