package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.domain.HittedEntityAddress;
import com.silenteight.searpayments.scb.etl.response.HitAndWatchlistPartyData;

import java.util.List;

@RequiredArgsConstructor
class CreateHitAddress {

  @NonNull private final HitAndWatchlistPartyData wpData;
  private HittedEntityAddress.HittedEntityAddressBuilder builder;

  HittedEntityAddress create() {
    builder = HittedEntityAddress.builder();
    //NOTE(ajaromin): we agreed that only the first city, state and country will be analysed
    //                based on the POV experience
    setPostalAddress();
    setCountry();
    setState();
    setCity();
    builder.isMain(wpData.isMainAddress());
    return builder.build();
  }

  private void setCity() {
    wpData.getCities().stream().findFirst().ifPresent(c -> builder.city(c));
  }

  private void setState() {
    wpData.getStates().stream().findFirst().ifPresent(c -> builder.state(c));
  }

  private void setCountry() {
    wpData.getCountries().stream().findFirst().ifPresent(c -> builder.country(c));
  }

  private void setPostalAddress() {
    builder.postalAddress(firstPostalAddress(wpData.getPostalAddresses()));
  }

  private static String firstPostalAddress(List<String> postalAddresses) {
    return !postalAddresses.isEmpty() ? postalAddresses.get(0) : "";
  }
}
