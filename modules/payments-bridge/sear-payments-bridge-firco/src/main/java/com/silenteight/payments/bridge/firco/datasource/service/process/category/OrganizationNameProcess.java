package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;
import com.silenteight.proto.agent.organizationname.v1.api.CompareOrganizationNamesRequest;
import com.silenteight.proto.agent.organizationname.v1.api.CompareOrganizationNamesResponse;
import com.silenteight.proto.agent.organizationname.v1.api.OrganizationNameAgentGrpc.OrganizationNameAgentBlockingStub;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
class OrganizationNameProcess implements CategoryValueProcess {

  public static final String CATEGORIES_ORGANIZATION_NAME = "categories/organizationName";

  private final OrganizationNameAgentBlockingStub stub;

  @Setter
  private Duration deadlineDuration = Duration.ofSeconds(10);

  @Override
  public CategoryValue extract(HitData hitData, String matchValue) {

    var response = compareNames(hitData);
    return CategoryValue
        .newBuilder()
        .setName(CATEGORIES_ORGANIZATION_NAME)
        .setMatch(matchValue)
        .setSingleValue(response.getSolution())
        .build();
  }

  private CompareOrganizationNamesResponse compareNames(HitData hitData) {

    var request = getRequest(hitData);

    var response = stub
        .withDeadlineAfter(deadlineDuration.toMillis(), TimeUnit.MILLISECONDS)
        .compareOrganizationNames(request);

    if (response == null)
      throw new MissingAgentResultException("Organization Name Agent");

    return response;
  }

  private static CompareOrganizationNamesRequest getRequest(HitData hitData) {

    var alertedPartyNames =
        Optional.ofNullable(hitData.getAlertedPartyData().getNames()).orElseGet(
            Collections::emptyList);

    var watchlistPartyNames =
        Optional.ofNullable(hitData.getHitAndWlPartyData().getName()).orElse("");

    return CompareOrganizationNamesRequest
        .newBuilder()
        .addAllAlertedPartyNames(alertedPartyNames)
        .addWatchlistPartyNames(watchlistPartyNames)
        .build();
  }
}
