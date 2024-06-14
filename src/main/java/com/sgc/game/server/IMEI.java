package com.sgc.game.server;

import java.io.*;

public class IMEI {
	int sumDig(int n) // Function for finding and returning sum of digits of a
						// number
	{
		int a = 0;
		while (n > 0) {
			a = a + n % 10;
			n = n / 10;
		}
		return a;
	}

	public static void main(String args[]) throws IOException {
		IMEI ob = new IMEI();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("Enter a 15 digit IMEI code : ");
		long n = Long.parseLong(br.readLine()); // 15 digits cannot be stored in
												// 'int' data type

		String s = Long.toString(n); // Converting the number into String for
										// finding length
		int l = s.length();

		if (l != 15) // If length is not 15 then IMEI is Invalid
			System.out.println("Output : Invalid Input");
		else {
			int d = 0, sum = 0;
			for (int i = 15; i >= 1; i--) {
				d = (int) (n % 10);

				if (i % 2 == 0) {
					d = 2 * d; // Doubling every alternate digit
				}

				sum = sum + ob.sumDig(d); // Finding sum of the digits

				n = n / 10;
			}

			System.out.println("Output : Sum = " + sum);

			if (sum % 10 == 0)
				System.out.println("Valid IMEI Code");
			else
				System.out.println("Invalid IMEI Code");
		}
	}

	public boolean check(String imei) {
		if (imei.length() == 16 || imei.length() == 15) {
			try {
				Long.parseLong(imei);
				return false;
			} catch (Exception e) {
				return true;
			}
		}
		else{
			return false;
		}
	}
}
