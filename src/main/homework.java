package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class homework {
	private static final String INPUT_FILENAME = "input.txt";
	private static final String OUTPUT_FILENAME = "output.txt";
	private static PrintWriter out = null;
	private static BufferedReader br = null;
	private static FileReader fr = null;

	public static void main(String[] args) {

		int nq = 0;
		int ns = 0;
		double k1 = 0;
		double k2 = 0;
		String[] queries;
		List<String> sentences = new ArrayList<String>();

		try {
			out = new PrintWriter(new FileWriter(OUTPUT_FILENAME));
			fr = new FileReader(INPUT_FILENAME);
			br = new BufferedReader(fr);

			nq = Integer.valueOf(br.readLine());
			queries = new String[nq];
			for (int i = 0; i < nq; i++) {
				String query = br.readLine();
				queries[i] = query;
			}
			ns = Integer.valueOf(br.readLine());
			for (int i = 0; i < ns; i++) {
				String s = br.readLine();
				sentences.add(s);
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

		solve(nq, ns, 1000, k1, k2); // Passing remaining time in milliseconds.
		out.close();
	}

	private static void solve(int n, int p, double budgetTime, double k1, double k2) {
		long startTime = System.currentTimeMillis();
		System.out.println(System.currentTimeMillis() - startTime);
	}

}
