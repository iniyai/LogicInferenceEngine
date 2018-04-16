package main;

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
	public Map<String, String> unifyPredicate(Predicate that) throws UnifyException {
		Map<String, String> varMap = new HashMap<String, String>();
		boolean allVariables = true;
		for (int i = 0; i < this.arguments.length; i++) {
			if (this.arguments[i].isConst()) {
				allVariables = false;
				if (that.arguments[i].isConst()) {
					if (!this.arguments[i].equals(that.arguments[i])) {
						throw new UnifyException(
								this.arguments[i].sName() + "cannot be unified with " + that.arguments[i].sName());
					}
				} else {
					varMap.put(that.arguments[i].sName(), this.arguments[i].sName());
				}
			} else {
				if (that.arguments[i].isConst()) {
					allVariables = false;
				}
				varMap.put(this.arguments[i].sName(), that.arguments[i].sName());
			}
		}
		if (allVariables) {
			throw new UnifyException("No point in unifying two variables");
		}
		return varMap;
	}

	public boolean hasSameVar() {
		for (int i = 0; i < arguments.length; i++) {
			for (int j = i + 1; j < arguments.length; j++) {
				if (arguments[i].equals(arguments[j])) {
					return true;
				}
			}
		}
		return false;
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
		if (arguments.length != other.arguments.length) {
			return false;
		}
		for (int i = 0; i < arguments.length; i++) {
			if (arguments[i].isConst() ^ other.arguments[i].isConst()) {
				return false;
			}
			if (arguments[i].isConst() && other.arguments[i].isConst() && !arguments[i].equals(other.arguments[i])) {
				return false;
			}
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

	public boolean equals(Object obj, Map<String, String> varLMap, Map<String, String> varRMap) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Predicate other = (Predicate) obj;
		if (arguments.length != other.arguments.length) {
			return false;
		}
		for (int i = 0; i < arguments.length; i++) {
			Symbol thisArg = arguments[i];
			Symbol thatArg = other.arguments[i];
			if (thisArg.isConst() ^ thatArg.isConst()) {
				return false;
			} else if (thisArg.isConst()) {
				if (!thisArg.equals(thatArg))
					return false;
			} else {
				//				System.out.println("Map");
				//				System.out.println(varLMap);
				//				System.out.println(varRMap);
				//				System.out.println(thisArg);
				//				System.out.println(thatArg);
				if (varLMap.containsKey(thisArg.sName())) {
					if (!thatArg.sName().equals(varLMap.get(thisArg.sName())))
						return false;
				} else if (varRMap.containsKey(thatArg.sName())) {
					if (!thisArg.sName().equals(varRMap.get(thatArg.sName())))
						return false;
				} else {
					varLMap.put(thisArg.sName(), thatArg.sName());
					varRMap.put(thatArg.sName(), thisArg.sName());
				}
			}
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
