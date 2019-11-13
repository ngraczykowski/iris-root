package com.silenteight.sens.webapp.backend.presentation.dto.alert;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import com.silenteight.sens.webapp.backend.presentation.dto.common.FieldEntityDto;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PartyDto extends FieldEntityDto {

  @NonNull
  private final String externalId;
}
