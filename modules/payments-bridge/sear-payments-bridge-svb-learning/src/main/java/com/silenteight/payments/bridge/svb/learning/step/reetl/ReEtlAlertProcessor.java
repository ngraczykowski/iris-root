package com.silenteight.payments.bridge.svb.learning.step.reetl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterMatchResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.svb.learning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.learning.step.etl.IngestDatasourceService;

import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class ReEtlAlertProcessor implements ItemProcessor<AlertComposite, Void> {

  private final FindRegisteredAlertUseCase findRegisteredAlertUseCase;
  private final FeatureInputSpecification featureInputSpecification;
  private final IngestDatasourceService ingestDatasourceService;

  @Override
  public Void process(final AlertComposite alertComposite) throws Exception {
    final List<String> registeredAlerts = List.of(alertComposite.toFindRegisterAlertRequest());
    var solvingRegistered =
        findRegisteredAlertUseCase.find(registeredAlerts);

    solvingRegistered
        .stream()
        .map(this::transformToRegisterAlertResponse)
        .forEachOrdered(responses -> createInput(alertComposite, responses));
    return null;
  }

  private void createInput(
      final AlertComposite alertComposite, final RegisterAlertResponse responses) {
    this.ingestDatasourceService.ingest(alertComposite, responses, this.featureInputSpecification);
  }

  private RegisterAlertResponse transformToRegisterAlertResponse(final RegisteredAlert alert) {
    final List<RegisterMatchResponse> registerMatchResponses = determineMarchesResponse(alert);
    return RegisterAlertResponse.builder()
        .alertName(alert.getAlertName())
        .systemId(alert.getSystemId())
        .matchResponses(registerMatchResponses)
        .build();
  }

  private List<RegisterMatchResponse> determineMarchesResponse(final RegisteredAlert alert) {
    return alert.getMatches()
        .stream()
        .map(this::determineMatchResponse)
        .collect(Collectors.toList());
  }

  private RegisterMatchResponse determineMatchResponse(final RegisteredMatch registeredMatch) {
    return RegisterMatchResponse.builder()
        .matchId(registeredMatch.getMatchId())
        .matchName(registeredMatch.getMatchName())
        .build();
  }
}
