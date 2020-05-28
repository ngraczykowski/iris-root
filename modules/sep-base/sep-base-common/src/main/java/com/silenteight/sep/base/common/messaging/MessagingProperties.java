package com.silenteight.sep.base.common.messaging;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "serp.messaging")
@Validated
@Data
class MessagingProperties {

  @NotNull
  private Compression compression = new Compression();

  @NotNull
  private Encryption encryption = new Encryption();

  @Data
  static class Compression {

    private boolean enabled = true;

    @Min(Lz4CompressMessagePostProcessor.FASTEST_COMPRESSION)
    @Max(Lz4CompressMessagePostProcessor.BEST_COMPRESSION)
    private int level = 4;
  }

  @Data
  static class Encryption {

    // TODO(ahaczewski): Implement message encryption.
    private boolean enabled = false;
  }
}
