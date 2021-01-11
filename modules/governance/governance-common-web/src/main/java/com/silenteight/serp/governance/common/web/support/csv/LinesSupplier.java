package com.silenteight.serp.governance.common.web.support.csv;

import java.util.stream.Stream;

public interface LinesSupplier {

  Stream<String> lines();
}
