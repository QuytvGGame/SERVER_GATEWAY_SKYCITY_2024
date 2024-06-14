package com.ggame.accountservice;

public class Dijkstra {
	public static int onDijkstra(int[][] g, int n, int a, int b, int[] p) {
		// khoảng cách ngắn nhất từ đỉnh a đến đỉnh i
		int[] len = new int[n];
		// i > 0 là đỉnh i đã được duyệt
		int[] s = new int[n];
		int sum = 0;
		// tính giá trị vô cùng (sum)
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				sum += g[i][j];
			}
		}
		// đạt vô cùng cho tất cả các cặp không nối với nhau
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i != j && g[i][j] == 0) {
					g[i][j] = sum;
				}
			}
		}

		for (int i = 0; i < n; i++) {
			len[i] = sum; // khởi tạo độ dài từ đỉnh a đến mỗi đỉnh là vô cùng
			s[i] = 0;
			p[i] = a; // điểm bắt đầu của mỗi điểm là a
		}

		len[a] = 0;
		int i;
		// trong khi điểm cuối chưa được xét
		while (s[b] == 0) {
			// tìm một vị trí mà không phải là vô cùng
			for (i = 0; i < n; i++) {
				if (s[i] == 0 && len[i] < sum)
					break;
			}
			// i>=n tức là duyệt hết các đỉnh mà không thể tìm thấy đỉnh b
			if (i >= n)
				return 0;

			// tìm điểm có vị trí mà độ dài là min
			for (int j = 0; j < n; j++) {
				if (s[j] == 0 && len[i] > len[j])
					i = j;
			}

			// cho i vào danh sách xét rồi
			s[i] = 1;

			// tính lại độ dài của các đỉnh chưa xét
			for (int j = 0; j < n; j++) {
				if (s[j] == 0 && len[i] + g[i][j] < len[j]) {
					len[j] = len[i] + g[i][j];// thay đổi len
					p[j] = i; // đánh dấu điểm trước đó

				}
			}

		}
		return len[b];
	}
}
