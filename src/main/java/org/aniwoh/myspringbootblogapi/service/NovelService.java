package org.aniwoh.myspringbootblogapi.service;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.Repository.BookshelfRepository;
import org.aniwoh.myspringbootblogapi.Repository.ChapterRepository;
import org.aniwoh.myspringbootblogapi.Repository.ReadingProcessRepository;
import org.aniwoh.myspringbootblogapi.entity.*;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    @Async("customTaskExecutor") // 异步执行解析任务
    public void parseNovel(BookShelf bookShelf) throws IOException {
        bookShelf.setCreateDate(LocalDateTime.now());
        bookShelf.setUpdateDate(LocalDateTime.now());
        bookShelf.setStatus("UPLOADED");
        bookshelfRepository.save(bookShelf);
        log.info("开始解析文件：{}", bookShelf.getFilePath());
        // 异步解析文件
        doParse(bookShelf);
    }

    public List<BookShelfVo> list(){
        List<BookShelf> bookShelves = bookshelfRepository.findAll();
        List<BookShelfVo> bookShelfVos = new ArrayList<>();
        bookShelves.forEach(bookShelf -> {
            BookShelfVo bookShelfVo = new BookShelfVo();
            BeanUtils.copyProperties(bookShelf,bookShelfVo);
            Chapter chapter =chapterRepository.findFirstByNovelIdOrderByIndexDesc(bookShelf.getId());
            bookShelfVo.setChapterCount(chapterRepository.findByNovelId(bookShelf.getId()).size());
            bookShelfVo.setLastChapter(chapter);
            ReadingProcess readingProcess=readingProcessRepository.findByNovelId(bookShelf.getId());
            if (readingProcess!=null){
                chapterRepository.findById(readingProcess.getChapterId()).ifPresent(bookShelfVo::setProcessChapter);
            }
            bookShelfVos.add(bookShelfVo);
        });
        return bookShelfVos;
    }

    public ReadingProcess getChapterProcessing(String id){
        return readingProcessRepository.findByNovelId(id);
    }

    public List<ChapterListVo> getChapterList(String novelId) {
        List<ChapterListVo> chapterListVos = new ArrayList<>();
        chapterRepository.findByNovelId(novelId).forEach(chapter -> {
            ChapterListVo chapterListVo = new ChapterListVo();
            BeanUtils.copyProperties(chapter,chapterListVo);
            chapterListVos.add(chapterListVo);
        });
        return chapterListVos;
    }

    public Chapter getChapter(String chapterId) {
        return chapterRepository.findById(chapterId).orElse(null);
    }

    public Boolean delete(String id) {
        bookshelfRepository.deleteById(id);
        chapterRepository.deleteChaptersByNovelId(id);
        readingProcessRepository.deleteReadingProcessesByNovelId(id);
        return true;
    }

    private void doParse(BookShelf book) {
        try {
            // 更新状态为 PARSING
            book.setStatus("PARSING");
            BookShelf savedBook = bookshelfRepository.save(book);
            String charsetName = detectEncoding(savedBook.getFilePath());
            String content = readFileContent(Path.of(savedBook.getFilePath()),charsetName);
            // 获取作者
            String author = getAuthor(content);
            List<Chapter> chapters = parseChapters(content,savedBook.getId());
            chapterRepository.saveAll(chapters);
            // 更新状态为 COMPLETED
            book.setStatus("COMPLETED");
            book.setAuthor(author);
            bookshelfRepository.save(book);
        } catch (Exception e) {
            // 更新状态为 FAILED
            book.setStatus("FAILED");
            log.error(e.getMessage());
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
        log.info("{}内容获取成功", path);
        return content.toString();
    }

    private String getAuthor(String content) {
        Pattern pattern = Pattern.compile("作者[：:](.*?)\n");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private List<Chapter> parseChapters(String content,String novelId) {
        List<Chapter> chapters = new ArrayList<>();

        // 使用正则表达式按章节拆分
        Pattern pattern = Pattern.compile(getTocRule(content),Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(content);

        int lastMatchEnd = 0;
        Integer index = 0;

        // 遍历匹配章节标题
        while (matcher.find()) {
            // 获取当前章节标题
            String title = matcher.group();

            // 如果有前一个章节，提取其内容
            if (!chapters.isEmpty()) {
                Chapter lastChapter = chapters.get(chapters.size() - 1);
                lastChapter.setContent(content.substring(lastMatchEnd, matcher.start()).trim());
            }
            Chapter chapter = new Chapter();
            chapter.setTitle(title);
            chapter.setNovelId(novelId);
            chapter.setContent("");
            chapter.setIndex(index);
            index++;
            chapters.add(chapter);
            lastMatchEnd = matcher.end();
        }

        // 最后一章的内容提取
        if (!chapters.isEmpty() && lastMatchEnd < content.length()) {
            chapters.get(chapters.size() - 1).setContent(content.substring(lastMatchEnd).trim());
        }
        return chapters;
    }

    private String detectEncoding(String filePath) {
        UniversalDetector detector = new UniversalDetector(null);
        byte[] bytes = FileUtil.readBytes(new File(filePath));
        if (bytes.length == 0) {
            throw new RuntimeException("文件内容为空");
        }
        int length = Math.min(bytes.length, 2000);
        detector.handleData(bytes,0,length);
        detector.dataEnd();
        return detector.getDetectedCharset();
    }

    private String getTocRule(String content) {
        try {
            ClassPathResource resource = new ClassPathResource("defaultData/txtRule.json");
            String jsonContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            List<TxtRule> txtRules = JSON.parseArray(jsonContent, TxtRule.class);
            Collections.reverse(txtRules);
            int maxCs = 0;
            String tocPattern = "";
            for (TxtRule txtRule : txtRules) {
                if (txtRule.getEnable()) {
                    Pattern pattern = Pattern.compile(txtRule.getRule(), Pattern.MULTILINE);
                    Matcher matcher = pattern.matcher(content);
                    int cs = 0;
                    while (matcher.find()) {
                        cs++;
                    }
                    if (maxCs < cs) {
                        maxCs = cs;
                        tocPattern = txtRule.getRule();
                    }
                }
            }
            return tocPattern;
        }catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }


}