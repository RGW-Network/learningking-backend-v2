package com.byaffe.learningking.config;

import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.shared.models.User;

public class SessionDTO {
    private User loggedInUser;
    private Member loggedInMember;

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public Member getLoggedInMember() {
        return loggedInMember;
    }

    public void setLoggedInMember(Member loggedInMember) {
        this.loggedInMember = loggedInMember;
    }
}
