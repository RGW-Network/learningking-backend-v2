package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.dtos.UserSubmissionRequestDto;
import com.byaffe.learningking.models.*;
import com.byaffe.learningking.models.UserSubmission;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.utilities.AppUtils;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserSubmissionServiceImpl extends GenericServiceImpl<UserSubmission> implements UserSubmissionService {

    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CategoryService categoryService;

    @Autowired
    InstructorService instructorService;

    public static Search generateSearchObjectForUserSubmissions(String searchTerm) {

   return  new Search();
    }

    @Override
    public UserSubmission saveInstance(UserSubmission plan) throws ValidationFailedException {

        return super.save(plan);

    }

    @Override
    public int countInstances(Search search) {
        return super.count(search);
    }

    @Override
    public void deleteInstance(UserSubmission plan) {
        plan.setRecordStatus(RecordStatus.DELETED);
        super.save(plan);

    }
 @Override
    public List<UserSubmission> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search();
        }
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return super.search(search);
    }


    @Override
    public UserSubmission save(UserSubmissionRequestDto dto) throws ValidationFailedException {


        if (dto.getType() == null) {
            throw new ValidationFailedException("Missing type");
        }
        if (!AppUtils.isValidEmail(dto.getEmailAddress())) {
            throw new ValidationFailedException("Missing or Invalid Email Address");
        }
        if (StringUtils.isBlank(dto.getSubject())) {
            throw new ValidationFailedException("Missing Title");
        }

        if (StringUtils.isBlank(dto.getDescription())) {
            throw new ValidationFailedException("Missing Description");
        }

        UserSubmission submission=modelMapper.map(dto,UserSubmission.class);
        submission.setRequestDetails(dto.getRequestDetails());
        submission= saveInstance(submission);
        return submission;
    }
   public UserSubmission getInstanceByID(Long id){
        return  super.findById(id).orElseThrow(()->new ValidationFailedException("Not found"));
    }
    @Override
    public UserSubmission resolve(long plan) throws ValidationFailedException {
        UserSubmission model=getInstanceByID(plan);
        model.setStatus(SubmissionStatus.Resolved);
        return super.save(model);
    }




    public static Search generateSearchTermsForUserSubmissions(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("subject", "description", "emailAddress"));
        return search;
    }


    @Override
    public boolean isDeletable(UserSubmission entity) throws OperationFailedException {
        return true; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<String> getStringFilterFields() {
        return Collections.emptyList();
    }


}
