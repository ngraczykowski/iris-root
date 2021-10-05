package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.port.ChineseCommercialCodeUseCase;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.firco.datasource.service.process.category.SpecificTermsProcess.joinMatchingFieldValues;

@Service
@Qualifier("chineseCode")
@RequiredArgsConstructor
class ChineseCommercialCodesProcess implements CategoryValueProcess {

  private final ChineseCommercialCodeUseCase chineseCommercialCodeUseCase;

  @Override
  public CategoryValue extract(AlertRegisteredEvent data, HitData hitData, String matchValue) {
    var value = chineseCommercialCodeUseCase.invoke(joinMatchingFieldValues(hitData));
    return CategoryValue
        .newBuilder()
        .setName("categories/chineseCode")
        .setMatch(matchValue)
        .setSingleValue(value.toString())
        .build();
  }
}
