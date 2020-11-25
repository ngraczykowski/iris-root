package com.silenteight.serp.governance.featurevector;

import com.silenteight.proto.serp.v1.alert.VectorValue;
import com.silenteight.sep.base.common.protocol.ByteStringUtils;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = { FeatureVectorConfiguration.class})
class FeatureVectorRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private FeatureVectorRepository featureVectorRepository;

  private final Fixtures fixtures = new Fixtures();

  @Test
  void findExistingIds() {
    FeatureVectorEntity entity = fixtures.entity1;
    featureVectorRepository.save(entity);

    assertThat(featureVectorRepository.findExistingIds(List.of(entity.getId(), 66L)))
        .containsOnly(entity.getId());
  }

  @Test
  void findExistingFeatureSignatures() {
    featureVectorRepository.save(fixtures.entity2);

    List<String> result = featureVectorRepository.findExistingFeatureSignatures(
        List.of(fixtures.signature2String, "dummy"));

    assertThat(result).containsOnly(fixtures.signature2String);
  }

  class Fixtures {

    ByteString signature1 = ByteString.copyFromUtf8("abc");

    ByteString signature2 = ByteString.copyFromUtf8("text");

    String signature2String = getStringSignature(signature2);

    List<VectorValue> values = emptyList();

    FeatureVectorEntity entity1 = new FeatureVectorEntity(
        signature1, signature1, values);

    FeatureVectorEntity entity2 = new FeatureVectorEntity(
        signature2, signature2, values);

    private String getStringSignature(ByteString signature) {
      return ByteStringUtils.toBase64String(signature);
    }
  }
}
