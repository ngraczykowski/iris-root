package com.silenteight.payments.bridge.notification.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import javax.annotation.Nullable;

@Builder
@Value
public class SendEmailRequest {

  List<Long> ids;
  String subject;
  String htmlText;
  @Nullable String attachmentName;
  @Nullable byte[] attachment;

}
