package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.NonNull;
import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.grpc.universal-data-source")
@Value
class UniversalDataSourceGrpcConfigurationProperties {

  @NonNull
  Duration udsDeadline;
}
