package com.silenteight.bridge.core.reports.domain.port.outgoing;

import com.silenteight.bridge.core.reports.domain.model.Report;

import java.util.List;

public interface ReportsSenderService {

  void send(String analysisName, List<Report> reports);
}
