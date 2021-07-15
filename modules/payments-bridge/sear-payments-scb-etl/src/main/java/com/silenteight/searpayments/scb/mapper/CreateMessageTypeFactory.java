package com.silenteight.searpayments.scb.mapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.AlertEtlResponse;

@RequiredArgsConstructor
class CreateMessageTypeFactory {

  private final String dtpMessageTypeNullAlias;
  private final String nbpMessageTypeNullAlias;

  CreateMessageType create(AlertEtlResponse alertEtlResponse) {
    return new CreateMessageType(
        alertEtlResponse, dtpMessageTypeNullAlias, nbpMessageTypeNullAlias);
  }
}
