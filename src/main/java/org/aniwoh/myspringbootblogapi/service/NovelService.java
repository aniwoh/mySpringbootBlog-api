package org.aniwoh.myspringbootblogapi.service;

import cn.hutool.core.io.CharsetDetector;
import cn.hutool.core.io.FileUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.Repository.BookshelfRepository;
import org.aniwoh.myspringbootblogapi.Repository.ChapterRepository;
import org.aniwoh.myspringbootblogapi.Repository.ReadingProcessRepository;
import org.aniwoh.myspringbootblogapi.entity.BookShelf;
import org.aniwoh.myspringbootblogapi.entity.BookShelfVo;
import org.aniwoh.myspringbootblogapi.entity.Chapter;
import org.aniwoh.myspringbootblogapi.entity.ReadingProcess;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class NovelService {

    @Resource
    private BookshelfRepository bookshelfRepository;

    @Resource
    private ChapterRepository chapterRepository;

    @Resource
    private ReadingProcessRepository readingProcessRepository;

    @Async // 异步执行解析任务
    public void parseNovel(BookShelf bookShelf) throws IOException {
        bookShelf.setCreateDate(LocalDateTime.now());
        bookShelf.setUpdateDate(LocalDateTime.now());
        bookShelf.setStatus("UPLOADED");
        bookshelfRepository.save(bookShelf);

        // 异步解析文件
        doParse(bookShelf);
    }

    public List<BookShelfVo> list(){
        List<BookShelf> bookShelves = bookshelfRepository.findAll();
        List<BookShelfVo> bookShelfVos = new ArrayList<>();
        bookShelves.stream().peek(bookShelf -> {
            BookShelfVo bookShelfVo = new BookShelfVo();
            BeanUtils.copyProperties(bookShelf,bookShelfVo);
            Optional<Chapter> optional =chapterRepository.findTopByNovelIdOrderByIndexDesc(bookShelf.getId());
            optional.ifPresent(bookShelfVo::setLastChapter);
            ReadingProcess readingProcess=readingProcessRepository.findByNovelId(bookShelf.getId());
            chapterRepository.findById(readingProcess.getChapterId()).ifPresent(bookShelfVo::setProcessChapter);
            bookShelfVos.add(bookShelfVo);
        });
        return bookShelfVos;
    }

    private void doParse(BookShelf book) {
        try {
            // 更新状态为 PARSING
            book.setStatus("PARSING");
            BookShelf savedBook = bookshelfRepository.save(book);
            String charsetName = detectEncoding(savedBook.getFilePath());
            String content = readFileContent(Path.of(savedBook.getFilePath()),charsetName);
            List<Chapter> chapters = parseChapters(content,savedBook.getId());
            chapterRepository.saveAll(chapters);
            // 更新状态为 COMPLETED
            book.setStatus("COMPLETED");
            bookshelfRepository.save(book);
        } catch (Exception e) {
            // 更新状态为 FAILED
            book.setStatus("FAILED");
            bookshelfRepository.save(book);
        }
    }

    private String readFileContent(Path path,String charsetName) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path.toString()), charsetName)
        )
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        log.info(path+"内容获取成功");
        return content.toString();
    }

    private List<Chapter> parseChapters(String content,String novelId) {
        List<Chapter> chapters = new ArrayList<>();

        // 使用正则表达式按章节拆分
        Pattern pattern = Pattern.compile("第[一二三四五六七八九十百千零0-9]+章.*");
        Matcher matcher = pattern.matcher(content);

        int start = 0; // 当前章节的开始位置
        String title = null;
        Integer index = 0;

        while (matcher.find()) {
            if (title != null) {
                // 获取章节内容
                String chapterContent = content.substring(start, matcher.start()).trim();
                Chapter chapter = new Chapter();
                chapter.setTitle(title.trim());
                chapter.setContent(chapterContent);
                chapter.setNovelId(novelId);
                chapter.setIndex(index);
                index++;
                chapters.add(chapter);
            }
            title = matcher.group(); // 匹配到的章节标题
            start = matcher.end();   // 下一章节的开始位置
        }

        // 添加最后一章
        if (title != null) {
            String chapterContent = content.substring(start).trim();
            Chapter chapter = new Chapter();
            chapter.setTitle(title);
            chapter.setContent(chapterContent);
            chapter.setNovelId(novelId);
            chapters.add(chapter);
        }
        return chapters;
    }

    private String detectEncoding(String filePath) throws IOException {
        UniversalDetector detector = new UniversalDetector(null);
        byte[] bytes = FileUtil.readBytes(new File(filePath));
        detector.handleData(bytes,0,2000);
        detector.dataEnd();
        return detector.getDetectedCharset();
    }
}