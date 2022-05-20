package com.silenteight.adjudication.engine.comments.comment;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface CommentTemplateRepository extends Repository<CommentTemplate, Long> {

  Optional<CommentTemplate> findFirstByTemplateNameOrderByRevisionDesc(String name);

  CommentTemplate save(CommentTemplate entity);
}
