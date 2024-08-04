package com.byaffe.learningking.controllers;

import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.services.ArticleService;
import com.byaffe.learningking.services.impl.ArticleServiceImpl;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/articles")
public class ArticlesController {
    @Autowired
    ModelMapper modelMapper;


    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Article>> getById(@PathVariable(name = "id") long id) throws JSONException {
        Article article=ApplicationContextProvider.getBean(ArticleService.class).getInstanceByID(id);
        return ResponseEntity.ok().body(new ResponseObject<>(article));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<Article>> getArticles(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                             @RequestParam(value = "offset", required = true) Integer offset,
                                                             @RequestParam(value = "limit", required = true) Integer limit,
                                                             @RequestParam(value = "sortBy", required = false) String sortBy,
                                                             @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                             @RequestParam(value = "featured", required = false) Boolean featured) throws JSONException {

        Search search = ArticleServiceImpl.generateSearchObjectForArticles(searchTerm).addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (featured!= null) {
            search.addFilterEqual("isFeatured",featured);
        }
        if (StringUtils.isEmpty(sortBy)) {
            search.addSort(sortBy, sortDescending);
        }
        List<Article> articles = ApplicationContextProvider.getBean(ArticleService.class).getInstances(search, offset, limit);
        long count = ApplicationContextProvider.getBean(ArticleService.class).countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(articles, (int) count, offset, limit));
    }




}
