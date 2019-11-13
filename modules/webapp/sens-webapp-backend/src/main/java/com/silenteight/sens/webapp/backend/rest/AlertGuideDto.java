package com.silenteight.sens.webapp.backend.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.common.support.utils.WhitespaceUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertGuideDto {

  private String unit;
  private String account;
  private String sourceDetails;

  public String getAccount() {
    return WhitespaceUtils.normalizeNbspChars(account);
  }
}
