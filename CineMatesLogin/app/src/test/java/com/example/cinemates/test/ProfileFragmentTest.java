package com.example.cinemates.test;

import com.example.cinemates.ui.CineMates.stub.ProfileFragment_Stub;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProfileFragmentTest {
      /*
        CE1: source = null  INVALID
        CE2: source = https://image.tmdb.org/t/p/w185/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpeg VALID
        CE3: source = https://image.tmdb.org/t/p/w185/8UlWHLMpgZm9bx6QYh0NFoq67TZ INVALID
         */
    @Test
    public void TC1_CE1(){
        ProfileFragment_Stub profileFragment_stub = new ProfileFragment_Stub();
        assertFalse(profileFragment_stub.getBitmapFromdownloadStub(null));
    }

    @Test
    public void TC2_CE2(){
        ProfileFragment_Stub profileFragment_stub = new ProfileFragment_Stub();
        assertTrue(profileFragment_stub.getBitmapFromdownloadStub("https://image.tmdb.org/t/p/w185/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpeg"));
    }

    @Test
    public void TC3_CE3(){
        ProfileFragment_Stub profileFragment_stub = new ProfileFragment_Stub();
        assertFalse(profileFragment_stub.getBitmapFromdownloadStub("https://image.tmdb.org/t/p/w185/8UlWHLMpgZm9bx6QYh0NFoq67TZ"));
    }
}