package com.wang.scaffold.sharded.feign;

import com.wang.scaffold.file.support.SimpleUploadFileEntity;
import com.wang.scaffold.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "files")
public interface IFilesFeign {

    @PostMapping("/files/persist/merge")
    BaseResponse<?> merge(@RequestBody List<SimpleUploadFileEntity> request);

    @PostMapping("/files/persist/remove")
    BaseResponse<?> remove(@RequestBody List<SimpleUploadFileEntity> request);
}
