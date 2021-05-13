package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.comments.domain.AlertTemplateModel;
import com.silenteight.adjudication.engine.common.protobuf.ProtoStructConverter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
class GenerateRecommendationCommentUseCase {

  private final PebbleCommentator commentator;
  private final ProtoStructConverter converter;

  @Transactional
  String generate(String templateName, AlertTemplateModel alertTemplateModel) {
    return commentator.evaluate(templateName, converter.convert(alertTemplateModel));
  }
}
