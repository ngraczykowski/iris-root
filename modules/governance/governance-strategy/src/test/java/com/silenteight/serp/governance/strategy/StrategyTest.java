package com.silenteight.serp.governance.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = { StrategyTestConfiguration.class })
@ExtendWith({ SpringExtension.class })
class StrategyTest {

  private static final String CURRENT_STRATEGY_NAME = "strategies/USE_ANALYST_SOLUTION";

  @Autowired
  CurrentStrategyProvider currentStrategyProvider;

  @Test
  void shouldReturnCurrentStrategy() {
    Optional<String> currentStrategy = currentStrategyProvider.getCurrentStrategy();

    assertThat(currentStrategy.get()).isEqualTo(CURRENT_STRATEGY_NAME);
  }
}
