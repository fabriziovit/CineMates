package com.example.cinemates.ui.CineMates;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.example.cinemates.R;

public class WarningDialog extends DialogFragment implements View.OnClickListener{
    private Button negativo;
    private Button positivo;
    private String dialog_title = "LOGOUT";

    public static interface Callback
    {
        void onClick(View view);

        public void accetta();
        public void decline();

        void showDialog(DialogFragment dialogFragment);
    }

    public WarningDialog() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.warning_dialog, container);

        negativo = view.findViewById(R.id.no_button_warningDialog);
        positivo = view.findViewById(R.id.yes_button_warningDialog);

        negativo.setOnClickListener(this);
        positivo.setOnClickListener(this);
        getDialog().setTitle(dialog_title);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        Callback callback = null;
        try
        {
            callback = (Callback) getTargetFragment();
        }
        catch (ClassCastException e)
        {
            Log.e(this.getClass().getSimpleName(), "Callback of this class must be implemented by target fragment!", e);
            throw e;
        }

        if (callback != null)
        {
            if (v == v.findViewById(R.id.yes_button_warningDialog))
            {
                callback.accetta();
                this.dismiss();
            }
            else if (v == negativo)
            {
                callback.decline();
                this.dismiss();
            }
        }
    }
}
