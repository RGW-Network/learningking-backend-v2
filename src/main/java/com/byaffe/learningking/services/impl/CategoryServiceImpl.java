package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.dtos.courses.CourseCategoryRequestDTO;
import com.byaffe.learningking.models.courses.CategoryType;
import com.byaffe.learningking.models.courses.Category;
import com.byaffe.learningking.services.CategoryService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
public class CategoryServiceImpl
        extends GenericServiceImpl<Category> implements CategoryService {
@Autowired
    ModelMapper modelMapper;

@Autowired
    ImageStorageService   imageStorageService;

    @Override
    public boolean isDeletable(Category entity) throws OperationFailedException {
        return true;
    }

    @Override
    public Category saveInstance(Category instance) throws ValidationFailedException, OperationFailedException {
        if (StringUtils.isBlank(instance.getName())) {
            throw new ValidationFailedException("Missing name");
        }

        if (instance.getType() == null) {
            throw new ValidationFailedException("Missing type");
        }
        Category existsWithType = getCategoryByNameAndType(instance.getName(), instance.getType());
        if (existsWithType != null && !existsWithType.getId().equals(instance.getId())) {
            throw new ValidationFailedException("Category With Same name and type exists");
        }

        return super.save(instance);
    }

    public Category saveInstance(CourseCategoryRequestDTO dto) throws ValidationFailedException, OperationFailedException {
        if (StringUtils.isBlank(dto.getName())) {
            throw new ValidationFailedException("Missing name");
        }
        if (dto.getType() == null) {
            throw new ValidationFailedException("Missing type");
        }
        Category existsWithType = getCategoryByNameAndType(dto.getName(), dto.getType());
        if (existsWithType != null && !existsWithType.getId().equals(dto.getId())) {
            throw new ValidationFailedException("Category With Same name and type exists");
        }

        Category category =modelMapper.map(dto, Category.class);
        category=super.save(category);
        if(dto.getIcon()!=null) {
            String imageUrl=   imageStorageService.uploadImage(dto.getIcon(), "categories/icons/" + category.getId());
            category.setIconUrl(imageUrl);
            category=super.save(category);
        }
        if(dto.getIcon()!=null) {
            String imageUrl=   imageStorageService.uploadImage(dto.getImage(), "categories/images/" + category.getId());
            category.setImageUrl(imageUrl);
            category=super.save(category);
        }
        return category;
    }

    
    @Override
    public Category getCategoryByNameAndType(String name, CategoryType courseType) {
        Search search = new Search().addFilterEqual("name", name)
                .addFilterEqual("type", courseType)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return super.searchUnique(search);
    }

    public static Search composeSearchObject(String searchTerm) {
        com.googlecode.genericdao.search.Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("name","description"));

        return search;
    }
}
