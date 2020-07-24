package com.silenteight.sens.webapp.backend.bulkchange.pending;

import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeDto;

import java.util.List;

public interface PendingBulkChangeQuery {

  List<BulkChangeDto> listPending();
}
