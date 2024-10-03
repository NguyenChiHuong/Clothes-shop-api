package com.example.shopweb.response;

import com.example.shopweb.entity.RoleEntity;
import com.example.shopweb.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    public static RoleResponse fromRole(RoleEntity userEntity){
        RoleResponse userResponse = RoleResponse.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .build();
        return userResponse;
    }
}
