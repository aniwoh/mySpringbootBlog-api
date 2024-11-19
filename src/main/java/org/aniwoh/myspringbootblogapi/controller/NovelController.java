package org.aniwoh.myspringbootblogapi.controller;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.Repository.BookshelfRepository;
import org.aniwoh.myspringbootblogapi.entity.BookShelf;
import org.aniwoh.myspringbootblogapi.entity.Result;
import org.aniwoh.myspringbootblogapi.service.NovelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/novel")
@Slf4j
public class NovelController {

    // 定义上传文件的存储路径,value注解不可作用域static
    @Value("${uploadPath}")
    private String UPLOAD_DIR;

    @Resource
    private NovelService novelService;
    @Resource
    private BookshelfRepository bookshelfRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("文件为空，请重新上传！");
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
                return ResponseEntity.ok("文件上传成功: " + fileName);
            }else {
                return ResponseEntity.status(500).body("文件上传失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("文件上传失败：" + e.getMessage());
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
