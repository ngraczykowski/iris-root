package com.silenteight.serp.governance.changerequest.attachment.add;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.changerequest.domain.exception.MaxAttachmentsPerChangeRequestException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
class AddAttachmentsControllerAdvice {

  @ExceptionHandler(MaxAttachmentsPerChangeRequestException.class)
  public ResponseEntity<String> handle(MaxAttachmentsPerChangeRequestException e) {
    log.error(e.getMessage());
    return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
  }
}
