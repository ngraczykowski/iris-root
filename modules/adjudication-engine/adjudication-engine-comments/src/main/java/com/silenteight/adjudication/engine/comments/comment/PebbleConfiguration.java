package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.Loader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class PebbleConfiguration {

  @Bean
  public PebbleEngine pebbleEngine(Loader loader) {
    return new PebbleEngine.Builder().loader(loader).build();
  }

  @Bean
  public Loader pebbleLoader(CommentTemplateRepository repository) {
    return new CommentTemplateLoader(repository);
  }


}
