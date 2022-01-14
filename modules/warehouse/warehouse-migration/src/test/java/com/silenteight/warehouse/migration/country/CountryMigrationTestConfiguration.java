package com.silenteight.warehouse.migration.country;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.warehouse.common.domain.DomainModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    CountryMigrationModule.class,
    OpendistroModule.class,
    TestElasticSearchModule.class,
    UserAwareTokenProvider.class,
    DomainModule.class
})
@RequiredArgsConstructor
@Slf4j
class CountryMigrationTestConfiguration {
}
