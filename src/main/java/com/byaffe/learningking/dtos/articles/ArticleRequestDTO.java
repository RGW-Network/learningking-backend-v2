package com.byaffe.learningking.dtos.articles;

import com.byaffe.learningking.models.courses.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArticleRequestDTO {
    private Long id;
    private String title;
    private String description;
    private String fullDescription;
    private String coverImageUrl;
    private MultipartFile coverImage;
    private Long areaOfBusinessId;
    private ArticleType type ;
    private Long categoryId;
    private String mainQuote;

}
