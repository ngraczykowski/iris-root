package com.silenteight.warehouse.migration.backupmessage;

import lombok.Getter;

public class MessageNotProcessedException extends RuntimeException {

  @Getter
  private final Message brokenMessage;

  public MessageNotProcessedException(Exception e, Message brokenMessage) {
    super(e);
    this.brokenMessage = brokenMessage;
  }
}
