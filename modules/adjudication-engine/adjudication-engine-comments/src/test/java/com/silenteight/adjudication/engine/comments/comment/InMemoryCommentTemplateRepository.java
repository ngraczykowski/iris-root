package com.silenteight.adjudication.engine.comments.comment;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
class InMemoryCommentTemplateRepository implements CommentTemplateRepository {

  private final List<CommentTemplate> store = new ArrayList<>();

  @Override
  public Optional<CommentTemplate> findFirstByTemplateName(String name) {
    return store
        .stream()
        .filter(ct -> ct.getTemplateName().equals(name))
        .findFirst();
  }

  @Override
  public synchronized CommentTemplate save(CommentTemplate entity) {
    store.add(entity);
    return entity;
  }

  @Override
  public List<CommentTemplate> saveAll(Iterable<CommentTemplate> entity) {
    store.addAll((Collection<? extends CommentTemplate>) entity);
    return store;
  }

  @Override
  public void deleteAll() {
    store.clear();
  }

  CommentTemplate getCommentTemplateByTemplateName(String templateName) {
    return store.stream().filter(s -> s.getTemplateName().equals(templateName)).findFirst().get();
  }
}
