package com.silenteight.warehouse.migration.country;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.warehouse.common.domain.DomainModule;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    CountryMigrationModule.class,
    OpendistroModule.class,
    ElasticsearchRestClientModule.class,
    UserAwareTokenProvider.class,
    DomainModule.class
})
@RequiredArgsConstructor
@Slf4j
class CountryMigrationTestConfiguration {
}
