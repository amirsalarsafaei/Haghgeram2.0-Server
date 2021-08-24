package com.SalarJavaDevGroup.Models.Responses;

public class UserDetails {
    public static UserDetails holder;
    private String username, token;
    private int id;
    private EditPrivacyResponse editPrivacyResponse;
    private UserInformationResponse userInformationResponse;

    public EditPrivacyResponse getEditPrivacyResponse() {
        return editPrivacyResponse;
    }

    public void setEditPrivacyResponse(EditPrivacyResponse editPrivacyResponse) {
        this.editPrivacyResponse = editPrivacyResponse;
    }

    public UserInformationResponse getUserInformationResponse() {
        return userInformationResponse;
    }

    public void setUserInformationResponse(UserInformationResponse userInformationResponse) {
        this.userInformationResponse = userInformationResponse;
    }
    public UserDetails(String username, int id, String token, EditPrivacyResponse editPrivacyResponse,
                       UserInformationResponse userInformationResponse) {
        this.username = username;
        this.id = id;
        this.token = token;
        this.editPrivacyResponse = editPrivacyResponse;
        this.userInformationResponse = userInformationResponse;
    }
}
