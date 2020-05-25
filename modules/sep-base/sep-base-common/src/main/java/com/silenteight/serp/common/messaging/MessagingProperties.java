package com.silenteight.serp.common.messaging;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.silenteight.serp.common.messaging.Lz4CompressMessagePostProcessor.BEST_COMPRESSION;
import static com.silenteight.serp.common.messaging.Lz4CompressMessagePostProcessor.FASTEST_COMPRESSION;

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

    @Min(FASTEST_COMPRESSION)
    @Max(BEST_COMPRESSION)
    private int level = 4;
  }

  @Data
  static class Encryption {

    // TODO(ahaczewski): Implement message encryption.
    private boolean enabled = false;
  }
}
