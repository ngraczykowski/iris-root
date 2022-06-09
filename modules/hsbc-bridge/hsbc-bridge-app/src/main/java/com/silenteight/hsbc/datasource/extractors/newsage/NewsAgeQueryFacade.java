package com.silenteight.hsbc.datasource.extractors.newsage;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningEntities;
import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;
import com.silenteight.hsbc.datasource.datamodel.WatchlistType;
import com.silenteight.hsbc.datasource.feature.newsage.NewsAgeQuery;

import java.util.stream.Collectors;

@RequiredArgsConstructor
class NewsAgeQueryFacade implements NewsAgeQuery {

  private final MatchData matchData;

  @Override
  public String id() {
    return matchData.getWatchlistId()
        .orElse("");
  }

  @Override
  public String type() {
    return matchData.getWatchlistType()
        .map(WatchlistType::getLabel)
        .orElse("");
  }

  @Override
  public String nnsIndividualsFurtherInformation() {
    var furtherInfo = matchData.getNnsIndividuals()
        .stream()
        .collect(Collectors.groupingBy(NegativeNewsScreeningIndividuals::getFurtherInformation))
        .keySet();

    if (furtherInfo.size() == 1) {
      return furtherInfo.stream()
          .findFirst()
          .orElse(null);
    }

    return null;
  }

  @Override
  public String nnsEntitiesFurtherInformation() {
    var furtherInfo = matchData.getNnsEntities()
        .stream()
        .collect(Collectors.groupingBy(NegativeNewsScreeningEntities::getFurtherInformation))
        .keySet();

    if (furtherInfo.size() == 1) {
      return furtherInfo.stream()
          .findFirst()
          .orElse(null);
    }

    return null;
  }
}
