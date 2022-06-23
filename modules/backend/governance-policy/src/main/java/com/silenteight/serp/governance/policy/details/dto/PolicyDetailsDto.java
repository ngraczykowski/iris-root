package com.silenteight.serp.governance.policy.details.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.serp.governance.policy.domain.PolicyState;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
@RequiredArgsConstructor
public class PolicyDetailsDto {

  @NonNull
  UUID id;
  @NonNull
  String name;
  @NonNull
  String policyName;
  String description;
  @NonNull
  PolicyState state;
  @NonNull
  OffsetDateTime createdAt;
  @NonNull
  String createdBy;
  OffsetDateTime updatedAt;
  @NonNull
  String updatedBy;
  long stepsCount;

  public PolicyDetailsDto(PolicyDto policyDto, long count) {
    this(policyDto.getId(),
         policyDto.getName(),
         policyDto.getPolicyName(),
         policyDto.getDescription(),
         policyDto.getState(),
         policyDto.getCreatedAt(),
         policyDto.getCreatedBy(),
         policyDto.getUpdatedAt(),
         policyDto.getUpdatedBy(),
         count);
  }
}
