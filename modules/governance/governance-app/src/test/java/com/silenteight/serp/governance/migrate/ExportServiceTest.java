package com.silenteight.serp.governance.migrate;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.featureset.FeatureSetFinder;
import com.silenteight.serp.governance.featureset.dto.FeatureSetViewDto;
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder;
import com.silenteight.serp.governance.featurevector.dto.FeatureVectorView;
import com.silenteight.serp.governance.migrate.dto.ExportMatchGroupsRequest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.protobuf.ByteString;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.silenteight.sep.base.testing.utils.ByteStringTestUtils.createSignature;
import static com.silenteight.sep.base.testing.utils.ByteStringTestUtils.randomSignature;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportServiceTest {

  private static final String TREE_NAME = "decisionTree";
  private static final ByteString FEATURES_SIGNATURE = randomSignature();
  private static final ObjectMapper OBJECT_MAPPER = JsonConversionHelper.INSTANCE.objectMapper();
  private static final MapType MAP_TYPE = OBJECT_MAPPER
      .getTypeFactory()
      .constructMapType(TreeMap.class, String.class, Object.class);
  public static final long FEATURE_SET_ID = 1L;

  @Mock
  private FeatureSetFinder featureSetFinder;
  @Mock
  private FeatureVectorFinder featureVectorFinder;
  @Mock
  private TimeSource timeSource;
  @InjectMocks
  private ExportService service;

  @Test
  void exportsCorrectly() throws IOException {
    when(timeSource.now()).thenReturn(Instant.ofEpochMilli(1566469674663L));
    mockFeatureSet(of("DOCNO_AGENT", "DATE_AGENT_NORMAL", "NAME_AGENT"));
    mockFeatureVectors(
        featureVector(4, createSignature("abc"), "NO_DATA", "OUT_OF_RANGE", "WEAK_MATCH"),
        featureVector(5, createSignature("bca"), "NO_DATA", "NO_DATA", "WEAK_MATCH"),
        featureVector(6, createSignature("cab"), "NO_DATA", "OUT_OF_RANGE", "NO_DATA"));

    Map<String, Object> expected = loadAndParseFile("expectedExportedTree.json");

    ExportMatchGroupsRequest request = exportRequest(TREE_NAME, FEATURES_SIGNATURE);
    Map<String, Object> actual = service.exportMatchGroups(request);

    assertThat(actual).isEqualTo(expected);
  }

  private static FeatureVectorView featureVector(
      long id, ByteString signature, String... features) {
    return FeatureVectorView
        .builder()
        .id(id)
        .values(of(features))
        .featuresSignature(FEATURES_SIGNATURE)
        .vectorSignature(signature)
        .build();
  }

  private void mockFeatureVectors(FeatureVectorView... featureVectors) {
    when(featureVectorFinder.findAllByFeaturesSignature(FEATURES_SIGNATURE))
        .thenReturn(of(featureVectors));
  }

  private void mockFeatureSet(List<String> features) {
    when(featureSetFinder.getByFeaturesSignature(FEATURES_SIGNATURE))
        .thenReturn(new FeatureSetViewDto(FEATURE_SET_ID, features));
  }

  private Map<String, Object> loadAndParseFile(String path) throws IOException {
    useLongForIntsOnDeserialization();
    String contents = loadFileAsString(path, getClass());
    return OBJECT_MAPPER.readValue(contents, MAP_TYPE);
  }

  private static void useLongForIntsOnDeserialization() {
    OBJECT_MAPPER.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
  }

  private static String loadFileAsString(String path, Class<?> klass) throws IOException {
    try (InputStream resourceStream = klass.getResourceAsStream(path)) {
      return IOUtils.toString(resourceStream, StandardCharsets.UTF_8);
    }
  }

  private static ExportMatchGroupsRequest exportRequest(
      String treeName,
      ByteString featuresSignature) {

    return ExportMatchGroupsRequest
        .builder()
        .decisionTreeName(treeName)
        .featuresSignature(featuresSignature).build();
  }
}
