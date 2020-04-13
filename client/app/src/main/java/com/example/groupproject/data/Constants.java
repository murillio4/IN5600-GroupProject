package com.example.groupproject.data;

public interface Constants {
    interface SharedPreferences {
        interface Name {
            String Person = "PERSON_DATABASE";
            String ClaimList = "CLAIM_LIST_DATABASE";
        }

        interface Keys {
            String Person = "PERSON_KEY";
            String ClaimList = "CLAIM_LIST_KEY";
        }
    }

    interface Api {
        //String Base = "http://192.168.0.145:8080/";
        String Base = "http://10.0.2.2:8080/";
        String SignIn = "methodPostRemoteLogin";
        String GetClaims = "getMethodMyClaims";
        String InsertClaim = "postInsertNewClaim";
        String UpdateClaim = "postUpdateClaim";
    }

    interface Serializable {
        String Claim = "CLAIM";
    }

    enum REQUEST_CODE {
        CAMERA,
        GALLERY
    }
}
