package com.silenteight.warehouse.report.billing.v1.generation;

import java.util.Collection;

interface Column {

  String getName();

  Collection<String> getLabels();
}
