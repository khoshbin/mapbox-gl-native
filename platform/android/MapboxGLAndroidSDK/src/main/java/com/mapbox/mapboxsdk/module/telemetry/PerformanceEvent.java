package com.mapbox.mapboxsdk.module.telemetry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.android.telemetry.Event;

import android.os.Bundle;
import android.os.Parcel;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Generic Performance Event that can be used for performance measurements.
 * Customer measurements can be added to the bundle.
 */
public class PerformanceEvent extends Event {
  private static final String PERFORMANCE_TRACE = "mobile_performance_trace";

  private final String event;

  private final String created;

  private final String sessionId;

  private final List<Attribute<String>> attributes;

  private final List<Attribute<? extends Number>> counters;

  private static final SimpleDateFormat DATE_FORMAT =
          new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

  PerformanceEvent(String sessionId, Bundle bundle)  {

    this.event = PERFORMANCE_TRACE;
    this.created = DATE_FORMAT.format(new Date());
    this.sessionId = sessionId;

    Gson gson = new Gson();
    this.attributes = gson.fromJson(bundle.getString("attributes"),
            new TypeToken<ArrayList<Attribute<String>>>() {}.getType());
    this.counters = gson.fromJson(bundle.getString("counters"),
            new TypeToken<ArrayList<Attribute<? extends Number>>>() {}.getType());
  }

  private PerformanceEvent(Parcel in) {
    this.event = in.readString();
    this.created = in.readString();
    this.sessionId = in.readString();

    Gson gson = new Gson();
    this.attributes = gson.fromJson(in.readString(),
            new TypeToken<ArrayList<Attribute<String>>>() {}.getType());
    this.counters = gson.fromJson(in.readString(),
            new TypeToken<ArrayList<Attribute<? extends Number>>>() {}.getType());
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(event);
    parcel.writeString(created);
    parcel.writeString(sessionId);

    Gson gson = new Gson();
    parcel.writeString(gson.toJson(attributes));
    parcel.writeString( gson.toJson(counters));
  }

  public static final Creator<PerformanceEvent> CREATOR = new Creator<PerformanceEvent>() {
    @Override
    public PerformanceEvent createFromParcel(Parcel in) {
      return new PerformanceEvent(in);
    }

    @Override
    public PerformanceEvent[] newArray(int size) {
      return new PerformanceEvent[size];
    }
  };

  class Attribute <T> {
    private final String name;
    private final T value;

    Attribute(String name, T value) {
      this.name = name;
      this.value = value;
    }
  }


}
