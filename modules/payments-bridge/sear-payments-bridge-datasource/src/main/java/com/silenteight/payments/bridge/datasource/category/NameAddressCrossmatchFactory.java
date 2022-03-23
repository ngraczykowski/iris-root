package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentRequest;
import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured;

import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_CROSSMATCH;

@Service
@RequiredArgsConstructor
class NameAddressCrossmatchFactory implements CategoryValueStructuredFactory {

  private final NameAddressCrossmatchUseCase nameAddressCrossmatchUseCase;

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

  @Override
  public Optional<CategoryValue> createCategoryValue(
      CategoryValueStructured categoryValueModel,
      final FeatureInputSpecification featureInputSpecification) {
    return Optional.of(CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_CROSSMATCH)
        .setAlert(categoryValueModel.getAlertName())
        .setMatch(categoryValueModel.getMatchName())
        .setSingleValue(getValue(categoryValueModel))
        .build());
  }

  private String getValue(CategoryValueStructured categoryValueModel) {
    return nameAddressCrossmatchUseCase.call(createRequest(categoryValueModel)).toString();
  }
}
