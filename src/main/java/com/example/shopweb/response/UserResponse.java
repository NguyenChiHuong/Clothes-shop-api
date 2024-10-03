package com.example.shopweb.response;

import com.example.shopweb.entity.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends BaseResponse{
    @JsonProperty("full_name")
    private String fullName;

    private String password;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    private String address;

    @JsonProperty("is_active")
    private boolean isActive;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @JsonProperty("role_id")
    private UUID roleId;

    public static UserResponse fromUser(UserEntity userEntity){
        UserResponse userResponse = UserResponse.builder()
                .fullName(userEntity.getFullName())
                .password(userEntity.getPassword())
                .phoneNumber(userEntity.getPhoneNumber())
                .dateOfBirth(userEntity.getDateOfBirth())
                .address(userEntity.getAddress())
                .isActive(userEntity.isActive())
                .facebookAccountId(userEntity.getFacebookAccountId())
                .googleAccountId(userEntity.getGoogleAccountId())
                .roleId(userEntity.getRole().getId())
                .build();
        userResponse.setCreateDate(userEntity.getCreateDate());
        userResponse.setModifyDate(userEntity.getModifyDate());
        return userResponse;
    }
}
