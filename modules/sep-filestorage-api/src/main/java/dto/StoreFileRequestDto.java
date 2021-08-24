package dto;

import lombok.Value;

import java.io.InputStream;

@Value
public class StoreFileRequestDto {

  String fileName;

  InputStream fileContent;
}
