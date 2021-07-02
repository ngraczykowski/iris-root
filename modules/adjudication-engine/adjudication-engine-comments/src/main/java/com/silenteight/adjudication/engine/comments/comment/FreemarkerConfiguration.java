package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import freemarker.cache.MruCacheStorage;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.util.Locale;

import static freemarker.template.Configuration.VERSION_2_3_28;
import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@RequiredArgsConstructor
@Slf4j
class FreemarkerConfiguration {

  private static final Version FREEMARKER_VERSION = VERSION_2_3_28;
  private static final Charset ENCODING = UTF_8;
  private static final Locale LOCALE = Locale.ENGLISH;
  private static final int CACHE_STRONG_SIZE_LIMIT = 200;
  private static final int CACHE_SOFT_SIZE_LIMIT = 500;

  @Bean
  freemarker.template.Configuration freemarker(FreemarkerCommentTemplateLoader freemarkerLoader) {
    var freeMarker = new freemarker.template.Configuration(FREEMARKER_VERSION);
    freeMarker.setTemplateLoader(freemarkerLoader);
    freeMarker.setDefaultEncoding(ENCODING.name());
    freeMarker.setLocale(LOCALE);
    freeMarker.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    freeMarker.setLogTemplateExceptions(true);
    freeMarker.setWrapUncheckedExceptions(true);
    freeMarker.setCacheStorage(new MruCacheStorage(
        CACHE_STRONG_SIZE_LIMIT,
        CACHE_SOFT_SIZE_LIMIT));
    var configurer = new FreemarkerSharedStaticsConfigurer(FREEMARKER_VERSION);
    configurer.configure(freeMarker, FreemarkerStringUtils.class);
    return freeMarker;
  }
}
