package com.example.shopweb.service;

import com.example.shopweb.dto.RoleDto;
import com.example.shopweb.entity.RoleEntity;
import com.example.shopweb.response.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface IRoleService {
    RoleEntity createRole(RoleDto roleDto);

    RoleEntity updateRole(RoleDto roleDto, UUID id) throws Exception;

    void deleteRole(UUID id) throws Exception;

    List<RoleResponse> getAllRoles();

}
