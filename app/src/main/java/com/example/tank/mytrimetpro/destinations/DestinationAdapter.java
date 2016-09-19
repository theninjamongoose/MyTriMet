package com.example.tank.mytrimetpro.destinations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tank.mytrimetpro.R;
import com.example.tank.mytrimetpro.data.Destination;

import java.util.List;

/**
 * Created by tank on 8/31/16.
 */

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {


    private final AdapterItemInteractionCallback mAdapterCallback;
    private List<Destination> mDestinations;

    public DestinationAdapter(List<Destination> destinationList, AdapterItemInteractionCallback adapterCallback) {
        mAdapterCallback = adapterCallback;
        mDestinations = destinationList;
    }

    private void setDestinations(List<Destination> destinations){
        this.mDestinations = destinations;
    }

    public void replaceData(List<Destination> destinations) {
        setDestinations(destinations);
        notifyDataSetChanged();
    }

    @Override
    public DestinationAdapter.DestinationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_destination_history, parent, false);

        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DestinationViewHolder holder, int position) {
        holder.bindDestination(mDestinations.get(position));
    }

    @Override
    public int getItemCount() {
        return mDestinations.size();
    }

    // Android view holder pattern
    public class DestinationViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mDestinationListItem;
        TextView mPlaceNameTextView;
        TextView mAddressTextView;

        public DestinationViewHolder(View destinationView) {
            super(destinationView);

            mDestinationListItem = (LinearLayout) destinationView.findViewById(R.id.destination_history_list_item);
            mPlaceNameTextView = (TextView) destinationView.findViewById(R.id.place_name);
            mAddressTextView = (TextView) destinationView.findViewById(R.id.address);
        }

        public void bindDestination(final Destination destination) {
            mPlaceNameTextView.setText(destination.getPlaceName());
            if (destination.getAddress() != null) {
                mAddressTextView.setText(destination.getAddress());
            }

            mDestinationListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterCallback.onAdapterItemClicked(destination);
                }
            });
        }
    }
}

