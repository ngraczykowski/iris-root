package com.silenteight.payments.bridge.firco.callback.model;

import lombok.Getter;
import lombok.Setter;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;

import java.util.UUID;

@Setter
@Getter
public class CallbackException extends RuntimeException {

  private static final long serialVersionUID = -8176814598212295319L;

  private AlertMessageStatus status;
  private UUID alertId;

  public CallbackException(Exception exception) {
    super(exception);
  }

}
