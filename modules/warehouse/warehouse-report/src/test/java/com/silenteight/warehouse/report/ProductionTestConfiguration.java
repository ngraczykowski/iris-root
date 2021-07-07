package com.silenteight.warehouse.report;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.auth.token.TokenModule;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.environment.EnvironmentModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.common.time.TimeModule;
import com.silenteight.warehouse.report.production.ProductionReportingModule;
import com.silenteight.warehouse.report.reporting.ReportingModule;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    ElasticsearchRestClientModule.class,
    EnvironmentModule.class,
    OpendistroModule.class,
    ReportingModule.class,
    ProductionReportingModule.class,
    TestElasticSearchModule.class,
    TokenModule.class,
    TimeModule.class
})
@RequiredArgsConstructor
public class ProductionTestConfiguration {
}
