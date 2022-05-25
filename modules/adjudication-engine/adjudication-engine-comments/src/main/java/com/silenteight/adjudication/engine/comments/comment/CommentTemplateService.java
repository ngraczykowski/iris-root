package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.dto.CommentTemplateDto;

import org.springframework.stereotype.Service;

import java.util.Optional;
import javax.validation.Valid;

import static java.lang.Integer.valueOf;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentTemplateService {

  private final CommentTemplateRepository commentTemplateRepository;

  public CommentTemplate save(@Valid CommentTemplateDto dto) {
    log.info("updating {} template", dto.getTemplateName());
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
