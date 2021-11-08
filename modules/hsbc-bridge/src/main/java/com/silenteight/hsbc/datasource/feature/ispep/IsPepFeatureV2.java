package com.silenteight.hsbc.datasource.feature.ispep;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepInputDto;
import com.silenteight.hsbc.datasource.dto.ispep.WatchListItemDto;
import com.silenteight.hsbc.datasource.dto.ispep.WatchListItemDto.WatchListItemDtoBuilder;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.IsPepFeatureValuesRetriever;
import com.silenteight.hsbc.datasource.util.StreamUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class IsPepFeatureV2 implements IsPepFeatureValuesRetriever<IsPepInputDto> {

  private final IsPepQueryV2.Factory isPepQueryFactory;

  @Override
  public IsPepInputDto retrieve(MatchData matchData, String matchName) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var result = Optional.of(matchData)
        .filter(MatchData::isIndividual)
        .map(md -> getIsPepInputForIndividual(md, matchName))
        .orElseGet(() -> getIsPepInputForEntities(matchData, matchName));

    log.debug("Datasource response for feature: {} received.", result.getFeature());

    return result;
  }

  private IsPepInputDto getIsPepInputForIndividual(MatchData matchData, String matchName) {
    var query = isPepQueryFactory.create(matchData);
    var apIndividualExtractLobCountry = query.apWorldCheckIndividualsExtractEdqLobCountryCode();
    var furtherInformation = query.mpWorldCheckIndividualsFurtherInformation();
    var linkedTo = StreamUtils.toDistinctList(query.mpWorldCheckIndividualsLinkedTo());

    return IsPepInputDto.builder()
        .feature(getFeatureName())
        .match(matchName)
        .watchListItem(
            createWatchListItem(
                matchData,
                apIndividualExtractLobCountry,
                furtherInformation,
                linkedTo))
        .build();
  }

  private IsPepInputDto getIsPepInputForEntities(MatchData matchData, String matchName) {
    var query = isPepQueryFactory.create(matchData);
    var apEntityExtractLobCountry = query.apWorldCheckEntitiesExtractEdqLobCountryCode();
    var furtherInformation = query.mpWorldCheckEntitiesFurtherInformation();
    var linkedTo = StreamUtils.toDistinctList(query.mpWorldCheckEntitiesLinkedTo());

    return IsPepInputDto.builder()
        .feature(getFeatureName())
        .match(matchName)
        .watchListItem(
            createWatchListItem(
                matchData,
                apEntityExtractLobCountry,
                furtherInformation,
                linkedTo))
        .build();
  }

  private static WatchListItemDto createWatchListItem(
      MatchData matchData, String apEntityExtractLobCountry, String furtherInformation, List<String> linkedTo) {
    return getWatchListItemDtoBuilderWithType(matchData)
        .id(matchData.getWatchlistId().orElse(""))
        .furtherInformation(furtherInformation)
        .country(apEntityExtractLobCountry)
        .linkedPepsUids(linkedTo)
        .build();
  }

  private static WatchListItemDtoBuilder getWatchListItemDtoBuilderWithType(MatchData matchData) {
    var watchlistItemBuilder = WatchListItemDto.builder();

    matchData
        .getWatchlistType()
        .ifPresentOrElse(e -> watchlistItemBuilder.type(e.getLabel()), () -> watchlistItemBuilder.type(""));

    return watchlistItemBuilder;
  }

  @Override
  public Feature getFeature() {
    return Feature.IS_PEP_V2;
  }
}
