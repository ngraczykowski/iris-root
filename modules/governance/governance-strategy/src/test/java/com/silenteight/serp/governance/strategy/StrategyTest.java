package com.silenteight.serp.governance.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.silenteight.serp.governance.strategy.StrategyFixture.CURRENT_STRATEGY_NAME;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = { StrategyTestConfiguration.class })
@ExtendWith({ SpringExtension.class })
class StrategyTest {

  @Autowired
  CurrentStrategyProvider currentStrategyProvider;

  @Test
  void shouldReturnCurrentStrategy() {
    Optional<String> currentStrategy = currentStrategyProvider.getCurrentStrategy();

    assertThat(currentStrategy.get()).isEqualTo(CURRENT_STRATEGY_NAME);
  }
}