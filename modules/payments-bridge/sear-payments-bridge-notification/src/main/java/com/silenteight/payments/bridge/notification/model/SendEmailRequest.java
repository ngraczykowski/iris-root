package com.silenteight.payments.bridge.notification.model;

import lombok.Builder;
import lombok.Value;

import javax.annotation.Nullable;

@Builder
@Value
public class SendEmailRequest {

  Long id;
  String subject;
  String htmlText;
  @Nullable String attachmentName;
  @Nullable byte[] attachment;

}
