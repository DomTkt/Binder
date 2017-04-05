package com.example.lp.binder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lp.binder.R;

/**
 * Created by romai on 05/04/2017.
 */

public class HomeGridAdapter extends RecyclerView.Adapter<HomeGridAdapter.ItemViewHolder> {

    private HomeGridAdapterListener homeGridAdapterListener;

    public HomeGridAdapter(HomeGridAdapterListener homeGridAdapterListener) {
        this.homeGridAdapterListener = homeGridAdapterListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_main_grid_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        ImageView matchButton;
        ImageView chatButton;
        ImageView rdvButton;

        private ItemViewHolder(View itemView) {
            super(itemView);
            profilePicture = (ImageView) itemView.findViewById(R.id.view_main_grid_item_picture);
            matchButton = (ImageView) itemView.findViewById(R.id.view_main_grid_item_button_match);
            chatButton = (ImageView) itemView.findViewById(R.id.view_main_grid_item_button_chat);
            rdvButton = (ImageView) itemView.findViewById(R.id.view_main_grid_item_button_rdv);
        }
    }
}
