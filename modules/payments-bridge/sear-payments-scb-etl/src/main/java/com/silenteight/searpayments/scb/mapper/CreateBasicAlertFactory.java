package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.dto.input.AlertMessageDto;

@RequiredArgsConstructor
class CreateBasicAlertFactory {

  private final String gitCommitId;

  CreateBasicAlert create(@NonNull String dataCenter, AlertMessageDto inputDto) {
    return new CreateBasicAlert(gitCommitId, dataCenter, inputDto);
  }
}
