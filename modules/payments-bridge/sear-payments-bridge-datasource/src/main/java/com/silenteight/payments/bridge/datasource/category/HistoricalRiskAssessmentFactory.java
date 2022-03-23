package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured.AlertedData;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_HISTORICAL_RISK_ASSESSMENT;

@Service
@RequiredArgsConstructor
class HistoricalRiskAssessmentFactory implements CategoryValueStructuredFactory {

  private final HistoricalRiskAssessmentUseCase historicalRiskAssessmentUseCase;

  @Nonnull
  private static HistoricalRiskAssessmentAgentRequest createRequest(
      CategoryValueStructured categoryValueModel) {
    var accountNumberOrName = getAccountNumberOrFirstName(categoryValueModel.getAlertedData());
    return HistoricalRiskAssessmentAgentRequest
        .builder()
        .accountNumber(accountNumberOrName)
        .ofacID(categoryValueModel.getWatchlistData().getOfacId())
        .build();
  }

  private static String getAccountNumberOrFirstName(AlertedData alertedData) {
    if (StringUtils.isNotBlank(alertedData.getAccountNumber()))
      return alertedData.getAccountNumber();
    else
      return alertedData.getNames().stream()
          .map(String::trim)
          .findFirst()
          .orElse("");
  }

  @Override
  public Optional<CategoryValue> createCategoryValue(
      CategoryValueStructured categoryValueModel,
      final FeatureInputSpecification featureInputSpecification) {
    if (!featureInputSpecification.isSatisfy(CATEGORY_NAME_HISTORICAL_RISK_ASSESSMENT)) {
      return Optional.empty();
    }
    return Optional.of(CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_HISTORICAL_RISK_ASSESSMENT)
        .setAlert(categoryValueModel.getAlertName())
        .setMatch(categoryValueModel.getMatchName())
        .setSingleValue(getValue(categoryValueModel))
        .build());
  }

  private String getValue(CategoryValueStructured categoryValueModel) {
    return historicalRiskAssessmentUseCase.invoke(createRequest(categoryValueModel)).toString();
  }
}
