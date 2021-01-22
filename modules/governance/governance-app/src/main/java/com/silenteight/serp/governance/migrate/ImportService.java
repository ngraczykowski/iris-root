package com.silenteight.serp.governance.migrate;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.proto.serp.v1.alert.FeatureGroupVector;
import com.silenteight.proto.serp.v1.alert.VectorValue;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.serp.governance.activation.ActivationService;
import com.silenteight.serp.governance.activation.dto.ActivationRequest;
import com.silenteight.serp.governance.branch.BranchService;
import com.silenteight.serp.governance.branch.ConfigureBranchRequest;
import com.silenteight.serp.governance.common.signature.SignatureCalculator;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupFinder;
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsRequest;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsResponse;
import com.silenteight.serp.governance.migrate.dto.ImportedDecisionTree;

import com.google.common.io.BaseEncoding;
import com.google.protobuf.ByteString;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;

import static com.silenteight.sep.base.common.logging.LogContextUtils.logObject;
import static com.silenteight.serp.governance.migrate.MigrateConstants.VECTOR_SIGNATURE_FEATURE;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

@Builder
@Slf4j
public class ImportService {

  private final AuditingLogger auditingLogger;
  private final DecisionTreeFacade decisionTreeService;
  private final FeatureVectorService featureVectorService;
  private final DecisionGroupFinder decisionGroupFinder;
  private final ActivationService activationService;
  private final BranchService branchService;
  private final SignatureCalculator calculator;

  private static final String BRANCH_SOLUTION_PREFIX = "BRANCH_";

  public ImportedDecisionTree importTree(InputStream inputStream) {
    Importer importer = new Importer(getParsedModel(inputStream));

    return importer.doImport();
  }

  @NotNull
  private static DecisionTreeMigrationRoot getParsedModel(InputStream inputStream) {
    try {
      String modelJson = IOUtils.toString(inputStream, UTF_8);
      if (modelJson.isEmpty())
        throw new MigrationException("Empty json migration file");

      return DecisionTreeModelParser.parseModel(modelJson);
    } catch (IOException e) {
      throw new MigrationException(e);
    }
  }

  private class Importer {

    private final DecisionTreeMigrationRoot parsedModel;
    private final ByteString featuresSignature;

    private long decisionTreeId;
    private StoreFeatureVectorsResponse featureVectorsResponse;

    private Importer(DecisionTreeMigrationRoot parsedModel) {
      this.parsedModel = parsedModel;

      List<String> schema = parsedModel.getModelSchemaFeatures();
      List<String> featureValues = schema.subList(getFeatureStartIndex(), schema.size());
      featuresSignature = calculator.calculateFeaturesSignature(featureValues);
    }

    ImportedDecisionTree doImport() {
      String decisionTreeName = parsedModel.getDecisionTreeName();
      decisionTreeId = decisionTreeService.getOrCreate(decisionTreeName);
      logObject("decisionTree.id", decisionTreeId);
      logObject("decisionTree.name", decisionTreeName);
      if (log.isDebugEnabled())
        log.debug(
            "Importing decision tree: decisionTreeId={}, decisionTreeName={}",
            decisionTreeId,
            decisionTreeName);

      activateAllDecisionGroupsForThisDecisionTree();

      featureVectorsResponse = featureVectorService.store(buildRequest());

      // BS
      var eventId = UUID.randomUUID();
      var auditDataDto = AuditDataDto.builder()
          .correlationId(eventId)
          .eventId(eventId)
          .timestamp(Timestamp.from(Instant.now()))
          .type(this.getClass().getSimpleName())
          .entityId(valueOf(decisionTreeId))
          .entityClass("DecisionTree")
          .entityAction("IMPORT")
          .build();

      auditingLogger.log(auditDataDto);

      updateOrCreateBranches(eventId);

      return new ImportedDecisionTree(decisionTreeId, decisionTreeName);
    }

    private void activateAllDecisionGroupsForThisDecisionTree() {
      Collection<Long> decisionGroupIds = decisionGroupFinder.findAllDecisionGroupIds();
      activationService.activate(ActivationRequest.of(decisionTreeId, decisionGroupIds));
    }

    private StoreFeatureVectorsRequest buildRequest() {
      List<FeatureGroupVector> featureGroupVectors = parsedModel
          .getBranches()
          .stream()
          .map(this::createFeatureGroupVector)
          .collect(toList());

      return StoreFeatureVectorsRequest.of(featuresSignature, featureGroupVectors);
    }

    private FeatureGroupVector createFeatureGroupVector(BranchMigration branchMigration) {
      ByteString vectorSignature = calculateVectorSignature(branchMigration);

      String vectorSignatureBase64 = BaseEncoding.base64().encode(vectorSignature.toByteArray());

      maybeValidateVectorSignature(branchMigration, vectorSignatureBase64);

      List<VectorValue> branchValues = branchMigration.getVectorValues();
      List<VectorValue> featureValues =
          branchValues.subList(getFeatureStartIndex(), branchValues.size());

      return FeatureGroupVector
          .newBuilder()
          .setVectorSignature(vectorSignature)
          .addAllValues(featureValues)
          .build();
    }

    private void maybeValidateVectorSignature(
        BranchMigration branchMigration,
        String vectorSignatureBase64) {

      if (!hasVectorSignature())
        return;

      String branchSignatureBase64 = branchMigration.getFeatureValues().get(0);
      if (!vectorSignatureBase64.equals(branchSignatureBase64)) {
        log.warn(
            "Feature vector signature does not match: imported={}, calculated={}",
            branchSignatureBase64,
            vectorSignatureBase64);
      }
    }

    void updateOrCreateBranches(UUID correlationId) {
      List<ConfigureBranchRequest> configureBranchRequests = parsedModel
          .getBranches()
          .stream()
          .map(branchMigration -> createRequest(correlationId, branchMigration))
          .collect(toList());

      branchService.bulkUpdateOrCreateBranches(configureBranchRequests);
    }

    private ConfigureBranchRequest createRequest(
        UUID correlationId, BranchMigration branchMigration) {
      ByteString vectorSignature = calculateVectorSignature(branchMigration);

      return ConfigureBranchRequest.builder()
          // BS
          .correlationId(correlationId)
          .decisionTreeId(decisionTreeId)
          .featureVectorId(featureVectorsResponse.getVectorId(vectorSignature))
          .solution(BranchSolution.valueOf(BRANCH_SOLUTION_PREFIX + branchMigration.getSolution()))
          .enabled(branchMigration.isEnabled())
          .build();
    }

    private ByteString calculateVectorSignature(BranchMigration branchMigration) {
      List<String> branchValues = branchMigration.getFeatureValues();

      int fromFeature = getFeatureStartIndex();
      if (branchValues.size() <= fromFeature)
        throw new IllegalArgumentException();

      List<String> featureValues = branchValues.subList(fromFeature, branchValues.size());

      return calculator.calculateVectorSignature(featuresSignature, featureValues);
    }

    private int getFeatureStartIndex() {
      return hasVectorSignature() ? 1 : 0;
    }

    private boolean hasVectorSignature() {
      return parsedModel.getFeatures().contains(VECTOR_SIGNATURE_FEATURE);
    }
  }
}
