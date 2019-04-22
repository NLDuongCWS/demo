package com.example.demo.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.goterl.lazycode.lazysodium.LazySodiumJava;
import com.goterl.lazycode.lazysodium.SodiumJava;

import net.aholbrook.paseto.PasetoV2;
import net.aholbrook.paseto.base64.jvm8.Jvm8Base64Provider;
import net.aholbrook.paseto.crypto.v2.libsodium.LibSodiumV2CryptoProvider;
import net.aholbrook.paseto.encoding.EncodingProvider;
import net.aholbrook.paseto.encoding.json.jackson.JacksonJsonProvider;
import net.aholbrook.paseto.meta.PasetoBuilders;
import net.aholbrook.paseto.service.PublicTokenService;
import net.aholbrook.paseto.service.Token;
import net.aholbrook.paseto.service.TokenService;
import net.aholbrook.paseto.util.Hex;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private RestTemplate restTemplate;

	private static String token = null;

	@GetMapping("/one")
	public ResponseEntity<?> one() {

		EncodingProvider enco2 = new JacksonJsonProvider();

		LibSodiumV2CryptoProvider v2CryptoProvider = new LibSodiumV2CryptoProvider(
				new LazySodiumJava(new SodiumJava()));
		PasetoV2 paseto = new PasetoV2.Builder(new Jvm8Base64Provider(), enco2, v2CryptoProvider).build();
		String priKey = "32aa18a5fcfa9f2a2e7b6a7d159037fac31451a9421509f45a5313650825754fc12f34ed0ea6bdc3af569e5bcbb1f9dd78f09ab42bdafe0c987c66ccd7f70727";
		byte[] privateKey = Hex.decode(priKey);

		// String payload
		// ="{\"id\":123,\"jsonrpc\":\"2.0\",\"method\":\"eapi_health\"}";

		SignDto payloadValue = new SignDto();
		payloadValue.setId(63202);
		payloadValue.setJsonrpc("2.0");
		payloadValue.setMethod("eapi_token_deploy");
		
		CodeDto params = new CodeDto();
		params.setName("Capbridge");
		params.setSymbol("CPB");
		params.setDecimals(1);
		params.setTokenOwner("0xdb2430B4e9AC14be6554d3942822BE74811A1AF9");
		payloadValue.setParams(params);
		
          String payload="{\"id\":63202,\"jsonrpc\":\"2.0\",\"method\":\"eapi_token_deploy\",\"params\":{\"decimals\":0,\"name\":\"WPJAPDYRQE\",\"symbol\":\"SKW\",\"tokenOwner\":\"0xdb2430B4e9AC14be6554d3942822BE74811A1AF9\"}}";

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = null;
		try {
			json = ow.writeValueAsString(payloadValue);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JsonNode actualObj = null;
		try {
			actualObj = mapper.readTree(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(actualObj);
		token = paseto.sign(actualObj, privateKey, "");
		System.out.println(token);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<String>(json, headers);

		ResponseEntity<String> result = restTemplate.exchange("https://endor-pub-dev.herokuapp.com/", HttpMethod.POST,
				entity, String.class);
		return new ResponseEntity<>(result, HttpStatus.OK);
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