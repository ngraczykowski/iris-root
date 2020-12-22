package com.silenteight.serp.governance.featurevector;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsRequest;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsRequest.Vector;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsResponse;

import com.google.protobuf.ByteString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;

@RequiredArgsConstructor
public class FeatureVectorService {

  private final FeatureVectorRepository repo;

  @Transactional
  public StoreFeatureVectorsResponse store(StoreFeatureVectorsRequest request) {
    ByteString featuresSignature = request.getFeaturesSignature();
    List<Vector> vectors = request.getVectors();

    //TODO(bgulowaty): make this faster
    Map<ByteString, Long> signaturesById = new HashMap<>();
    vectors.forEach(vector -> signaturesById.put(
        vector.getVectorSignature(), storeAndGetId(featuresSignature, vector)));

    return new StoreFeatureVectorsResponse(signaturesById);
  }

  private long storeAndGetId(ByteString featuresSignature, Vector featureVector) {
    String vectorSignature = toBase64String(featureVector.getVectorSignature());

    Optional<FeatureVectorEntity> existingVector = repo.findByVectorSignature(vectorSignature);

    return existingVector
        .map(FeatureVectorEntity::getId)
        .orElseGet(() -> persistAndGetId(featuresSignature, featureVector));
  }

  private Long persistAndGetId(ByteString featuresSignature, Vector featureVector) {
    FeatureVectorEntity entity = new FeatureVectorEntity(
        featuresSignature,
        featureVector.getVectorSignature(),
        featureVector.getValues());

    FeatureVectorEntity saved = repo.save(entity);
    return saved.getId();
  }
}
