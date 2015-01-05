package com.kerem.dist.tom.util;

import com.google.gson.GsonBuilder;
import com.kerem.dist.tom.model.ResponseModel;

/**
 * Created by keremgocen on 1/3/15.
 */
public class ResponseDeserializer {
    
    public static synchronized ResponseModel responseFromString(final String message) throws Exception{
        return new GsonBuilder().create().fromJson(message, ResponseModel.class);
    }

    public static synchronized String stringFromResponse(final ResponseModel message) throws Exception{
        return new GsonBuilder().create().toJson(message, ResponseModel.class);
    }
}
