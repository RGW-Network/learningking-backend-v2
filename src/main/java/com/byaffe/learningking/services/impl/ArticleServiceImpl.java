package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.dtos.courses.ArticleRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.services.ArticleService;
import com.byaffe.learningking.services.CategoryService;
import com.byaffe.learningking.services.NotificationService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class ArticleServiceImpl extends GenericServiceImpl<Article> implements ArticleService {

    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CategoryService categoryService;

    public static Search generateSearchObjectForArticles(String searchTerm) {

   return  new Search();
    }

    @Override
    public Article saveInstance(Article plan) throws ValidationFailedException {

        if (plan.getCategory() == null) {
            throw new ValidationFailedException("Mising category");
        }

        if (StringUtils.isBlank(plan.getTitle())) {
            throw new ValidationFailedException("Missing Title");
        }

        if (StringUtils.isBlank(plan.getDescription())) {
            throw new ValidationFailedException("Missing Description");
        }

        Article existingWithTitle = getByTitle(plan.getTitle());

        if (existingWithTitle != null && !existingWithTitle.getId().equals(plan.getId())) {
            throw new ValidationFailedException("An article with the same title already exists!");
        }
        plan.setPublicationStatus(PublicationStatus.INACTIVE);

        return super.merge(plan);

    }

    @Override
    public int countInstances(Search search) {
        return super.count(search);
    }

    @Override
    public void deleteInstance(Article plan) {
        plan.setRecordStatus(RecordStatus.DELETED);
        super.save(plan);

    }
 @Override
    public List<Article> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search();
        }
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return super.search(search);
    }


    @Override
    public Article save(ArticleRequestDTO dto) throws ValidationFailedException {
        if (dto.getCategoryId() == null) {
            throw new ValidationFailedException("Missing category");
        }

        if (StringUtils.isBlank(dto.getTitle())) {
            throw new ValidationFailedException("Missing Title");
        }

        if (StringUtils.isBlank(dto.getDescription())) {
            throw new ValidationFailedException("Missing Description");
        }

        Article existingWithTitle = getByTitle(dto.getTitle());

        if (existingWithTitle != null && !existingWithTitle.getId().equals(dto.getId())) {
            throw new ValidationFailedException("An article with the same title already exists!");
        }

        Article article=modelMapper.map(dto,Article.class);
        article.setCategory(categoryService.getInstanceByID(dto.getCategoryId()));
        article= saveInstance(article);

        if(dto.getCoverImage()!=null) {
            String imageUrl=   imageStorageService.uploadImage(dto.getCoverImage(), "articles/" + article.getId());
            article.setCoverImageUrl(imageUrl);
            article=super.save(article);
        }

        return article;
    }
   public Article getInstanceByID(Long id){
        return  super.findById(id).orElseThrow(()->new ValidationFailedException("Not found"));
    }
    @Override
    public Article activate(long plan) throws ValidationFailedException {
        Article article=getInstanceByID(plan);
        article.setPublicationStatus(PublicationStatus.ACTIVE);

        Article savedDevotionPlan = super.save(article);
        try {

            ApplicationContextProvider.getBean(NotificationService.class).sendNotificationsToAllStudents(
                    new NotificationBuilder()
                            .setTitle("New Articles added")
                            .setDescription(article.getTitle())
                            .setImageUrl("")
                            .setFmsTopicName("")
                            .setDestinationActivity(NotificationDestinationActivity.DASHBOARD)
                            .setDestinationInstanceId(String.valueOf(article.getId()))
                            .build());

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return savedDevotionPlan;
    }

    @Override
    public Article deActivate(long id) {
        Article plan=getInstanceByID(id);
        plan.setPublicationStatus(PublicationStatus.INACTIVE);
        return super.save(plan);
    }

    @Override
    public Article getByTitle(String planTitle) {
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("title", planTitle);

        return super.searchUnique(search);

    }


    public static Search generateSearchTermsForArticles(String searchTerm) {
        com.googlecode.genericdao.search.Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("title",
                        "description"));

        return search;
    }


    @Override
    public boolean isDeletable(Article entity) throws OperationFailedException {
        return true; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
