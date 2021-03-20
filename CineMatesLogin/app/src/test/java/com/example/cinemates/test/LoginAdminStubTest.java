package com.example.cinemates.test;

import Stub.LoginAdminStub;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginAdminStubTest {
    /* CE1: email = null INVALID
    CE2: email != cinemates.amministratore@gmail.com INVALID
    CE3: email = cinemates.amministratore@gmail.com VALID
    CE4: password = null INVALID
    CE5: password < 8 INVALID
    CE6: password >= 8 VALID
    */

    @Test
    public void TC1_CE1_CE4(){
        LoginAdminStub loginAdminStub = new LoginAdminStub();
        assertThrows(IllegalArgumentException.class, () -> { loginAdminStub.loginAdmin(null, null);});
    }

    @Test
    public void TC1_CE2_CE5(){
        LoginAdminStub loginAdminStub = new LoginAdminStub();
        assertFalse(loginAdminStub.loginAdmin("hello@live.it", "ciao"));
    }

    @Test
    public void TC1_CE3_CE6(){
        LoginAdminStub loginAdminStub = new LoginAdminStub();
        assertTrue(loginAdminStub.loginAdmin("cinemates.amministratore@gmail.com", "cinemates"));
    }
}