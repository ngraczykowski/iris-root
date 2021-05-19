package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;

import static com.silenteight.warehouse.common.opendistro.kibana.PostHttpRequest.builder;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.KIBANA_INDEX_PATTERN_NAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.SAVED_SEARCH;

@RequiredArgsConstructor
public class OpendistroKibanaTestClient {

  private static final TypeReference<HashMap<String, Object>> MAP_TYPE = new TypeReference<>() {};
  private final OpendistroKibanaClient opendistroKibanaClient;

  public void createKibanaIndex(String tenant, byte[] payload) {
    opendistroKibanaClient.post(builder()
        .endpoint("/api/saved_objects/index-pattern/" + KIBANA_INDEX_PATTERN_NAME)
        .tenant(tenant)
        .payload(payload)
        .build(), MAP_TYPE);
  }

  public void createSavedSearch(String tenant, byte[] payload) {
    opendistroKibanaClient.post(builder()
        .endpoint("/api/saved_objects/search/" + SAVED_SEARCH)
        .tenant(tenant)
        .payload(payload)
        .build(), MAP_TYPE);
  }

  public String createReportDefinition(String tenant, byte[] payload) {
    TypeReference<CreateReportDefinitionResponse> typeRef = new TypeReference<>() {};

    CreateReportDefinitionResponse response =
        opendistroKibanaClient.post(builder()
            .endpoint("/api/reporting/reportDefinition")
            .tenant(tenant)
            .payload(payload)
            .build(), typeRef);

    return response.getSchedulerResponse().getReportDefinitionId();
  }

  public void generateReport(String tenant, String reportDefinitionId) {
    TypeReference<Void> typeRef = new TypeReference<>() {};

    opendistroKibanaClient.post(builder()
        .endpoint("/api/reporting/generateReport/" + reportDefinitionId + "?timezone=Europe/Warsaw")
        .tenant(tenant)
        .build(), typeRef);
  }
}
