package com.equilibriummusicgroup.SpotyCheck.model;

public class CustomException extends Exception{
    public CustomException(String message)
    {
        super(message);
    }



    public static class ResponseCodeException extends  Exception{

        public ResponseCodeException(String message)
        {
            super(message);
        }


    }
}
