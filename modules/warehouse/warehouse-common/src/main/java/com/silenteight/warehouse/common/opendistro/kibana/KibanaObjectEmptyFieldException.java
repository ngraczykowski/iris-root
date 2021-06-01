package com.silenteight.warehouse.common.opendistro.kibana;

import static java.lang.String.format;

public class KibanaObjectEmptyFieldException extends IllegalStateException {

  private static final long serialVersionUID = -3200811458038550246L;

  KibanaObjectEmptyFieldException(String fieldName, String id, Object body) {
    super(format("%s is empty. id=%s, body=%s", fieldName, id, body));
  }
}
