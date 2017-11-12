package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
		String[] queries = null;
		String[] statements = null;

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
			statements = new String[ns + 1];
			for (int i = 0; i < ns; i++) {
				String s = br.readLine();
				statements[i] = s;
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

		solve(nq, ns, queries, statements); // Passing remaining time in milliseconds.
		out.close();
	}

	private static void solve(int qs, int sts, String[] queries, String[] statements) {
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < qs; i++) {
			statements[sts] = ((queries[i].charAt(0) == '~') ? queries[i].substring(1) : "~" + queries[i]);
			KB kb = new KB(statements);
			kb.print();
			boolean result = kb.resolve();
			out.println(result ? "FALSE" : "TRUE");
		}
		//System.out.println(System.currentTimeMillis() - startTime);
	}

}
