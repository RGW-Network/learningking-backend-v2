package com.byaffe.learningking.services;


import com.byaffe.learningking.constants.TemplateType;
import com.byaffe.learningking.models.EmailTemplate;

import java.util.List;


public interface EmailTemplateService extends GenericService<EmailTemplate>{
	/*
	 * Retrieve all email templates
	 */
    List<EmailTemplate> getTemplates();
    
	/*
	 * Get template by type
	 */
    EmailTemplate getEmailTemplateByType(TemplateType templateType);

	/*
	 * Count templates
	 */
    int countTemplates();
}
