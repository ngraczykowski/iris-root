package com.silenteight.fab.dataprep.domain;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.jayway.jsonpath.Option.ALWAYS_RETURN_LIST;
import static com.jayway.jsonpath.Option.SUPPRESS_EXCEPTIONS;

@Configuration
@Import({ AlertParser.class, MessageDataTokenizer.class })
class ParserConfiguration {

  @Bean
  ParseContext parseContext() {
    return JsonPath.using(com.jayway.jsonpath.Configuration
        .builder()
        .options(SUPPRESS_EXCEPTIONS, ALWAYS_RETURN_LIST)
        .mappingProvider(new JacksonMappingProvider())
        .jsonProvider(new JacksonJsonNodeJsonProvider())
        .build());
  }
}
