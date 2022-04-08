package com.silenteight.bridge.core.reports.adapter.outgoing.crossmodule;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.GetAlertsWithMatchesCommand;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches.Match;
import com.silenteight.bridge.core.reports.domain.model.AlertWithMatchesDto;
import com.silenteight.bridge.core.reports.domain.model.AlertWithMatchesDto.MatchDto;
import com.silenteight.bridge.core.reports.domain.port.outgoing.RegistrationService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class ReportsRegistrationServiceAdapter implements RegistrationService {

  private final RegistrationFacade registrationFacade;

  @Override
  public List<AlertWithMatchesDto> getAlertsWithMatches(String batchId) {
    return registrationFacade.getAlertsWithMatches(new GetAlertsWithMatchesCommand(batchId))
        .stream()
        .map(this::toAlertWithMatches)
        .toList();
  }

  private AlertWithMatchesDto toAlertWithMatches(AlertWithMatches alertWithMatches) {
    return AlertWithMatchesDto.builder()
        .id(alertWithMatches.id())
        .name(alertWithMatches.name())
        .status(alertWithMatches.status().toString())
        .metadata(alertWithMatches.metadata())
        .errorDescription(alertWithMatches.errorDescription())
        .matches(alertWithMatches.matches().stream()
            .map(this::toMatchDto)
            .toList()
        )
        .build();
  }

  private MatchDto toMatchDto(Match match) {
    return new MatchDto(match.id(), match.name());
  }
}
