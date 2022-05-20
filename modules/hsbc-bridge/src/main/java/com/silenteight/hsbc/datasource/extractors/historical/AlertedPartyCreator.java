package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.dto.historical.AlertedPartyDto;
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto;
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto.ModelKeyType;

@RequiredArgsConstructor
class AlertedPartyCreator extends HistoricalDecisionsRequestCreator {

  private final String alertedPartyId;

  @Override
  ModelKeyDto create() {
    return ModelKeyDto.builder()
        .modelKeyType(ModelKeyType.ALERTED_PARTY)
        .modelKeyValue(AlertedPartyDto.builder().id(alertedPartyId).build())
        .build();
  }
}
