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
    return store
        .stream()
        .filter(ct -> ct.getTemplateName().equals(name))
        .max(Comparator.comparingInt(CommentTemplate::getRevision));
  }

  @Override
  public synchronized CommentTemplate save(CommentTemplate entity) {
    store.add(entity);
    return entity;
  }
}
