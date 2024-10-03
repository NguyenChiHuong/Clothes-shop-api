package com.example.shopweb.controller;

import com.example.shopweb.dto.RoleDto;
import com.example.shopweb.entity.RoleEntity;
import com.example.shopweb.response.RoleResponse;
import com.example.shopweb.service.impl.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("")
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto) {
        try {
            RoleEntity newRole = roleService.createRole(roleDto);
            return ResponseEntity.ok(newRole);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable("id") UUID id
            , @RequestBody RoleDto roleDto) {
        try {
            RoleEntity updateRole = roleService.updateRole(roleDto,id);
            return ResponseEntity.ok(updateRole);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable("id") UUID id) throws Exception {
        roleService.deleteRole(id);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllRoles() {
        List<RoleResponse> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}
