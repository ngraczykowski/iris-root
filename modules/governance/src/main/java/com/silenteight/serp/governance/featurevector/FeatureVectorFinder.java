package com.silenteight.serp.governance.featurevector;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.protocol.ByteStringUtils;
import com.silenteight.serp.governance.featurevector.dto.FeatureVectorView;

import com.google.protobuf.ByteString;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class FeatureVectorFinder {

  private final FeatureVectorRepository repository;

  @Transactional(readOnly = true)
  public Collection<ByteString> findSignatures(ByteString featuresSignature) {
    return repository
        .findByFeaturesSignature(toBase64String(featuresSignature))
        .map(FeatureVectorEntity::getVectorSignature)
        .map(ByteStringUtils::fromBase64String)
        .collect(toList());
  }

  @Transactional(readOnly = true)
  public Collection<FeatureVectorView> findAllByFeaturesSignature(ByteString featuresSignature) {
    return repository
        .findByFeaturesSignature(toBase64String(featuresSignature))
        .map(FeatureVectorEntity::asView)
        .collect(toList());
  }

  @Transactional(readOnly = true)
  public Collection<FeatureVectorView> findByVectorSignatureIn(List<ByteString> vectorSignatures) {
    if (vectorSignatures.isEmpty())
      return emptyList();

    List<String> convertedSignatures =
        vectorSignatures.stream().map(ByteStringUtils::toBase64String).collect(toList());

    return repository.findByVectorSignatureIn(convertedSignatures)
        .map(FeatureVectorEntity::asView)
        .collect(toList());
  }

  public List<Long> findExistingFeatureVectorIds(List<Long> featureVectorIds) {
    return repository.findExistingIds(featureVectorIds);
  }

  public List<ByteString> findExistingFeatureSignatures(List<ByteString> featureSignatures) {
    List<String> convertedSignatures = featureSignatures.stream()
        .map(ByteStringUtils::toBase64String)
        .collect(toList());

    return repository.findExistingFeatureSignatures(convertedSignatures)
        .stream()
        .map(ByteStringUtils::fromBase64String)
        .collect(toList());
  }
}
