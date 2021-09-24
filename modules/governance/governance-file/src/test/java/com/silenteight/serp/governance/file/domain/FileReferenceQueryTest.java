package com.silenteight.serp.governance.file.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@ContextConfiguration(classes = { FileReferenceTestConfiguration.class })
@TestPropertySource("classpath:data-test.properties")
@AutoConfigureJsonTesters
class FileReferenceQueryTest extends BaseDataJpaTest {

  private static final String UPLOADER_NAME = "John Doe";
  private static final String FILE_NAME = "test.pdf";
  private static final UUID FILE_ID = randomUUID();

  @Autowired
  FileReferenceQuery underTest;

  @Autowired
  FileReferenceRepository repository;

  @Test
  void shouldReturnFileDescriptionById() {
    //given
    persistData();
    //when
    FileReferenceDto fileDescription = underTest.get(FILE_ID.toString());
    //then
    assertThat(fileDescription.getFileId()).isEqualTo(FILE_ID);
    assertThat(fileDescription.getUploaderName()).isEqualTo(UPLOADER_NAME);
  }

  private void persistData() {
    FileReference fileReference = new FileReference();
    fileReference.setFileId(FILE_ID);
    fileReference.setOriginalName(FILE_NAME);
    fileReference.setUploaderName(UPLOADER_NAME);
    fileReference.setSize(1234L);
    fileReference.setMimeType("application/pdf");
    repository.save(fileReference);
  }
}
