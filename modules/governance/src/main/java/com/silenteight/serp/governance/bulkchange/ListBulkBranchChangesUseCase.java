package com.silenteight.serp.governance.bulkchange;

import lombok.Builder;

import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesResponse;

import org.springframework.transaction.annotation.Transactional;


@Builder
class ListBulkBranchChangesUseCase {

  private final BulkBranchChangeFinder bulkBranchChangeFinder;

  @Transactional(readOnly = true)
  ListBulkBranchChangesResponse activate(ListBulkBranchChangesRequest request) {
    var responseBuilder = ListBulkBranchChangesResponse.newBuilder();

    Iterable<BulkBranchChange> changes = bulkBranchChangeFinder.findAll(request);

    changes.forEach(change -> responseBuilder
        .addChanges(BulkBranchChangeViewMapper.mapToProtoBulkBranchChangeView(change)));

    return responseBuilder.build();
  }

}
