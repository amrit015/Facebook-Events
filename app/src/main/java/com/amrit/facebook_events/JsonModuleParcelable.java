package com.amrit.facebook_events;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amrit on 6/8/2017.
 */

class JsonModuleParcelable implements Parcelable {
    public static final Parcelable.Creator<JsonModuleParcelable> CREATOR = new Parcelable.Creator<JsonModuleParcelable>() {
        //Generates instance of Parcelable class from a parcel

        @Override
        public JsonModuleParcelable createFromParcel(Parcel source) {
            /* Creates instance of new Parcelable class,instantiating it from the given Parcel
               whose data were previously written with writeToParcel
            */
            return new JsonModuleParcelable(source);
        }

        @Override
        public JsonModuleParcelable[] newArray(int size) {
            return new JsonModuleParcelable[size];
        }
    };
    //    String ETag;
    String name;
    String description;
    String city;
    String startDate;
    String endDate;
    String source;

    public JsonModuleParcelable() {
    }

    private JsonModuleParcelable(Parcel in) {
        /* reading back each field from the parcel in the same order it was written
           to the parcel
        */
        this.name = in.readString();
        this.description = in.readString();
        this.city = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.source = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /* writing each field to the parcel,same order is used to read from parcel
        */
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(city);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(source);
    }
}
