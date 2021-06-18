package com.silenteight.warehouse.management.group.domain.dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryGroupDto {

  @NonNull
  private UUID id;
  @NonNull
  private String name;
}
