/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.espmail.launcher;

import com.espmail.message.ESPMailMessage;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author luismi
 */
public class LauncherTest {
    
    public LauncherTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getEstado method, of class Launcher.
     */
    @Test
    public void testGetEstado() {
        System.out.println("getEstado");
        Launcher instance = new LauncherImpl();
        String expResult = "";
        String result = instance.getEstado();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEstado method, of class Launcher.
     */
    @Test
    public void testSetEstado() {
        System.out.println("setEstado");
        String codigo = "";
        Launcher instance = new LauncherImpl();
        instance.setEstado(codigo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setReader method, of class Launcher.
     */
    @Test
    public void testSetReader() {
        System.out.println("setReader");
        Class lector = null;
        Launcher instance = new LauncherImpl();
        instance.setReader(lector);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReader method, of class Launcher.
     */
    @Test
    public void testGetReader() {
        System.out.println("getReader");
        Launcher instance = new LauncherImpl();
        Class expResult = null;
        Class result = instance.getReader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertBad method, of class Launcher.
     */
    @Test
    public void testInsertBad() {
        System.out.println("insertBad");
        ESPMailMessage message = null;
        Launcher instance = new LauncherImpl();
        instance.insertBad(message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clone method, of class Launcher.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        Launcher instance = new LauncherImpl();
        Object expResult = null;
        Object result = instance.clone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of send method, of class Launcher.
     */
    @Test
    public void testSend() throws Exception {
        System.out.println("send");
        ESPMailMessage mensaje = null;
        Launcher instance = new LauncherImpl();
        instance.send(mensaje);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRetardo method, of class Launcher.
     */
    @Test
    public void testGetRetardo() {
        System.out.println("getRetardo");
        Launcher instance = new LauncherImpl();
        long expResult = 0L;
        long result = instance.getRetardo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRetardo method, of class Launcher.
     */
    @Test
    public void testSetRetardo_long() {
        System.out.println("setRetardo");
        long retardo = 0L;
        Launcher instance = new LauncherImpl();
        instance.setRetardo(retardo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRetardo method, of class Launcher.
     */
    @Test
    public void testSetRetardo_String() {
        System.out.println("setRetardo");
        String retardo = "";
        Launcher instance = new LauncherImpl();
        instance.setRetardo(retardo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBads method, of class Launcher.
     */
    @Test
    public void testGetBads() {
        System.out.println("getBads");
        Launcher instance = new LauncherImpl();
        List expResult = null;
        List result = instance.getBads();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBads method, of class Launcher.
     */
    @Test
    public void testSetBads() {
        System.out.println("setBads");
        List bads = null;
        Launcher instance = new LauncherImpl();
        instance.setBads(bads);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setReintentos method, of class Launcher.
     */
    @Test
    public void testSetReintentos_String() {
        System.out.println("setReintentos");
        String reintentos = "";
        Launcher instance = new LauncherImpl();
        instance.setReintentos(reintentos);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReintentos method, of class Launcher.
     */
    @Test
    public void testGetReintentos() {
        System.out.println("getReintentos");
        Launcher instance = new LauncherImpl();
        int expResult = 0;
        int result = instance.getReintentos();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setReintentos method, of class Launcher.
     */
    @Test
    public void testSetReintentos_int() {
        System.out.println("setReintentos");
        int reintentos = 0;
        Launcher instance = new LauncherImpl();
        instance.setReintentos(reintentos);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getId method, of class Launcher.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Launcher instance = new LauncherImpl();
        int expResult = 0;
        int result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setId method, of class Launcher.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 0;
        Launcher instance = new LauncherImpl();
        instance.setId(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSerialVersion method, of class Launcher.
     */
    @Test
    public void testGetSerialVersion() {
        System.out.println("getSerialVersion");
        Launcher instance = new LauncherImpl();
        long expResult = 0L;
        long result = instance.getSerialVersion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInstance method, of class Launcher.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        Launcher instance = new LauncherImpl();
        Object expResult = null;
        Object result = instance.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class LauncherImpl extends Launcher {

        public void send(ESPMailMessage mensaje) throws LaunchException {
        }

        public Object getInstance() {
            return null;
        }
    }
}
