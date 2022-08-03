package com.wqddg.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.Map;

/**
 * @Author: wqddg
 * @ClassName JwtUtils
 * @DateTime: 2022/1/6 18:29
 * @remarks : #
 */
@Getter
@Setter
@ConfigurationProperties("jwt.config")
public class JwtUtils {
    //签名密钥
    private String key="wqddg";
    //设置认证 token
    private Long ttl= Long.valueOf(24 * 60 * 60 * 1000);


    public String createJwt(String id, String name, Map<String,Object> maps){
        JwtBuilder claim = Jwts.builder().setId(id).setSubject(name)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key)
                .setExpiration(new Date(System.currentTimeMillis() +ttl)) //设置token的过期日期一天的间隔
                .claim("companyId", maps.get("companyId"))
                .claim("apis", maps.get("apis"))
                .claim("companyName", maps.get("companyName"));
        String compact = claim.compact();
        return compact;
    }



    public Claims getJwts(String jwt){
        Claims body = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
        return body;
    }
}
