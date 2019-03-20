package com.example.android.fabapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FabricListAdapter extends RecyclerView.Adapter<FabricListAdapter.FabricViewHolder> {
    private static final String TAG = "FabricListAdapter";

    private OnFabricListener mOnFabricListener;
    private final LayoutInflater mInflater;

    private List<Fabric> mFabrics; // Cached copy of fabricss

    class FabricViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView fabricImageView;
        private final TextView fabricNameView;

        OnFabricListener onFabricListener;

        private FabricViewHolder(View itemView, OnFabricListener onFabricListener) {
            super(itemView);
            fabricNameView = itemView.findViewById(R.id.textView);
            fabricImageView = itemView.findViewById(R.id.fabricImage);
            this.onFabricListener = onFabricListener;

            fabricNameView.setOnClickListener(this);
            fabricImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onFabricListener.onFabricClick(getAdapterPosition());
        }
    }


    //     Added in place of package private one below March 3
    public FabricListAdapter(Context context, OnFabricListener onFabricListener) {
        this.mOnFabricListener = onFabricListener;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public FabricViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fabric_recyclerview_item, parent, false);
        return new FabricViewHolder(itemView, mOnFabricListener);
    }

    @Override
    public void onBindViewHolder(FabricViewHolder holder, int position) {
        int THUMBSIZE = 64;
        if (mFabrics != null) {
            Fabric current = mFabrics.get(position);
            String content = current.getFabricName();
            String fabricUri = current.getFabricUri();
            Log.d(TAG, "onBindViewHolder: content is: " + content);
//            content += " ";
//            content += current.getDimension();
            holder.fabricNameView.setText(content);
//            ImageView picture = findViewById(R.id.fabric_picture);


            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(fabricUri),
                    THUMBSIZE, THUMBSIZE);
//            Bitmap bmImg = BitmapFactory.decodeFile(fabricUri);
//            picture.setImageBitmap(bmImg);
            holder.fabricImageView.setImageBitmap(thumbImage);
        } else {
            // Covers the case of data not being ready yet.
            holder.fabricNameView.setText("No Fabric");
        }
    }

    void setFabrics(List<Fabric> fabrics){
        mFabrics = fabrics;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mProjects has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mFabrics != null)
            return mFabrics.size();
        else return 0;
    }

    public interface OnFabricListener {
        void onFabricClick(int position);
    }
}
