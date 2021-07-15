package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.AlertEtlResponse;

import java.util.Optional;

@RequiredArgsConstructor
class CreateMessageType {

  @NonNull private final AlertEtlResponse alertEtlResponse;
  private final String dtpMessageTypeNullAlias;
  private final String nbpMessageTypeNullAlias;

  String create() {
    return Optional
        .of(alertEtlResponse)
        .map(AlertEtlResponse::getMessageType)
        .orElse(generateMessageTypeNullAliasValueDependOnSystem(alertEtlResponse));
  }

  private String generateMessageTypeNullAliasValueDependOnSystem(
      AlertEtlResponse alertEtlResponse) {
    String businessUnit = Optional.of(alertEtlResponse)
        .map(AlertEtlResponse::getBusinessUnit)
        .orElse("");
    String applicationCode = Optional.of(alertEtlResponse)
        .map(AlertEtlResponse::getApplicationCode)
        .orElse("");
    if (businessUnit.contains("DTP") || applicationCode.contains("DTP")) {
      return dtpMessageTypeNullAlias;
    } else if (businessUnit.contains("NBP") || applicationCode.contains("NBP")) {
      return nbpMessageTypeNullAlias;
    } else {
      return "";
    }
  }
}
