package com.silenteight.hsbc.bridge.report;

import java.util.Collection;

public interface AlertFinder {

  Collection<Alert> find(Collection<Long> alertIds);
}
