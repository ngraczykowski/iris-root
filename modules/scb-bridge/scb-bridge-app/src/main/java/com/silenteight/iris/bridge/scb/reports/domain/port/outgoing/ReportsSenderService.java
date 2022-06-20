/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.reports.domain.port.outgoing;

import com.silenteight.iris.bridge.scb.reports.domain.model.Report;

import java.util.List;

public interface ReportsSenderService {

  void send(List<Report> reports);
}
