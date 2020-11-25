package com.silenteight.serp.governance.bulkchange;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.serp.governance.bulkchange.BulkBranchChange.State;
import com.silenteight.serp.governance.bulkchange.BulkBranchChangeRepositoryIT.TestRepositoryConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;

import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(
    classes = TestRepositoryConfiguration.class,
    initializers = PostgresTestInitializer.class)
class BulkBranchChangeRepositoryIT extends BaseDataJpaTest {

  private final Fixtures fixtures = new Fixtures();

  @Autowired
  private BulkBranchChangeRepository repository;

  @Test
  void shouldStoreEmptyBulkAndSaveProperties() {
    store(fixtures.emptyBulk);

    assertThat(find(fixtures.emptyBulkId)).hasValue(fixtures.emptyBulk);
  }

  @Test
  void shouldStoreBulkWithChanges() {
    store(fixtures.bulkWithChanges);

    assertThat(find(fixtures.bulkWithChangesId)).hasValue(fixtures.bulkWithChanges);
  }

  @Test
  void shouldFailWhenStoreEntityWithTheSameBulkChangeIdTwice() {
    store(fixtures.emptyBulk);

    assertThrows(
        DataIntegrityViolationException.class,
        () -> store(fixtures.duplicatedEmptyBulk));
  }

  @Test
  void shouldStoreCompletedBulks() {
    store(fixtures.appliedBulk);
    store(fixtures.rejectedBulk);

    assertThat(find(fixtures.appliedBulkId)).hasValue(fixtures.appliedBulk);
    assertThat(find(fixtures.rejectedBulkId)).hasValue(fixtures.rejectedBulk);
  }

  private void store(BulkBranchChange change) {
    repository.save(change);

    entityManager.flush();
    entityManager.clear();
  }

  private Optional<BulkBranchChange> find(UUID id) {
    return repository.findByBulkBranchChangeId(id);
  }

  private static class Fixtures {

    UUID emptyBulkId = UUID.randomUUID();
    UUID bulkWithChangesId = UUID.randomUUID();
    UUID appliedBulkId = UUID.randomUUID();
    UUID rejectedBulkId = UUID.randomUUID();

    BulkBranchChange emptyBulk = createBulk(emptyBulkId, null, null);
    BulkBranchChange duplicatedEmptyBulk = createBulk(emptyBulkId, null, null);

    ReasoningBranchIdToChange id1 = new ReasoningBranchIdToChange(1, 1);
    ReasoningBranchIdToChange id2 = new ReasoningBranchIdToChange(1, 2);
    ReasoningBranchIdToChange id3 = new ReasoningBranchIdToChange(2, 1);
    ReasoningBranchIdToChange id4 = new ReasoningBranchIdToChange(2, 2);

    BulkBranchChange bulkWithChanges = createBulk(
        bulkWithChangesId, BranchSolution.BRANCH_FALSE_POSITIVE, true, id1, id2, id3, id4);

    BulkBranchChange appliedBulk = createCompletedBulk(appliedBulkId, State.APPLIED);
    BulkBranchChange rejectedBulk = createCompletedBulk(rejectedBulkId, State.REJECTED);

    private static BulkBranchChange createBulk(
        UUID id,
        @Nullable BranchSolution solutionChange,
        @Nullable Boolean enablementChange,
        ReasoningBranchIdToChange... changes) {

      var bulk = new BulkBranchChange(id, of(changes));
      bulk.setEnablementChange(enablementChange);
      bulk.setSolutionChange(solutionChange);
      return bulk;
    }

    private static BulkBranchChange createCompletedBulk(UUID id, State state) {
      var bulk = createBulk(id, BranchSolution.BRANCH_FALSE_POSITIVE, null);
      bulk.setState(state);
      bulk.setCompletedAt(OffsetDateTime.now());
      return bulk;
    }
  }

  @EntityScan
  @EnableJpaRepositories
  @Configuration
  static class TestRepositoryConfiguration {

  }
}
