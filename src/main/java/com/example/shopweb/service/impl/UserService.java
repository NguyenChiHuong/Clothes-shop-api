package com.example.shopweb.service.impl;

import com.example.shopweb.constant.Constant;
import com.example.shopweb.dto.UserDto;
import com.example.shopweb.entity.RoleEntity;
import com.example.shopweb.entity.UserEntity;
import com.example.shopweb.exceptions.DataNotFoundException;
import com.example.shopweb.exceptions.PermissionDenyException;
import com.example.shopweb.repository.RoleRepository;
import com.example.shopweb.repository.UserRepository;
import com.example.shopweb.service.IUserService;
import com.example.shopweb.util.JwtTokenUtil;
import com.example.shopweb.util.LocalizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtil localizationUtil;

    @Override
    public UserEntity createUser(UserDto userDto) throws Exception {
        String phoneNumber = userDto.getPhoneNumber();

        //Kiểm tra xem số điện thoại đã tồn tại chưa
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        RoleEntity existRole = roleRepository.findById(userDto.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("Role not found"));

        if (existRole.getName().toUpperCase().equals(Constant.ADMIN)){
            throw new PermissionDenyException("Can not register an admin account");
        }

        //convert
        UserEntity newUser = UserEntity.builder()
                .fullName(userDto.getFullName())
                .phoneNumber(userDto.getPhoneNumber())
                .password(userDto.getPassword())
                .address(userDto.getAddress())
                .dateOfBirth(userDto.getDateOfBirth())
                .facebookAccountId(userDto.getFacebookAccountId())
                .googleAccountId(userDto.getGoogleAccountId())
                .build();
        RoleEntity role = roleRepository.findById(userDto.getRoleId())
                .orElseThrow(()->new DataNotFoundException("Role not found"));
        newUser.setRole(role);

        //Kiểm tra nếu có account google,fb thì không hiện mật khẩu
        if (userDto.getGoogleAccountId()==0 || userDto.getFacebookAccountId()==0) {
            String password = userDto.getPassword();
            String encodePassword = passwordEncoder.encode(password);
            newUser.setPassword(encodePassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password, UUID roleId) throws Exception {
        Optional<UserEntity> user = userRepository.findByPhoneNumber(phoneNumber);
        if(user.isEmpty()){
            throw new DataNotFoundException(localizationUtil.getLocalizedMessage(Constant.WRONG_PHONE_PASS));
        }
//        return user.get();
        
        //Trả về JWT token
        UserEntity existingUser = user.get();

        //Kiểm tra mật khẩu
        if (existingUser.getGoogleAccountId()==0 || existingUser.getFacebookAccountId()==0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())){
                throw new BadCredentialsException(localizationUtil.getLocalizedMessage(Constant.WRONG_PHONE_PASS));
            }
        }

        Optional<RoleEntity> role = roleRepository.findById(roleId);
        if(role.isEmpty() || !roleId.equals(existingUser.getRole().getId())){
            throw new DataNotFoundException(localizationUtil.getLocalizedMessage(Constant.ROLE_DOES_NOT_EXIST));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber,password,existingUser.getAuthorities());

        //Authenticate với Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }
}
