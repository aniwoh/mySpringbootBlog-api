package org.aniwoh.myspringbootblogapi.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.Repository.BookshelfRepository;
import org.aniwoh.myspringbootblogapi.entity.BookShelf;
import org.aniwoh.myspringbootblogapi.entity.Result;
import org.aniwoh.myspringbootblogapi.entity.ResultCode;
import org.aniwoh.myspringbootblogapi.service.NovelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/novel")
@Slf4j
@SaCheckRole("admin")
public class NovelController {

    // 定义上传文件的存储路径,value注解不可作用域static
    @Value("${uploadPath}")
    private String UPLOAD_DIR;

    @Resource
    private NovelService novelService;

    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return Result.error(ResultCode.ERROR,"文件为空，请重新上传！");
        }
        // 获取 MIME 类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("text/plain")) {
            return Result.error(ResultCode.ERROR,"类型错误，请重新上传！");
        }

        try {
            // 确保上传目录存在
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件
            String fileName = file.getOriginalFilename();
            if (fileName!=null){
                BookShelf bookShelf = new BookShelf();
                bookShelf.setTitle(fileName.split("\\.")[0]);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
                String timestamp = sdf.format(new Date());
                Path filePath = uploadPath.resolve(timestamp+fileName);
                file.transferTo(filePath.toFile());
                bookShelf.setFilePath(filePath.toString());
                novelService.parseNovel(bookShelf);
                return Result.success("文件上传成功: " + fileName);
            }else {
                return Result.error(ResultCode.ERROR,"文件上传失败");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return Result.error(ResultCode.ERROR,e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result list(){
        return Result.success(novelService.list());
    }

    @GetMapping("/getChapterProcessing")
    public Result getChapterProcessing(@RequestParam("id") String id){
        return Result.success(novelService.getChapterProcessing(id));
    }

    @GetMapping("/getChapterList")
    public Result getChapterList(@RequestParam("id") String id){
        return Result.success(novelService.getChapterList(id));
    }

    @GetMapping("getChapter")
    public Result getChapter(@RequestParam("id") String id){
        return Result.success(novelService.getChapter(id));
    }
}
