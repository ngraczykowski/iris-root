package com.silenteight.serp.governance.migrate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.featureset.FeatureSetFinder;
import com.silenteight.serp.governance.featureset.dto.FeatureSetViewDto;
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder;
import com.silenteight.serp.governance.featurevector.dto.FeatureVectorView;
import com.silenteight.serp.governance.migrate.dto.ExportMatchGroupsRequest;

import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.io.BaseEncoding;
import com.google.protobuf.ByteString;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.silenteight.serp.governance.migrate.MigrateConstants.VECTOR_SIGNATURE_FEATURE;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ExportService {

  private static final MapType MAP_TYPE;

  static {
    TypeFactory typeFactory = JsonConversionHelper.INSTANCE.objectMapper().getTypeFactory();
    MAP_TYPE = typeFactory.constructMapType(
        TreeMap.class,
        typeFactory.constructType(String.class),
        typeFactory.constructType(Object.class));
  }

  private final FeatureSetFinder featureSetFinder;
  private final FeatureVectorFinder featureVectorFinder;
  private final TimeSource timeSource;

  @Transactional(readOnly = true)
  @NotNull
  public Map<String, Object> exportMatchGroups(@NonNull ExportMatchGroupsRequest request) {
    ByteString featuresSignature = request.getFeaturesSignature();
    String decisionTreeName = request
        .getDecisionTreeName()
        .orElseGet(() -> UUID.randomUUID().toString());

    ModelSchemaMigration schemaMigration = createSchemaMigration(featuresSignature);
    DecisionTreeMigration decisionTreeMigration = createTreeMigration(
        featuresSignature, decisionTreeName);

    DecisionTreeMigrationRoot migrationRoot = new DecisionTreeMigrationRoot(
        MigrationMetadata.build(timeSource.now()), schemaMigration, decisionTreeMigration);

    return JsonConversionHelper.INSTANCE.objectMapper().convertValue(migrationRoot, MAP_TYPE);
  }

  @NotNull
  private ModelSchemaMigration createSchemaMigration(ByteString featuresSignature) {
    FeatureSetViewDto featureSet = featureSetFinder.getByFeaturesSignature(featuresSignature);
    List<String> features = featureSet.getFeatures();

    List<String> schema = new ArrayList<>(features.size() + 1);

    schema.add(VECTOR_SIGNATURE_FEATURE);
    schema.addAll(features);

    return new ModelSchemaMigration(schema);
  }

  @NotNull
  private DecisionTreeMigration createTreeMigration(
      ByteString featuresSignature,
      String decisionTreeName) {

    Collection<FeatureVectorView> featureVectors =
        featureVectorFinder.findAllByFeaturesSignature(featuresSignature);

    List<BranchMigration> branches = featureVectors
        .stream()
        .map(ExportService::createBranchMigration)
        .collect(toList());

    return new DecisionTreeMigration(999L, decisionTreeName, branches);
  }

  @NotNull
  private static BranchMigration createBranchMigration(FeatureVectorView featureVector) {
    long id = featureVector.getId();
    String vectorSignature =
        BaseEncoding.base64().encode(featureVector.getVectorSignature().toByteArray());

    List<String> values = featureVector.getValues();

    List<String> matchGroupValues = new ArrayList<>(values.size() + 1);

    matchGroupValues.add(vectorSignature);
    matchGroupValues.addAll(values);

    return new BranchMigration(id, id, "NO_DECISION", true, matchGroupValues);
  }
}
