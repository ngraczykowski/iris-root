package com.silenteight.serp.governance.policy.transform.rbs;

import com.silenteight.serp.governance.policy.PolicyModule;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    PolicyModule.class
})
class RbsToPolicyTransformationTestConfiguration {

}
