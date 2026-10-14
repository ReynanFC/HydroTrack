package com.reynanfc.hydrotrack.data.local;

import com.reynanfc.hydrotrack.model.RegisterWater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

 public class RegisterWaterJsonMapper {

    private static final String JSON_ID = "id";
    private static final String JSON_ML = "ml";
    private static final String JSON_TIME = "time";

    private RegisterWaterJsonMapper() {
    }

    static List<RegisterWater> fromJson(String json) {
        List<RegisterWater> records = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                records.add(new RegisterWater(
                        item.optString(JSON_ID, UUID.randomUUID().toString()),
                        item.getInt(JSON_ML),
                        LocalDateTime.parse(item.getString(JSON_TIME))));
            }
        } catch (JSONException e) {
            return new ArrayList<>();
        }
        return records;
    }

    static String toJson(List<RegisterWater> records) {
        JSONArray array = new JSONArray();
        try {
            for (RegisterWater record : records) {
                array.put(new JSONObject()
                        .put(JSON_ID, record.id())
                        .put(JSON_ML, record.consumeMl())
                        .put(JSON_TIME, record.time().toString()));
            }
        } catch (JSONException e) {
            throw new IllegalStateException("Falha ao serializar registros", e);
        }
        return array.toString();
    }
}
