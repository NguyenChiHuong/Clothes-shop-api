package com.example.shopweb.controller;

import com.example.shopweb.constant.Constant;
import com.example.shopweb.dto.UserDto;
import com.example.shopweb.dto.UserLoginDto;
import com.example.shopweb.entity.UserEntity;
import com.example.shopweb.response.LoginResponse;
import com.example.shopweb.response.RegisterResponse;
import com.example.shopweb.response.UserResponse;
import com.example.shopweb.service.IUserService;
import com.example.shopweb.util.LocalizationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final LocalizationUtil localizationUtil;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDto userDto,
            BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDto.getPassword().equals(userDto.getRetypePassword())){
                return ResponseEntity.badRequest().body(RegisterResponse.builder()
                                .message(localizationUtil.getLocalizedMessage(Constant.PASSWORD_NOT_MATCH))
                        .build());
            }
            UserEntity newUser = userService.createUser(userDto);
            UserResponse.fromUser(newUser);
            return ResponseEntity.ok(RegisterResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(Constant.REGISTER_SUCCESSFULLY))
                    .build());
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        try {
            //Kiếm tra thông tin đăng nhập -> token
            String token = userService.login(
                    userLoginDto.getPhoneNumber(),
                    userLoginDto.getPassword(),
                    userLoginDto.getRoleId());
            return ResponseEntity.ok(LoginResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(Constant.LOGIN_SUCCESSFULLY))
                            .token(token)
                    .build());
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(Constant.LOGIN_FAILED, ex.getMessage()))
                            .build()
            );
        }
    }

}
