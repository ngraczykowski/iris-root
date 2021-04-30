package com.silenteight.adjudication.engine.solve.agentexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

// todo tkleszcz: remove class before merge
@Slf4j
@Profile("dev")
@RestController
@RequiredArgsConstructor
class SolveController {

  //  private final SolveGateway solveGateway;

  @PostMapping("/solve")
  void solve() {
    //    solveGateway.startSolve();
  }
}
