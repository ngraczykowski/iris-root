package com.silenteight.hsbc.datasource.feature.newsage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.newsage.NewsAgeFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.newsage.NewsAgeWatchlistItemDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

@Slf4j
@RequiredArgsConstructor
public class NewsAgeFeature implements FeatureValuesRetriever<NewsAgeFeatureInputDto> {

  private final NewsAgeQuery.Factory queryFactory;

  @Override
  public NewsAgeFeatureInputDto retrieve(MatchData matchData) {
    var query = queryFactory.create(matchData);

    if (matchData.isIndividual()) {
      return NewsAgeFeatureInputDto.builder()
          .feature(getFeatureName())
          .watchlistItem(NewsAgeWatchlistItemDto.builder()
              .id(query.id())
              .type(query.type())
              .furtherInformation(query.nnsIndividualsFurtherInformation())
              .build())
          .build();
    }

    return NewsAgeFeatureInputDto.builder()
        .feature(getFeatureName())
        .watchlistItem(NewsAgeWatchlistItemDto.builder()
            .id(query.id())
            .type(query.type())
            .furtherInformation(query.nnsEntitiesFurtherInformation())
            .build())
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.NEWS_AGE;
  }
}
