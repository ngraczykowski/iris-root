package com.silenteight.hsbc.bridge.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.match.event.UpdateMatchWithNameEvent;

import org.springframework.context.event.EventListener;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
class MatchEventHandler {

  private final MatchRepository matchRepository;

  @EventListener
  @Transactional
  public void onUpdateMatchWithNameEvent(UpdateMatchWithNameEvent updateMatchWithNameEvent) {
    log.info("Received updateMatchWithNameEvent.");

    updateMatchWithNameEvent.getNames().forEach(n -> {
      String[] splitName = n.split("/");
      matchRepository.updateNameById(Long.parseLong(splitName[0]), n);
    });

    log.info("End processing updateMatchWithNameEvent.");
  }
}
