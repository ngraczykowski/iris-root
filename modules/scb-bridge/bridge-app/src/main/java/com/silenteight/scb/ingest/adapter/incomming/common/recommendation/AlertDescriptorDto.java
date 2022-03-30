package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class AlertDescriptorDto {

  private final String unit;
  private final String account;
  private final String recordDetails;
}