package com.silenteight.warehouse.report.billing.generation;

import java.util.Collection;

interface Column {

  String getName();

  Collection<String> getLabels();
}
