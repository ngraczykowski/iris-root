package com.silenteight.serp.governance.strategy;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    StrategyModule.class
})
public class StrategyTestConfiguration {
}
