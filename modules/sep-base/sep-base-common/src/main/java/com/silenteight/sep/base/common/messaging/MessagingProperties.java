package com.silenteight.sep.base.common.messaging;

import lombok.Data;

import com.silenteight.sep.base.common.support.validation.AesKeyLength;
import com.silenteight.sep.base.common.support.validation.MacLength;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "serp.messaging")
@Validated
@Data
class MessagingProperties {

  @NotNull
  private Compression compression = new Compression();

  @NotNull
  private Encryption encryption = new Encryption();

  @NotNull
  private ErrorQueue errorQueue = new ErrorQueue();

  @Data
  static class Compression {

    private boolean enabled = true;

    @Min(Lz4CompressMessagePostProcessor.FASTEST_COMPRESSION)
    @Max(Lz4CompressMessagePostProcessor.BEST_COMPRESSION)
    private int level = 4;
  }

  @Data
  static class Encryption {

    private boolean enabled = false;
    @NotEmpty
    private String keySeed;
    @NotEmpty
    private String salt;
    @AesKeyLength
    private int keySizeInBits = 256;
    @Min(32)
    private int nonceSizeInBits = 48; // Recommended by NIST
    @MacLength
    private int macSizeInBits = 64; // Lesser should not be used
  }

  @Data
  static class ErrorQueue {

    private boolean enabled = false;

    private String name = "error-queue";
  }
}
