package com.silenteight.hsbc.datasource.feature.historical;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto;

import java.util.Optional;

public interface HistoricalDecisionsQuery {

  String DISCRIMINATOR = "hotel_true_positive";

  Optional<ModelKeyDto> getIsApTpMarkedInput();

  Optional<ModelKeyDto> getIsTpMarkedInput();

  Optional<ModelKeyDto> getCaseTpMarkedInput();

  interface Factory {

    HistoricalDecisionsQuery create(MatchData matchData);
  }
}
