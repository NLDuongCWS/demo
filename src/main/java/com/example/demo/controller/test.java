package com.example.demo.controller;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getUrlDecoder;
import static java.util.Base64.getUrlEncoder;

import java.security.MessageDigest;
import java.security.SignatureException;
import java.time.Duration;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Verify;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;

import net.aholbrook.paseto.meta.PasetoBuilders;
import net.aholbrook.paseto.service.KeyId;
import net.aholbrook.paseto.service.PublicTokenService;
import net.aholbrook.paseto.service.Token;
import net.aholbrook.paseto.service.TokenService;
import net.aholbrook.paseto.util.Hex;
import net.consensys.cava.crypto.sodium.CryptoCavaWrapper;


@RestController
@RequestMapping("/test")
public class test {
  @GetMapping("/")
  public ResponseEntity<?> search() {

	  //local
	  /*byte[] key = Hex.decode("707172737475767778797a7b7c7d7e7f808182838485868788898a8b8c8d8e8f");
	  TokenService<Token> tokenService = PasetoBuilders.V2.localService(() -> key, Token.class)
	      .withDefaultValidityPeriod(Duration.ofDays(15))
	      .build();

	  Token token = new Token();
	  token.setTokenId("example-id"); // A session key, user id, etc.

	  String encoded = tokenService.encode(token);
	  
	  KeyId footer = new KeyId();
	  footer.setKeyId("1"); // first key we're using
	  encoded = tokenService.encode(token, footer);	
	 
	  Token decoded = tokenService.decode(encoded);
	  decoded = tokenService.decode(encoded, footer);*/
	  
	  
	  //public
	  //byte[] key1 = Hex.decode("11324397f535562178d53ff538e49d5a162242970556b4edd950c87c7d86648a");
	  byte[] key = Hex.decode("1eb9dbbbbc047c03fd70604e0071f0987e16b28b757225c11f00415d0e20b1a2");
	  TokenService<Token> tokenService = PasetoBuilders.V2.publicService(rfcPublicKeyProvider(), Token.class)
	      .withDefaultValidityPeriod(Duration.ofDays(15))
	      .build();

	  Token token = new Token();
	  token.setTokenId("example-id"); // A session key, user id, etc.

	  String encoded = tokenService.encode(token);
	  
	  KeyId footer = new KeyId();
	  footer.setKeyId("1"); // first key we're using
	  encoded = tokenService.encode(token, footer);	
	  //encoded = "v2.public.ImV5SnFjMjl1Y25Caklqb2lNaTR3SWl3aWFXUWlPakV5TXl3aWJXVjBhRzlrSWpvaVpXRndhVjkwYjJ0bGJsOWtaWEJzYjNraUxDSndZWEpoYlhNaU9uc2laR1ZqYVcxaGJITWlPakV3TURBc0ltNWhiV1VpT2lKRGJHVmhjbUp5YVdSblpTSXNJbk41YldKdmJDSTZJa05PUWlKOWZRPT0iaE16_eqx0AoYLmduLMtR7bIQUJjOyUaJGC7cUHkRa_uZgyy6iX8kB5461mSREBO_bYR-ET3TVOYrPJ0xYxoyBA";
	 
	  Token decoded = tokenService.decode(encoded);
	  decoded = tokenService.decode(encoded);
	  ResponsePaseto o = new ResponsePaseto();
	  o.decoded = decoded;
	  o.encoded = encoded;
	  String priKey= "32aa18a5fcfa9f2a2e7b6a7d159037fac31451a9421509f45a5313650825754fc12f34ed0ea6bdc3af569e5bcbb1f9dd78f09ab42bdafe0c987c66ccd7f70727";
	 
	 
	  String str = sign(Hex.decode(priKey),"{\r\n" + 
	  		"    \"jsonrpc\": \"2.0\",\r\n" + 
	  		"    \"id\": 123,\r\n" + 
	  		"    \"method\": \"eapi_token_deploy\",\r\n" + 
	  		"    \"params\":\r\n" + 
	  		"    {\r\n" + 
	  		"    	\"decimals\":1000,\r\n" + 
	  		"    	\"name\":\"Clearbridge\",\r\n" + 
	  		"    	\"symbol\":\"CNB\"\r\n" + 
	  		"    }\r\n" + 
	  		"}","");
    return new ResponseEntity<>(str , HttpStatus.OK);
  }
  
  public class ResponsePaseto {
	  public String encoded;
	  public Object decoded;
  }
  private static final String PUBLIC = "v2.public."; 

  /**
   * Sign the token, https://github.com/paragonie/paseto/blob/master/docs/01-Protocol-Versions/Version2.md#sign
   */
  static String sign(byte[] privateKey, String payload, String footer) {
      Preconditions.checkNotNull(privateKey);
      Preconditions.checkNotNull(payload);
      Preconditions.checkArgument(privateKey.length == 64, "Private signing key should be 64 bytes");

      byte[] m2 = BaseEncoding.base16().lowerCase().decode(Util.pae(PUBLIC, payload, footer));
      byte[] signature = new byte[64];
      CryptoCavaWrapper.cryptoSignDetached(signature, m2, privateKey);

      String signedToken = PUBLIC + getUrlEncoder().withoutPadding().encodeToString(Bytes.concat(payload.getBytes(UTF_8), signature));

      if (!Strings.isNullOrEmpty(footer)) {
          signedToken = signedToken + "." + getUrlEncoder().withoutPadding().encodeToString(footer.getBytes(UTF_8));
      }
      return signedToken;
  }

  /**
   * Parse the token, https://github.com/paragonie/paseto/blob/master/docs/01-Protocol-Versions/Version2.md#verify
   */
  static String parse(byte[] publicKey, String signedMessage, String footer) throws SignatureException {
      Preconditions.checkNotNull(publicKey);
      Preconditions.checkNotNull(signedMessage);
      Preconditions.checkArgument(publicKey.length == 32, "Public key should be 32 bytes");

      String[] tokenParts = signedMessage.split("\\.");

      //1
      if (!Strings.isNullOrEmpty(footer)) {
          Verify.verify(MessageDigest.isEqual(getUrlDecoder().decode(tokenParts[3]), footer.getBytes(UTF_8)), "footer does not match");
      }

      //2
      Verify.verify(signedMessage.startsWith(PUBLIC), "Token should start with " + PUBLIC);

      //3
      byte[] sm = getUrlDecoder().decode(tokenParts[2]);
      byte[] signature = Arrays.copyOfRange(sm, sm.length - 64, sm.length);
      byte[] message = Arrays.copyOfRange(sm, 0, sm.length - 64);

      //4
      byte[] m2 = Util.pae(PUBLIC.getBytes(UTF_8), message, footer.getBytes(UTF_8));

      //5
      verify(publicKey, m2, signature);

      return new String(message);
  }

  private static void verify(byte[] key, byte[] message, byte[] signature) throws SignatureException {
      int valid = CryptoCavaWrapper.cryptoSignVerifyDetached(signature, message, key);
      if (valid != 0) {
          throw new SignatureException("Invalid signature");
      }
  }
  private static PublicTokenService.KeyProvider rfcPublicKeyProvider() {
		return new PublicTokenService.KeyProvider() {

			@Override
			public byte[] getSecretKey() {
				//byte[] key1 = Hex.decode("b4cbfb43df4ce210727d953e4a713307fa19bb7d9f85041438d9e11b942a377"
						//+ "41eb9dbbbbc047c03fd70604e0071f0987e16b28b757225c11f00415d0e20b1a2");
				byte[] key1 = Hex.decode("b4cbfb43df4ce210727d953e4a713307fa19bb7d9f85041438d9e11b942a37741eb9dbbbbc047c03fd70604e0071f0987e16b28b757225c11f00415d0e20b1a2");
				
				return key1;
			}

			@Override
			public byte[] getPublicKey() {
				//byte[] dbKey = Hex.decode("1eb9dbbbbc047c03fd70604e0071f0987e16b28b757225c11f00415d0e20b1a"						//+ "2");
				byte[] dbKey = Hex.decode("1eb9dbbbbc047c03fd70604e0071f0987e16b28b757225c11f00415d0e20b1a2");
				
				return dbKey;
			}
		};
	}

}