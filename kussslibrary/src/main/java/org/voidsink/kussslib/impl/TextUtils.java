package org.voidsink.kussslib.impl;

public final class TextUtils {
	private TextUtils() {
		throw new UnsupportedOperationException();
	}
	
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}
	
	public static boolean isEmpty(int i) {
		return i < 1;
	}
}

