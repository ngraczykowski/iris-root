package com.silenteight.hsbc.bridge.watchlist;

import com.silenteight.hsbc.bridge.watchlist.WatchlistController.NoFileException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class WatchlistControllerTest {

  private static final String NO_FILE_EXCEPTION = "No file was specified";
  private static final String CORE = "coreWatchlist";
  private static final String ALIASES = "nameAliasesWatchlist";
  private static final String KEYWORDS = "keywordsWatchlist";
  private static final String CHECKSUM_1 = "coreChecksum";
  private static final String CHECKSUM_2 = "aliasesChecksum";

  WatchlistController underTest;

  @Mock
  private SaveOriginalWatchlistUseCase saveOriginalWatchlistUseCase;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    underTest = new WatchlistController(saveOriginalWatchlistUseCase);
  }

  @Test
  public void saveFile_shouldVerifyStatus() throws Exception {
    //given
    var mockMvc = standaloneSetup(underTest).build();

    //then
    mockMvc.perform(multipart("/watchlist/v1/upload")
        .file(getMultipartFileMock(CORE))
        .file(getMultipartFileMock(ALIASES))
        .file(getMultipartKeywordsFile(KEYWORDS))
        .file(getMultipartMd5Mock(CHECKSUM_1))
        .file(getMultipartMd5Mock(CHECKSUM_2))
        .accept(MediaType.parseMediaType("multipart/form-data")))
        .andExpect(status().isOk());
  }

  @Test
  public void saveFile_whenFileNameIsEmpty_shouldThrowNoFileException() {
    //given
    var emptyName = " ";
    var mockFile = new MockMultipartFile(emptyName, "some content".getBytes());

    //then
    assertThatThrownBy(() -> underTest.transferFile(mockFile, mockFile, mockFile, mockFile, mockFile))
        .isInstanceOf(NoFileException.class)
        .hasMessageContaining(NO_FILE_EXCEPTION);
  }

  private static MockMultipartFile getMultipartFileMock(String name) {
    return new MockMultipartFile(
        name,
        "someFile.gz",
        MediaType.MULTIPART_FORM_DATA_VALUE,
        "Testing content".getBytes()
    );
  }

  private static MockMultipartFile getMultipartMd5Mock(String name) {
    return new MockMultipartFile(
        name,
        "md5checksum.md5",
        MediaType.APPLICATION_OCTET_STREAM_VALUE,
        "Testing checksum".getBytes()
    );
  }

  private static MockMultipartFile getMultipartKeywordsFile(String name) {
    return new MockMultipartFile(
        name,
        "watch_list_keywords.xml",
        MediaType.APPLICATION_OCTET_STREAM_VALUE,
        "Testing content".getBytes()
    );
  }
}
