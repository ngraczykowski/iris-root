package com.silenteight.sens.webapp.backend.presentation.dto.alert;

import lombok.Data;

import java.util.List;

@Data
public class AlertModelDto {

  private final List<String> alertFieldNames;
}
