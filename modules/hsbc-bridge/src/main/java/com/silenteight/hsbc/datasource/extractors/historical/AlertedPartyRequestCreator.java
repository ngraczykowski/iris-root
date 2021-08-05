package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyType;

import java.util.List;

@RequiredArgsConstructor
class AlertedPartyRequestCreator extends HistoricalDecisionsRequestCreator {

  private final String alertedPartyId;

  @Override
  GetHistoricalDecisionsRequestDto createRequest() {
    return GetHistoricalDecisionsRequestDto.builder()
        .modelKeys(getModelKeys())
        .build();
  }

  private List<ModelKeyDto> getModelKeys() {
    return List.of(
        ModelKeyDto.builder()
            .modelKeyType(ModelKeyType.ALERTED_PARTY)
            .modelKeyValue(new AlertedPartyDto(alertedPartyId))
            .build()
    );
  }
}
