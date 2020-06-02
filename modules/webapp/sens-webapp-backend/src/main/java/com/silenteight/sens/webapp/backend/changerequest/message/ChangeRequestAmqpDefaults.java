package com.silenteight.sens.webapp.backend.changerequest.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChangeRequestAmqpDefaults {

  public static final String EXCHANGE_CHANGE_REQUEST = "change-request";
  public static final String ROUTE_CHANGE_REQUEST_CREATE = "change-request.create";
  public static final String CHANGE_REQUEST_CREATE_QUEUE = "webapp.change-request.create";
  public static final String ROUTE_CHANGE_REQUEST_APPROVE = "change-request.approve";
  public static final String CHANGE_REQUEST_APPROVE_QUEUE = "webapp.change-request.approve";
  public static final String ROUTE_CHANGE_REQUEST_REJECT = "change-request.reject";
  public static final String CHANGE_REQUEST_REJECT_QUEUE = "webapp.change-request.reject";
}
