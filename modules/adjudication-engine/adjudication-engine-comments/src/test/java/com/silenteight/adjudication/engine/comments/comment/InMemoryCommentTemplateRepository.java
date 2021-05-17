package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
class InMemoryCommentTemplateRepository implements CommentTemplateRepository {

  private final List<CommentTemplate> store = new ArrayList<>();

  @Override
  public Optional<CommentTemplate> findFirstByTemplateNameOrderByRevisionDesc(String name) {
    log.debug("InMemory findFirstByTemplateNameOrderByRevisionDesc:{}", name);
    return store.stream().filter(c -> c.getTemplateName().equals(name))
        .sorted(Comparator.comparingInt(CommentTemplate::getRevision).reversed())
        .findFirst();
  }

  @Override
  public synchronized CommentTemplate save(CommentTemplate entity) {
    log.debug("InMemory save:{}", entity.getTemplateName());
    store.add(entity);
    return entity;
  }
}
