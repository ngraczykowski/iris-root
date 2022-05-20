package com.silenteight.adjudication.engine.comments.comment;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.comments.comment.dto.CommentTemplateDto;

import org.springframework.stereotype.Service;

import java.util.Optional;
import javax.validation.Valid;

import static java.lang.Integer.valueOf;

@RequiredArgsConstructor
@Service
public class CommentTemplateService {

  @NonNull private final CommentTemplateRepository commentTemplateRepository;

  public CommentTemplate save(@Valid CommentTemplateDto dto) {
    return commentTemplateRepository.save(buildTemplate(dto));
  }

  private CommentTemplate buildTemplate(CommentTemplateDto dto) {
    return CommentTemplate.builder()
        .templateName(dto.getTemplateName())
        .template(dto.getTemplate())
        .revision(getRevision(dto))
        .build();
  }

  private Integer getRevision(CommentTemplateDto dto) {
    Optional<CommentTemplate> entity =
        commentTemplateRepository.findFirstByTemplateNameOrderByRevisionDesc(dto.getTemplateName());

    return entity.isPresent() ? valueOf(entity.get().getRevision() + 1) : dto.getRevision();
  }
}
