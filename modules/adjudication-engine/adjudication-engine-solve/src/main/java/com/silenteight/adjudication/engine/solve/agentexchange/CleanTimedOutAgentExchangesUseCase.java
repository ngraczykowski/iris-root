package com.silenteight.adjudication.engine.solve.agentexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
class CleanTimedOutAgentExchangesUseCase implements Runnable {

  private final SolveProperties solveProperties;
  private final AgentExchangeRepository agentExchangeRepository;

  @Override
  @Transactional
  public void run() {
//    long timeoutSeconds = solveProperties.getAgentTimeout().getSeconds();
//    OffsetDateTime cleanBefore = OffsetDateTime.now().minusSeconds(timeoutSeconds);
//    long cleanCount = agentExchangeRepository.countAllByCreatedAtBefore(cleanBefore);
//    log.debug(
//        "Cleaning timed out agent exchanges: {} with timeout: {} seconds",
//        cleanCount, timeoutSeconds);
//    agentExchangeRepository.deleteAllByCreatedAtBefore(cleanBefore);
  }
}
