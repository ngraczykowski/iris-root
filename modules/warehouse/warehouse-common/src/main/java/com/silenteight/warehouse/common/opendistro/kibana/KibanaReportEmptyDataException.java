package com.silenteight.warehouse.common.opendistro.kibana;

public class KibanaReportEmptyDataException extends RuntimeException {

  private static final long serialVersionUID = -6627442594123482007L;

  public KibanaReportEmptyDataException(String reportInstanceId) {
    super("Empty data in Kibana report, reportInstanceId=" + reportInstanceId);
  }
}
