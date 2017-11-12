package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Statement {

	private static final String OR_SEPARATOR = "\\s\\|\\s";

	public List<Predicate> predicates;
	public Set<String> variables;
	private static int counter = 0;

	public Statement(String input) {
		predicates = new ArrayList<Predicate>();
		variables = new HashSet<String>();
		construct(input);
		standardizeVars();
	}

	public Statement(String input, boolean isTest) {
		predicates = new ArrayList<Predicate>();
		variables = new HashSet<String>();
		construct(input);
		if (!isTest) {
			standardizeVars();
		}
	}

	public Statement() {
		predicates = new ArrayList<Predicate>();
	}

	public void construct(String input) {
		//System.out.println(input);
		String[] arr = input.split(OR_SEPARATOR);
		for (int i = 0; i < arr.length; i++) {
			//System.out.println(arr[i]);
			Predicate p = new Predicate(arr[i]);
			predicates.add(p);
			variables.addAll(p.variables);
		}
	}

	public void removeRedundant() {
		for (int i = 0; i < predicates.size(); i++) {
			for (int j = i + 1; j < predicates.size(); j++) {
				if (predicates.get(i).equals(predicates.get(j))) {
					predicates.remove(i);
				}
			}
		}
	}

	public void inferTautology() {
		for (int i = 0; i < predicates.size(); i++) {
			for (int j = i + 1; j < predicates.size(); j++) {
				if (predicates.get(i).equals(predicates.get(j))) {
					predicates.remove(i);
					predicates.remove(j);
				}
			}
		}
	}

	public void standardizeVars() {
		//System.out.println("Counter: " + counter);
		Map<String, String> varHash = new HashMap<String, String>();
		for (String s : this.variables) {
			varHash.put(s, s + Integer.toString(counter++));
		}
		for (Predicate p : this.predicates) {
			p.standardize(varHash);
		}
	}

	public boolean resultContradict() {
		return (this.predicates.isEmpty());
	}

	public boolean isContradict(Statement that) {
		if (this.predicates.size() != 1) {
			return false;
		}
		if (that.predicates.size() != 1) {
			return false;
		}
		if (this.predicates.get(0).isContradict(that.predicates.get(0))) {
			return true;
		}
		return false;
	}

	public Statement unify(Statement that) {
		for (Predicate first : this.predicates) {
			for (Predicate second : that.predicates) {
				if (first.isSamePredicate(second) && ((first.isNegated && !second.isNegated)
						|| (!first.isNegated && second.isNegated))) { // first unifies with second
					Map<String, String> varMap;
					//System.out.println("main: " + first.name);
					try {
						varMap = first.unifyPredicate(second);
					} catch (UnifyException e) {
						continue;
					}
					// something can be unified for sure
					return unifyHelper(that, varMap, first.name, first.isNegated, first, second);
				}
			}
		}
		return null;
	}

	private Statement unifyHelper(Statement that, Map<String, String> varMap, String name, boolean firstBool,
			Predicate first, Predicate second) {
		Statement ret = new Statement();
		List<Predicate> predList = new ArrayList<Predicate>();

		//System.out.println("helper" + name);

		for (Predicate p : this.predicates) {
			//if (!(p.name.equals(name) && p.isNegated == firstBool)) {
			if (!p.equals(first)) {
				Predicate newPred = new Predicate(p.name, p.arguments, p.isNegated);
				for (Symbol sym : newPred.arguments) {
					if (!sym.isConst() && varMap.containsKey(sym.sName())) {
						sym.setSymbol(varMap.get(sym.sName()));
					}
				}
				predList.add(newPred);
			}
		}

		for (Predicate p : that.predicates) {
			//if (!(p.name.equals(name) && p.isNegated == !firstBool)) {
			if (!p.equals(second)) {
				Predicate newPred = new Predicate(p.name, p.arguments, p.isNegated);
				//newPred.print();
				for (Symbol sym : newPred.arguments) {
					if (!sym.isConst() && varMap.containsKey(sym.sName())) {
						//System.out.println(sym.sName() + " &^$%%^ " + varMap.get(sym.sName()));
						sym.setSymbol(varMap.get(sym.sName()));
						//System.out.println("sName inside: " + sym.sName());
					}
				}
				//System.out.println("sName outside: " + newPred.arguments[0].sName());
				predList.add(newPred);
			}
		}
		ret.predicates = predList;
		ret.removeRedundant();
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
			System.out.print(" | ");
		}
		System.out.println("");
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Predicate p : predicates) {
			sb.append(p.toString());
			sb.append(" | ");
		}
		System.out.println("");
		return sb.toString();
	}

}
