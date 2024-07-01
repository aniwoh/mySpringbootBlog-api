package org.aniwoh.myspringbootblogapi.mapper;

import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

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
    List<Map<String, Object>> findAllArticlesWithTags();
    @Select("select at.* from (select a.* , GROUP_CONCAT(t.tagName) as tagNames from me_article a  left join me_article_tag at on a.id = at.articleId left join me_tags t on at.tagId = t.id group by a.id) at where at.id = #{id}")
    Map<String,Object> findArticleByIdWithTags(int id);

    @Select("select * from me_article where id = #{id}")
    Article findArticleById(int id);

    @Select("select * from me_tags")
    List<Tag> findAllTags();
}
