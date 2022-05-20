package com.silenteight.sep.base.testing.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ReactorTestingTest {

  @Test
  void ensureTestingReactorWorks() {
    Flux<String> source = Flux.just("foo", "bar");

    StepVerifier
        .create(appendBoomError(source))
        .expectNext("foo")
        .expectNext("bar")
        .expectErrorMessage("boom")
        .verify();
  }

  private static <T> Flux<T> appendBoomError(Flux<T> source) {
    return source.concatWith(Mono.error(new IllegalArgumentException("boom")));
  }
}
