package com.art.usermanagement.dto.response;

import com.art.usermanagement.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AccountSummaryDto extends AccountDto {
    private UUID id;
    private String name;
    private String phoneNumber;
    private String nrcNumber;
    private UserRole role;


}
