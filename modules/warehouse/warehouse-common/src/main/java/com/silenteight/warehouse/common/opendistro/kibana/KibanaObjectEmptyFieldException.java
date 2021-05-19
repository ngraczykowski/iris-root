package com.silenteight.warehouse.common.opendistro.kibana;

import static java.lang.String.format;

public class KibanaObjectEmptyFieldException extends IllegalStateException {

  KibanaObjectEmptyFieldException(String fieldName, String id, Object body) {
    super(format("%s is empty. id=%s, body=%s", fieldName, id, body));
  }
}
