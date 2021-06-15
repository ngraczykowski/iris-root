package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;

import static com.silenteight.warehouse.common.opendistro.kibana.PostHttpRequest.builder;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.SAVED_SEARCH;

@RequiredArgsConstructor
public class OpendistroKibanaTestClient {

  private static final TypeReference<HashMap<String, Object>> MAP_TYPE = new TypeReference<>() {};
  private final OpendistroKibanaClient opendistroKibanaClient;

  public void createKibanaIndex(String tenant, String kibanaIndexPatternName, byte[] payload) {
    PostHttpRequest request = builder()
        .endpoint("/api/saved_objects/index-pattern/" + kibanaIndexPatternName)
        .tenant(tenant)
        .payload(payload)
        .build();
    opendistroKibanaClient.post(request, MAP_TYPE);
  }

  public void createSavedSearch(String tenant, byte[] payload) {
    PostHttpRequest request = builder()
        .endpoint("/api/saved_objects/search/" + SAVED_SEARCH)
        .tenant(tenant)
        .payload(payload)
        .build();
    opendistroKibanaClient.post(request, MAP_TYPE);
  }

  public String createReportDefinition(String tenant, byte[] payload) {
    TypeReference<CreateReportDefinitionResponse> typeRef = new TypeReference<>() {};

    PostHttpRequest request = builder()
        .endpoint("/api/reporting/reportDefinition")
        .tenant(tenant)
        .payload(payload)
        .build();
    CreateReportDefinitionResponse response =
        opendistroKibanaClient.post(request, typeRef);

    return response.getSchedulerResponse().getReportDefinitionId();
  }

  public void generateReport(String tenant, String reportDefinitionId) {
    TypeReference<Void> typeRef = new TypeReference<>() {};

    PostHttpRequest request = builder()
        .endpoint("/api/reporting/generateReport/" + reportDefinitionId + "?timezone=Europe/Warsaw")
        .tenant(tenant)
        .build();
    opendistroKibanaClient.post(request, typeRef);
  }
}
