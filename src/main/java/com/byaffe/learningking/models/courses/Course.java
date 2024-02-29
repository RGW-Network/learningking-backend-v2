package com.byaffe.learningking.models.courses;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.byaffe.learningking.shared.models.BaseEntity;

@Entity
@Table(name = "courses")
public class Course extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String title;
    private String description;
    private int numberOfTopics = 0;
    private String coverImageUrl;
    private String welcomeVideoUrl;
    private List<String> whatYouWillLearn;
    private String guidelineVideoUrl;
    private String welcomeRemarks;
    private String certificateTemplate;
    private PublicationStatus publicationStatus=PublicationStatus.INACTIVE;
    private CourseInstructor instructor;
    private CourseCategory category;
    private CourseAcademyType academy;
    private boolean isFeatured;
    private boolean isPaid;
    private float cost;
     private CourseOwnerShipType ownershipType=CourseOwnerShipType.OPEN;
    private Company Company;
    
    private Set<Testimonial> testimonials;
     private String fullDescription;

      @Column(name = "full_description",columnDefinition = "TEXT" )
    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
     
     

    @Column(name = "number_of_topics")
    public int getNumberOfTopics() {
        return numberOfTopics;
    }

    public void setNumberOfTopics(int numberOfTopics) {
        this.numberOfTopics = numberOfTopics;
    }

     @Column(name = "cover_image_url")
    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

     @Column(name = "welcome_video_url")
    public String getWelcomeVideoUrl() {
        return welcomeVideoUrl;
    }

    @ElementCollection (fetch = FetchType.EAGER)
@CollectionTable(name="what_you_will_learn", joinColumns=@JoinColumn(name="course_id"))
@Column(name="outcome")
    public List<String> getWhatYouWillLearn() {
        return whatYouWillLearn;
    }

    public void setWhatYouWillLearn(List<String> whatYouWillLearn) {
        this.whatYouWillLearn = whatYouWillLearn;
    }
    
    

    public void setWelcomeVideoUrl(String welcomeVideoUrl) {
        this.welcomeVideoUrl = welcomeVideoUrl;
    }

     @Column(name = "guideline_video_url")
    public String getGuidelineVideoUrl() {
        return guidelineVideoUrl;
    }

    public void setGuidelineVideoUrl(String guidelineVideoUrl) {
        this.guidelineVideoUrl = guidelineVideoUrl;
    }

     @Column(name = "welcome_remarks",columnDefinition = "TEXT")
    public String getWelcomeRemarks() {
        return welcomeRemarks;
    }

    public void setWelcomeRemarks(String welcomeRemarks) {
        this.welcomeRemarks = welcomeRemarks;
    }

     @Column(name = "certificate_template",columnDefinition = "TEXT")
    public String getCertificateTemplate() {
        return certificateTemplate;
    }

    public void setCertificateTemplate(String certificateTemplate) {
        this.certificateTemplate = certificateTemplate;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "publication_status", nullable = true)
    public PublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(PublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "course_category")
    public CourseCategory getCategory() {
        return category;
    }

    public void setCategory(CourseCategory category) {
        this.category = category;
    }

      @Enumerated(EnumType.ORDINAL)
    @Column(name = "ownership_types", nullable = true)
    public CourseOwnerShipType getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(CourseOwnerShipType ownershipType) {
        this.ownershipType = ownershipType;
    }

     @ManyToOne(optional = true)
    @JoinColumn(name = "owning_company")
    public Company getCompany() {
        return Company;
    }

    public void setCompany(Company Company) {
        this.Company = Company;
    }
    
    

    @Column(name = "description", nullable = false,columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "instructor_id")
    public CourseInstructor getInstructor() {
        return instructor;
    }

    public void setInstructor(CourseInstructor instructor) {
        this.instructor = instructor;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "academy")
    public CourseAcademyType getAcademy() {
        return academy;
    }

    public void setAcademy(CourseAcademyType academy) {
        this.academy = academy;
    }

    @Column(name = "is_featured")
    public boolean isIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

     @Column(name = "is_paid")
    public boolean isIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

     @Column(name = "cost")
    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
    
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Course() {
        super();
    }
    
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "course_testimonials", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "tesstimonial_id"))
  public Set<Testimonial> getTestimonials() {
        return testimonials;
    }

    public void setTestimonials(Set<Testimonial> testimonials) {
        this.testimonials = testimonials;
    }

    public void addTestimonial(Testimonial testimonial) {
        if (this.testimonials == null) {
            this.testimonials = new HashSet<>();
        }
        this.testimonials.add(testimonial);
    }

    public void removeTestimonial(Testimonial testimonial) {
        if (this.testimonials != null) {
            this.testimonials.remove(testimonial);
        }
    }
    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Course && (super.getId() != null) ? super.getId().equals(((Course) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
