package com.silenteight.warehouse.report.rbs.generation;

import java.util.Collection;

@Deprecated(since = "2.0.0")
interface Column {

  String getName();

  Collection<String> getLabels();
}
