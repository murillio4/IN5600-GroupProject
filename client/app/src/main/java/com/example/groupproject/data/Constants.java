package com.example.groupproject.data;

public interface Constants {
    interface SharedPreferences {
        String Name = "DATABASE";

        interface Keys {
            String User = "USER_KEY";
        }
    }

    interface Api {
        String Base = "http://192.168.0.145:8080/";
        String SignIn = "methodPostRemoteLogin";
    }
}
