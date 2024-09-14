package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object class for {@link Article}
 */
@Repository
public class ArticleDaoImpl extends BaseDAOImpl<Article> implements ArticleDao {
}

