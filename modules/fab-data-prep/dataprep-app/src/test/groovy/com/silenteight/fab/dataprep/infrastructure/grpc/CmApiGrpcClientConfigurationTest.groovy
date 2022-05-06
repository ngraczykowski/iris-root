package com.silenteight.fab.dataprep.infrastructure.grpc

import com.silenteight.fab.dataprep.infrastructure.grpc.CmApiGrpcClientConfiguration.GrpcProperties

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.Duration
import java.time.temporal.ChronoUnit

@SpringBootTest(classes = CmApiGrpcClientConfiguration.class)
class CmApiGrpcClientConfigurationTest extends Specification {

  @Autowired
  private GrpcProperties properties

  def "cm api grpc deadline property is read correctly"() {
    expect:
    properties.getDeadline() == Duration.of(1, ChronoUnit.MINUTES)
  }
}

