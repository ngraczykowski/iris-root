package com.silenteight.sens.webapp.backend.bulkchange;

import java.util.List;

public interface BulkChangeQuery {

  List<BulkChangeDto> listPending();
}
