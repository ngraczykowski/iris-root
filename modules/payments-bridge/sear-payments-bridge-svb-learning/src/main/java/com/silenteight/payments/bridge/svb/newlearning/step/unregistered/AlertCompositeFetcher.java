package com.silenteight.payments.bridge.svb.newlearning.step.unregistered;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;

@Slf4j
@Service
class AlertCompositeFetcher extends BaseCompositeFetcher<List<Long>, List<AlertComposite>> {

  private final HitCompositeFetcher hitCompositeFetcher;
  private final ActionCompositeFetcher actionCompositeFetcher;

  public AlertCompositeFetcher(
      DataSource dataSource, HitCompositeFetcher hitCompositeFetcher,
      ActionCompositeFetcher actionCompositeFetcher) {
    super(dataSource);
    this.hitCompositeFetcher = hitCompositeFetcher;
    this.actionCompositeFetcher = actionCompositeFetcher;
  }

  @Override
  protected List<AlertComposite> fetchWithConnection(Connection connection, List<Long> fkcoIds) {
    var hits = hitCompositeFetcher.fetchWithConnection(connection, fkcoIds);
    var actions = actionCompositeFetcher.fetchWithConnection(connection, fkcoIds);
    return fkcoIds
        .stream()
        .map(fkcoId -> AlertComposite
            .builder()
            .fkcoId(fkcoId)
            .hits(hits.get(fkcoId))
            .actionComposites(actions.get(fkcoId))
            .build())
        .collect(
            Collectors.toList());
  }
}
