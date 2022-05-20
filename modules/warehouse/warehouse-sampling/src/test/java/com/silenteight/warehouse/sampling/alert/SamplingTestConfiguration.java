package com.silenteight.warehouse.sampling.alert;

import com.silenteight.warehouse.common.time.TimeModule;
import com.silenteight.warehouse.indexer.query.single.SingleAlertQueryConfiguration;

import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    SamplingConfiguration.class,
    SingleAlertQueryConfiguration.class,
    TimeModule.class
})
@JdbcTest
public class SamplingTestConfiguration {
}
