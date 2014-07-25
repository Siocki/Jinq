package org.jinq.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.jinq.jpa.test.entities.Customer;
import org.jinq.jpa.test.entities.Item;
import org.jinq.jpa.test.entities.Lineorder;
import org.jinq.jpa.test.entities.Supplier;
import org.junit.Test;

public class JinqJPAAggregateTest extends JinqJPATestBase
{
   @Test
   public void testCount()
   {
      long count = streams.streamAll(em, Customer.class)
            .count();
      assertEquals("SELECT COUNT(1) FROM Customer A", query);
      assertEquals(5, count);
   }
   
   @Test
   public void testCountWhere()
   {
      long count = streams.streamAll(em, Customer.class)
            .where(c -> c.getCountry().equals("UK") )
            .count();
      assertEquals("SELECT COUNT(1) FROM Customer A WHERE A.country = 'UK'", query);
      assertEquals(1, count);
   }

   @Test
   public void testSum()
   {
      assertEquals(10001500l, (long)streams.streamAll(em, Supplier.class)
            .sumLong(s -> s.getRevenue()));
      assertEquals("SELECT SUM(A.revenue) FROM Supplier A", query);
      
      assertEquals(1117.0, (double)streams.streamAll(em, Item.class)
            .sumDouble(i -> i.getSaleprice()), 0.001);
      assertEquals("SELECT SUM(A.saleprice) FROM Item A", query);
      
      assertEquals(new BigDecimal(2467), streams.streamAll(em, Lineorder.class)
            .sumBigDecimal(lo -> lo.getTotal()));
      assertEquals("SELECT SUM(A.total) FROM Lineorder A", query);

      assertEquals(BigInteger.valueOf(66000l), streams.streamAll(em, Lineorder.class)
            .sumBigInteger(lo -> lo.getTransactionConfirmation()));
      assertEquals("SELECT SUM(A.transactionConfirmation) FROM Lineorder A", query);
   }
   
   @Test
   public void testSumInteger()
   {
      // Sum of integers is a long
      assertEquals(1280, (long)streams.streamAll(em, Customer.class)
            .sumInteger(s -> s.getSalary()));
      assertEquals("SELECT SUM(A.salary) FROM Customer A", query);
   }
   
   @Test
   public void testSumExpression()
   {
      // Sum of integers is a long
      assertEquals(205300, (long)streams.streamAll(em, Customer.class)
            .sumInteger(s -> s.getSalary() * s.getDebt()));
      assertEquals("SELECT SUM(A.salary * (A.debt)) FROM Customer A", query);
   }

}
