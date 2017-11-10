package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
		Queue<Statement> queue = new LinkedList<Statement>();
		queue.addAll(statements);
		while (!queue.isEmpty()) {
			//for (int k = 0; k < 2; k++) {
			Statement s1 = queue.remove();
			for (int i = 0; i < statements.size(); i++) {
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
						s3.print();
					if (s3 != null) {
						if (s3.resultContradict()) {
							System.out.println("Falseeeeeeeeeeeeeeeeeeeeeeeee");
							return false;
						} else {
							//s3.print();
							statements.add(s3);
							queue.add(s3);
						}
					}
				}
			}
		}
		return true;
	}

}
