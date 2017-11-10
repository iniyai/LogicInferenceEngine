package test;

import java.util.HashMap;
import java.util.Map;

//import org.hamcrest.collection.IsMapContaining;
import org.junit.Assert;
import org.junit.Test;

import main.Predicate;
import main.Symbol;

public class PredicateTest {

	@Test
	public void construct() {
		Predicate predicate = new Predicate("F(x,y,Iniyai)");
		Assert.assertEquals(predicate.name, "F");
		Assert.assertFalse(predicate.isNegated);
		String[] names = { "x", "y", "Iniyai" };
		boolean[] isC = { false, false, true };
		checkArguments(predicate, names, isC);
	}

	private void checkArguments(Predicate predicate, String[] names, boolean[] isC) {
		for (int i = 0; i < predicate.arguments.length; i++) {
			Symbol output = predicate.arguments[i];
			//Assert.assertNull(output);
			Assert.assertEquals(names[i], output.sName());
			Assert.assertEquals(isC[i], output.isConst());
		}
	}

	@Test
	public void unifyConstVar() {
		Predicate p1 = new Predicate("F(G,a)");
		Predicate p2 = new Predicate("F(y,X)");
		Map<String, String> map = p1.unifyPredicate(p2);
		Map<String, String> expected = new HashMap<>();
		expected.put("a", "X");
		expected.put("y", "G");
		Assert.assertEquals(expected, map);
	}

	@Test
	public void unifyConstVar2() {
		Predicate p1 = new Predicate("F(x,Alpha)");
		Predicate p2 = new Predicate("F(y,z)");
		Map<String, String> map = p1.unifyPredicate(p2);
		Map<String, String> expected = new HashMap<>();
		expected.put("z", "Alpha");
		expected.put("x", "y");
		Assert.assertEquals(expected, map);
	}

	@Test
	public void unifyTwoConstants() {
		Predicate p1 = new Predicate("F(x,Alpha)");
		Predicate p2 = new Predicate("F(x,Beta)");
		Map<String, String> map = p1.unifyPredicate(p2);
		Assert.assertNull(map);
		//expected.put(new Symbol("x"), new Symbol("x"));
	}

	@Test
	public void unifyTwoVariable() {
		Predicate p1 = new Predicate("F(x,a)");
		Predicate p2 = new Predicate("F(y,x)");
		Map<String, String> map = p1.unifyPredicate(p2);
		Map<String, String> expected = new HashMap<>();
		expected.put("a", "x");
		expected.put("x", "y");
		Assert.assertEquals(expected, map);
	}

	@Test
	public void contradictPredicates() {
		Predicate p1 = new Predicate("Mother(x,y)");
		Predicate p2 = new Predicate("~Mother(y,z)");
		Predicate p3 = new Predicate("Mother(Charlie,y)");
		Predicate p4 = new Predicate("~Mother(Bill,z)");
		Predicate p5 = new Predicate("~Mother(x,Ronnie)");
		Predicate pf = new Predicate("~Father(a,c)");
		Assert.assertTrue(p1.isContradict(p2));
		Assert.assertFalse(p3.isContradict(p4));
		Assert.assertTrue(p1.isContradict(p4));
		Assert.assertFalse(p2.isContradict(p4));
		Assert.assertTrue(p1.isContradict(p5));
		Assert.assertFalse(p1.isContradict(pf));
	}

}
