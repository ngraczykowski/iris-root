package com.silenteight.adjudication.engine.solving.application.listener;

import com.silenteight.adjudication.engine.solving.application.process.GovernanceMatchResponseProcess;
import com.silenteight.adjudication.engine.solving.application.process.dto.MatchSolutionResponse;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@Service
class GovernanceResolvedMatchListener {

  private final GovernanceMatchResponseProcess governanceMatchResponseProcess;

  GovernanceResolvedMatchListener(
      final GovernanceMatchResponseProcess governanceMatchResponseProcess
  ) {
    this.governanceMatchResponseProcess = governanceMatchResponseProcess;
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  void onReceiveMessage(final MatchSolutionResponse matchSolutionResponse) {
    // Update matches received solution.
    // Verify state of all matches solution for alert - if completed
    // Send for solving alert solution to governance via queue (internal)
    // Create Governance internal queue listener and send to Gov
    //TODO verify is correct
    this.governanceMatchResponseProcess.processAlert(matchSolutionResponse);
  }

}
