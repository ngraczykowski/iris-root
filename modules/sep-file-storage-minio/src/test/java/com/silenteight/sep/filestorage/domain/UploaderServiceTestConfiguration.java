package com.silenteight.sep.filestorage.domain;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    UploaderModule.class
})
@RequiredArgsConstructor
public class UploaderServiceTestConfiguration {
}
