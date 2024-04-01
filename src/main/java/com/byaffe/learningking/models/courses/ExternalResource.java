package com.byaffe.learningking.models.courses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import com.byaffe.learningking.shared.models.BaseEntity;


@Entity
@Table(name = "external_resources")
@SuppressWarnings("ConsistentAccessType")
public class ExternalResource extends BaseEntity {

    private static final long serialVersionUID = 1L;
 
    @Enumerated(EnumType.ORDINAL)
    private ExternalResourceType type;
    private String name;
    private String link;
    
    
    
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public ExternalResource() {
        super();
    }

    public ExternalResource(String link) {
        super();
        this.link = link;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    public ExternalResourceType getType() {
        return type;
    }

    public void setType(ExternalResourceType type) {
        this.type = type;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
  
    @Column(name = "link", nullable = false, length = 1000)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

   

    @Override
    public String toString() {
        return "ExternalResource{" + "name=" + name + ", link=" + link + '}';
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ExternalResource && (super.getId() != null) ? super.getId().equals(((ExternalResource) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
    

}
