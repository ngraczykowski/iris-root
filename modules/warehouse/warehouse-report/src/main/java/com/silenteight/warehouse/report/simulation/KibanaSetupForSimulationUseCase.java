package com.silenteight.warehouse.report.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.tenant.TenantCloningSpecification;
import com.silenteight.warehouse.common.opendistro.tenant.TenantService;
import com.silenteight.warehouse.indexer.analysis.AnalysisMetadataDto;
import com.silenteight.warehouse.indexer.analysis.NewSimulationAnalysisEvent;
import com.silenteight.warehouse.indexer.analysis.NewSimulationAnalysisHandler;

@Slf4j
@RequiredArgsConstructor
public class KibanaSetupForSimulationUseCase implements NewSimulationAnalysisHandler {

  @NonNull
  private final TenantService tenantService;
  @NonNull
  private final String sourceTenant;

  @Override
  public void handle(NewSimulationAnalysisEvent event) {
    activate(event.getAnalysis(), event.getAnalysisMetadataDto());
  }

  void activate(String analysis, AnalysisMetadataDto analysisMetadata) {
    String elasticIndexName = analysisMetadata.getElasticIndexName();
    String tenant = analysisMetadata.getTenant();

    try {
      cloneTenant(analysis, tenant, elasticIndexName);
      log.info("Tenant cloned successfully. "
          + "analysis=" + analysis + ", tenant=" + tenant + ", index=" + elasticIndexName);
    } catch (Exception e) {
      log.error("Exception during tenant cloning. "
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
