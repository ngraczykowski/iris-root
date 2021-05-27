package com.silenteight.warehouse.report.reporting;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ReportTypeTest {

  @Test
  void shouldReturnTenantName() {
    String tenantName = ReportType.AI_REASONING.getTenantName("loc", "sim");
    assertThat(tenantName).isEqualTo("loc_sim_ai_reasoning");
  }

  @Test
  void shouldReturnTestProdAccuracyTenant() {
    String tenantName = ReportType.ACCURACY.getTenantName("test", "prod");
    assertThat(tenantName).isEqualTo("test_prod_accuracy");
  }
}
