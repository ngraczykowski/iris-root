package com.silenteight.serp.governance.file.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;

@Transactional
@ContextConfiguration(classes = { FileReferenceTestConfiguration.class })
@TestPropertySource("classpath:data-test.properties")
@AutoConfigureJsonTesters
class FileReferenceServiceTest extends BaseDataJpaTest {

  @Autowired
  FileReferenceService underTest;

  @Autowired
  FileReferenceRepository repository;

  private static final String UPLOADER_NAME = "John Doe";
  private static final String FILE_NAME = "test.pdf";
  private static final UUID FILE_ID = randomUUID();

  @Test
  void shouldSaveFileReference() {
    //given
    underTest.saveFileReference(FILE_ID, UPLOADER_NAME, FILE_NAME, 124L, "application/pdf");

    //when
    FileReference fileReferenceByFileId = repository.getFileReferenceByFileId(FILE_ID);

    //then
    assertThat(fileReferenceByFileId.getFileId()).isEqualTo(FILE_ID);
    assertThat(fileReferenceByFileId.getOriginalName()).isEqualTo(FILE_NAME);
  }
}
