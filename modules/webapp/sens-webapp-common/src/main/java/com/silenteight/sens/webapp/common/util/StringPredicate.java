package com.silenteight.sens.webapp.common.util;

import java.util.function.Predicate;

public interface StringPredicate extends Predicate<String> {

  boolean isEnabled();

  boolean defaultValue();
}

