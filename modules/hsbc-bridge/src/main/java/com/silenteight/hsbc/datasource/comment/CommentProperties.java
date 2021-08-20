package com.silenteight.hsbc.datasource.comment;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("silenteight.bridge.comment")
@Value
@ConstructorBinding
class CommentProperties {

  Map<String, List<String>> wlTypes;
}
