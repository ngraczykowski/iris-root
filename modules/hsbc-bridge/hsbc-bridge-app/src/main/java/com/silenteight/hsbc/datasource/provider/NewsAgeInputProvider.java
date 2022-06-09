package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.newsage.NewsAgeFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.newsage.NewsAgeInputDto;
import com.silenteight.hsbc.datasource.dto.newsage.NewsAgeInputResponse;
import com.silenteight.hsbc.datasource.dto.newsage.NewsAgeWatchlistItemDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.newsage.NewsAgeFeature;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NewsAgeInputProvider implements DataSourceInputProvider<NewsAgeInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public NewsAgeInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return NewsAgeInputResponse.builder()
        .inputs(getInputs(matches, features))
        .build();
  }

  @Override
  public List<Feature> getAllowedFeatures() {
    return List.of(Feature.NEWS_AGE);
  }

  private List<NewsAgeInputDto> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> NewsAgeInputDto.builder()
            .match(match.getName())
            .newsAgeFeatureInput(getFeatureInputs(features, match.getMatchData()))
            .build())
        .collect(Collectors.toList());
  }

  private NewsAgeFeatureInputDto getFeatureInputs(List<String> features, MatchData matchData) {
    return features.stream()
        .map(featureName -> (NewsAgeFeatureInputDto)
            ((NewsAgeFeature) getFeatureRetriever(featureName)).retrieve(matchData))
        .findFirst()
        .orElse(NewsAgeFeatureInputDto.builder()
            .feature("")
            .watchlistItem(NewsAgeWatchlistItemDto.builder()
                .build())
            .build());
  }
}
