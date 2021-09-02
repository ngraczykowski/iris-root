package com.silenteight.sep.filestorage;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.minio.FileStorageMinioModule;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    FileStorageMinioModule.class
})
@RequiredArgsConstructor
public class MinioFileStorageConfiguration {
}
