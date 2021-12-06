package com.silenteight.payments.bridge.app.integration.newlearning;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("pb.svb-learning.aws.file-list")
@Data
@Validated
class S3CsvFileResourceProviderProperties {

  private String bucketName;

}
