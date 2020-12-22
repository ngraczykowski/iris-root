package com.silenteight.serp.governance.app.grpc;

import com.silenteight.proto.serp.v1.api.DecisionTreeResponse;
import com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest;
import com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest.DecisionTreeSpecCase;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.serp.governance.decisiontreesummaryquery.DecisionTreeSummaryFinder;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest.DecisionTreeSpecCase.DECISIONTREESPEC_NOT_SET;
import static com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest.DecisionTreeSpecCase.DECISION_GROUP;
import static com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest.DecisionTreeSpecCase.DECISION_TREE_ID;

class GetDecisionTreeUseCase {

  private final Map<DecisionTreeSpecCase, DecisionTreeSpecHandler> handlers =
      new EnumMap<>(DecisionTreeSpecCase.class);

  private final DecisionTreeSummaryFinder finder;

  GetDecisionTreeUseCase(DecisionTreeSummaryFinder finder) {
    this.finder = finder;
    handlers.put(DECISION_TREE_ID, this::handleDecisionTreeIdCase);
    handlers.put(DECISION_GROUP, this::handleDecisionGroupCase);
    handlers.put(DECISIONTREESPEC_NOT_SET, (r, l) -> handleNotSpecifiedCase(l));
  }

  void activate(
      GetDecisionTreeRequest request,
      GetDecisionTreeUseCaseListener listener) {

    handlers.get(request.getDecisionTreeSpecCase()).accept(request, listener);
  }

  private void handleDecisionTreeIdCase(
      GetDecisionTreeRequest request,
      GetDecisionTreeUseCaseListener listener) {

    finder.findById(request.getDecisionTreeId())
        .map(GetDecisionTreeUseCase::mapToSingleTreeResponse)
        .ifPresentOrElse(listener::onDecisionTreeFound, listener::onDecisionTreeNotFound);
  }

  private void handleDecisionGroupCase(
      GetDecisionTreeRequest request,
      GetDecisionTreeUseCaseListener listener) {

    finder.findByDecisionGroupName(request.getDecisionGroup())
        .map(GetDecisionTreeUseCase::mapToSingleTreeResponse)
        .ifPresentOrElse(listener::onDecisionTreeFound, listener::onDecisionTreeNotFound);
  }

  private static void handleNotSpecifiedCase(GetDecisionTreeUseCaseListener listener) {
    listener.onMissingDecisionTreeSpec();
  }

  @NotNull
  private static DecisionTreeResponse mapToSingleTreeResponse(DecisionTreeSummary treeSummary) {
    return DecisionTreeResponse.newBuilder().setDecisionTree(treeSummary).build();
  }

  interface DecisionTreeSpecHandler extends
      BiConsumer<GetDecisionTreeRequest, GetDecisionTreeUseCaseListener> {
  }

  interface GetDecisionTreeUseCaseListener {

    void onDecisionTreeFound(DecisionTreeResponse response);

    void onDecisionTreeNotFound();

    void onMissingDecisionTreeSpec();
  }
}
