package com.silenteight.warehouse.report.reporting;

import com.silenteight.warehouse.report.simulation.TenantDto;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.ADMIN_TENANT;

public class ReportServiceTestConstants {

  public static final TenantDto TENANT_NAME_WRAPPER = TenantDto.builder()
      .tenantName(ADMIN_TENANT)
      .build();
}
