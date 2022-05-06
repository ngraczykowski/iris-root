package com.silenteight.fab.dataprep.infrastructure.grpc

import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesGrpcServiceConfiguration.GrpcCategoryProperties
import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesGrpcServiceConfiguration.GrpcCategoryValueProperties

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.Duration
import java.time.temporal.ChronoUnit

@SpringBootTest(classes = CategoriesGrpcServiceConfiguration.class)
class CategoriesGrpcServiceConfigurationTest extends Specification {

  @Autowired
  private GrpcCategoryValueProperties grpcCategoryValueProperties

  @Autowired
  private GrpcCategoryProperties grpcCategoryProperties

  def "category grpc deadline property is read correctly"() {
    expect:
    grpcCategoryProperties.getDeadline() == Duration.of(1, ChronoUnit.MINUTES)
  }

  def "category value grpc deadline property is read correctly"() {
    expect:
    grpcCategoryValueProperties.getDeadline() == Duration.of(1, ChronoUnit.MINUTES)
  }
}
