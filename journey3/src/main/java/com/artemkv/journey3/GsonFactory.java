package com.artemkv.journey3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Instant;

final class GsonFactory {
    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantSerializer())
                .create();
    }

    private GsonFactory() {
    }

    public static Gson Gson() {
        return gson;
    }

    private static final class InstantSerializer implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
        @Override
        public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            return Instant.parse(json.getAsString());
        }
    }
}
