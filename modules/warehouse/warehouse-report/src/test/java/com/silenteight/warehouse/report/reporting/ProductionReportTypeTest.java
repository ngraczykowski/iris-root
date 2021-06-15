package com.silenteight.warehouse.report.reporting;

import com.silenteight.warehouse.report.production.ProductionReportType;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProductionReportTypeTest {

  @Test
  void shouldReturnTenantName() {
    String tenantName = ProductionReportType.AI_REASONING.getTenantName("loc");
    assertThat(tenantName).isEqualTo("loc_production_ai_reasoning");
  }

  @Test
  void shouldReturnTestProdAccuracyTenant() {
    String tenantName = ProductionReportType.ACCURACY.getTenantName("test");
    assertThat(tenantName).isEqualTo("test_production_accuracy");
  }
}
