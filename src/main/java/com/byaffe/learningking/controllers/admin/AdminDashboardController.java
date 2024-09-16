package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.dtos.articles.ArticleRequestDTO;
import com.byaffe.learningking.dtos.articles.ArticlesFilterDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.services.ArticleService;
import com.byaffe.learningking.services.impl.ArticleServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/admin/dashboard")
public class AdminDashboardController {
@Autowired
    ModelMapper modelMapper;
    @PostMapping("")
    public ResponseEntity<ResponseObject<Article>> addArticle(@RequestBody ArticleRequestDTO dto) throws JSONException {
Article Article=ApplicationContextProvider.getBean(ArticleService.class).save(dto);
        return ResponseEntity.ok().body(new ResponseObject<>(Article));
    }

    @PostMapping(path = "/multipart", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> uploadCSV(@RequestPart  ArticleRequestDTO dto, @RequestPart(value = "file",required = false) MultipartFile file)  {
      dto.setCoverImage(file);
         ApplicationContextProvider.getBean(ArticleService.class).save(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @PostMapping("/{id}/publish")
    public ResponseEntity<BaseResponse> publishArticle(@PathVariable long id) throws JSONException {
        ApplicationContextProvider.getBean(ArticleService.class).activate(id);
        return ResponseEntity.ok().body(new BaseResponse(true));

    }
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<BaseResponse> unPublishArticle(@PathVariable long id) throws JSONException {
        ApplicationContextProvider.getBean(ArticleService.class).deActivate(id);
        return ResponseEntity.ok().body(new BaseResponse(true));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Article>> getById(@PathVariable(name = "id") long id) throws JSONException {
        Article article=ApplicationContextProvider.getBean(ArticleService.class).getInstanceByID(id);
        return ResponseEntity.ok().body(new ResponseObject<>(article));

    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<BaseResponse> deleteArticle(@PathVariable long id) throws JSONException {
        Article Article=ApplicationContextProvider.getBean(ArticleService.class).getInstanceByID(id);
        ApplicationContextProvider.getBean(ArticleService.class).deleteInstance(Article);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @GetMapping("")
    public ResponseEntity<ResponseList<Article>> getArticles(ArticlesFilterDTO queryParamModel) throws JSONException {

        Search search = ArticleServiceImpl.generateSearchTermsForArticles(queryParamModel.getSearchTerm())
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (queryParamModel.getCategoryId() != null) {
            search.addFilterEqual("category.id", queryParamModel.getCategoryId());
        }

        if (queryParamModel.getFeatured() != null) {
            search.addFilterEqual("isFeatured", queryParamModel.getFeatured());
        }

        if (queryParamModel.getSortBy() != null) {
            search.addSort(queryParamModel.getSortBy(), queryParamModel.getSortDescending());
        }
        List<Article> Articles = ApplicationContextProvider.getBean(ArticleService.class).getInstances(search, queryParamModel.getOffset(), queryParamModel.getLimit());
        long count = ApplicationContextProvider.getBean(ArticleService.class).countInstances(search);


        return ResponseEntity.ok().body(new ResponseList<>(Articles, (int) count, queryParamModel.getOffset(), queryParamModel.getLimit()));

    }

    @GetMapping("/v2/{id}")
    public ResponseEntity<ResponseObject<Article>> getArticleById(@PathVariable("id") Long id) throws JSONException {
         Article article = ApplicationContextProvider.getBean(ArticleService.class).getInstanceByID(id);

        return ResponseEntity.ok().body(new ResponseObject<>(article));
    }



}
