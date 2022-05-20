package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class TemplateEngineCacheInvalidator {

  private final List<TemplateEngineWithCache> engines;

  @Scheduled(cron = "${ae.comments.template.cache.invalidation:0 * * * * ?}")
  void invalidateCache() {
    engines.forEach(TemplateEngineWithCache::invalidateCache);
  }
}
