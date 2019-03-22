package com.example.android.fabapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder> {

    private OnProjectListener mOnProjectListener;
    private final LayoutInflater mInflater;
    private List<Project> mProjects; // Cached copy of projects

    class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView projectItemView;

        OnProjectListener onProjectListener;

        private ProjectViewHolder(View itemView, OnProjectListener onProjectListener) {
            super(itemView);
            projectItemView = itemView.findViewById(R.id.textView);
            this.onProjectListener = onProjectListener;

            projectItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProjectListener.onProjectClick(getAdapterPosition());
        }
    }


//     Added in place of package private one below March 3
ProjectListAdapter(Context context, OnProjectListener onProjectListener) {
//        this.mProjects = projects;
        this.mOnProjectListener = onProjectListener;
        mInflater = LayoutInflater.from(context);
    }
//    ProjectListAdapter(Context context) {
//        mInflater = LayoutInflater.from(context);
//    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ProjectViewHolder(itemView, mOnProjectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        if (mProjects != null) {
            Project current = mProjects.get(position);
            String content = current.getProject();
            holder.projectItemView.setText(content);
        } else {
            // Covers the case of data not being ready yet.
            holder.projectItemView.setText(R.string.no_project);
        }
    }

    void setProjects(List<Project> projects){
        mProjects = projects;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mProjects has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mProjects != null)
            return mProjects.size();
        else return 0;
    }

    public interface OnProjectListener {
        void onProjectClick(int position);
    }
}