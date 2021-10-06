package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.svb.etl.model.CreateAlertedPartyEntitiesRequest;
import com.silenteight.payments.bridge.svb.etl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.svb.etl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

@Service
@Qualifier("crossmatch")
@RequiredArgsConstructor
class NameAddressCrossmatchProcess implements CategoryValueProcess {

  private final NameAddressCrossmatchUseCase nameAddressCrossmatchUseCase;
  private final CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;

  @Override
  public CategoryValue extract(HitData hitData, String matchValue) {
    var value = nameAddressCrossmatchUseCase.call(createRequest(hitData));
    return CategoryValue
        .newBuilder()
        .setName("categories/crossmatch")
        .setMatch(matchValue)
        .setSingleValue(value.getResult().toString())
        .build();
  }

  @Nonnull
  private NameAddressCrossmatchAgentRequest createRequest(HitData hitData) {
    return NameAddressCrossmatchAgentRequest
        .builder()
        .alertPartyEntities(createAlertedPartyEntities(
            hitData.getAlertedPartyData(),
            hitData.getHitAndWlPartyData().getAllMatchingTexts()))
        .watchlistName(hitData.getHitAndWlPartyData().getName())
        .watchlistCountry(getWatchlistCountryIfExists(hitData))
        .watchlistType(hitData.getHitAndWlPartyData().getWatchlistType().getName())
        .build();
  }

  @Nonnull
  private String getWatchlistCountryIfExists(HitData hitData) {
    List<String> countries = hitData.getHitAndWlPartyData().getCountries();
    return countries.isEmpty() ? "" : countries.get(0);
  }

  private Map<AlertedPartyKey, String> createAlertedPartyEntities(
      AlertedPartyData alertedPartyData, List<String> matchingTexts) {
    return createAlertedPartyEntitiesUseCase.create(CreateAlertedPartyEntitiesRequest
        .builder()
        .alertedPartyData(alertedPartyData)
        .allMatchingText(matchingTexts)
        .build());
  }
}
