package com.silenteight.serp.governance.common.grpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@ConfigurationProperties("serp.governance.grpc.client")
public class GrpcCommonProperties {

  @NotNull
  private Duration timeout;

  public long getTimoutMillis() {
    return getTimeout().toMillis();
  }
}
