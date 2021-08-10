package com.silenteight.hsbc.datasource.feature.historical;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.historical.HistoricalDecisionsServiceClient;
import com.silenteight.hsbc.datasource.extractors.historical.ModelCountsDto;

import java.util.List;

public interface HistoricalDecisionsQuery {

  List<ModelCountsDto> getIsApTpMarkedInput();

  List<ModelCountsDto> getIsTpMarkedInput();

  List<ModelCountsDto> getCaseTpMarkedInput();


  interface Factory {

    HistoricalDecisionsQuery create(MatchData matchData, HistoricalDecisionsServiceClient client);
  }
}
