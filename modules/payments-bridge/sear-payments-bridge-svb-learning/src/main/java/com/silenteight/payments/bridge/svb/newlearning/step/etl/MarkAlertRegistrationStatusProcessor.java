package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.port.FindRegisteredAlertPort;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlAlert;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.silenteight.payments.bridge.svb.newlearning.domain.AlertRegistrationStatus.REGISTERED;
import static com.silenteight.payments.bridge.svb.newlearning.domain.AlertRegistrationStatus.UNREGISTERED;

@Service
@RequiredArgsConstructor
class MarkAlertRegistrationStatusProcessor implements ItemProcessor<AlertComposite, EtlAlert> {

  private final FindRegisteredAlertPort findRegisteredAlertPort;

  @Override
  public EtlAlert process(AlertComposite alertComposite) {
    var registered =
        findRegisteredAlertPort.find(List.of(alertComposite.toFindRegisterAlertRequest()));
    var status = registered.isEmpty() ? UNREGISTERED : REGISTERED;
    return EtlAlert.builder().status(status).alertComposite(alertComposite).build();
  }
}
