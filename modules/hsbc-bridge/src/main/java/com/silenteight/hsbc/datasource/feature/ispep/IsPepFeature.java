package com.silenteight.hsbc.datasource.feature.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepFeatureSolutionDto;
import com.silenteight.hsbc.datasource.extractors.ispep.IsPepResponseDto;
import com.silenteight.hsbc.datasource.extractors.ispep.IsPepServiceClient;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.IsPepFeatureClientValuesRetriever;

import java.util.List;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class IsPepFeature implements IsPepFeatureClientValuesRetriever<IsPepFeatureInputDto> {

  private final IsPepQuery.Factory isPepQueryFactory;

  @Override
  public IsPepFeatureInputDto retrieve(MatchData matchData, IsPepServiceClient client) {
    var query = isPepQueryFactory.create(matchData, client);

    var inputBuilder = IsPepFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      var apIndividualExtractLobCountry = toDistinctList(query.apIndividualExtractLobCountry());
      var requiredFields = query.provideRequiredModelFieldNames();
      var responses =
          query.verifyIsPep(apIndividualExtractLobCountry, requiredFields);

      inputBuilder.featureSolutions(mapToFeatureSolutions(responses));
    }

    return inputBuilder
        .feature(getFeatureName())
        .build();
  }

  private List<IsPepFeatureSolutionDto> mapToFeatureSolutions(List<IsPepResponseDto> responses) {
    return responses.stream()
        .map(response -> IsPepFeatureSolutionDto.builder()
            .solution(response.getSolution())
            .reason(response.getReason())
            .build())
        .collect(toList());
  }

  @Override
  public Feature getFeature() {
    return Feature.IS_PEP;
  }
}
