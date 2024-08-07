package org.aniwoh.myspringbootblogapi.service.impl;

import org.aniwoh.myspringbootblogapi.Repository.ArticleTagRepository;
import org.aniwoh.myspringbootblogapi.Repository.TagRepository;
import org.aniwoh.myspringbootblogapi.entity.ArticleTag;
import org.aniwoh.myspringbootblogapi.entity.Tag;
import org.aniwoh.myspringbootblogapi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ArticleTagRepository articleTagRepository;
    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> findTagsByArticleId(String articleId) {
        List<ArticleTag> articleTags = articleTagRepository.findArticleTagsByArticleId(articleId);
        List<Tag> tagList = new ArrayList<>();
        articleTags.forEach(articleTag -> {
            String tagId = articleTag.getTagId();
            Tag tag = tagRepository.findTagById(tagId);
            tagList.add(tag);
        });
        return tagList;
    }

    @Override
    public List<String> findTagNamesByArticleId(String articleId) {
        List<ArticleTag> articleTags = articleTagRepository.findArticleTagsByArticleId(articleId);
        List<String> tagNamesList = new ArrayList<>();
        articleTags.forEach(articleTag -> {
            String tagId = articleTag.getTagId();
            Tag tag = tagRepository.findTagById(tagId);
            tagNamesList.add(tag.getTagName());
        });
        return tagNamesList;
    }

    @Override
    public Optional<Tag> findTagByTagNames(String tagNames){
        return tagRepository.findTagByTagName(tagNames);
    }

    @Override
    public void insertTag(Tag tag){
        tagRepository.save(tag);
    }

    @Override
    public void insertArticleTag(ArticleTag articleTag){
        articleTagRepository.save(articleTag);
    }
}
