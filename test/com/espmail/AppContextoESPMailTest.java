/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espmail;

import java.sql.Connection;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author luismi
 */
public class AppContextoESPMailTest {
   
   public AppContextoESPMailTest() {
   }
   
   @BeforeClass
   public static void setUpClass() {
   }
   
   @Before
   public void setUp() {
   }
   
   @After
   public void tearDown() {
   }

   /**
    * Test of getConexion method, of class AppContextoESPMail.
    */
   @Test
   public void testGetConexion() throws Exception {
      System.out.println("getConexion");
      AppContextoESPMail instance = new AppContextoESPMail();
      Connection expResult = null;
      Connection result = instance.getConexion();
      assertNotNull(result);
      
   }

   /**
    * Test of init method, of class AppContextoESPMail.
    
   @Test
   public void testInit() {
      System.out.println("init");
      Properties prop = null;
      AppContextoESPMail.TYPE = AppContextoESPMail.class;
      AppContextoESPMail instance = ( AppContextoESPMail )AppContextoESPMail.getInstance();
      instance.init(prop);
      
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of setReader method, of class AppContextoESPMail.
    
   @Test
   public void testSetReader() {
      System.out.println("setReader");
      String className = "";
      AppContextoESPMail instance = new AppContextoESPMail();
      instance.setReader(className);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of getReader method, of class AppContextoESPMail.
    
   @Test
   public void testGetReader() {
      System.out.println("getReader");
      AppContextoESPMail instance = new AppContextoESPMail();
      String expResult = "";
      String result = instance.getReader();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }

   /**
    * Test of getProperties method, of class AppContextoESPMail.
    
   @Test
   public void testGetProperties() {
      System.out.println("getProperties");
      AppContextoESPMail instance = new AppContextoESPMail();
      Properties expResult = null;
      Properties result = instance.getProperties();
      assertEquals(expResult, result);
      // TODO review the generated test code and remove the default call to fail.
      fail("The test case is a prototype.");
   }
   * 
   * */
   
}
