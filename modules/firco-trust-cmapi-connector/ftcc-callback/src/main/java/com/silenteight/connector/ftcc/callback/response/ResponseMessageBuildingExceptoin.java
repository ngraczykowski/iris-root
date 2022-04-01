package com.silenteight.connector.ftcc.callback.response;

class ResponseMessageBuildingExceptoin extends RuntimeException {

  private static final long serialVersionUID = -4466937251811641668L;

  ResponseMessageBuildingExceptoin(Exception e) {
    super(e);
  }
}
