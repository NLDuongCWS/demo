package com.example.demo.controller;

import java.nio.charset.StandardCharsets;

import net.consensys.cava.crypto.sodium.CryptoCavaWrapper;
import okio.Buffer;

public class Util {

	private Util() {
	}

	/**
	 * Authentication Padding
	 * <p>
	 * https://github.com/paragonie/paseto/blob/master/docs/01-Protocol-Versions/Common.md#pae-definition
	 *
	 * @param pieces string[] of the pieces
	 */
	public static String pae(String... pieces) {
		try (Buffer accumulator = new Buffer()) {
			accumulator.writeLongLe(pieces.length);

			for (String piece : pieces) {
				accumulator.writeLongLe(piece.length());
				accumulator.writeUtf8(piece);
			}
			return accumulator.snapshot().hex();
		}
	}

	public static byte[] pae(byte[]... pieces) {
		try (Buffer accumulator = new Buffer()) {
			accumulator.writeLongLe(pieces.length);

			for (byte[] piece : pieces) {
				accumulator.writeLongLe(piece.length);
				accumulator.write(piece);
			}
			return accumulator.snapshot().toByteArray();
		}
	}

	public static byte[] hexToBytes(String hex) {
		return CryptoCavaWrapper.hexToBin(hex.getBytes(StandardCharsets.UTF_8));
	}
}