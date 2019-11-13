package com.silenteight.sens.webapp.backend.presentation.dto.decisiontree.status;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErrorStatusDto extends StatusDto {

  public static final String NAME = "ERROR";
  @NonNull
  private final String errorMessage;

  public ErrorStatusDto(String errorMessage) {
    super(NAME);
    this.errorMessage = errorMessage;
  }
}
