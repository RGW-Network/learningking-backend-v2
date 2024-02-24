package com.byaffe.microtasks.dtos;

import com.byaffe.microtasks.shared.api.BaseResponse;
import com.byaffe.microtasks.shared.security.TokenProvider;
import lombok.Data;

@Data
public class FullUserDTO extends BaseResponse {
    public UserDTO user;
    public String accessToken;
    public String refreshToken;

    public FullUserDTO(UserDTO user, TokenProvider.TokenPair tokenPair) {
        this.user= user;
        this.refreshToken = tokenPair.getRefreshToken();
        this.accessToken = tokenPair.getAccessToken();

    }
}
