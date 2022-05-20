package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.cache.tag.CaffeineTagCache;
import com.mitchellbosecke.pebble.cache.template.CaffeineTemplateCache;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.DelegatingLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Configuration
class PebbleConfiguration {

  @Bean
  PebbleEngine pebbleEngine(PebbleCommentTemplateLoader pebbleLoader) {
    var classpathLoader = new ClasspathLoader();
    classpathLoader.setPrefix("com/silenteight/adjudication/engine/comments/comment/");
    classpathLoader.setSuffix(".default.peb");

    var loaders = new DelegatingLoader(List.of(pebbleLoader, classpathLoader));

    return new PebbleEngine.Builder()
        .loader(loaders)
        .cacheActive(true)
        .tagCache(new CaffeineTagCache())
        .templateCache(new CaffeineTemplateCache())
        .autoEscaping(false)
        .newLineTrimming(false)
        .build();
  }
}
