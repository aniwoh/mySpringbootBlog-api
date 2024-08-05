package org.aniwoh.myspringbootblogapi.mapper;

import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.entity.Tag;
import org.aniwoh.myspringbootblogapi.vo.ArticleVo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleMapper {
    @Select("select * from me_article")
    List<Article> findAllArticles();

    @Select("select a.id,a.author,a.title,a.createDate,a.commentCounts,a.thumbsUpCounts,a.viewCounts , GROUP_CONCAT(t.tagName) as tagNames " +
            "from me_article a " +
            "left join me_article_tag at on a.id = at.articleId " +
            "left join me_tags t on at.tagId = t.id " +
            "group by a.id")
    List<ArticleVo> findAllArticlesWithTags();

    @Select("select at.* from (select a.* , GROUP_CONCAT(t.tagName) as tagNames from me_article a  left join me_article_tag at on a.id = at.articleId left join me_tags t on at.tagId = t.id group by a.id) at where at.id = #{id}")
    ArticleVo findArticleByIdWithTags(int id);

    @Select("select * from me_article where id = #{id}")
    Article findArticleById(int id);

    @Select("select * from me_tags")
    List<Tag> findAllTags();

    @Insert("INSERT INTO me_article (title, author, body, createDate) VALUES (#{title}, #{author}, #{body}, #{createDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertArticle(Article article);

    @Update("UPDATE me_article SET viewCounts = viewCounts + 1 WHERE id = #{id}")
    void incrementViewCount(@Param("id") int id);

    @Select("select * from me_tags where tagName=#{name}")
    Tag findTagByName(String name);

    @Insert("INSERT INTO me_tags (tagName) VALUES (#{tagName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertTag(Tag tag);

    @Insert("insert into learnspringweb.me_article_tag(articleId,tagId) values (#{articleId}, #{tagId})")
    void insertArticleTag(Integer articleId, Integer tagId);

    @Select("SELECT t.tagName FROM me_tags t JOIN me_article_tag at ON t.id = at.tagId WHERE at.articleId = #{articleId}")
    List<String> findTagsByArticleId(Integer articleId);
}
