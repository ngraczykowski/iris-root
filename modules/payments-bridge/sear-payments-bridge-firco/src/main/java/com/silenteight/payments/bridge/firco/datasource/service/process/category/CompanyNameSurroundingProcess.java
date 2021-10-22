package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;
import com.silenteight.proto.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingRequest;
import com.silenteight.proto.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingResponse;
import com.silenteight.proto.agent.companynamesurrounding.v1.api.CompanyNameSurroundingAgentGrpc.CompanyNameSurroundingAgentBlockingStub;

import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Profile("companynamesurrounding")
class CompanyNameSurroundingProcess implements CategoryValueProcess {

  public static final String CATEGORIES_COMPANY_NAME_SURROUNDING =
      "categories/companyNameSurrounding";

  private final CompanyNameSurroundingAgentBlockingStub stub;

  @Setter
  private Duration deadlineDuration = Duration.ofSeconds(10);

  @Override
  public CategoryValue extract(HitData hitData, String matchValue) {

    var response = compareNames(hitData);
    return CategoryValue
        .newBuilder()
        .setName(CATEGORIES_COMPANY_NAME_SURROUNDING)
        .setMatch(matchValue)
        .setSingleValue(String.valueOf(response.getResult()))
        .build();
  }

  private CheckCompanyNameSurroundingResponse compareNames(HitData hitData) {

    var request = getRequest(hitData);

    var response = stub
        .withDeadlineAfter(deadlineDuration.toMillis(), TimeUnit.MILLISECONDS)
        .checkCompanyNameSurrounding(request);

    if (response == null)
      throw new MissingAgentResultException("Company Name Surrounding Agent");

    return response;
  }

  private static CheckCompanyNameSurroundingRequest getRequest(HitData hitData) {

    var alertedPartyNames =
        Optional.ofNullable(hitData.getAlertedPartyData().getNames()).orElseGet(
            Collections::emptyList);

    return CheckCompanyNameSurroundingRequest
        .newBuilder()
        .addAllNames(alertedPartyNames)
        .build();
  }
}
