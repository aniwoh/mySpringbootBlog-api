package org.aniwoh.myspringbootblogapi.mapper;

import org.aniwoh.myspringbootblogapi.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Select("select * from me_article")
    List<Article> findAllArticles();

}
