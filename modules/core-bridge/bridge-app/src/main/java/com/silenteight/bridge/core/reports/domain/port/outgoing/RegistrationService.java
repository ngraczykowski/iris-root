package com.silenteight.bridge.core.reports.domain.port.outgoing;


import com.silenteight.bridge.core.reports.domain.model.AlertWithMatchesDto;

import java.util.List;

public interface RegistrationService {

  List<AlertWithMatchesDto> getAlertsWithMatches(String batchId);
}
