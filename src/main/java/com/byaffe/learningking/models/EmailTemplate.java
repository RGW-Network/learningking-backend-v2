package com.byaffe.learningking.models;

import com.byaffe.learningking.constants.TemplateType;
import javax.persistence.*;

import com.byaffe.learningking.shared.models.BaseEntity;

@Entity
@Table(name = "email_templates")
public class EmailTemplate extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private TemplateType templateType;
	private String template;
	
	@Column(name = "template_type")
	@Enumerated(EnumType.STRING)
	public TemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}

	@Column(name = "template", columnDefinition = "TEXT")
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
}
