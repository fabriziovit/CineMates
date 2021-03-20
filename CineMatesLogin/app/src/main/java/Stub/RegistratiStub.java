package Stub;

import com.example.cinemates.ui.CineMates.model.UserHelperClass;
import com.example.cinemates.ui.CineMates.views.activities.RegistratiActivity;

import static com.example.cinemates.ui.CineMates.util.Constants.DEFAULT_PROFILE_PIC;

public class RegistratiStub extends RegistratiActivity {

    public boolean dataSet(String email, String username){
        if( email == null || username == null)
            return false;
        UserHelperClass userHelperClass = new UserHelperClass();
        userHelperClass.setEmail(email);
        userHelperClass.setUsername(username);
        userHelperClass.setImageUrl(DEFAULT_PROFILE_PIC);
        if(userHelperClass == null || userHelperClass.getImageUrl().equals(null))
            return false;
        else
            return true;
    }
}
