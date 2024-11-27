package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.dtos.SettingsRequestDto;
import com.byaffe.learningking.dtos.quiz.QuizQuestionRequestDTO;
import com.byaffe.learningking.dtos.quiz.QuizRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.SystemSetting;
import com.byaffe.learningking.models.quizes.Question;
import com.byaffe.learningking.models.quizes.Quiz;
import com.byaffe.learningking.services.ArticleService;
import com.byaffe.learningking.services.QuizService;
import com.byaffe.learningking.services.SystemSettingService;
import com.byaffe.learningking.services.impl.QuizServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/admin/settings")
public class AdminSettingsController {
    @Autowired
    SystemSettingService settingService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("")
    public ResponseEntity<ResponseObject<SystemSetting>> save(@RequestBody SettingsRequestDto dto) throws JSONException {
        return ResponseEntity.ok().body(new ResponseObject<>(settingService.save(dto)));
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject<SystemSetting>> getActiveSettings() throws JSONException {
        return ResponseEntity.ok().body(new ResponseObject<>(settingService.getAppSetting()));

    }

}
