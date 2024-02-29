package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;

import java.util.List;

;
/**
 * Responsible for CRUD operations on {@link CourseCategory}
 *
 * @author RayGdhrt
 *
 */
public interface CompanyService  extends GenericService<Company> {

    
    public Company activate(Company plan) throws ValidationFailedException;

    public Company deActivate(Company plan);
    
     public List<CompanyCourse> getCompanyCourses(Company company);

    public CompanyCourse getCompanyCourse(Company company, Course course);

    public void delete(CompanyCourse companyCourse);

    public void saveCompanyCourse(CompanyCourse companyCourse) throws ValidationFailedException;
   
    
     public List<CompanyMember> getCompanyMembers(Search search, int offset, int limit);

    public CompanyMember getCompanyMember(Company company, Member member);

    public void delete(CompanyMember companyMember);

    public void saveCompanyMember(CompanyMember companyMember) throws ValidationFailedException;
     public CompanyMember activate(CompanyMember plan) throws ValidationFailedException ;

    public CompanyMember deActivate(CompanyMember plan);
   

}
