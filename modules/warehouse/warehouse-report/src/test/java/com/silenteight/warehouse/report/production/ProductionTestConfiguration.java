package com.silenteight.warehouse.report.production;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ProductionConfiguration.class)
public class ProductionTestConfiguration {
}
