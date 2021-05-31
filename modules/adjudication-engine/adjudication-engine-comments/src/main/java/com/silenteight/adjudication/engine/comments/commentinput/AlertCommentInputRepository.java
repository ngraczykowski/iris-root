package com.silenteight.adjudication.engine.comments.commentinput;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface AlertCommentInputRepository extends Repository<AlertCommentInput, Long> {

  Optional<AlertCommentInput> findAllByAlertId(long alertId);

  AlertCommentInput save(AlertCommentInput entity);
}
