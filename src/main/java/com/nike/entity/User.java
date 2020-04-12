package com.nike.entity;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 存储用户的信息
 */
public class User implements Serializable {

    private String userId;

    @NotNull(message = "{user.not.null}")
    @Size(min = 2,max = 15,message = "{user.userNickname.length,error}")
    private String userNickname;

    @NotNull(message = "{user.not.null}")
    @Size(min = 6,max = 12,message = "{user.userPassword.length.error}")
    private String userPassword;


    @NotNull(message = "{user.not.null}")
    @Email(message = "{user.userEmail.not.correct}")
    private String userEmail;

    private Integer actiState;

    //激活成功与激活失败常量
    public static final int ACTIVATION_SUCCESSFUL=1;
    public static final int ACTIVATION_UNSUCCESSFUL=0;
    private String activCode;
    private String tokenExptime;
    private String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getActiState() {
        return actiState;
    }

    public void setActiState(Integer actiState) {
        this.actiState = actiState;
    }



    public String getActivCode() {
        return activCode;
    }

    public void setActivCode(String activCode) {
        this.activCode = activCode;
    }

    public String getTokenExptime() {
        return tokenExptime;
    }

    public void setTokenExptime(String tokenExptime) {
        this.tokenExptime = tokenExptime;
    }
}
