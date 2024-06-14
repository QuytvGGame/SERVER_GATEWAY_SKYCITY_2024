package com.ggame.database;

public class MD5 {
	private int state[] = new int[4];
	private int count;
	private int buffer[] = new int[16];

	private static int T[] = new int[64];

	static {
		for (int i = 0; i != 64; i++)
			T[i] = (int) (long) (4294967296.0 * Math.abs(Math.sin(i + 1)));
	}

	MD5() {
		setup();
	}

	private void setup() {
		count = 0;

		state[0] = 0x67452301;
		state[1] = 0xefcdab89;
		state[2] = 0x98badcfe;
		state[3] = 0x10325476;

		for (int i = 0; i != 16; i++)
			buffer[i] = 0;
	}

	private static void Transform(int state[], int buffer[]) {
		int a = state[0];
		int b = state[1];
		int c = state[2];
		int d = state[3];

		for (int i = 0; i != 64; i++) {
			int t;
			int ss =
			/* 7 c 11 16 5 9 e 14 4 b 10 17 6 a f 15 */
			"GLQVGLQVGLQVGLQVEINTEINTEINTEINTDKPWDKPWDKPWDKPWFJOUFJOUFJOUFJOU".charAt(i) & 0x1F;

			switch (i >> 4) {
			default:
			case 0:
				t = (c ^ d) & b ^ d;
				break;
			case 1:
				t = (b ^ c) & d ^ c;
				break;
			case 2:
				t = b ^ c ^ d;
				break;
			case 3:
				t = (~d | b) ^ c;
				break;
			}

			t += a + T[i] + buffer[
			/*
			 * 0123456789abcdef 16b05af49e38d27c 58be147ad0369cf2
			 * 07e5c3a18f6d4b29
			 */
			("@ABCDEFGHIJKLMNO" + "AFK@EJODINCHMBGL" + "EHKNADGJM@CFILOB" + "@GNELCJAHOFMDKBI").charAt(i) & 0xF];

			t = b + (t << ss | t >>> 32 - ss);
			a = d;
			d = c;
			c = b;
			b = t;
		}
		state[0] += a;
		state[1] += b;
		state[2] += c;
		state[3] += d;
	}

	private void Update(byte b) {
		buffer[count >> 2 & 0xF] |= (b & 0xFF) << (count << 3 & 0x18);

		if ((++count & 0x3F) == 0) {
			Transform(state, buffer);
			for (int i = 0; i != 16; i++)
				buffer[i] = 0;
		}
	}

	private void UpdateASCII(String s) {
		int l = s.length();

		for (int i = 0; i != l; i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 127)
				Update((byte) c);
		}
	}

	private void Update(String s) {
		int l = s.length();
		for (int i = 0; i != l; i++) {
			char c = s.charAt(i);
			Update((byte) (c >> 8));
			Update((byte) c);
		}
	}

	private void Update(byte[] b) {
		int l = b.length;

		for (int i = 0; i != l; i++)
			Update(b[i]);
	}

	private byte[] result() {
		byte[] r = new byte[16];
		long c = count * 8;

		Update((byte) 0x80);

		while ((count & 0x3F) != 0x38)
			Update((byte) 0);

		buffer[14] = (int) c;
		buffer[15] = (int) (c >> 32);

		Transform(state, buffer);

		for (int i = 0; i != 16; i++)
			r[i] = (byte) (state[i >> 2] >> (i << 3 & 0x18));

		setup();

		return r;
	}

	protected void finalize() {
		setup();
	}

	static public byte[] md5ASCII(String s) {
		MD5 c = new MD5();

		c.UpdateASCII(s);

		return c.result();
	}

	static public byte[] md5(byte[] b) {
		MD5 c = new MD5();

		c.Update(b);

		return c.result();
	}

	static public byte[] md5(String s) {
		MD5 c = new MD5();

		c.Update(s);

		return c.result();
	}

	static public String byteToHex(byte[] b) {
		int l = b.length;
		char r[] = new char[l * 2];

		for (int i = 0; i < l * 2; i++)
			r[i] = "0123456789abcdef".charAt(b[i >> 1] >> 4 - (i << 2 & 4) & 0xF);

		return new String(r);
	}

	static public String hash(String s) {
		MD5 c = new MD5();

		c.UpdateASCII(s);

		return byteToHex(c.result());
	}

	public static void main(String[] args) {
//		System.out.println(MD5.);

	}
}
