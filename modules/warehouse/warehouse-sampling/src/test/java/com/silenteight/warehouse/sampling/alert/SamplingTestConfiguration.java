package com.silenteight.warehouse.sampling.alert;

import com.silenteight.sep.auth.token.TokenModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.common.time.TimeModule;
import com.silenteight.warehouse.indexer.query.single.SingleAlertQueryConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    SamplingConfiguration.class,
    SingleAlertQueryConfiguration.class,
    TestElasticSearchModule.class,
    TimeModule.class,
    TokenModule.class
})
public class SamplingTestConfiguration {
}
