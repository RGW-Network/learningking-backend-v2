package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.models.VideoRecord;
import com.byaffe.learningking.services.VideoRecordService;
import com.googlecode.genericdao.search.Search;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoRecordService vimeoService;

    @PostMapping("/upload-ticket")
    public ResponseEntity<?> createTicket(@RequestParam String title) {
        try {
            VideoRecord ticket = vimeoService.createUploadTicket(title);
            return ResponseEntity.ok(ticket); // Contains: vimeoId + uploadLink
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed: " + e.getMessage());
        }
    }
    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody String payload,
                                     @RequestHeader("Vimeo-Signature") String signature)
    {
        // Simplified - add signature validation if needed
        JSONObject json = new JSONObject(payload);

        if (json.has("clip")) {
            String videoId = json.getJSONObject("clip").getString("uri").replace("/videos/", "");

            // Get thumbnail
            String thumb = json
                    .getJSONObject("clip")
                    .getJSONArray("pictures")
                    .getJSONObject(0)
                    .getString("link");

            vimeoService.updateVideoReady(videoId, thumb);
        }

        return ResponseEntity.ok("OK");
    }

    @GetMapping
    public List<VideoRecord> list() {
        return vimeoService.getInstances(new Search(),0,0);
    }
}
