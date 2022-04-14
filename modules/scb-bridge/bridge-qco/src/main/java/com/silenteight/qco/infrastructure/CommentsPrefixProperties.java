package com.silenteight.qco.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "silenteight.qco")
public record CommentsPrefixProperties(String commentsPrefix) {
}
