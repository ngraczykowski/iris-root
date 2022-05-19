package com.silenteight.bridge.core.reports.domain.port.outgoing;

import com.silenteight.bridge.core.reports.domain.model.AlertWithMatchesDto;
import com.silenteight.bridge.core.reports.domain.model.AlertWithoutMatchesDto;
import com.silenteight.bridge.core.reports.domain.model.MatchWithAlertId;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface RegistrationService {

  List<AlertWithMatchesDto> getAlertsWithMatches(String batchId);

  Stream<AlertWithoutMatchesDto> streamAlerts(String batchId);

  List<MatchWithAlertId> getMatches(Set<Long> ids);
}
