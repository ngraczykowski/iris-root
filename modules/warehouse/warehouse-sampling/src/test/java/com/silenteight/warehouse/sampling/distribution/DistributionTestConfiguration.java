package com.silenteight.warehouse.sampling.distribution;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ DistributionConfiguration.class })
public class DistributionTestConfiguration {
}
