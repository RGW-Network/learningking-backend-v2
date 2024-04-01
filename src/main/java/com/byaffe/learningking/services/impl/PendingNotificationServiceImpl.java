package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.StudentPendingNotification;
import com.byaffe.learningking.models.Notification;
import com.byaffe.learningking.services.PendingNotificationService;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class PendingNotificationServiceImpl extends GenericServiceImpl<StudentPendingNotification> implements PendingNotificationService {

    @Override
    public boolean isDeletable(StudentPendingNotification entity) throws OperationFailedException {
     return true;
    }

    @Override
    public StudentPendingNotification saveInstance(StudentPendingNotification instance) throws ValidationFailedException, OperationFailedException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public StudentPendingNotification addNotification(Student student, Notification notification) throws ValidationFailedException, OperationFailedException {
     if(student ==null){
         throw  new ValidationFailedException("Missing Student");
     }
     
     if(notification==null){
          throw  new ValidationFailedException("Missing Notication");
     }
        
        return super.save(new StudentPendingNotification(student, notification) );
    
    }

    @Override
    public void removeNotification(StudentPendingNotification notification) throws ValidationFailedException, OperationFailedException {
      super.deleteInstance(notification);
    
    }

    @Override
    public void removeNotifications(List<StudentPendingNotification> notifications) throws ValidationFailedException, OperationFailedException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
   
    }

    @Override
    public void removeAllNotifications(Student student) throws ValidationFailedException, OperationFailedException {
      
    super.deleteInstances(new Search().addFilterEqual("member", student));
    }

   
}
