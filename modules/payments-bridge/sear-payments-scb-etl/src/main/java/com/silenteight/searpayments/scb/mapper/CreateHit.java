package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.bridge.model.SolutionType;
import com.silenteight.searpayments.scb.domain.Hit;
import com.silenteight.searpayments.scb.domain.HittedEntityAddress;
import com.silenteight.searpayments.scb.etl.response.HitAndWatchlistPartyData;
import com.silenteight.searpayments.scb.etl.response.HitData;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;
import static java.util.Collections.singletonList;

@RequiredArgsConstructor
class CreateHit {

  @NonNull private final HitData requestHitDto;
  private final int hitIndex;
  @NonNull private final CreateNameAddressCrossmatchAgentRequestFactory
      crossmatchAgentRequestFactory;
  @NonNull private final CreateHitAddressFactory createHitAddressFactory;
  @NonNull private final CreateCompareFreeTextRequestFactory freeTextRequestFactory;
  @NonNull private final CreateCompareNamesRequestFactory createCompareNamesRequestFactory;
  @NonNull private final CreateCompareLocationsRequestFactory createCompareLocationsRequestFactory;
  @NonNull private final CreateOneLinerAgentRequestFactory createOneLinerAgentRequestFactory;
  @NonNull private final CreateDelimiterInNameLineAgentRequestFactory
      createDelimiterInNameLineAgentRequestFactory;
  @NonNull private final CreateMatchtextFirstTokenOfAddressAgentRequestFactory
      createMatchtextFirstTokenOfAddressAgentRequestFactory;
  private Hit.HitBuilder builder;
  private Hit hit;

  Hit create() {
    HitAndWatchlistPartyData hitAndWlPartyData = requestHitDto.getHitAndWlPartyData();
    builder = Hit.builder()
        .entityText(hitAndWlPartyData.getEntityText())
        .tag(hitAndWlPartyData.getTag())
        .solutionType(extractSolutionType())
        .matchingText(hitAndWlPartyData.getMatchingText())
        .accountNumber(hitAndWlPartyData.getAccountNumberOrNormalizedName())
        .watchlistType(hitAndWlPartyData.getWatchlistType().getName())
        .watchlistName(extractWatchlistName())
        .watchlistOfacId(hitAndWlPartyData.getId())
        .watchlistInterventionId(hitIndex)
        .origin(hitAndWlPartyData.getOrigin())
        .designation(hitAndWlPartyData.getDesignation())
        .messageFieldStructure(
            requestHitDto.getAlertedPartyData().getMessageFieldStructure().name());
    setNameIfNotEmpty(hitAndWlPartyData);
    hit = builder.build();
    setAddressesIfNoEmptyData();
    setAgentRequests();
    return hit;
  }

  private void setNameIfNotEmpty(HitAndWatchlistPartyData hitAndWlPartyData) {
    if (StringUtils.isNotEmpty(hitAndWlPartyData.getName()))
      builder.names(singletonList(hitAndWlPartyData.getName()));
  }

  private void setAddressesIfNoEmptyData() {
    HittedEntityAddress hitAddress =
        createHitAddressFactory.create(requestHitDto.getHitAndWlPartyData()).create();
    if (hitAddress.hasNonEmptyData())
      hit.addAddress(hitAddress);
  }

  private void setAgentRequests() {
    hit.setCrossmatchAgentRequest(
        crossmatchAgentRequestFactory.create(requestHitDto).create());
    hit.setFreeTextAgentRequest(freeTextRequestFactory.create(requestHitDto).create());
    hit.setNameAgentRequest(
        createCompareNamesRequestFactory.create(requestHitDto).create());
    hit.setGeoAgentRequest(
        createCompareLocationsRequestFactory.create(requestHitDto).create());
    hit.setOneLineAddressAgentRequest(
        createOneLinerAgentRequestFactory.create(requestHitDto).create());
    hit.setSpecificTermsAgentRequest(
        String.join(" ", requestHitDto.getHitAndWlPartyData().getAllMatchingFieldValues()));
    hit.setTwoLinesNameAgentRequest(
        createSerializedTwoLinesNameAgentRequest(
            requestHitDto.getAlertedPartyData().getAddresses()));
    hit.setDelimiterInNameLineAgentRequest(
        createDelimiterInNameLineAgentRequestFactory.create(requestHitDto).create());
    hit.setMatchtextFirstTokenOfAddressAgentRequest(
        createMatchtextFirstTokenOfAddressAgentRequestFactory.create(requestHitDto).create());
  }

  private static String createSerializedTwoLinesNameAgentRequest(
      List<String> alertedPartyAddresses) {
    TwoLinesNameAgentRequest twoLinesNameAgentRequest =
        new TwoLinesNameAgentRequest(alertedPartyAddresses);
    return INSTANCE.serializeToString(twoLinesNameAgentRequest);
  }


  @NotNull
  private String extractWatchlistName() {
    return Optional.of(requestHitDto)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getName)
        .orElse("");
  }

  @NotNull
  private String extractSolutionType() {
    return Optional.of(requestHitDto)
        .map(HitData::getHitAndWlPartyData)
        .map(HitAndWatchlistPartyData::getSolutionType)
        .map(SolutionType::getCode)
        .orElse("");
  }
}
