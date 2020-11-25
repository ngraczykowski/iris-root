package com.silenteight.serp.governance.bulkchange;

import com.silenteight.serp.governance.bulkchange.audit.BulkChangeAuditModule;
import com.silenteight.serp.governance.bulkchange.integration.BulkChangeIntegrationModule;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    BulkChangeIntegrationModule.class,
    BulkChangeAuditModule.class,
})
public class BulkChangeModule {
}
