package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.TemplateType;
import com.byaffe.learningking.models.EmailTemplate;
import com.byaffe.learningking.services.EmailTemplateService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class EmailTemplateServiceImpl extends GenericServiceImpl<EmailTemplate> implements EmailTemplateService {
	
	@Override
	public List<EmailTemplate> getTemplates() {
		Search search = new Search();
		search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
		return super.search(search);
	}

	@Override
	public int countTemplates() {
		Search search = new Search();
		search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
		return super.count(search);
	}

	@Override
	public EmailTemplate saveInstance(EmailTemplate emailTemplate)
			throws ValidationFailedException, OperationFailedException {
		Validate.notNull(emailTemplate, "Missing template");
		Validate.notNull(emailTemplate.getTemplateType(), "Missing template type");
		Validate.notNull(emailTemplate.getTemplate(), "Missing template details");
		
		EmailTemplate existingTemplate = getEmailTemplateByType(emailTemplate.getTemplateType());
		if (existingTemplate != null && !Objects.equals(existingTemplate.getId(), emailTemplate.getId()))
			throw new ValidationFailedException(String.format("%s template already exists", emailTemplate.getTemplateType().getName()));

		return super.save(emailTemplate);
	}

	@Override
	public EmailTemplate getEmailTemplateByType(TemplateType templateType) {
		Search search = new Search();
		search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
		if(templateType != null)
			search.addFilterEqual("templateType", templateType);
		search.setMaxResults(1);
		return super.searchUnique(search);
	}

	@Override
	public boolean isDeletable(EmailTemplate entity) throws OperationFailedException {
		return true;
	}
}
