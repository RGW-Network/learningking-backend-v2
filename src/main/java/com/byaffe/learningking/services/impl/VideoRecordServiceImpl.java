package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.VideoRecord;
import com.byaffe.learningking.services.*;
import com.googlecode.genericdao.search.Search;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class VideoRecordServiceImpl extends GenericServiceImpl<VideoRecord> implements VideoRecordService {

String accessToken="";
    /**
     * Creates a TUS upload ticket
     */
    public VideoRecord createUploadTicket(String title) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                "{\"upload\":{\"approach\":\"tus\",\"size\": null}}",
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://api.vimeo.com/me/videos")
                .addHeader("Authorization", "Bearer " + accessToken)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Cannot create Vimeo upload ticket: " + response.message());
        }

        String responseBody = response.body().string();
        JSONObject json = new JSONObject(responseBody);

        String videoUri = json.getString("uri");           // "/videos/12345"
        String uploadLink = json.getJSONObject("upload").getString("upload_link");

        VideoRecord record = new VideoRecord();
        record.setTitle(title);
        record.setUploadLink(uploadLink);
        record.setVimeoId(videoUri.replace("/videos/", ""));
        record.setStatus("uploading");

        return saveInstance(record);
    }

    public void updateVideoReady(String vimeoId, String thumbnail) {
        VideoRecord record = searchUnique(new Search().addFilterEqual("vimeoId",vimeoId));

        if (record != null) {
            record.setStatus("ready");
            record.setThumbnailUrl(thumbnail);
            saveInstance(record);
        }
    }

    @Override
    public List<String> getStringFilterFields() {
        return Collections.emptyList();
    }


}
