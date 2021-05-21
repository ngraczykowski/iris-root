package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.common.opendistro.tenant.TenantCloningSpecification;
import com.silenteight.warehouse.common.opendistro.tenant.TenantService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.SIMULATION_TENANT;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.createNewProductionEvent;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.createNewSimulationEvent;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KibanaSetupForSimulationUseCaseTest {

  private static final String SOURCE_TENANT = "source_tenant";
  private static final String ANALYSIS = "analysis/123";

  @Mock
  private TenantService tenantService;

  @Captor
  private ArgumentCaptor<TenantCloningSpecification> specificationCaptor;

  private KibanaSetupForSimulationUseCase underTest;

  @BeforeEach
  void init() {
    underTest = new KibanaSetupForSimulationUseCase(tenantService, SOURCE_TENANT);
  }

  @Test
  void shouldTriggerTenantCloningForSimulationAnalysis() {
    underTest.handle(createNewSimulationEvent(ANALYSIS));

    verify(tenantService, times(1)).cloneTenant(specificationCaptor.capture());
    assertThat(specificationCaptor.getValue().getSourceTenant()).isEqualTo(SOURCE_TENANT);
    assertThat(specificationCaptor.getValue().getTargetTenant()).isEqualTo(SIMULATION_TENANT);
  }

  @Test
  void shouldNotTriggerAnyActionForProductionAnalysis() {
    underTest.handle(createNewProductionEvent(ANALYSIS));

    verifyNoInteractions(tenantService);
  }
}
