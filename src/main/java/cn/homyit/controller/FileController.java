package cn.homyit.controller;

import cn.homyit.config.MinioConfig;
import cn.homyit.entity.VO.Result;
import cn.homyit.enums.ResultCodeEnum;
import cn.homyit.exception.BizException;
import cn.homyit.log.SystemLog;
import cn.homyit.utils.MinioUtils;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static cn.homyit.constant.CommonConstants.SERVER_IP;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-27 20:50
 **/
@Slf4j
@RestController
@RequestMapping(value = "/file")
public class FileController {


    @Resource
    private MinioUtils minioUtils;
    @Resource
    private MinioConfig prop;

    //@ApiOperation(value = "查看存储bucket是否存在")
    @GetMapping("/bucketExists")
    public Result<?> bucketExists(@RequestParam("bucketName") String bucketName) {
        if (!minioUtils.bucketExists(bucketName)) {
            throw new BizException(ResultCodeEnum.BUCKET_NOT_EXIST);
        }
        return Result.success();
    }

    //@ApiOperation(value = "创建存储bucket")
    @GetMapping("/makeBucket")
    public Result<?> makeBucket(String bucketName) {
        return Result.success();
        //return R.ok().put("bucketName", minioUtils.makeBucket(bucketName));
    }

    //@ApiOperation(value = "删除存储bucket")
    //@GetMapping("/removeBucket")
    //public R removeBucket(String bucketName) {
    //    return R.ok().put("bucketName", minioUtils.removeBucket(bucketName));
    //}


    //@ApiOperation(value = "获取全部bucket")
    @GetMapping("/getAllBuckets")
    public Result<?> getAllBuckets() {
        List<Bucket> allBuckets = minioUtils.getAllBuckets();
        return Result.success(allBuckets);
    }


    @PostMapping("/upload")
    @SystemLog(businessName = "图片上传返回url")
    public Result<?> upload(@RequestParam("file") MultipartFile file) {
        return Result.success(SERVER_IP + "img/" + minioUtils.upload(file));
    }

    @PostMapping("/batch-upload")
    @SystemLog(businessName = "批量图片上传返回url")
    public Result<?> upload(@RequestParam("files") MultipartFile[] files) {
        List<String> urls = minioUtils.batchUpload(files)
                .stream().map(s -> SERVER_IP + "img/" + s).collect(Collectors.toList());
        return Result.success(urls);
    }

    //@ApiOperation(value = "图片/视频预览")
    //@GetMapping("/preview")
    //public R preview(@RequestParam("fileName") String fileName) {
    //    return R.ok().put("filleName", minioUtil.preview(fileName));
    //}

    @PostMapping("/upload-file")
    @SystemLog(businessName = "文件上传返回文件名称")
    public Result<?> uploadFile(@RequestParam("file") MultipartFile file) {
        return Result.success(minioUtils.upload(file));
    }

    @PostMapping("/batch-upload-file")
    @SystemLog(businessName = "批量文件上传返回名称")
    public Result<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        List<String> fileNames = minioUtils.batchUpload(files);
        return Result.success(fileNames);
    }

    @GetMapping("/download")
    @SystemLog(businessName = "文件下载")
    public Result<?> download(@RequestParam("fileName") String fileName,
                              @RequestParam(value = "assignName", required = false) String assignName,
                              HttpServletResponse response, HttpServletRequest request) {
        minioUtils.download(fileName, assignName, response, request);
        return Result.success();
    }


    //
    //@ApiOperation(value = "删除文件", notes = "根据url地址删除文件")
    //@PostMapping("/delete")
    //public R remove(String url) {
    //    String objName = url.substring(url.lastIndexOf(prop.getBucketName() + "/") + prop.getBucketName().length() + 1);
    //    minioUtil.remove(objName);
    //    return R.ok().put("objName", objName);
    //}

}
