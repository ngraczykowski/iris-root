package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.cache.tag.CaffeineTagCache;
import com.mitchellbosecke.pebble.cache.template.CaffeineTemplateCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class PebbleConfiguration {

  @Bean
  PebbleEngine pebbleEngine(CommentTemplateLoader loader) {
    return new PebbleEngine.Builder()
        .loader(loader)
        .cacheActive(true)
        .tagCache(new CaffeineTagCache())
        .templateCache(new CaffeineTemplateCache())
        .build();
  }

  @Bean
  CommentTemplateLoader pebbleLoader(CommentTemplateRepository repository) {
    return new CommentTemplateLoader(repository);
  }
}
