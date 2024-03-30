package com.byaffe.learningking.utilities;

import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.NotificationTopic;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.services.NotificationTopicService;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.Gson;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;


public interface ImageStorageService {

    /**
     * Uploads image bytes to image store and returns the public URL to access
     * the image. The public_id represents the name used to store the image. You
     * can also specify the directory by including a "/" in the public_id eg
     * myfolder/pics/myImageName
     *
     * @param contents  - the image bytes
     * @param public_id - unique image identifier
     * @return
     */
     String uploadImage(byte[] contents, String public_id) ;

    /**
     * Uploads image bytes  and returns the public URL to access
     * the image. The public_id represents the name used to store the image. You
     * can also specify the directory by including a "/" in the public_id eg
     * myfolder/pics/myImageName
     *
     * @param contents  - the image bytes
     * @param public_id - unique image identifier
     * @return
     */
     String uploadImage(MultipartFile contents, String public_id);


}
