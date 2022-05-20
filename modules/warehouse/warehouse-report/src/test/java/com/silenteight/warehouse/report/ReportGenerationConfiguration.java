package com.silenteight.warehouse.report;

import com.silenteight.sep.auth.authorization.AuthorizationModule;
import com.silenteight.sep.auth.authorization.RoleAccessor;
import com.silenteight.sep.auth.authorization.RoleAccessorConfiguration;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.report.create.ReportCreateModule;
import com.silenteight.warehouse.report.download.ReportDownloadModule;
import com.silenteight.warehouse.report.generation.ReportGenerationModule;
import com.silenteight.warehouse.report.generation.ReportZipProperties;
import com.silenteight.warehouse.report.name.ReportFileNameModule;
import com.silenteight.warehouse.report.persistence.ReportPersistenceModule;
import com.silenteight.warehouse.report.sql.SqlExecutorModule;
import com.silenteight.warehouse.report.status.ReportStatusModule;
import com.silenteight.warehouse.report.storage.StorageModule;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    ReportCreateModule.class,
    ReportDownloadModule.class,
    ReportFileNameModule.class,
    ReportGenerationModule.class,
    ReportPersistenceModule.class,
    ReportStatusModule.class,
    StorageModule.class,
    SqlExecutorModule.class
})
@Import(RoleAccessorConfiguration.class)
class ReportGenerationConfiguration {

  @Bean
  @Primary
  CountryPermissionService countryPermissionServiceMock() {
    return mock(CountryPermissionService.class);
  }

  @Bean
  @Primary
  ReportZipProperties reportZipProperties() {
    return mock(ReportZipProperties.class);
  }
}
