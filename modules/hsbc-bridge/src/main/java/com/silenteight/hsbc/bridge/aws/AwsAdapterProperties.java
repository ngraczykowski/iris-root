package com.silenteight.hsbc.bridge.aws;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.net.URI;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.aws")
@Value
class AwsAdapterProperties {

  URI uri;
  String accessKey;
  String secretKey;
  Buckets buckets;
  String region;

  @Value
  static class Buckets {

    String modelBucketName;
    String watchlistBucketName;
  }
}
