package com.byaffe.learningking.utilities;

import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.NotificationTopic;
import com.byaffe.learningking.services.NotificationTopicService;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.Gson;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppUtils {

    public static String TUTOR_ROLE_NAME = "Learning king tutor";
     public static String STUDENT_ROLE_NAME = "Learning king student";
      public static String NORMAL_USER_ROLE_NAME = "Normal User";
    public static String PROF_DATASET_NAME = "Professionals";
    public static String COURSE_TYPES_DATASET_NAME = "Course types";

    public static String CLOUDINARY_CLOUD_NAME = "revival-gateway";
    public static String CLOUDINARY_API_KEY = "114516888855596";
    public static String CLOUDINARY_API_SECRET = "gG2NUayJtKGuFcQln1yvxYPTMzQ";
    public static String SCRIPTURES_API_KEY = "a07ad608d1a7d71629319bcf7acae795";
    public static String SCRIPTURES_API_BIBLE_ID = "de4e12af7f28f599-02";

    public static final String FLUTTER_SECRET_KEY = "FLWSECK-433eeec81d5f1e5bdb811111db9484dd-X";
    final static String senderGridApiKey = "";

    private static String smtpHost = "smtp.gmail.com";
    private static String smtpPort = "587";
    private static String senderAddress = "mail.byaffe@gmail.com";
    private static String senderPassword = "pass@2020@byaffe";
 public static final int MAX_CONTENT_PACKAGES = 9;
    public static final int MAX_MESSAGES_PER_BATCH = 100;
    public static final int EGO_SMS_MESSAGE_COST = 30;
    public static final int MIN_TRANSACRION_AMOUNT = 1000;

    public static String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        phoneNumber = phoneNumber.replace("+", "");
        if (phoneNumber.matches("\\d{12}") && phoneNumber.startsWith("256")) {
            return phoneNumber;
        } else if (phoneNumber.matches("\\d{9}") && phoneNumber.startsWith("7")) {
            return "256" + phoneNumber;
        } else if (phoneNumber.matches("\\d{10}") && phoneNumber.startsWith("07")) {
            return "256" + phoneNumber.substring(1);
        } else {
            return null;
        }
    }

    public static Date addMinutesToJavaUtilDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addDayssToJavaUtilDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
    }


    /**
     * Uploads image bytes to cloudinary and returns the public URL to access
     * the image. The public_id represents the name used to store the image. You
     * can also specify the directory by including a "/" in the public_id eg
     * myfolder/pics/myImageName
     *
     * @param contents - the image bytes
     * @param public_id - unique image identifier
     * @return
     */
    public String uploadCloudinaryImage(byte[] contents, String public_id) {
        System.out.println("Started image upload...");
        try {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", CLOUDINARY_CLOUD_NAME,
                    "api_key", CLOUDINARY_API_KEY,
                    "api_secret", CLOUDINARY_API_SECRET,
                    "secure", true));
            System.out.println(Arrays.toString(contents));
            Map uploadResult = cloudinary.uploader().upload(contents, ObjectUtils.asMap("public_id", public_id));
            String imageUrl = uploadResult.get("secure_url").toString();
            System.out.println("Image url = " + imageUrl);
            return imageUrl;
        } catch (IOException ex) {
            Logger.getLogger(AppUtils.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static void main(String[] args) {
        System.err.println("Current Hour >>>>>>"+ LocalDateTime.now().getHour());       
    }

    /**
     * 
     * @param url
     * @return 
     */
    public static String convertToYouTubeEmbededUrl(String url) {
        return url.replace("watch?v=", "embed/");

    }

    /**
     * 
     * @param url
     * @return 
     */
    public static String convertToYouTubeWatchableUrl(String url) {
        return url.replace("embed/", "watch?v=");

    }

    /**
     * 
     * @param length
     * @return 
     */
    public static String getRandomString(int length) {
        final Random random = new Random();
        String ALLOWED_CHARACTERS = "0123456789QWERTYUIOPASDFGHJKLZXCVBNM";
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return sb.toString();
    }

    /**
     * 
     * @param len
     * @return 
     */
    public static String generateOTP(int len) {
        System.out.println("Generating OTP using random() : ");

        // Using numeric values
        String numbers = "0123456789";

        // Using random method
        Random rndm_method = new Random();

        char[] otp = new char[len];

        for (int i = 0; i < len; i++) {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i]
                    = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }

        String OTP = String.valueOf(otp);
        System.out.print("Generated OTP is : " + OTP);
        return OTP;
    }

    /**
     * 
     * @param topicName
     * @param title
     * @param description
     * @param destinationActivity
     * @param entityId
     */
    public static void sendNotificationToTopic(String topicName, String title, String description, NotificationDestinationActivity destinationActivity, String entityId) {
        /**
         * Code to send GCM notification after successful payment
         */

        NotificationTopic notificationTopic = ApplicationContextProvider.getBean(NotificationTopicService.class).getTopicByName(topicName);
        if (notificationTopic != null) {
            List<String> deviceIds = notificationTopic.getSubscribingTokens();
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("destination", destinationActivity.toString());
            dataMap.put("entityId", entityId);
            try {
                ExpoNotificationService.sendNotification(title, description, deviceIds, dataMap);
            } catch (PushClientException | InterruptedException ex) {
                Logger.getLogger(AppUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     * @param title
     * @param description
     * @param iconUrl
     * @param members
     * @param destinationActivity
     * @param entityId 
     */
    public static void sendDirectNotifications(String title, String description,
                                               String iconUrl, List<Member> members, NotificationDestinationActivity destinationActivity,
                                               String entityId) {
        System.out.println("Started Expo service..");

        List<String> deviceIds = new ArrayList<>();
        for (Member member : members) {
            if (StringUtils.isNotBlank(member.getDeviceId()) && PushClient.isExponentPushToken(member.getDeviceId())) {
                deviceIds.add(member.getDeviceId());
                System.out.println("Added Token >>>>>>>>>>>>>>>" + member.getDeviceId());
            }
        }
        System.err.println(">>>>>>>>>>>>>>>>>>>>Got device IDs>>>>>>>>>" + new Gson().toJson(deviceIds));
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("destination", destinationActivity.toString());
        dataMap.put("entityId", entityId);
        try {
            ExpoNotificationService.sendNotification(title, description, deviceIds, dataMap);
        } catch (PushClientException ex) {
            Logger.getLogger(AppUtils.class.getName()).log(Level.SEVERE, (String) null);
        } catch (InterruptedException ex) {
            Logger.getLogger(AppUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @return 
     */
    public  String generateVerificationCode() {
        final String NUMERIC_STRING = "0123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= 4; i++) {
            int character = (int) (Math.random() * NUMERIC_STRING.length());
            builder.append(NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

}
