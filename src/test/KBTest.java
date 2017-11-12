package test;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import main.KB;
import main.Statement;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KBTest {

	@Test
	public void addToKB() {
		String[] stStrings = new String[] {
				"F(x) | G(x)",
				"~F(x)",
				"~G(x)"
		};
		KB kb = new KB(stStrings);
		Assert.assertEquals(3, kb.statements.size());
		Statement s1 = new Statement("F(x0) | G(x0)", true);
		Statement s2 = new Statement("~F(x1)", true);
		Statement s3 = new Statement("~G(x2)", true);
		Assert.assertEquals(s1, kb.statements.get(0));
		Assert.assertEquals(s2, kb.statements.get(1));
		Assert.assertEquals(s3, kb.statements.get(2));
	}

	@Test
	public void easyResolve() {
		String[] stStrings = new String[] {
				"F(x) | G(x)",
				"~F(x)",
				"~G(x)"
		};
		KB kb = new KB(stStrings);
		boolean result = kb.resolve();
		System.out.println(kb.statements.size());
		Assert.assertFalse(result);
	}

	@Test
	public void ex1Assign() {
		String[] stStrings = new String[] {
				"F(x0) | G(x0)",
				"~G(x1) | H(x1)",
				"~H(x2) | F(x2)",
				"~F(Joe)"
		};
		KB kb = new KB(stStrings);
		boolean result = kb.resolve();
		System.out.println(kb.statements.size());
		Assert.assertFalse(result);
	}

	@Test
	public void ex2Assign() {
		String[] stStrings = new String[] {
				"F(x) | G(x)",
				"~G(x) | H(x)",
				"~H(x) | F(x)",
				"F(Joe)"
		};
		KB kb = new KB(stStrings);
		boolean result = kb.resolve();
		System.out.println(kb.statements.size());
		Assert.assertTrue(result);
	}

	@Test
	public void ex3Assign() {
		String[] stStrings = new String[] {
				"~F(x) | G(x)",
				"~G(x) | H(x)",
				"~H(x) | F(x)",
				"~R(x) | H(x)",
				"~A(x) | H(x)",
				"~D(x,y) | H(x)",
				"~R(x) | H(x)",
				"~A(x) | H(x)",
				"~D(x,y) | H(x)",
				"~H(John)"
		};
		KB kb = new KB(stStrings);
		boolean result = kb.resolve();
		System.out.println(kb.statements.size());
		Assert.assertTrue(result);
	}

	@Test
	public void ex4Assign() {
		String[] stStrings = new String[] {
				"~A(x) | H(x)",
				"~B(x,y) | ~C(x,y) | A(x)",
				"B(John,Joe)",
				"~D(x,y) | ~Q(y) | C(x,y)",
				"Q(Joe)",
				"D(John,Joe)",
				"~H(John)"
		};
		KB kb = new KB(stStrings);
		boolean result = kb.resolve();
		System.out.println(kb.statements.size());
		Assert.assertFalse(result);
	}
}
