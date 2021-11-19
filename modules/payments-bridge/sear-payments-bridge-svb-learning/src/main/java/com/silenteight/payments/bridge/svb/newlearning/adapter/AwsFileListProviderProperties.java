package com.silenteight.payments.bridge.svb.newlearning.adapter;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("pb.svb-learning.aws.file-list")
@Data
@Validated
class AwsFileListProviderProperties {

  private static final String BUCKET_NAME = "sierra-dev-decrypted-files";

  private String bucketName = BUCKET_NAME;
}
