package com.almissbah.barberaclient.model;

import java.io.Serializable;

/**
 * Created by mohamed on 7/14/2018.
 */

public class Order implements Serializable {
    int id=0;
    int adminId=0;
    int userId=0;
    String customerPhone="";
    int balanceTime=0;
    boolean isAccepted=false;
    boolean isOrderNew=false;

    public boolean isOrderNew() {
        return isOrderNew;
    }

    public void setOrderNew(boolean orderNew) {
        isOrderNew = orderNew;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getBalanceTime() {
        return balanceTime;
    }

    public void setBalanceTime(int balanceTime) {
        this.balanceTime = balanceTime;
    }
}
