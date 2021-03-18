package com.example.cinemates.test;

import com.example.cinemates.ui.CineMates.stub.RegistratiStub;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegistratiPresenterTest {
    /*CE1 = email = null
     * CE2 = email != null
     * CE3 = username = null
     * CE4 = username != null*/

    @Test
    public void TC1_CE1_CE3(){
        RegistratiStub registratiStub = new RegistratiStub();
        assertFalse(registratiStub.dataSet(null, null));
    }

    @Test
    public void TC2_CE2_CE4(){
        RegistratiStub registratiStub = new RegistratiStub();
        assertTrue(registratiStub.dataSet("email@email.com", "test"));
    }
}