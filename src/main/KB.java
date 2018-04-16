package main;

import java.util.ArrayList;
import java.util.List;

public class KB {
	private static final int MAX_TIME = 10 * 1000;
	public List<Statement> statements;
	public int counter = 0;

	public KB(String[] KBStrings) {
		statements = new ArrayList<Statement>();
		for (int i = 0; i < KBStrings.length; i++) {
			statements.add(new Statement(KBStrings[i]));
		}
	}

	public boolean resolve() {
		long startTime = System.currentTimeMillis();
		//System.out.println("resolve");
		//Queue<Statement> queue = new LinkedList<Statement>();
		//queue.addAll(statements);
		//while (!queue.isEmpty()) {
		boolean newAddition = false;
		//do {
		newAddition = false;
		for (int k = 0; k < statements.size(); k++) {
			Statement s1 = statements.get(k);
			for (int i = 0; i < k; i++) {
				if (System.currentTimeMillis() - startTime > MAX_TIME) {
					return true;
				}
				final Statement s2 = statements.get(i);
				//System.out.println("AAAAAAAAAAAS1");
				//s1.print();
				//System.out.println("BBBBBBBBBBBS2");
				//System.out.println("prSize: " + s2.predicates.size());
				//s2.print();
				if (s1 != s2) {
					//					System.out.println("Statement S1");
					//					s1.print();
					//					System.out.println("Statement S2");
					//					s2.print();
					Statement s3 = s1.unify(s2);
					if (s3 != null)
						//s3.print();
						if (s3 != null) {
							s1.print();
							s2.print();
							s3.print();
							System.out.println("***********");
							if (s3.resultContradict()) {
								//								s1.print();
								//								s2.print();
								//								s3.print();
								//								System.out.println("Falseeeeeeeeeeeeeeeeeeeeeeeee");
								return false;
							} else {
								if (!statements.contains(s3)) {
									newAddition = true;
									statements.add(s3);
									//queue.add(s3);
								}
							}
						}
				}
			}
		}
		return true;
	}

	public void print() {
		for (int i = 0; i < statements.size(); i++) {
			System.out.println(statements.get(i));
		}
	}

}
