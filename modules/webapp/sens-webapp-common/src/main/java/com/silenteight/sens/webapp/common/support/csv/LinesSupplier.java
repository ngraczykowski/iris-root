package com.silenteight.sens.webapp.common.support.csv;

import java.util.stream.Stream;

public interface LinesSupplier {

  Stream<String> lines();
}
