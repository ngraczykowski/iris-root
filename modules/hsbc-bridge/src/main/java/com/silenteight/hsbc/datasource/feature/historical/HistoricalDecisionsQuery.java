package com.silenteight.hsbc.datasource.feature.historical;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.historical.HistoricalDecisionsServiceClient;
import com.silenteight.hsbc.datasource.extractors.historical.ModelCountsDto;

import java.util.List;


public interface HistoricalDecisionsQuery {

  List<ModelCountsDto> getIsApTpMarkedSolution();

  List<ModelCountsDto> getIsTpMarkedSolution();

  List<ModelCountsDto> getCaseTpMarkedSolution();


  interface Factory {

    HistoricalDecisionsQuery create(MatchData matchData, HistoricalDecisionsServiceClient client);
  }
}
