package com.silenteight.scb.reports.domain.port.outgoing;

import com.silenteight.scb.reports.domain.model.Report;

import java.util.List;

public interface ReportsSenderService {

  void send(List<Report> reports);
}
