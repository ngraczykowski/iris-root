package com.silenteight.searpayments.scb.mapper;

import com.silenteight.searpayments.bridge.dto.input.AlertMessageDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateBasicAlertFactory {

  private final String gitCommitId;

  CreateBasicAlert create(@NonNull String dataCenter, AlertMessageDto inputDto) {
    return new CreateBasicAlert(gitCommitId, dataCenter, inputDto);
  }
}
