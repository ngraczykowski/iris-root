package com.silenteight.hsbc.bridge.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.match.event.UpdateMatchWithNameEvent;

import org.springframework.context.event.EventListener;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Slf4j
class MatchEventHandler {

  private final MatchUpdater matchUpdater;

  @EventListener
  public void onUpdateMatchWithNameEvent(UpdateMatchWithNameEvent event) {
    log.debug("Received match ids with names = {}", event.getMatchIdsWithNames());

    matchUpdater.updateNames(event.getMatchIdsWithNames());
  }
}
