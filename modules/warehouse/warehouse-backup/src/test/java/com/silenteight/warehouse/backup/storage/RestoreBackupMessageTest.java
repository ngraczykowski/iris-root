package com.silenteight.warehouse.backup.storage;

import lombok.SneakyThrows;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static com.silenteight.warehouse.backup.storage.ProductionDataIndexRequestFixtures.PRODUCTION_DATA_INDEX_REQUEST_1;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;

class RestoreBackupMessageTest {

  private static final String DATA_API_VERSION = "1.4.0";
  private static final String FILE_NAME = format(
      "productiondataindexrequest_1_v%s",
      DATA_API_VERSION);
  private static final Resource FILE_RESOURCE = new ClassPathResource(FILE_NAME);

  @Test
  @SneakyThrows
  void parseShouldRestoreProductionDataIndex() {
    //given
    byte[] fileContent = getFileAsBytes(FILE_RESOURCE);

    //when
    ProductionDataIndexRequest productionDataIndexRequest =
        ProductionDataIndexRequest.parseFrom(fileContent);

    //then
    assertThat(FILE_RESOURCE.exists()).isTrue();
    assertThat(fileContent).isNotNull();
    assertThat(productionDataIndexRequest).isEqualTo(PRODUCTION_DATA_INDEX_REQUEST_1);
  }

  private byte[] getFileAsBytes(Resource resource) {
    byte[] fileBytes = null;
    try {
      fileBytes = resource.getInputStream().readAllBytes();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return fileBytes;
  }
}
