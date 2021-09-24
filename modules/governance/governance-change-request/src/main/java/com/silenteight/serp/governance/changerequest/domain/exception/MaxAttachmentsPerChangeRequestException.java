package com.silenteight.serp.governance.changerequest.domain.exception;

import java.util.UUID;

import static java.lang.String.*;

public class MaxAttachmentsPerChangeRequestException extends RuntimeException {

  private static final long serialVersionUID = -7741376825489814924L;

  public MaxAttachmentsPerChangeRequestException(UUID uuid) {
    super(format("Change request with %s id has already max number of attachments.",uuid));
  }
}
