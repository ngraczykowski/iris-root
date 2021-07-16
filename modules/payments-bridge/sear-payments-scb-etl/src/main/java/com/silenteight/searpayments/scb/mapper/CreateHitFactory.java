package com.silenteight.searpayments.scb.mapper;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.HitData;

@RequiredArgsConstructor
class CreateHitFactory {

  @NonNull private final CreateNameAddressCrossmatchAgentRequestFactory
      createNameAddressCrossmatchAgentRequestFactory;
  @NonNull private final CreateHitAddressFactory createHitAddressFactory;
  @NonNull private final CreateCompareFreeTextRequestFactory createCompareFreeTextRequestFactory;
  @NonNull private final CreateCompareNamesRequestFactory createCompareNamesRequestFactory;
  @NonNull private final CreateCompareLocationsRequestFactory createCompareLocationsRequestFactory;
  @NonNull private final CreateOneLinerAgentRequestFactory createOneLinerAgentRequestFactory;

  @NonNull private final CreateDelimiterInNameLineAgentRequestFactory
      createDelimiterInNameLineAgentRequestFactory;
  @NonNull private final CreateMatchtextFirstTokenOfAddressAgentRequestFactory
      createMatchtextFirstTokenOfAddressAgentRequestFactory;

  public CreateHit create(HitData requestHitDto, int hitIndex) {
    return new CreateHit(
        requestHitDto, hitIndex, createNameAddressCrossmatchAgentRequestFactory,
        createHitAddressFactory, createCompareFreeTextRequestFactory,
        createCompareNamesRequestFactory, createCompareLocationsRequestFactory,
        createOneLinerAgentRequestFactory, createDelimiterInNameLineAgentRequestFactory,
        createMatchtextFirstTokenOfAddressAgentRequestFactory);
  }
}
