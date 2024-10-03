package com.example.shopweb.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_users")
public class UserEntity extends BaseObject implements UserDetails {
    @Column(name = "full_name",length = 100)
    private String fullName;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(name = "phone_number",length = 10,nullable = false)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(length = 200)
    private  String address;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "facebook_account_id")
    private int facebookAccountId;

    @Column(name = "google_account_id")
    private int googleAccountId;

    @OneToMany(mappedBy = "user")
    private List<TokenEntity> tokens;

    @OneToMany(mappedBy = "user")
    private List<SocialAccountEntity> socialAccounts;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<OrderEntity> orders;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Tạo danh sách được cấp quyền
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+getRole().getName().toUpperCase()));
        //authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    @Override
    public String getUsername() {
        return phoneNumber;//Tài khoản đăng nhập
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //Tài khoản chưa hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //Không khóa tài khoản
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //Thông tin xác thực chưa hết hạn
    }

    @Override
    public boolean isEnabled() {
        return true; //Kích hoạt
    }
}
