package com.zjh.otp.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zjh.otp.R;
import com.zjh.otp.database.OtpHistoryEntity;
import com.zjh.otp.databinding.LayoutOtpHistoryItemBinding;

import java.util.List;

public class OtpHistoryAdapter extends RecyclerView.Adapter<OtpHistoryAdapter.HistoryHolder> {

    private List<OtpHistoryEntity> mDataList;
    private String currentTime;

    public OtpHistoryAdapter() {
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<OtpHistoryEntity> dataList) {
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_otp_history_item, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        OtpHistoryEntity entity = mDataList.get(position);
        if (entity == null || holder.mBinding == null) {
            return;
        }
        holder.mBinding.optHistoryDescription.setText(entity.getDescription());
        String time = entity.getTime();
        String startTime = "";
        if (time != null) {
            startTime = time.split(" ")[0];
        }
        holder.mBinding.optHistoryTime.setText(time);
        if (position == 0) {
            holder.mBinding.otpHistoryTop.setVisibility(View.VISIBLE);
            holder.mBinding.otpHistoryMiddle.setVisibility(View.GONE);
            holder.mBinding.otpHistoryBottom.setVisibility(View.GONE);
            currentTime = startTime;
        } else {
            if (currentTime.equals(startTime)) {
                holder.mBinding.otpHistoryTop.setVisibility(View.GONE);
                holder.mBinding.otpHistoryMiddle.setVisibility(View.VISIBLE);
                holder.mBinding.otpHistoryBottom.setVisibility(View.GONE);
            } else {
                holder.mBinding.otpHistoryTop.setVisibility(View.GONE);
                holder.mBinding.otpHistoryMiddle.setVisibility(View.GONE);
                holder.mBinding.otpHistoryBottom.setVisibility(View.VISIBLE);
                currentTime = startTime;
            }
        }
    }

    @Override
    public int getItemCount() {
        return null != mDataList ? mDataList.size() : 0;
    }

    public static class HistoryHolder extends RecyclerView.ViewHolder {

        private final LayoutOtpHistoryItemBinding mBinding;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

    }

}
