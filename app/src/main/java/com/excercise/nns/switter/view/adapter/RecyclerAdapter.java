package com.excercise.nns.switter.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.excercise.nns.switter.contract.OnRecyclerListener;
import com.excercise.nns.switter.contract.TimelineContract;
import com.excercise.nns.switter.databinding.TimelineItemBinding;
import com.excercise.nns.switter.model.entity.TwitterStatus;
import com.excercise.nns.switter.view.component.RecyclerViewHolder;

import java.util.List;

/**
 * Created by nns on 2017/07/12.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<TwitterStatus> statuses;
    private OnRecyclerListener listener;
    private TimelineContract contract;

    public RecyclerAdapter(List<TwitterStatus> statuses, OnRecyclerListener listener, TimelineContract contract) {
        this.statuses = statuses;
        this.listener = listener;
        this.contract = contract;
    }

    public void setStatuses(List<TwitterStatus> statuses) {
        this.statuses = statuses;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TimelineItemBinding binding = TimelineItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        TwitterStatus status = statuses.get(position);
        holder.binding.setStatus(status);
        holder.binding.setListener(listener);
        holder.binding.setContract(contract);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

}
