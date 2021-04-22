package com.silenteight.warehouse.common.opendistro.kibana;

public class KibanaReportGenerationFailedException extends RuntimeException {

  private static final long serialVersionUID = 7119171700396343370L;

  KibanaReportGenerationFailedException(String reportInstanceId) {
    super("Report generation failed, reportInstanceId=" + reportInstanceId);
  }
}
