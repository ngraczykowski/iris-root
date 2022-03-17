package com.silenteight.fab.dataprep.infrastructure.grpc

import com.silenteight.fab.dataprep.infrastructure.grpc.RegistrationGrpcServiceConfiguration.GrpcProperties

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.Duration
import java.time.temporal.ChronoUnit

@SpringBootTest(classes = RegistrationGrpcServiceConfiguration.class)
class RegistrationGrpcServiceConfigurationTest extends Specification {

  @Autowired
  private GrpcProperties properties;

  def "registration grpc client deadline property is read correctly"() {
    expect:
    properties.getDeadline() == Duration.of(1, ChronoUnit.MINUTES);
  }
}
