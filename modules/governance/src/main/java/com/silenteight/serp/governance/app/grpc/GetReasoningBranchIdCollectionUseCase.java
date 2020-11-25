package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.GetReasoningBranchIdCollectionRequest;
import com.silenteight.proto.serp.v1.api.GetReasoningBranchIdCollectionResponse;
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder;
import com.silenteight.serp.governance.featurevector.dto.FeatureVectorView;

import com.google.protobuf.ByteString;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.silenteight.sep.base.common.logging.LogContextUtils.logCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
class GetReasoningBranchIdCollectionUseCase {

  private final FeatureVectorFinder featureVectorFinder;

  GetReasoningBranchIdCollectionResponse activate(GetReasoningBranchIdCollectionRequest request) {
    List<ByteString> vectorSignaturesList = request.getVectorSignaturesList();
    logCollection("request.vectorSignaturesList", vectorSignaturesList);

    Collection<FeatureVectorView> featureVectorViews =
        featureVectorFinder.findByVectorSignatureIn(vectorSignaturesList);

    // FIXME(mbartecki) it's a hack/temporary solution, which utilizes the fact that
    //  reasoning_branch_id is feature_vector_id in governance_feature_vector table.
    Map<ByteString, Long> vectorIdsMap = featureVectorViews
        .stream()
        .collect((toMap(FeatureVectorView::getVectorSignature, FeatureVectorView::getId)));

    List<Long> vectorIds =
        vectorSignaturesList.stream().map(vectorIdsMap::get).collect(toList());

    return GetReasoningBranchIdCollectionResponse
        .newBuilder()
        .addAllFeatureVectorId(vectorIds)
        .build();
  }
}
