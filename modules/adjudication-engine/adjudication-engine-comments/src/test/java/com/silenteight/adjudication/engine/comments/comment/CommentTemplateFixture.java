package com.silenteight.adjudication.engine.comments.comment;


import com.mitchellbosecke.pebble.PebbleEngine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

class CommentTemplateFixture {

  private static final ConcurrentMap<String, AtomicInteger> TEMPLATE_REVISIONS =
      new ConcurrentHashMap<>();

  static CommentFacade inMemoryCommentFacade(InMemoryCommentTemplateRepository repository) {
    var engine = new PebbleEngine.Builder()
        .loader(new CommentTemplateLoader(repository))
        .build();
    var useCase = new GenerateCommentUseCase(engine);

    return new CommentFacade(useCase);
  }

  public static CommentTemplate commentTemplate(String name, String payload) {
    var revision = TEMPLATE_REVISIONS.computeIfAbsent(name, s -> new AtomicInteger(1));
    return commentTemplate(name, revision.getAndIncrement(), payload);
  }

  public static CommentTemplate commentTemplate(String name, int revision, String payload) {
    return CommentTemplate.builder()
        .templateName(name)
        .revision(revision)
        .template(payload)
        .build();
  }
}
