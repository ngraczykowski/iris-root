package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.cache.tag.CaffeineTagCache;
import com.mitchellbosecke.pebble.cache.template.CaffeineTemplateCache;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.DelegatingLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
class PebbleConfiguration {

  @Bean
  PebbleEngine pebbleEngine(CommentTemplateLoader commentTemplateLoader) {
    var classpathLoader = new ClasspathLoader();
    classpathLoader.setPrefix("com/silenteight/adjudication/engine/comments/comment/");
    classpathLoader.setSuffix(".default.peb");

    var loaders = new DelegatingLoader(List.of(commentTemplateLoader, classpathLoader));

    return new PebbleEngine.Builder()
        .loader(loaders)
        .cacheActive(true)
        .tagCache(new CaffeineTagCache())
        .templateCache(new CaffeineTemplateCache())
        .autoEscaping(false)
        .newLineTrimming(false)
        .build();
  }

  @Bean
  CommentTemplateLoader pebbleLoader(CommentTemplateRepository repository) {
    return new CommentTemplateLoader(repository);
  }
}
