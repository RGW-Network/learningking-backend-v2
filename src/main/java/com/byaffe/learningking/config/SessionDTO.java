package com.byaffe.learningking.config;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.models.User;

public class SessionDTO {
    private User loggedInUser;
    private Student loggedInStudent;

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public Student getLoggedInMember() {
        return loggedInStudent;
    }

    public void setLoggedInMember(Student loggedInStudent) {
        this.loggedInStudent = loggedInStudent;
    }
}
