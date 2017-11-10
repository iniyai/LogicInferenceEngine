package main;

public class Symbol {
	private String constVal;
	//private int varVal;
	private boolean isConstant;

	public Symbol(String val) {
		this.constVal = val;
		char firstLetter = val.charAt(0);
		isConstant = ((firstLetter >= 'A') && (firstLetter <= 'Z'));
		//		if (!isConstant) {
		//			this.constVal += Integer.toString(counter);
		//			counter++;
		//		}
	}

	public void setSymbol(Symbol symbol) {
		this.constVal = symbol.constVal;
		this.isConstant = symbol.isConstant;
	}

	public void setSymbol(String s) {
		this.constVal = s;
		this.isConstant = ((s.charAt(0) >= 'A') && (s.charAt(0) <= 'Z'));
	}

	public Symbol(Symbol symbol) {
		setSymbol(symbol);
	}

	public boolean isConst() {
		return isConstant;
	}

	public String sName() {
		return constVal;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Symbol other = (Symbol) obj;
		if (constVal == null) {
			if (other.constVal != null)
				return false;
		} else if (!constVal.equals(other.constVal))
			return false;
		if (isConstant != other.isConstant)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return constVal + " " + isConstant;
	}

}
