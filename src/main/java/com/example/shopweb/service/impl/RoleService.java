package com.example.shopweb.service.impl;

import com.example.shopweb.dto.RoleDto;
import com.example.shopweb.entity.RoleEntity;
import com.example.shopweb.exceptions.DataNotFoundException;
import com.example.shopweb.repository.RoleRepository;
import com.example.shopweb.response.RoleResponse;
import com.example.shopweb.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleEntity createRole(RoleDto roleDto) {
        //Kiểm tra xem số điện thoại đã tồn tại chưa
        if (roleRepository.existsByName(roleDto.getName())) {
            throw new DataIntegrityViolationException("Role is not match");
        }
        RoleEntity role = RoleEntity.builder()
                .name(roleDto.getName())
                .build();
        return roleRepository.save(role);
    }

    @Override
    public RoleEntity updateRole(RoleDto roleDto, UUID id) throws Exception {
        RoleEntity existRole = roleRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Role with id "+id+" not found"));
        existRole.setName(roleDto.getName());
        return roleRepository.save(existRole);
    }

    @Override
    public void deleteRole(UUID id) throws Exception {
        RoleEntity existRole = roleRepository.findById(id).orElse(null);
        if (existRole == null) {
            throw new DataNotFoundException("Role is not found" + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        List<RoleResponse> roleResponses = roleEntities.stream()
                .map(RoleResponse::fromRole)
                .collect(Collectors.toList());
        return roleResponses;
    }
}
