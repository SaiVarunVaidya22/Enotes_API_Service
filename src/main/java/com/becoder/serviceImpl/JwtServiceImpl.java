package com.becoder.serviceImpl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.becoder.entity.User;
import com.becoder.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService{

	private String secretKey="";
	
	public JwtServiceImpl() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			// Encoding the generated Secret Key Using Base64 Encoders
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getId());
		claims.put("role", user.getRoles());
		claims.put("status", user.getStatus());
		
		String token = Jwts.builder()
		.claims().add(claims)
		.subject(user.getEmail())
		.issuedAt(new Date(System.currentTimeMillis()))
		.expiration(new Date(System.currentTimeMillis() + 60*60*100))
		.and()
		.signWith(getKey())
		.compact();
		
		return token;
	}

	private Key getKey() {
		// Decoding and returning the encoded Secret key usig Base64 Decoders
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String role(String token) {
		Claims claims = extractAllClaims(token);
		return (String)claims.get("role");
	}
	
	@Override
	public String extractUsername(String token) {
		Claims claims = extractAllClaims(token);
		return claims.getSubject();
	}
	
	private Claims extractAllClaims(String token) {
		Claims claims = Jwts.parser()
			.verifyWith(decryptKey(secretKey))
			.build()
			.parseSignedClaims(token)
			.getPayload();
		return claims;
	}

	private SecretKey decryptKey(String secretKey2) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey2);
		SecretKey sk = Keys.hmacShaKeyFor(keyBytes);
		return sk;
	}

	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {
		
		String username = extractUsername(token);
		Boolean isExpired = isTokenExpired(token);
		
		// Comparing both the User name and expiration date
		if(username.equalsIgnoreCase(userDetails.getUsername()) && !isExpired) {
			return true;
		}
		
		return false;
	}

	private Boolean isTokenExpired(String token) {
		Claims claims = extractAllClaims(token);
		Date expiredDate = claims.getExpiration();
		
		return expiredDate.before(new Date());
	}
}
