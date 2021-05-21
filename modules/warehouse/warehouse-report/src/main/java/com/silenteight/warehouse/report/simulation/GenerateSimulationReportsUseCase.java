package com.silenteight.warehouse.report.simulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.common.opendistro.kibana.ReportDefinitionDto;
import com.silenteight.warehouse.common.opendistro.tenant.TenantCloningResult;
import com.silenteight.warehouse.common.opendistro.tenant.TenantCloningSpecification;
import com.silenteight.warehouse.common.opendistro.tenant.TenantService;
import com.silenteight.warehouse.indexer.analysis.AnalysisMetadataDto;
import com.silenteight.warehouse.indexer.analysis.AnalysisService;

import com.dyngr.core.AttemptMaker;
import com.dyngr.core.AttemptResult;

import java.util.Set;

import static com.dyngr.Polling.waitPeriodly;
import static com.dyngr.core.AttemptResults.justContinue;
import static com.dyngr.core.AttemptResults.justFinish;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toSet;

@Slf4j
@RequiredArgsConstructor
public class GenerateSimulationReportsUseCase {

  private final AnalysisService analysisService;
  private final OpendistroKibanaClient opendistroKibanaClient;
  private final TenantService tenantService;
  private final String sourceTenant;
  private final int pollingIntervalMs;
  private final int pollingMaxAttemptCount;

  public void activate(String analysis) {
    AnalysisMetadataDto analysisMetadata = analysisService.getAnalysisMetadata(analysis);
    String elasticIndexName = analysisMetadata.getElasticIndexName();
    String tenant = analysisMetadata.getTenant();

    try {
      triggerReportGeneration(analysis, tenant, elasticIndexName);
      log.info("Simulation report generation triggered successfully. "
          + "analysis=" + analysis + ", " + "tenant=" + tenant);
    } catch (Exception e) {
      log.error("Exception during simulation report generation. "
          + "analysis=" + analysis + ", " + "tenant=" + tenant, e);
    }
  }

  private void triggerReportGeneration(String analysis, String tenant, String elasticIndexName) {
    TenantCloningSpecification newTenantSpecification = TenantCloningSpecification.builder()
        .sourceTenant(sourceTenant)
        .targetTenant(tenant)
        .tenantDescription(prepareTenantDescription(analysis))
        .elasticIndexName(elasticIndexName)
        .build();
    TenantCloningResult tenantCloningResult = tenantService.cloneTenant(newTenantSpecification);

    Set<String> reportDefinitionIds = tenantCloningResult.getReportDefinitionIds();
    waitForReportDefinitions(reportDefinitionIds, tenant);

    createReports(reportDefinitionIds, tenant);
  }

  private static String prepareTenantDescription(String analysis) {
    return "Simulation: " + analysis;
  }

  private void waitForReportDefinitions(Set<String> reportDefinitionIds, String tenant) {
    waitPeriodly(pollingIntervalMs, MILLISECONDS)
        .stopAfterAttempt(pollingMaxAttemptCount)
        .run(new ReportDefinitionsPollerCondition(reportDefinitionIds, tenant,
            opendistroKibanaClient));
  }

  private void createReports(Set<String> reportDefinitionIds, String tenant) {
    reportDefinitionIds.forEach(reportDefinitionId ->
        opendistroKibanaClient.createReportInstance(tenant, reportDefinitionId));
  }

  @RequiredArgsConstructor
  private static class ReportDefinitionsPollerCondition implements AttemptMaker<Void> {

    private final Set<String> expectedReportDefinitionIds;
    private final String tenant;
    private final OpendistroKibanaClient opendistroKibanaClient;

    @Override
    public AttemptResult<Void> process() {
      Set<String> actualReportDefinitionIds = opendistroKibanaClient.listReportDefinitions(tenant)
          .stream()
          .map(ReportDefinitionDto::getId)
          .collect(toSet());

      if (actualReportDefinitionIds.containsAll(expectedReportDefinitionIds)) {
        return justFinish();
      }

      return justContinue();
    }
  }
}
