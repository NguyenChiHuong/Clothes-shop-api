package com.example.shopweb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_tokens")
public class TokenEntity extends BaseObject{

    @Column(unique = true,nullable = false,length = 100)
    private String token;

    @Column(name = "token_type",nullable = false,length = 50)
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;//Thời gian hết hạn

    @Column(nullable = false)
    private boolean revoked;//Thu hồi

    @Column(nullable = false)
    private boolean expired;//Hết hạn

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
