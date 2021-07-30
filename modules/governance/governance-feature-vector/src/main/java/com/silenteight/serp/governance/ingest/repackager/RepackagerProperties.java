package com.silenteight.serp.governance.ingest.repackager;

import lombok.NonNull;
import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConfigurationProperties("serp.governance.featurevector.ingest.repackager")
@ConstructorBinding
public class RepackagerProperties {

  @NonNull
  String featureOrCategoryRegex;

  @NonNull
  String prefixAndSuffixRegex;

  @NonNull
  String fvSignatureKey;
}
