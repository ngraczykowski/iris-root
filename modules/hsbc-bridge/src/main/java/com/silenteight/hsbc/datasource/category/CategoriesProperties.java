package com.silenteight.hsbc.datasource.category;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotEmpty;

@Validated
@ConfigurationProperties("silenteight.bridge.category")
@Value
@ConstructorBinding
class CategoriesProperties {

  @NotEmpty
  Map<String, List<String>> riskTypeValues;
}
