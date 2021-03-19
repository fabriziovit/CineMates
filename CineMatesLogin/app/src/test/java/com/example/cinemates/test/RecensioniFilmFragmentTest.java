package com.example.cinemates.test;

import com.example.cinemates.ui.CineMates.stub.RecensioniFilmStub;

import org.junit.Test;

import static org.junit.Assert.*;

public class RecensioniFilmFragmentTest {
    /*CE1 = i<=0
    * CE2 = 1=<i >=5
    * CE3 = i>5
    * CE4 = s = null
    * CE5 = s != ""
    * CE6 = s = "" */

    @Test
    public void TC1_CE1_CE4(){
        RecensioniFilmStub recensioniFilmStub = new RecensioniFilmStub();
        assertFalse(recensioniFilmStub.onPositiveButtonClickedStub(-3, null));
    }

    @Test
    public void TC2_CE2_CE5(){
        RecensioniFilmStub recensioniFilmStub = new RecensioniFilmStub();
        assertTrue(recensioniFilmStub.onPositiveButtonClickedStub(3, "bello"));
    }

    @Test
    public void TC3_CE3_CE6(){
        RecensioniFilmStub recensioniFilmStub = new RecensioniFilmStub();
        assertFalse(recensioniFilmStub.onPositiveButtonClickedStub(9, ""));
    }
}