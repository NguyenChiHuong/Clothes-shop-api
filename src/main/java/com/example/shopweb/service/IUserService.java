package com.example.shopweb.service;

import com.example.shopweb.dto.UserDto;
import com.example.shopweb.entity.UserEntity;

import java.util.UUID;

public interface IUserService {
    UserEntity createUser(UserDto userDto) throws Exception;

    String login(String phoneNumber, String password, UUID roleId) throws Exception;
}
