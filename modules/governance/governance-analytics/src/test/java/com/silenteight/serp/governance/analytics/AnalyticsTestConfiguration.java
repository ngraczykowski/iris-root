package com.silenteight.serp.governance.analytics;

import com.silenteight.serp.governance.analytics.AnalyticsModule;
import com.silenteight.serp.governance.common.signature.SignatureModule;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    AnalyticsModule.class,
    SignatureModule.class
})
public class AnalyticsTestConfiguration {
}
