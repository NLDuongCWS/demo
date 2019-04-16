package com.example.demo.controller;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.version2.Paseto;

import net.aholbrook.paseto.meta.PasetoBuilders;
import net.aholbrook.paseto.service.PublicTokenService;
import net.aholbrook.paseto.service.Token;
import net.aholbrook.paseto.service.TokenService;
import net.aholbrook.paseto.util.Hex;

@RestController
@RequestMapping("/test")
public class TestController {
	@GetMapping("/one")
	public ResponseEntity<?> one() {
		String priKey = "32aa18a5fcfa9f2a2e7b6a7d159037fac31451a9421509f45a5313650825754fc12f34ed0ea6bdc3af569e5bcbb1f9dd78f09ab42bdafe0c987c66ccd7f70727";
		byte[] privateKey = Hex.decode(priKey);

		String signedToken = Paseto.sign(privateKey, "{\"id\":123,\"jsonrpc\":\"2.0\",\"method\":\"eapi_health\"}", "");

		return new ResponseEntity<>(signedToken, HttpStatus.OK);
	}

	@GetMapping("/two")
	public ResponseEntity<?> two() {
		TokenService<Token> tokenService = PasetoBuilders.V2.publicService(rfcPublicKeyProvider(), Token.class)
				.withDefaultValidityPeriod(Duration.ofDays(15)).build();

		Token token = new Token();
		token.setTokenId("123");		
		String encoded = tokenService.encode(token);

		return new ResponseEntity<>(encoded, HttpStatus.OK);
	}

	private static PublicTokenService.KeyProvider rfcPublicKeyProvider() {
		return new PublicTokenService.KeyProvider() {
			@Override
			public byte[] getSecretKey() {
				byte[] key1 = Hex.decode(
						"32aa18a5fcfa9f2a2e7b6a7d159037fac31451a9421509f45a5313650825754fc12f34ed0ea6bdc3af569e5bcbb1f9dd78f09ab42bdafe0c987c66ccd7f70727");
				return key1;
			}

			@Override
			public byte[] getPublicKey() {
				byte[] dbKey = Hex.decode("df8620cb97684121f7265e262c31cbb7a0085c67095eb0d4bd81fbff415dc4a6");
				return dbKey;
			}
		};
	}
}