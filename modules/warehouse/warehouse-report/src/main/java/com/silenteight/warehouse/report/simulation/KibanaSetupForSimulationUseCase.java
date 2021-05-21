package com.silenteight.warehouse.report.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.tenant.TenantCloningSpecification;
import com.silenteight.warehouse.common.opendistro.tenant.TenantService;
import com.silenteight.warehouse.indexer.analysis.AnalysisMetadataDto;
import com.silenteight.warehouse.indexer.analysis.NewAnalysisEvent;
import com.silenteight.warehouse.indexer.analysis.NewAnalysisHandler;

@Slf4j
@RequiredArgsConstructor
public class KibanaSetupForSimulationUseCase implements NewAnalysisHandler {

  @NonNull
  private final TenantService tenantService;
  @NonNull
  private final String sourceTenant;

  @Override
  public void handle(NewAnalysisEvent event) {
    if (event.isSimulation()) {
      activate(event.getAnalysis(), event.getAnalysisMetadataDto());
    }
  }

  void activate(String analysis, AnalysisMetadataDto analysisMetadata) {
    String elasticIndexName = analysisMetadata.getElasticIndexName();
    String tenant = analysisMetadata.getTenant();

    try {
      cloneTenant(analysis, tenant, elasticIndexName);
      log.info("Simulation report generation triggered successfully. "
          + "analysis=" + analysis + ", tenant=" + tenant + ", index=" + elasticIndexName);
    } catch (Exception e) {
      log.error("Exception during simulation report generation. "
          + "analysis=" + analysis + ", tenant=" + tenant + ", index=" + elasticIndexName, e);
    }
  }

  private void cloneTenant(String analysis, String tenant, String elasticIndexName) {
    TenantCloningSpecification newTenantSpecification = TenantCloningSpecification.builder()
        .sourceTenant(sourceTenant)
        .targetTenant(tenant)
        .tenantDescription(prepareTenantDescription(analysis))
        .elasticIndexName(elasticIndexName)
        .build();
    tenantService.cloneTenant(newTenantSpecification);
  }

  private static String prepareTenantDescription(String analysis) {
    return "Simulation for " + analysis;
  }
}
