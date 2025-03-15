package com.byaffe.learningking.dtos;

import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.models.EventAttendance;
import com.byaffe.learningking.services.EventAttendanceService;
import com.byaffe.learningking.shared.security.SessionContext;
import com.googlecode.genericdao.search.Search;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class EventResponseDto extends Event {
    private String attendanceWasSubmitted;
    private EventAttendance attendance;
    public Boolean getAttendanceWasSubmitted(){
        return  attendance!=null;
    }
    public EventResponseDto fromModel(Event  model, EventAttendanceService attendanceService){
        EventResponseDto  eventResponseDto= new EventResponseDto();
         eventResponseDto= new ModelMapper().map(model,EventResponseDto.class);

       if(attendanceService!=null &&SessionContext.getLoggedInStudent()!=null)
       {
           eventResponseDto.attendance=attendanceService.getByUser(model.getId(), SessionContext.getLoggedInStudent().getId());
       }

       return eventResponseDto;
    }
}
