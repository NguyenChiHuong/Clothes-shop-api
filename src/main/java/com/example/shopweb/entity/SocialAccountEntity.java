package com.example.shopweb.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_social_account")
public class SocialAccountEntity extends BaseObject {

    @Comment("Tên nhà social network")
    @Column(nullable = false, length = 20)
    private String provider;

    @Comment("Email tài khoản")
    @Column(nullable = false, length = 150)
    private String email;

    @Comment("Tên người dùng")
    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}