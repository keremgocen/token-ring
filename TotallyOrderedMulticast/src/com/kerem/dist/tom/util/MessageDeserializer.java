package com.kerem.dist.tom.util;

import com.google.gson.GsonBuilder;
import com.kerem.dist.tom.model.MulticastMessageModel;

/**
 * Created by keremgocen on 1/3/15.
 */
public class MessageDeserializer {
    
    public static synchronized MulticastMessageModel createMessageFromString(final String message) throws Exception{
        return new GsonBuilder().create().fromJson(message, MulticastMessageModel.class);
    }

    public static synchronized String createStringFromMessage(final MulticastMessageModel message) throws Exception{
        return new GsonBuilder().create().toJson(message, MulticastMessageModel.class);
    }
}
