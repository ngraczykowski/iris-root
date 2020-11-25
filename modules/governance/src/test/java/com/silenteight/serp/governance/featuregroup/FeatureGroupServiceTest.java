package com.silenteight.serp.governance.featuregroup;

import lombok.experimental.UtilityClass;

import com.silenteight.proto.serp.v1.alert.FeatureGroup;
import com.silenteight.proto.serp.v1.alert.FeatureGroupElement;
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.serp.governance.featuregroup.FeatureGroupServiceTest.ProtoMessageUtils.createFeatureGroup;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureGroupServiceTest {

  @InjectMocks
  FeatureGroupService underTest;
  @Mock
  private FeatureVectorService featureVectorService;
  @Mock
  private DecisionTreeFacade decisionTreeService;

  @Test
  void whenStoringGroup_ensuresThatAtLeastOneTreeExists() {
    var matchElement = FeatureGroupElement.getDefaultInstance();
    underTest.storeVectors(createFeatureGroup(matchElement));

    verify(decisionTreeService).ensureAtLeastOneDecisionTreeExists();
  }

  @Test
  void whenStoringGroup_invokesStoreWithValidArgument() {
    var matchElement = FeatureGroupElement.getDefaultInstance();
    var expectedRequest = StoreFeatureVectorsRequest.of(
        matchElement.getFeaturesSignature(), matchElement.getVectorsList());

    underTest.storeVectors(createFeatureGroup(matchElement));

    verify(featureVectorService).store(eq(expectedRequest));
  }

  @UtilityClass
  static class ProtoMessageUtils {

    static FeatureGroup createFeatureGroup(FeatureGroupElement featureGroupElement) {
      return FeatureGroup.newBuilder().setMatchElement(featureGroupElement).build();
    }
  }
}
