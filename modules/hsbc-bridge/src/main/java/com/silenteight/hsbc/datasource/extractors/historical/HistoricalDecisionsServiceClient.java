package com.silenteight.hsbc.datasource.extractors.historical;

public interface HistoricalDecisionsServiceClient {

  GetHistoricalDecisionsResponseDto getHistoricalDecisions(
      GetHistoricalDecisionsRequestDto request);
}
