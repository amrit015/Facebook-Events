package com.amrit.facebook_events;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by Amrit on 6/10/2017.
 */

class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.EventsHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    Context context;
    JsonModuleParcelable jsonModuleParcelable;
    private ArrayList<JsonModuleParcelable> mDataset;
    private String name;
    //    private String startDate;
//    private String endDate;
//    private String description;
//    private String city;
    private int position;
    private String image;

    public MyRecyclerViewAdapter(Context activity, ArrayList mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public EventsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_view_myrecycler, parent, false);
        context = parent.getContext();
        EventsHolder eventsHolder = new EventsHolder(view);
        return eventsHolder;
    }

    @Override
    public void onBindViewHolder(final EventsHolder holder, final int position) {
        // binding the views to display the name, description and user in the recyclerview
        jsonModuleParcelable = mDataset.get(position);
        if (!jsonModuleParcelable.getName().equals("")) {
            holder.eventsName.setText(jsonModuleParcelable.getName());
//            holder.eventsStartDate.setText(jsonModuleParcelable.getStartDate());
//            holder.eventsEndDate.setText(jsonModuleParcelable.getEndDate());
//            holder.eventsDescription.setText(jsonModuleParcelable.getDescription());
//            holder.eventsCity.setText(jsonModuleParcelable.getCity());

            holder.progressBar.setVisibility(View.VISIBLE);
            //using Glide to load images
            Glide.clear(holder.eventsImage);
            Glide.with(context)
                    .load(jsonModuleParcelable.getSource())
                    .fitCenter()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.eventsImage);
        } else {
            holder.cardView.setVisibility(View.GONE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonModuleParcelable = mDataset.get(position);
//                setName(holder.eventsName.getText().toString());
//                setStartDate(holder.eventsStartDate.getText().toString());
//                setEndDate(holder.eventsEndDate.getText().toString());
//                setDescription(holder.eventsDescription.getText().toString());
//                setCity(holder.eventsCity.getText().toString());
                setPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    /*

    // saving and accessing name,description,position for use with descriptionFragment
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    */

    public class EventsHolder extends RecyclerView.ViewHolder {

        TextView eventsName;
        TextView eventsDescription;
        TextView eventsStartDate;
        TextView eventsEndDate;
        TextView eventsCity;
        CardView cardView;
        ImageView eventsImage;
        ProgressBar progressBar;

        public EventsHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            eventsImage = itemView.findViewById(R.id.events_image);
            eventsName = itemView.findViewById(R.id.events_name);
//            eventsStartDate = (TextView) itemView.findViewById(R.id.events_start_date);
//            eventsEndDate = (TextView) itemView.findViewById(R.id.events_end_date);
//            eventsDescription = (TextView) itemView.findViewById(R.id.events_description);
//            eventsCity = (TextView) itemView.findViewById(R.id.events_city);
            progressBar = itemView.findViewById(R.id.progressBar);
            Log.i(LOG_TAG, "Adding Listener");
        }
    }
}
