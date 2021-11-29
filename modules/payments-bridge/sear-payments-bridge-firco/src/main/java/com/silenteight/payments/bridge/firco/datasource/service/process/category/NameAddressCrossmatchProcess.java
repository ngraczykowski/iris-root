package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.svb.oldetl.model.CreateAlertedPartyEntitiesRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
class NameAddressCrossmatchProcess extends BaseCategoryValueProcess {

  public static final String CATEGORY_CROSSMATCH = "crossmatch";

  private final NameAddressCrossmatchUseCase nameAddressCrossmatchUseCase;
  private final CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_CROSSMATCH;
  }

  @Override
  protected String getValue(HitData hitData) {
    return nameAddressCrossmatchUseCase.call(createRequest(hitData)).getResult().toString();
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
  private static String getWatchlistCountryIfExists(HitData hitData) {
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
