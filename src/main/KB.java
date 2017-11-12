package main;

import java.util.ArrayList;
import java.util.List;

public class KB {
	public List<Statement> statements;

	public KB(String[] KBStrings) {
		statements = new ArrayList<Statement>();
		for (int i = 0; i < KBStrings.length; i++) {
			statements.add(new Statement(KBStrings[i]));
		}
	}

	public boolean resolve() {
		System.out.println("resolve");
		//Queue<Statement> queue = new LinkedList<Statement>();
		//queue.addAll(statements);
		//while (!queue.isEmpty()) {
		for (int k = 0; k < statements.size(); k++) {
			Statement s1 = statements.get(k);
			for (int i = k + 1; i < statements.size(); i++) {
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
							if (s3.resultContradict()) {
								s1.print();
								s2.print();
								System.out.println("Falseeeeeeeeeeeeeeeeeeeeeeeee");
								s3.print();
								return false;
							} else {
								s3.print();
								if (!statements.contains(s3)) {
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
