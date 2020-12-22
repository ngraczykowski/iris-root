package com.silenteight.serp.governance.bulkchange;

import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesResponse;
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeRequest;
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeResponse;

public interface BulkChangeQueries {

  ValidateBulkChangeResponse validateBulkBranchChange(ValidateBulkChangeRequest request);

  ListBulkBranchChangesResponse listBulkBranchChanges(ListBulkBranchChangesRequest request);
}
