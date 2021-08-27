package com.silenteight.sep.filestorage;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.filestorage.minio.FileStorageMinio;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    FileStorageMinio.class
})
@RequiredArgsConstructor
public class MinioFileStorageConfiguration {
}
