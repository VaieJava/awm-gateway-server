package com.outdd.gateway.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	    public static final String SECRET = "123";
	    public static final String TOKEN_PREFIX = "Bearer";
	    public static final String HEADER_AUTH = "Authorization";

	    public static String generateToken(String user) {
	        HashMap<String, Object> map = new HashMap<>();
	        map.put("id", new Random().nextInt());
	        map.put("user", user);
	        String jwt = Jwts.builder()
    			  .setSubject("user info").setClaims(map)
    			  .signWith(SignatureAlgorithm.HS512, SECRET)
    			  .compact();
	        String finalJwt = TOKEN_PREFIX + " " +jwt;
	        return finalJwt;
	    }

	    public static Map<String,String> validateToken(String token) {
	        if (token != null) {
	        	HashMap<String, String> map = new HashMap<String, String>();
//	            Map<String,Object> body = Jwts.parser()
//	                    .setSigningKey(SECRET)
//	                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
//	                    .getBody();
				Claims body = getClaimsFromToken(token.replace(TOKEN_PREFIX, "").replace(" ", ""));
	            String id =  String.valueOf(body.get("id"));
	            String user = (String) (body.get("user"));
	            map.put("id", id);
	            map.put("user", user);
	            if(StringUtils.isEmpty(user)) {
					throw new PermissionException("user is error, please check");
	            }
	            return map;
	        } else {
	        	throw new PermissionException("token is error, please check");
	        }
	    }

	/**
	 * 利用jwt解析token信息.
	 * @param token 要解析的token信息
	 * @return
	 * @throws Exception
	 */
	private static Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser()
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			claims = null;
		}
		return claims;
	}

}