package com.silenteight.serp.governance.bulkchange;

import com.silenteight.proto.serp.v1.api.BulkBranchChangeView;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest.ReasoningBranchFilter;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest.StateFilter;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesResponse;
import com.silenteight.proto.serp.v1.governance.ApplyBulkBranchChangeCommand;
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand;
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand.BranchSolutionChange;
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand.EnablementChange;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.RejectBulkBranchChangeCommand;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.protocol.utils.Uuids;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.bulkchange.ListBulkBranchChangeUseCaseIT.TestRepositoryConfiguration;
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.silenteight.serp.governance.bulkchange.ListBulkBranchChangeUseCaseIT.Fixtures.withId;
import static com.silenteight.serp.governance.bulkchange.ListBulkBranchChangeUseCaseIT.Fixtures.withStatuses;
import static com.silenteight.serp.governance.bulkchange.ListBulkBranchChangeUseCaseIT.Fixtures.withStatusesAndIds;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = { TestRepositoryConfiguration.class })
class ListBulkBranchChangeUseCaseIT extends BaseDataJpaTest {

  private final Fixtures fixtures = new Fixtures();

  @Autowired
  private BulkBranchChangeRepository bulkBranchChangeRepository;

  private BulkChangeFacade facade;

  @BeforeEach
  void setUp() {
    BulkChangeConfiguration configuration = new BulkChangeConfiguration(
        Mockito.mock(ApplicationEventPublisher.class),
        bulkBranchChangeRepository,
        Mockito.mock(DecisionTreeFacade.class),
        Mockito.mock(FeatureVectorFinder.class)
    );
    facade = configuration.bulkChangeFacade();
  }

  @Test
  void shouldFindNothing() {
    ListBulkBranchChangesResponse response = facade.listBulkBranchChanges(
        withStatuses(List.of(BulkBranchChangeView.State.STATE_REJECTED)));
    assertThat(response.getChangesCount()).isEqualTo(0);
  }

  @Test
  void shouldProperlyFilterByStates() {
    //when
    facade.createBulkBranchChange(fixtures.createBulkChange12);
    facade.createBulkBranchChange(fixtures.createBulkChange13);
    facade.createBulkBranchChange(fixtures.createBulkChange24);
    facade.applyBulkBranchChange(fixtures.applyBulkChange12);
    facade.rejectBulkBranchChange(fixtures.rejectBulkChange13);

    //then
    var list = facade
        .listBulkBranchChanges(
            withStatuses(List.of(BulkBranchChangeView.State.STATE_REJECTED)))
        .getChangesList();
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0).getId()).isEqualTo(fixtures.createBulkChange13.getId());

    //and
    list = facade
        .listBulkBranchChanges(
            withStatuses(List.of(BulkBranchChangeView.State.STATE_APPLIED)))
        .getChangesList();
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0).getId()).isEqualTo(fixtures.createBulkChange12.getId());

    //and
    list = facade
        .listBulkBranchChanges(
            withStatuses(List.of(BulkBranchChangeView.State.STATE_CREATED)))
        .getChangesList();
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0).getId()).isEqualTo(fixtures.createBulkChange24.getId());

    //and
    list = facade.listBulkBranchChanges(
        withStatuses(List.of(BulkBranchChangeView.State.STATE_CREATED,
            BulkBranchChangeView.State.STATE_APPLIED, BulkBranchChangeView.State.STATE_REJECTED)))
        .getChangesList();
    assertThat(list.size()).isEqualTo(3);
    var ids = list.stream().map(BulkBranchChangeView::getId).collect(Collectors.toList());
    assertThat(ids).contains(fixtures.createBulkChange12.getId());
    assertThat(ids).contains(fixtures.createBulkChange13.getId());
    assertThat(ids).contains(fixtures.createBulkChange24.getId());
  }

  @Test
  void shouldProperlyFilterByBranchIds() {
    //when:
    facade.createBulkBranchChange(fixtures.createBulkChange12);
    facade.createBulkBranchChange(fixtures.createBulkChange13);
    facade.createBulkBranchChange(fixtures.createBulkChange24);

    facade.applyBulkBranchChange(fixtures.applyBulkChange12);
    facade.rejectBulkBranchChange(fixtures.rejectBulkChange13);

    //then:
    var list = facade
        .listBulkBranchChanges(withId(List.of(Map.of(1, 2), Map.of(1, 3))))
        .getChangesList();
    assertThat(list.size()).isEqualTo(2);
    var ids = list.stream().map(BulkBranchChangeView::getId).collect(Collectors.toList());
    assertThat(ids).contains(fixtures.createBulkChange12.getId());
    assertThat(ids).contains(fixtures.createBulkChange13.getId());

    //and:
    list = facade
        .listBulkBranchChanges(withId(List.of(Map.of(1, 3), Map.of(1, 7))))
        .getChangesList();
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0).getId()).isEqualTo(fixtures.createBulkChange13.getId());

    //and:
    list = facade
        .listBulkBranchChanges(withId(List.of(Map.of(2, 4), Map.of(2, 1))))
        .getChangesList();
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0).getId()).isEqualTo(fixtures.createBulkChange24.getId());

    //and:
    assertThat(facade
        .listBulkBranchChanges(withId(List.of(Map.of(1, 5), Map.of(1, 9))))
        .getChangesList()).isEmpty();
  }

  @Test
  void shouldProperlyFilterByBranchIdsAndStatus() {
    //when:
    facade.createBulkBranchChange(fixtures.createBulkChange12);
    facade.createBulkBranchChange(fixtures.createBulkChange13);
    facade.createBulkBranchChange(fixtures.createBulkChange24);

    facade.applyBulkBranchChange(fixtures.applyBulkChange12);
    facade.rejectBulkBranchChange(fixtures.rejectBulkChange13);

    //then:
    var list = facade
        .listBulkBranchChanges(
            withStatusesAndIds(
                List.of(BulkBranchChangeView.State.STATE_APPLIED),
                List.of(Map.of(1, 2), Map.of(1, 3))))
        .getChangesList();
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0).getId()).isEqualTo(fixtures.createBulkChange12.getId());

    //and:
    list = facade
        .listBulkBranchChanges(
            withStatusesAndIds(
                List.of(BulkBranchChangeView.State.STATE_REJECTED),
                List.of(Map.of(1, 2), Map.of(1, 3))))
        .getChangesList();
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0).getId()).isEqualTo(fixtures.createBulkChange13.getId());

    //and:
    list = facade
        .listBulkBranchChanges(
            withStatusesAndIds(
                List.of(BulkBranchChangeView.State.STATE_CREATED),
                List.of(Map.of(1, 2), Map.of(1, 3), Map.of(2, 4))))
        .getChangesList();
    assertThat(list.size()).isEqualTo(1);
    assertThat(list.get(0).getId()).isEqualTo(fixtures.createBulkChange24.getId());

    //and:
    assertThat(facade
        .listBulkBranchChanges(
            withStatusesAndIds(
                List.of(BulkBranchChangeView.State.STATE_CREATED),
                List.of(Map.of(1, 2), Map.of(1, 6))))
        .getChangesList()).isEmpty();
  }


  static class Fixtures {

    CreateBulkBranchChangeCommand createBulkChange12 = createBulkBranchChangeCommand(1, 2);
    CreateBulkBranchChangeCommand createBulkChange13 = createBulkBranchChangeCommand(1, 3);
    CreateBulkBranchChangeCommand createBulkChange24 = createBulkBranchChangeCommand(2, 4);

    ApplyBulkBranchChangeCommand applyBulkChange12 =
        applyBulkBranchChangeCommand(createBulkChange12);
    RejectBulkBranchChangeCommand rejectBulkChange13 = rejectBulkBranchChangeCommand(
        createBulkChange13);


    private static CreateBulkBranchChangeCommand createBulkBranchChangeCommand(
        long treeId, long fvId) {
      var branchId = ReasoningBranchId
          .newBuilder()
          .setDecisionTreeId(treeId)
          .setFeatureVectorId(fvId)
          .build();

      return CreateBulkBranchChangeCommand
          .newBuilder()
          .setId(Uuids.random())
          .setCorrelationId(Uuids.random())
          .addReasoningBranchIds(branchId)
          .setEnablementChange(EnablementChange.newBuilder().setEnabled(Boolean.TRUE).build())
          .setSolutionChange(BranchSolutionChange.newBuilder().setSolution(
              BranchSolution.BRANCH_FALSE_POSITIVE).build())
          .build();
    }

    private static ApplyBulkBranchChangeCommand applyBulkBranchChangeCommand(
        CreateBulkBranchChangeCommand createCommand) {
      return ApplyBulkBranchChangeCommand.newBuilder()
          .setId(createCommand.getId())
          .setCorrelationId(Uuids.random())
          .build();
    }

    private static RejectBulkBranchChangeCommand rejectBulkBranchChangeCommand(
        CreateBulkBranchChangeCommand createCommand) {
      return RejectBulkBranchChangeCommand.newBuilder()
          .setId(createCommand.getId())
          .setCorrelationId(Uuids.random())
          .build();
    }

    static ListBulkBranchChangesRequest withStatuses(List<BulkBranchChangeView.State> states) {
      return ListBulkBranchChangesRequest
          .newBuilder()
          .setStateFilter(buildStateFilter(states))
          .build();
    }

    private static StateFilter buildStateFilter(List<BulkBranchChangeView.State> states) {
      return StateFilter.newBuilder()
          .addAllStates(states)
          .build();
    }

    static ListBulkBranchChangesRequest withId(List<Map<Integer, Integer>> mapList) {
      return ListBulkBranchChangesRequest.newBuilder().setReasoningBranchFilter(
          ReasoningBranchFilter.newBuilder().addAllReasoningBranchIds(buildIdsFilter(mapList))
              .build())
          .build();
    }

    static ListBulkBranchChangesRequest withStatusesAndIds(
        List<BulkBranchChangeView.State> states, List<Map<Integer, Integer>> mapList) {

      return ListBulkBranchChangesRequest.newBuilder()
          .setStateFilter(buildStateFilter(states))
          .setReasoningBranchFilter(
              ReasoningBranchFilter.newBuilder().addAllReasoningBranchIds(buildIdsFilter(mapList))
                  .build())
          .build();
    }

    private static Collection<ReasoningBranchId> buildIdsFilter(
        List<Map<Integer, Integer>> mapList) {
      Collection<ReasoningBranchId> allBranchIds = new ArrayList<>();
      mapList.forEach(map ->
          map.forEach(
              ((treeId, fvId) ->
                  allBranchIds.add(
                      ReasoningBranchId
                          .newBuilder()
                          .setDecisionTreeId(treeId)
                          .setFeatureVectorId(fvId)
                          .build())
              ))
      );
      return allBranchIds;
    }
  }


  @EntityScan
  @EnableJpaRepositories
  @Configuration
  static class TestRepositoryConfiguration {
  }
}
