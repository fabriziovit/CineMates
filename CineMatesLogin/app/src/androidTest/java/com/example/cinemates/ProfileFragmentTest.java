package com.example.cinemates;


import com.example.cinemates.ui.CineMates.views.fragments.ProfileFragment;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class ProfileFragmentTest {
        /*
        CE1: source = null  INVALID
        CE2: source = https://image.tmdb.org/t/p/w185/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpeg VALID
        CE3: source = https://image.tmdb.org/t/p/w185/8UlWHLMpgZm9bx6QYh0NFoq67TZ INVALID
         */

    @Test
    void TC1_CE1(){
        ProfileFragment profileFragment = new ProfileFragment();
        assertEquals(profileFragment.getBitmapFromdownload(null), null);
    }

    @Test
    void TC1_CE2(){
        ProfileFragment profileFragment = new ProfileFragment();
        assertEquals(profileFragment.getBitmapFromdownload("https://image.tmdb.org/t/p/w185/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpeg"), true);
    }

    @Test
    void TC1_CE3(){
        ProfileFragment profileFragment = new ProfileFragment();
        assertEquals(profileFragment.getBitmapFromdownload("https://image.tmdb.org/t/p/w185/8UlWHLMpgZm9bx6QYh0NFoq67TZ"), null);
    }

}