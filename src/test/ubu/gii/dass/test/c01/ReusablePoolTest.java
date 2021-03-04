/**
 * 
 */
package ubu.gii.dass.test.c01;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import ubu.gii.dass.c01.DuplicatedInstanceException;
import ubu.gii.dass.c01.NotFreeInstanceException;
import ubu.gii.dass.c01.Reusable;
import ubu.gii.dass.c01.ReusablePool;

/**
 * @author Marcos Romano Ibáñez y María Portugal Tomé
 *
 */
public class ReusablePoolTest {
	
	private ReusablePool pool;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		pool = ReusablePool.getInstance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		pool = null;
	}

	/**
	 * Test method for {@link ubu.gii.dass.c01.ReusablePool#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		Assert.assertNotNull(pool);
		Assert.assertEquals(pool, ReusablePool.getInstance());
		
	}

	/**
	 * Test method for {@link ubu.gii.dass.c01.ReusablePool#acquireReusable()}.
	 */
	@Test
	public void testAcquireReusable() {
		Assert.assertNotNull(pool);
		try {
			Assert.assertEquals(2, pool.size());
			Assert.assertNotNull(pool.acquireReusable());
			Assert.assertEquals(1, pool.size());
			Assert.assertNotNull(pool.acquireReusable());		
		} catch (NotFreeInstanceException e) {
			Assert.fail();
		}
		
		try {
			// Solo hay 2 instancias, por lo tanto en la tercera salta la excepción.
			Assert.assertEquals(0, pool.size());
			pool.acquireReusable();
		} catch (NotFreeInstanceException e) {
			Assert.assertEquals("No hay más instancias reutilizables disponibles. Reintentalo más tarde", e.getMessage());
		}
		
	}

	/**
	 * Test method for {@link ubu.gii.dass.c01.ReusablePool#releaseReusable(ubu.gii.dass.c01.Reusable)}.
	 */
	@Test
	public void testReleaseReusable() {
		Reusable r = null;
		try {
			Assert.assertNotNull(pool);
			Assert.assertEquals(2, pool.size());
			r = pool.acquireReusable();
			Assert.assertEquals(1, pool.size());
			pool.releaseReusable(r);				
		} catch (NotFreeInstanceException e) {
			Assert.fail();
		} catch (DuplicatedInstanceException e) {
			Assert.fail();
		}
		try {
			// Volver a meter el mismo reusable, debe saltar excepcion
			Assert.assertEquals(2, pool.size());	
			pool.releaseReusable(r);
		} catch (DuplicatedInstanceException e) {
			Assert.assertEquals("Ya existe esa instancia en el pool.", e.getMessage());
		}
	}

}
