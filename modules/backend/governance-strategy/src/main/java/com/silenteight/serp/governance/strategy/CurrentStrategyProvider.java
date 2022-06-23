package com.silenteight.serp.governance.strategy;

import java.util.Optional;

public interface CurrentStrategyProvider {

  Optional<String> getCurrentStrategy();
}
