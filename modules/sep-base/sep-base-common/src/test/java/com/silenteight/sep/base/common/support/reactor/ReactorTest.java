package com.silenteight.sep.base.common.support.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.*;

class ReactorTest {

  @Test
  void ensureReactorWorks() {
    String[] fixture = { "silent", "eight", "is", "great" };

    Flux<String> seq1 = Flux.just(fixture);

    assertThat(seq1.collectList().block()).containsExactly(fixture);
  }
}
