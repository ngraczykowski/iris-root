package com.silenteight.serp.governance.file.validation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties("serp.governance.files.validation")
class ValidationProperties {

  @NotNull
  int maxFileNameLength;
  @NotNull
  long maxFileSizeInBytes;
  @NotNull
  int maxNumberFilesToUpload;
  @NotNull
  List<String> allowedTypes;
  @NotNull
  String allowedCharactersForFileName;
}
