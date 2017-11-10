package test;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import main.Predicate;
import main.Statement;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatementTest {

	@Test
	public void construct() {
		Statement statement = new Statement("F(x,Alpha) | G(x)");
		Assert.assertEquals(2, statement.predicates.size());
		String[] expPredStrings = new String[] { "F(x0,Alpha)", "G(x0)" };
		for (int i = 0; i < statement.predicates.size(); i++) {
			Predicate p = statement.predicates.get(i);
			Predicate expected = new Predicate(expPredStrings[i]);
			Assert.assertEquals(p, expected);
		}
	}

	@Test
	public void unifyBothPositiveReturnsNull() {
		String inp1 = "~F(x,Alpha) | G(x)"; // x1
		String inp2 = "G(y) | H(z)"; // y2, z3
		String out = "";
		checkUnifier(inp1, inp2, out);
	}

	@Test
	public void unifyTwoConstantsReturnsNull() {
		String inp1 = "~F(x,Alpha) | G(x)"; // x4
		String inp2 = "F(y,Beta) | H(z)"; // y5 z6
		String out = "";
		checkUnifier(inp1, inp2, out);
	}

	@Test
	public void unifyTwoVariables() {
		String inp1 = "F(x,Alpha) | ~G(x)"; // x7
		String inp2 = "G(y) | H(z)"; // y8, z9
		String out = "F(y8,Alpha) | H(z9)";
		checkUnifier(inp1, inp2, out);
	}

	@Test
	public void unifyVarsAndConstants() {
		String inp1 = "F(x,Alpha) | ~G(x)"; //x10
		String inp2 = "~F(y,z) | H(z)"; //y11 z12
		String out = "~G(y11) | H(Alpha)";
		checkUnifier(inp1, inp2, out);
	}

	@Test
	public void zexamplefromAss() {
		String inp1 = "Mother(Liz,Charlie)";
		String inp2 = "~Mother(x,y) | Parent(x,y)"; //x13 y14
		String out = "Parent(Liz,Charlie)";
		checkUnifier(inp1, inp2, out);
	}

	@Test
	public void zexamplefromAss2() {
		String inp1 = "Parent(Liz,Charlie)";
		String inp2 = "~Parent(x,y) | ~Ancestor(y,z) | Ancestor(x,z)"; //x15 y16 z17
		String out = "~Ancestor(Charlie,z17) | Ancestor(Liz,z17)";
		checkUnifier(inp1, inp2, out);
	}

	@Test
	public void zzContradict() {
		Statement s1 = new Statement("Ancestor(A,B)");
		Statement s2 = new Statement("~Ancestor(A,B)");
		Statement s3 = new Statement("Ancestor(x,B)");
		Assert.assertTrue(s1.isContradict(s2));
		Assert.assertTrue(s2.isContradict(s3));
	}

	@Test
	public void zzzContradict() {
		String s1 = "F(x) | G(x)"; //x18
		String s2 = "~G(x)"; //x19
		String s3 = "F(x)";
		checkUnifier(s1, s2, s3);
	}

	private void checkUnifier(String inp1, String inp2, String out) {
		Statement s1 = new Statement(inp1);
		Statement s2 = new Statement(inp2);
		Statement s3 = s1.unify(s2);
		Statement expected = null;
		if (!out.equals("")) {
			//			System.out.println("actual");
			//			s3.print();
			//			System.out.println("expected");
			expected = new Statement(out, true);
			//expected.print();
			Assert.assertEquals(expected, s3);
		} else {
			Assert.assertNull(expected);
		}
	}
}
