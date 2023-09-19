package com.zjh.otp.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zjh.otp.R;
import com.zjh.otp.bean.OtpAccountBean;
import com.zjh.otp.databinding.LayoutOtpSelectItemBinding;

import java.util.List;

import cn.zjh.otp.TOTPEntity;

public class OtpSelectAdapter extends RecyclerView.Adapter<OtpSelectAdapter.AccountHolder> {

    private List<OtpAccountBean> mDataList;
    private final OnItemCheckListener listener;

    public OtpSelectAdapter(OnItemCheckListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<OtpAccountBean> dataList) {
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_otp_select_item, parent, false);
        return new AccountHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, int position) {
        OtpAccountBean data = mDataList.get(position);
        if (holder.mBinding == null || data == null || data.getTotpEntity() == null) {
            return;
        }
        TOTPEntity totpEntity = data.getTotpEntity();
        holder.mBinding.otpExportCheckbox.setChecked(data.isHasSelect());
        holder.mBinding.otpExportCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            data.setHasSelect(isChecked);
            if (listener != null) {
                listener.onItemCheck();
            }
        });
        holder.mBinding.optExportAccount.setText(totpEntity.getAccountDetail());
    }


    @Override
    public int getItemCount() {
        return null != mDataList ? mDataList.size() : 0;
    }

    public static class AccountHolder extends RecyclerView.ViewHolder {

        private final LayoutOtpSelectItemBinding mBinding;

        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

    }

    public interface OnItemCheckListener {

        void onItemCheck();

    }
}
