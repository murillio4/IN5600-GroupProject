package com.example.groupproject.data;

public interface Constants {
    interface SharedPreferences {
        String Name = "DATABASE";

        interface Keys {
            String User = "USER_KEY";
        }
    }

    interface Api {
        String Base = "http://10.0.2.2:8080/";
        String SignIn = "methodPostRemoteLogin";
    }
}
