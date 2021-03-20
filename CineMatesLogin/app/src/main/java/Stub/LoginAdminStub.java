package Stub;

public class LoginAdminStub {

    public boolean loginAdmin(String email, String password){
        if(email != null && password != null){
            if(!email.equals("cinemates.amministratore@gmail.com") || password.length() < 8)
                return false;
            else
                return true;
        }else
            throw new IllegalArgumentException();
    }
}
