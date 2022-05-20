package com.silenteight.hsbc.bridge.grpc.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import javax.annotation.Nullable;
import javax.validation.constraints.Min;

@Validated
@ConfigurationProperties(value = "bridge.rpc")
@ConstructorBinding
@RequiredArgsConstructor
@ToString
class GrpcConfigurationProperties {

  @Min(0)
  @Nullable
  private final Integer threads;

  Optional<Integer> getThreads() {
    return Optional.ofNullable(threads);
  }
}
