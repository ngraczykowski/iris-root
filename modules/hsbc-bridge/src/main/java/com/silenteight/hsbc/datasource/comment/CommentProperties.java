package com.silenteight.hsbc.datasource.comment;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotEmpty;

@Validated
@ConfigurationProperties("silenteight.bridge.comment")
@Value
@ConstructorBinding
class CommentProperties {

  @NotEmpty
  Map<String, List<String>> wlTypes;
}
