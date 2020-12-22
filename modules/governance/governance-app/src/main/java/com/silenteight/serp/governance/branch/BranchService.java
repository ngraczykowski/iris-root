package com.silenteight.serp.governance.branch;

import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import static java.lang.String.format;
import static java.lang.String.valueOf;

@RequiredArgsConstructor
public class BranchService {

  private final AuditingLogger auditingLogger;
  private final BranchRepository branchRepository;

  @Transactional
  public void bulkUpdateOrCreateBranches(List<ConfigureBranchRequest> requests) {
    requests.forEach(this::updateOrCreateBranch);
  }

  @Transactional
  public void bulkUpdateBranches(List<ConfigureBranchRequest> requests) {
    requests.forEach(this::updateBranch);
  }

  private void updateOrCreateBranch(ConfigureBranchRequest configureBranchRequest) {
    long treeId = configureBranchRequest.getDecisionTreeId();
    long vectorId = configureBranchRequest.getFeatureVectorId();

    Branch branch = findBranch(treeId, vectorId)
        .orElseGet(() -> new Branch(treeId, vectorId));

    configureAndSaveBranch(branch, configureBranchRequest);
  }

  private void updateBranch(ConfigureBranchRequest configureBranchRequest) {
    long treeId = configureBranchRequest.getDecisionTreeId();
    long vectorId = configureBranchRequest.getFeatureVectorId();

    Branch branch = findBranch(treeId, vectorId)
        .orElseThrow(() -> new EntityNotFoundException(
            format("Could not find any branch for decisionTreeId: %d and featureVectorId: %d",
                treeId, vectorId)));

    configureAndSaveBranch(branch, configureBranchRequest);
  }

  private Optional<Branch> findBranch(long treeId, long vectorId) {
    return branchRepository.findByDecisionTreeIdAndFeatureVectorId(treeId, vectorId);
  }

  private void configureAndSaveBranch(Branch branch, ConfigureBranchRequest request) {
    branch.apply(request);
    branchRepository.save(branch);

    // BS
    var auditDataDto = AuditDataDto.builder()
        .correlationId(request.getCorrelationId())
        .eventId(UUID.randomUUID())
        .timestamp(Timestamp.from(Instant.now()))
        .type(request.getClass().getSimpleName())
        .entityId(valueOf(branch.getId()))
        .entityClass("Branch")
        .entityAction("UPDATE")
        .details(request.toString())
        .build();
    auditingLogger.log(auditDataDto);
  }
}
