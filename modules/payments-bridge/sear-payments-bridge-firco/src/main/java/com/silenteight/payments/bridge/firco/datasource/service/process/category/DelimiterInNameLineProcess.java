package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.DelimiterInNameLineAgentRequest;
import com.silenteight.payments.bridge.agents.port.DelimiterInNameLineUseCase;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.firco.datasource.service.process.category.SpecificTermsProcess.joinMatchingFieldValues;

@Service
@Qualifier("delimiter")
@RequiredArgsConstructor
class DelimiterInNameLineProcess implements CategoryValueProcess {

  private final DelimiterInNameLineUseCase delimiterInNameLineUseCase;

  @Override
  public CategoryValue extract(AlertRegisteredEvent data, HitData hitData, String matchValue) {
    var value = delimiterInNameLineUseCase.invoke(createRequest(hitData));
    return CategoryValue
        .newBuilder()
        .setName("categories/delimiter")
        .setMatch(matchValue)
        .setSingleValue(value.toString())
        .build();
  }

  @Nonnull
  private DelimiterInNameLineAgentRequest createRequest(HitData hitData) {
    return DelimiterInNameLineAgentRequest
        .builder()
        .allMatchingFieldsValue(joinMatchingFieldValues(hitData))
        .messageFieldStructureText(hitData.getHitAndWlPartyData()
            .getMessageStructure()
            .getMessageFieldStructure()
            .name())
        .build();
  }
}
