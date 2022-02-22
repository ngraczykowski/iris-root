package com.silenteight.serp.governance.policy.transform.rbs;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
@ConfigurationProperties(prefix = "serp.governance.rbs.import")
@ConstructorBinding
@Validated
class RbsImportProperties {

  @NotNull
  @NotEmpty
  List<String> solutions;
}
