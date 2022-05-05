package com.silenteight.sep.filestorage;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.minio.S3FileStorageMinioModule;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    S3FileStorageMinioModule.class
})
@RequiredArgsConstructor
public class S3MinioFileStorageConfiguration {
}
