package com.mapbox.mapboxsdk.testapp.telemetry;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class PerformanceEventTest {

    @Test
    public void triggerPerformanceEvent() {

      List<Attribute<String>> attributes = new ArrayList<>();
      attributes.add(
        new Attribute<>("style_id", "mapbox://styles/mapbox/streets-v10"));

      List<Attribute<? extends Number>> counters = new ArrayList();
      counters.add(new Attribute<>("fps_average", 90.7655486547093));
      counters.add(new Attribute<>("frames", 362));


      Bundle bundle = new Bundle();
      bundle.putString("attributes", new Gson().toJson(attributes));
      bundle.putString("counters", new Gson().toJson(counters));

      Mapbox.getTelemetry().onPerformanceEvent(bundle);

    }


    class Attribute<T> {
      private String name;
      private T value;

      Attribute(String name, T value) {
        this.name = name;
        this.value = value;
      }
    }
}


