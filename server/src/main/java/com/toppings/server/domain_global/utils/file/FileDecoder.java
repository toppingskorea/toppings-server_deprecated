package com.toppings.server.domain_global.utils.file;

import javax.xml.bind.DatatypeConverter;

public class FileDecoder {

	public static byte[] base64StringToByteArray(String base64) {
		return DatatypeConverter.parseBase64Binary(base64.substring(base64.indexOf(",") + 1));
	}
}
