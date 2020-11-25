package com.silenteight.serp.governance.bulkchange;

import java.util.UUID;

class BulkChangeNotFoundException extends IllegalStateException {

  private static final long serialVersionUID = -7545153442415179264L;

  BulkChangeNotFoundException(UUID uuid) {
    super("Bulk change does not exist, id = " + uuid);
  }
}
