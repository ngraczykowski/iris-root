package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.infrastructure.FeatureAndCategoryConfiguration
import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesGrpcServiceConfiguration
import com.silenteight.fab.dataprep.infrastructure.grpc.UniversalDataSourceGrpcServiceConfiguration

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import

@TestConfiguration
@Import([ParserConfiguration, FeatureAndCategoryConfiguration,
    FeedingService, UniversalDataSourceGrpcServiceConfiguration,
    CategoryService, CategoriesGrpcServiceConfiguration])
class ServiceTestConfig {

}
