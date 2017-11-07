package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Statement {

	private static final String OR_SEPARATOR = "\\s\\|\\s";

	public List<Predicate> predicates;

	public Statement(String input) {
		predicates = new ArrayList<Predicate>();
		construct(input);
	}

	public Statement() {
		predicates = new ArrayList<Predicate>();
	}

	public void construct(String input) {
		//System.out.println(input);
		String[] arr = input.split(OR_SEPARATOR);
		for (int i = 0; i < arr.length; i++) {
			//System.out.println(arr[i]);
			predicates.add(new Predicate(arr[i]));
		}
	}

	public Statement unify(Statement that) {
		for (Predicate first : this.predicates) {
			for (Predicate second : that.predicates) {
				if (first.isSamePredicate(second) && (first.isNegated && !second.isNegated)
						|| (!first.isNegated && second.isNegated)) {
					Map<String, String> varMap = first.unifyPredicate(second);
					if (varMap == null)
						return null;
					return unifyHelper(that, varMap, first.name);
				}
			}
		}
		return null;
	}

	private Statement unifyHelper(Statement that, Map<String, String> varMap, String name) {
		Statement ret = new Statement();
		List<Predicate> predList = new ArrayList<Predicate>();

		for (Predicate p : this.predicates) {
			if (!p.name.equals(name)) {
				Predicate newPred = new Predicate(p.name, p.arguments, p.isNegated);
				for (Symbol sym : newPred.arguments) {
					if (varMap.containsKey(sym.sName())) {
						sym.setSymbol(new Symbol(varMap.get(sym.sName())));
					}
				}
				predList.add(newPred);
			}
		}

		for (Predicate p : that.predicates) {
			if (!p.name.equals(name)) {
				Predicate newPred = new Predicate(p.name, p.arguments, p.isNegated);
				for (Symbol sym : p.arguments) {
					if (varMap.containsKey(sym.sName())) {
						sym.setSymbol(new Symbol(varMap.get(sym.sName())));
					}
				}
				predList.add(newPred);
			}
		}
		ret.predicates = predList;
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Statement other = (Statement) obj;
		if (predicates == null) {
			if (other.predicates != null)
				return false;
		} else if (!predicates.equals(other.predicates))
			return false;
		return true;
	}

	public void print() {
		for (Predicate p : predicates) {
			p.print();
		}
		System.out.println("****");
	}
}
