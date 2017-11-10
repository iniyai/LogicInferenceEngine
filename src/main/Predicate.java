package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Predicate {
	private static final String PREDICATE_VARIABLES = "[(),]";
	public String name;
	public Symbol[] arguments;
	public Set<String> variables;
	public boolean isNegated = false;

	public Predicate(String name, Symbol[] arguments, boolean isNegated) {
		this.name = name;
		this.isNegated = isNegated;
		this.arguments = new Symbol[arguments.length];
		for (int i = 0; i < this.arguments.length; i++) {
			this.arguments[i] = new Symbol(arguments[i]);
		}
	}

	public Predicate(String pred) {
		constructPredicate(pred);
	}

	public void standardize(Map<String, String> varHash) {
		for (int i = 0; i < arguments.length; i++) {
			if (varHash.containsKey(arguments[i].sName())) {
				arguments[i].setSymbol(varHash.get(arguments[i].sName()));
			}
		}
	}

	public boolean isContradict(Predicate that) { // return true if the names contradict
		if (!this.name.equals(that.name)) {
			//System.out.println(this.name + " " + that.name);
			return false;
		}
		if ((this.isNegated && that.isNegated) || (!this.isNegated && !that.isNegated)) {
			//System.out.println(this.isNegated + " " + that.isNegated);
			return false;
		}
		for (int i = 0; i < arguments.length; i++) {
			//System.out.println(this.arguments[i] + " " + that.arguments[i]);
			if (arguments[i].isConst() && that.arguments[i].isConst()
					&& !(arguments[i].sName().equals(that.arguments[i].sName()))) {
				return false;
			}
		}
		return true;
	}

	void constructPredicate(String pred) {
		if (pred.charAt(0) == '~') {
			isNegated = true;
			pred = pred.substring(1);
		}
		String[] arr = pred.split(PREDICATE_VARIABLES);
		//System.out.println(arr.length);
		name = arr[0];
		arguments = new Symbol[arr.length - 1];
		variables = new HashSet<String>();
		for (int i = 1; i < arr.length; i++) {
			arguments[i - 1] = new Symbol(arr[i]);
			if (!arguments[i - 1].isConst()) {
				variables.add(arguments[i - 1].sName());
			}
		}
	}

	public boolean isSamePredicate(Object obj) {
		if (!(obj instanceof Predicate)) {
			return false;
		}
		Predicate that = (Predicate) obj;
		return name.equals(that.name);
	}

	// predicates have the same name, and one of them is the negation of the other.
	public Map<String, String> unifyPredicate(Predicate that) {
		Map<String, String> varMap = new HashMap<String, String>();
		for (int i = 0; i < this.arguments.length; i++) {
			if (this.arguments[i].isConst()) {
				if (that.arguments[i].isConst()) {
					return null;
				}
				varMap.put(that.arguments[i].sName(), this.arguments[i].sName());
			} else {
				varMap.put(this.arguments[i].sName(), that.arguments[i].sName());
			}
		}
		return varMap;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Predicate other = (Predicate) obj;
		if (!Arrays.equals(arguments, other.arguments)) {
			return false;
		}
		if (isNegated != other.isNegated)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public void print() {
		//System.out.println("******");
		System.out.print((isNegated ? "~" : "") + name + "(");
		for (int i = 0; i < arguments.length; i++) {
			System.out.print(arguments[i].sName() + ",");
		}
		System.out.print(")");
		//System.out.println("******");
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		//System.out.println("******");
		sb.append((isNegated ? "~" : "") + name + "(");
		for (int i = 0; i < arguments.length; i++) {
			sb.append(arguments[i].sName() + ",");
		}
		sb.append(")");
		//System.out.println("******");
		return sb.toString();
	}

}
