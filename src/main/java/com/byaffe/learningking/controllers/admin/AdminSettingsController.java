package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.dtos.SettingsRequestDto;
import com.byaffe.learningking.models.SystemSetting;
import com.byaffe.learningking.services.SystemSettingService;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.models.MessageTemplate;
import com.byaffe.learningking.shared.models.MessageTemplateRequestDto;
import com.byaffe.learningking.shared.models.MessageTemplateChannel;
import com.byaffe.learningking.shared.security.UserDetailsContext;
import com.byaffe.learningking.shared.services.MessageTemplateService;
import com.byaffe.learningking.shared.services.MessageTemplateServiceImpl;
import com.byaffe.learningking.shared.services.MessageTemplateUtils;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.security.AccessControlException;
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
    MessageTemplateService messageTemplateService;
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

    @PostMapping("/message-templates")
    public ResponseEntity<MessageTemplate> saveMessageTemplate(@RequestBody MessageTemplateRequestDto userDTO) throws ValidationException {
        if(!UserDetailsContext.getLoggedInUser().hasAdministrativePrivileges()){
            throw new AccessControlException("Access Denied");
        }
        return ResponseEntity.ok().body(messageTemplateService.saveInstance(userDTO));
    }


    @GetMapping("/message-templates")
    public ResponseEntity<ResponseList<MessageTemplate>> getMessageTemplates(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                             @RequestParam(value = "offset", required = true) Integer offset,
                                                                             @RequestParam(value = "limit", required = true) Integer limit,
                                                                             @RequestParam(value = "type", required = false) MessageTemplateChannel type) {
        if(!UserDetailsContext.getLoggedInUser().hasAdministrativePrivileges()){
            throw new AccessControlException("Access Denied");
        }
        Search search = MessageTemplateServiceImpl.composeSearchObject(searchTerm);

        if (type != null) {
            search.addFilterEqual("type", type);
        }
        if (limit == 0) {
            limit = 1000;
        }
        long totalItems = messageTemplateService.countInstances(search);

        List<MessageTemplate> list = messageTemplateService.getInstances(search, offset, limit);
        return ResponseEntity.ok().body(new ResponseList<>(list, (int) totalItems, offset, limit));

    }

    @GetMapping("/message-templates/params")
    public ResponseEntity<ResponseList<String>> getMessageTemplates() {
        return ResponseEntity.ok().body(new ResponseList<>(MessageTemplateUtils.getDisplayNames(), MessageTemplateUtils.getDisplayNames().size(), 0, 0));

    }

    @DeleteMapping("/message-templates/{id}")
    public ResponseEntity<BaseResponse> deleteTemplate(@PathVariable(value = "id", required = true) long paymentId) throws ValidationException {
        if(!UserDetailsContext.getLoggedInUser().hasAdministrativePrivileges()){
            throw new AccessControlException("Access Denied");
        }
        messageTemplateService.deleteInstance(paymentId);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

}
