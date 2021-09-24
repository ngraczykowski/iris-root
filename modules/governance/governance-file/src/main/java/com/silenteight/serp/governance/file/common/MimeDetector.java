package com.silenteight.serp.governance.file.common;

import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.file.validation.exception.FileBrokenContentException;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;

@NoArgsConstructor
public final class MimeDetector {

  private static final Tika MIME_DETECTOR = new Tika();

  public static String getMimeType(MultipartFile file) {
    try {
      InputStream inputStream = file.getInputStream();
      String mimeType = MIME_DETECTOR.detect(inputStream);
      inputStream.close();
      return mimeType;
    } catch (IOException e) {
      throw new FileBrokenContentException(
          format("Content of file %s is broken", file.getOriginalFilename()), e);
    }
  }
}
