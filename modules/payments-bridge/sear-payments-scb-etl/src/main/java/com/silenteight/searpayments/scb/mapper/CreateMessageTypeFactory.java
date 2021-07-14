package com.silenteight.searpayments.scb.mapper;

import com.silenteight.searpayments.scb.etl.response.AlertEtlResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateMessageTypeFactory {

  private final String dtpMessageTypeNullAlias;
  private final String nbpMessageTypeNullAlias;

  CreateMessageType create(AlertEtlResponse alertEtlResponse) {
    return new CreateMessageType(
        alertEtlResponse, dtpMessageTypeNullAlias, nbpMessageTypeNullAlias);
  }
}
