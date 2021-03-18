package com.example.cinemates.ui.CineMates.stub;


import com.example.cinemates.ui.CineMates.views.fragments.RecensioniFilmFragment;


public class RecensioniFilmStub extends RecensioniFilmFragment {

    public boolean onPositiveButtonClickedStub(int i, String s){
        if(i<=0 || i>5)
            return false;
        return true;
    }
}
