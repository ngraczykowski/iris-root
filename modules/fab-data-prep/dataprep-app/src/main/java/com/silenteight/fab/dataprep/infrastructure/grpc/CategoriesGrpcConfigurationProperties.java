package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.grpc.category")
@Value
class CategoriesGrpcConfigurationProperties {

  Duration categoryDeadline;

  Duration categoryValueDeadline;
}
