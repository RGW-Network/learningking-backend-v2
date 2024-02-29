/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.utilities;

/**
 *
 * @author Ray Gdhrt
 */
public class EgoResponse {
    
   
    private String Message;
    private String Status;
    private String Cost;
    private String MsgFollowUpUniqueCode;
    private String Balance;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String Cost) {
        this.Cost = Cost;
    }

    public String getMsgFollowUpUniqueCode() {
        return MsgFollowUpUniqueCode;
    }

    public void setMsgFollowUpUniqueCode(String MsgFollowUpUniqueCode) {
        this.MsgFollowUpUniqueCode = MsgFollowUpUniqueCode;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String Balance) {
        this.Balance = Balance;
    }

    @Override
    public String toString() {
        return "EgoResponse{" + "Message=" + Message + ", Status=" + Status + ", Cost=" + Cost + ", MsgFollowUpUniqueCode=" + MsgFollowUpUniqueCode + '}';
    }
    
    
    
    
    
}
