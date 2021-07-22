package com.silenteight.hsbc.datasource.category;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("silenteight.bridge.category")
@Value
@ConstructorBinding
class CategoriesProperties {

  Map<String, List<String>> riskTypeValues;
}
