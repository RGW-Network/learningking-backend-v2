package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.articles.ArticleRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.VideoRecord;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

import java.io.IOException;

/**
 * Responsible for CRUD operations on {@link Article}
 *
 * @author RayGdhrt
 *
 */
public interface VideoRecordService extends GenericService<VideoRecord> {
     VideoRecord createUploadTicket(String title) throws IOException;

    void updateVideoReady(String videoId, String thumb);
}
