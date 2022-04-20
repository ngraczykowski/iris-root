package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.comments.comment.dto.CommentTemplateDto;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class CommentTemplateController {

  private final CommentTemplateService commentTemplateService;

  @PostMapping("/comment/template")
  public ResponseEntity<Void> addTemplate(@Valid @RequestBody CommentTemplateDto commentTemplate) {
    commentTemplateService.save(commentTemplate);
    return ResponseEntity.ok().build();
  }
}
