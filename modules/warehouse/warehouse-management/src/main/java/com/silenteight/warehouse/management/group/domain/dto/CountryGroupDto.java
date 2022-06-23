package com.silenteight.warehouse.management.group.domain.dto;

import lombok.*;

import java.util.UUID;
import javax.validation.constraints.Pattern;

import static com.silenteight.warehouse.common.web.rest.RestValidationConstants.FIELD_REGEX;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryGroupDto {

  @NonNull
  private UUID id;
  @NonNull
  @Pattern(regexp = FIELD_REGEX)
  private String name;
}
