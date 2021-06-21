package com.silenteight.adjudication.engine.comments.comment;


import com.silenteight.adjudication.engine.common.protobuf.ProtoStructConverter;

import com.mitchellbosecke.pebble.PebbleEngine;

class CommentTemplateFixture {

  static CommentFacade inMemoryCommentFacade(InMemoryCommentTemplateRepository repository) {
    var engine = new PebbleEngine.Builder()
        .loader(new CommentTemplateLoader(repository))
        .build();
    var useCase = new GenerateCommentUseCase(new ProtoStructConverter(), engine);
    return new CommentFacade(useCase);
  }

  public static CommentTemplate newCommentTemplateEntity(
      String name, int revision, String payload) {
    return CommentTemplate.builder()
        .templateName(name)
        .revision(revision)
        .template(payload)
        .build();
  }
}
