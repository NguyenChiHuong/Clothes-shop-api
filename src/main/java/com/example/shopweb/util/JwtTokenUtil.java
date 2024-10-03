package com.example.shopweb.util;

import com.example.shopweb.entity.UserEntity;
import com.example.shopweb.exceptions.InvalidParentException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private int expiration; //Thời gian hết hạn của token

    @Value("${jwt.secretKey}")
    private String secretKey; //Khóa bí mật

    private Key getSignInKey() {
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        String base64Key = Encoders.BASE64.encode(key.getEncoded());
//        System.out.println("Generated Base64 Secret Key: " + base64Key);
        byte[] bytes = Decoders.BASE64.decode(secretKey);// Chuyển khóa bí mật từ chuỗi Base64 thành mảng byte.
        return Keys.hmacShaKeyFor(bytes);// Tạo khóa từ mảng byte
    }

    public String generateToken(UserEntity user) throws Exception {
        // Tạo một map chứa các thông tin (claims) cần lưu vào token
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        try {
            //Tạo token jwt
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Ký token bằng khóa bí mật và thuật toán HS256
                    .compact();
        }catch (Exception e){
            //Có thể tạo Logger
            throw new InvalidParentException("Cannot create jwt token: "+e.getMessage());
        }
    }

    private Claims extractAllClaims(String token) {
        // Giải mã token
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Đặt khóa bí mật để xác thực token
                .build() // Xây dựng parser để phân tích token
                .parseClaimsJws(token) // Giải mã token JWT
                .getBody(); // Lấy phần body của token (chứa claims)
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);// Giải mã từ token
        return claimsResolver.apply(claims); //Trích xuất thông tin cụ thể từ claims
    }

    //Kiểm tra thời gian hết hạn token
    public boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    //Lấy dữ liệu các trường trong claims
    public String extractPhoneNumber(String token){
        return extractClaim(token, claims -> claims.get("phoneNumber", String.class));
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}
