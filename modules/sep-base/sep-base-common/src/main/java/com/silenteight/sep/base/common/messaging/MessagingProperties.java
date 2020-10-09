package com.silenteight.sep.base.common.messaging;

import lombok.Data;

import com.silenteight.sep.base.common.messaging.encryption.MessagingEncryptionProperties;
import com.silenteight.sep.base.common.support.validation.IsDividableBy;
import com.silenteight.sep.base.common.support.validation.IsOneOf;

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
  static class Encryption implements MessagingEncryptionProperties {

    private boolean enabled = false;
    @NotEmpty
    private String keySeed;
    @NotEmpty
    private String salt;
    @NotEmpty
    private String nonceHeader = "encryption-nonce";

    // Cipher uses AES underneath.
    @IsOneOf({ 128, 192, 256 })
    private int keySizeInBits = 256;

    // Also known as IV (initialization vector). Default is recommended value in NIST SP 800-38D.
    @Min(8)
    @IsDividableBy(8)
    private int nonceSizeInBits = 96;

    // Also known as authentication tag. See NIST SP 800-38D.
    @IsOneOf({ 128, 120, 112, 104, 96 })
    private int macSizeInBits = 112;
  }

  @Data
  static class ErrorQueue {

    private boolean enabled = false;

    private String name = "error-queue";
  }
}
