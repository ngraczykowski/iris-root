package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

@Slf4j
@AllArgsConstructor
public class OpendistroReportLoader {

  private final ObjectMapper objectMapper;
  private final OpendistroKibanaClient opendistroKibanaClient;

  @SneakyThrows
  public void loadAll(String tenant, InputStream payload) {
    TypeReference<ReportDefinitionList> typeRef = new TypeReference<>() {};
    ReportDefinitionList reportDefinitionList = objectMapper.readValue(payload, typeRef);

    reportDefinitionList.getData()
        .stream().map(ReportDefinition::forModification)
        .forEach(reportDefinition -> this.loadSingle(tenant, reportDefinition));
  }

  private void loadSingle(String tenant, KibanaReportDefinitionForModification reportDefinition) {
    try {
      String reportDefinitionId =
          opendistroKibanaClient.createReportDefinition(tenant, reportDefinition);
      log.info("Report definition loaded successfully, reportDefinitionId={}, tenant={}",
          reportDefinitionId, tenant);
    } catch (OpendistroKibanaClientException e) {
      log.error("Loading report definition failed, reportDefinitionId=" + reportDefinition, e);
    }
  }
}
