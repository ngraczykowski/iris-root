package com.silenteight.adjudication.engine.comments.comment;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

interface CommentTemplateRepository extends Repository<CommentTemplate, Long> {

  @Cacheable(
      cacheNames = "templates",
      condition = "@commentCacheConfiguration.shouldCacheCommentTemplates()")
  Optional<CommentTemplate> findFirstByTemplateNameOrderByRevisionDesc(String name);

  @CacheEvict(cacheNames = "templates", allEntries = true)
  CommentTemplate save(CommentTemplate entity);
}
