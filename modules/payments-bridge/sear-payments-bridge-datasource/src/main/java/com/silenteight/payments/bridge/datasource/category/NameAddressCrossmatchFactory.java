package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_CROSSMATCH;

@Service
@RequiredArgsConstructor
class NameAddressCrossmatchFactory implements CategoryValueStructuredFactory {

  private final NameAddressCrossmatchUseCase nameAddressCrossmatchUseCase;

  @Override
  public CategoryValue createCategoryValue(CategoryValueStructured categoryValueModel) {
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_CROSSMATCH)
        .setAlert(categoryValueModel.getAlertName())
        .setMatch(categoryValueModel.getMatchName())
        .setSingleValue(getValue(categoryValueModel))
        .build();
  }

  private String getValue(CategoryValueStructured categoryValueModel) {
    return nameAddressCrossmatchUseCase.call(createRequest(categoryValueModel)).toString();
  }

  private static NameAddressCrossmatchAgentRequest createRequest(
      CategoryValueStructured categoryValueModel) {
    return NameAddressCrossmatchAgentRequest
        .builder()
        .alertPartyEntities(categoryValueModel.getAlertedData().getAlertPartyEntities())
        .watchlistName(categoryValueModel.getWatchlistData().getWatchlistName())
        .watchlistCountry(categoryValueModel.getWatchlistData().getCountry())
        .watchlistType(categoryValueModel.getWatchlistData().getWatchlistType())
        .build();
  }
}
