package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.courses.CourseCategoryRequestDTO;
import com.byaffe.learningking.models.courses.CategoryType;
import com.byaffe.learningking.models.courses.Category;

;

/**
 * Responsible for CRUD operations on {@link Category}
 *
 * @author RayGdhrt
 *
 */
public interface CategoryService extends GenericService<Category> {

  
    public Category saveInstance(CourseCategoryRequestDTO dto) ;
    public Category getCategoryByNameAndType(String name, CategoryType courseType);
}
