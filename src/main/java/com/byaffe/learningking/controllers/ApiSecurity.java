package com.byaffe.learningking.controllers;

public class ApiSecurity {

   private String username;
    private String password;
    private String oldPassword;
     private String newPassword;
      private String newConfirmedPassword;
    private String apiToken;
    private String deviceId;
     private String deviceToken;

    private String searchTerm;
    private int offset = 0;
    private int limit = 0;
    private String sortBy;
    private Boolean sortDescending = true;

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceID) {
        this.deviceId = deviceID;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getSortDescending() {
        return sortDescending;
    }

    public void setSortDescending(Boolean sortDescending) {
        this.sortDescending = sortDescending;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewConfirmedPassword() {
        return newConfirmedPassword;
    }

    public void setNewConfirmedPassword(String newConfirmedPassword) {
        this.newConfirmedPassword = newConfirmedPassword;
    }
    
    

}
