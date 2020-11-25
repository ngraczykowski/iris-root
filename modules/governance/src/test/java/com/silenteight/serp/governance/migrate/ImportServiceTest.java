package com.silenteight.serp.governance.migrate;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.proto.serp.v1.alert.VectorValue;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.serp.common.model.SignatureCalculator;
import com.silenteight.serp.governance.activation.ActivationService;
import com.silenteight.serp.governance.activation.dto.ActivationRequest;
import com.silenteight.serp.governance.branch.BranchService;
import com.silenteight.serp.governance.branch.ConfigureBranchRequest;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupFinder;
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsRequest;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsRequest.Vector;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsResponse;
import com.silenteight.serp.governance.migrate.dto.ImportedDecisionTree;

import com.google.common.io.BaseEncoding;
import com.google.protobuf.ByteString;
import org.apache.http.impl.io.EmptyInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportServiceTest {

  private static final long DT_ID = 12L;
  private static final ByteString VECTOR_SIGNATURE_1 = ByteString.copyFrom(
      BaseEncoding.base64().decode("TNdDsVT0xUv2BDQlkkFFc7r88kc="));
  private static final ByteString FEATURE_SIGNATURE = ByteString.copyFromUtf8("fsig");
  private static final long VECTOR_ID_1 = 123L;
  private static final String NO_DATA_VALUE = "NO_DATA";
  private static final VectorValue NO_DATA_VECTOR =
      VectorValue.newBuilder().setTextValue(NO_DATA_VALUE).build();
  private static final long DECISION_GROUP_ID = 789L;

  @Mock
  private AuditingLogger auditingLogger;
  @Mock
  private FeatureVectorService featureVectorService;
  @Mock
  private BranchService branchService;
  @Mock
  private DecisionTreeFacade decisionTreeService;
  @Mock
  private SignatureCalculator signatureCalculator;
  @Mock
  private DecisionGroupFinder decisionGroupFinder;
  @Mock
  private ActivationService activationService;

  private ImportService importService;

  @BeforeEach
  void setUp() {
    MigrationModuleConfiguration migrationModuleConfiguration = new MigrationModuleConfiguration();

    importService = migrationModuleConfiguration.importService(
        auditingLogger,
        decisionTreeService,
        featureVectorService,
        decisionGroupFinder,
        activationService,
        branchService,
        signatureCalculator);
  }

  @Test
  void emptyFile_noImport() {
    assertThatExceptionOfType(MigrationException.class)
        .isThrownBy(() -> importService.importTree(EmptyInputStream.INSTANCE));
  }

  @Test
  void oneFeatureOneBranchFile_featureImported() {
    testSingleBranchImport("oneFeatureOneBranch.json", "DecisionTree1");
  }

  @Test
  void oneFeatureOneBranchNoSignatureFile_featureImported() {
    testSingleBranchImport("oneFeatureOneBranchNoSignature.json", "DecisionTree1");
  }

  private void testSingleBranchImport(String resourceName, String decisionTreeName) {
    // given
    when(decisionTreeService.getOrCreate(anyString()))
        .thenReturn(DT_ID);
    when(featureVectorService.store(any(StoreFeatureVectorsRequest.class)))
        .thenReturn(new StoreFeatureVectorsResponse(Map.of(VECTOR_SIGNATURE_1, VECTOR_ID_1)));
    when(signatureCalculator.calculateVectorSignature(any(), any())).thenReturn(VECTOR_SIGNATURE_1);
    when(signatureCalculator.calculateFeaturesSignature(anyList())).thenReturn(FEATURE_SIGNATURE);
    when(decisionGroupFinder.findAllDecisionGroupIds())
        .thenReturn(singletonList(DECISION_GROUP_ID));

    //when
    ImportedDecisionTree importedDecisionTree =
        importService.importTree(loadResource(resourceName));

    //then
    assertThat(importedDecisionTree.getId()).isEqualTo(DT_ID);
    assertThat(importedDecisionTree.getName()).isEqualTo(decisionTreeName);

    StoreFeatureVectorsRequest request = new StoreFeatureVectorsRequest(
        FEATURE_SIGNATURE,
        singletonList(new Vector(VECTOR_SIGNATURE_1, singletonList(NO_DATA_VECTOR))));

    verify(featureVectorService).store(request);

    verify(branchService).bulkUpdateOrCreateBranches(
        argThat(this::verifyBulkUpdateRequest));

    verify(activationService)
        .activate(ActivationRequest.of(DT_ID, singletonList(DECISION_GROUP_ID)));
    verify(auditingLogger).log(any());
  }

  private boolean verifyBulkUpdateRequest(List<ConfigureBranchRequest> requestList) {
    return requestList.stream().allMatch(ImportServiceTest::verifyConfigureBranchRequest);
  }

  private static boolean verifyConfigureBranchRequest(ConfigureBranchRequest request) {
    return BranchSolution.BRANCH_FALSE_POSITIVE.equals(request.getSolution())
        && request.getDecisionTreeId() == DT_ID
        && request.getFeatureVectorId() == VECTOR_ID_1;
  }

  @Test
  @Disabled("TODO(bgulowaty): reimplement")
  void manyFeaturesManyBranchesFile_featuresImported() {

  }

  private static InputStream loadResource(String resource) {
    return ImportService.class.getResourceAsStream(resource);
  }
}
