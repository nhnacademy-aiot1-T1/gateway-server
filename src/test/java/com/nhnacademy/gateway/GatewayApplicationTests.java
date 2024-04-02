package com.nhnacademy.gateway;

import com.nhnacademy.gateway.filter.JwtAuthorizationHeaderFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GatewayApplicationTests {

	@Test
	void contextLoads() {
		String sub = "1234567890";
		String name = "John Doe";
		int iat = 1516239022;

		String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.iYgwNqmNCBGCscaNbZtAYWK_GT4m5kj3zrll-glmT_8";
		String secretKey = "DNIoanoisdfoinaeihihiasdvnunbui092093naskdnfncnjDNnieindfijDNQUhE";

		JwtParser jwtParser = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build();

		Jws<Claims> claimsJws = jwtParser.parseClaimsJws(accessToken);
		Assertions.assertEquals(sub, claimsJws.getBody().get("sub").toString());
		Assertions.assertEquals(name, claimsJws.getBody().get("name"));
		Assertions.assertEquals(iat, claimsJws.getBody().get("iat"));
	}

}
