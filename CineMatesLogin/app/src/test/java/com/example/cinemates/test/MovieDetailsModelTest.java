package com.example.cinemates.test;

import com.example.cinemates.ui.CineMates.stub.MovieDetailsModelStub;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MovieDetailsModelTest {
   /*CE1 = nomi = null
   * CE2 = nomi = {Martin Scorzese}
   * CE3 = nomi = {Martin Scorzese, Quentin Tarantino, Vin Diesel}
   * CE4 = jobs = null
   * CE5 = jobs = {Director}
   * CE6 = jobs = {Director, Director, Actor}
   * CE7 = dim<0
   * CE8 = dim >=0
   *  */

    @Test
    public void TC1_CE1_CE4_C7(){
        ArrayList<String> nomi = null;
        ArrayList<String> jobs = null;
        int dim = -5;
        MovieDetailsModelStub movieDetailsModelStub = new MovieDetailsModelStub();
        assertNull(movieDetailsModelStub.getMovieDetails(nomi, jobs, dim));
    }

    @Test
    public void TC2_CE2_CE5_CE8(){
        ArrayList<String> nomi = new ArrayList<>();
        nomi.add("Martin Scorzese");
        ArrayList<String> jobs = new ArrayList<>();
        jobs.add("Director");
        int dim = 1;
        MovieDetailsModelStub movieDetailsModelStub = new MovieDetailsModelStub();
        assertEquals(movieDetailsModelStub.getMovieDetails(nomi, jobs, dim), "Martin Scorzese");
    }

    @Test
    public void TC3_CE3_CE6_CE8(){
        ArrayList<String> nomi = new ArrayList<>();
        nomi.add("Martin Scorzese");
        nomi.add("Quentin Tarantino");
        nomi.add("Vin Diesel");
        ArrayList<String> jobs = new ArrayList<>();
        jobs.add("Director");
        jobs.add("Director");
        jobs.add("Actor");
        int dim = 3;
        MovieDetailsModelStub movieDetailsModelStub = new MovieDetailsModelStub();
        assertEquals(movieDetailsModelStub.getMovieDetails(nomi, jobs, dim), "Martin Scorzese, Quentin Tarantino");
    }
}