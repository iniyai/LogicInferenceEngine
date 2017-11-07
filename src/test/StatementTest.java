package test;

import org.junit.Assert;
import org.junit.Test;

import main.Predicate;
import main.Statement;

public class StatementTest {

	@Test
	public void construct() {
		Statement statement = new Statement("F(x,Alpha) | G(x)");
		Assert.assertEquals(2, statement.predicates.size());
		String[] expPredStrings = new String[] { "F(x,Alpha)", "G(x)" };
		for (int i = 0; i < statement.predicates.size(); i++) {
			Predicate p = statement.predicates.get(i);
			Predicate expected = new Predicate(expPredStrings[i]);
			Assert.assertEquals(p, expected);
		}
	}

	@Test
	public void unifierReturnsCorrectStatement1() {
		String inp1 = "F(x,Alpha) | ~G(x)";
		String inp2 = "G(y) | H(z)";
		String out = "F(y,Alpha) | H(z)";
		checkUnifier(inp1, inp2, out);
		//s3.print();
	}

	@Test
	public void unifierReturnsCorrectStatement2() {
		String inp1 = "~F(x,Alpha) | G(x)";
		String inp2 = "G(y) | H(z)";
		String out = "";
		checkUnifier(inp1, inp2, out);
		//s3.print();
	}

	@Test
	public void unifyTwoConstants() {
		String inp1 = "~F(x,Alpha) | G(x)";
		String inp2 = "F(y,Beta) | H(z)";
		String out = "";
		checkUnifier(inp1, inp2, out);
		//s3.print();
	}

	private void checkUnifier(String inp1, String inp2, String out) {
		Statement s1 = new Statement(inp1);
		Statement s2 = new Statement(inp2);
		Statement s3 = s1.unify(s2);
		Statement expected = null;
		if (!out.equals("")) {
			expected = new Statement(out);
			Assert.assertEquals(expected, s3);
		} else {
			Assert.assertNull(expected);
		}
	}
}
