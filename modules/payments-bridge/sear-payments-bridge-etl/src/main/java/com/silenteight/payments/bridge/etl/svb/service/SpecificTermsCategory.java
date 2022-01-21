package com.silenteight.payments.bridge.etl.svb.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;
import com.silenteight.payments.bridge.etl.processing.model.firco.FircoAlert;
import com.silenteight.payments.bridge.etl.processing.model.firco.FircoMatch;
import com.silenteight.payments.bridge.etl.processing.port.CategoryEtlProcess;

import java.util.stream.Stream;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_SPECIFIC_TERMS;

//@Service
@RequiredArgsConstructor
class SpecificTermsCategory extends CategoryEtlProcess {

  private final SpecificTermsUseCase useCase;

  Stream<CategoryValue> extract(FircoAlert alert) {
    return alert.getMatches().stream().flatMap(match -> doExtract(alert, match));
  }

  private Stream<CategoryValue> doExtract(FircoAlert alert, FircoMatch match) {
    return match
        .getHits()
        .stream()
        .findFirst()
        .map(hit -> runAgent(match.getMatchName(), hit.getHitTagValue(alert.getMessageData())))
        .stream();
  }

  private CategoryValue runAgent(String matchName, String value) {
    var response =
        useCase.invoke(SpecificTermsRequest.builder().allMatchFieldsValue(value).build());

    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_SPECIFIC_TERMS)
        .setMatch(matchName)
        .setSingleValue(response.toString())
        .build();
  }
}
