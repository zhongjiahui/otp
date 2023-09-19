package cn.authing.otp.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.authing.otp.wideget.CountDownListener;
import cn.authing.otp.R;
import cn.authing.otp.TOTPEntity;
import cn.authing.otp.TOTPGenerator;
import cn.authing.otp.wideget.CountDownPie;
import cn.authing.otp.wideget.CodeView;

public class TOTPAdapter extends BaseAdapter implements CountDownListener {

    private List<TOTPEntity> totpList;
    private float degree = 360;
    private boolean countingDown;
    private boolean showAnimation;
    private int currentMode = AuthenticatorFragment.MODE_DEFAULT;
    private final onItemViewClickListener onItemViewClickListener;

    public TOTPAdapter(onItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }

    public void setTotpList(List<TOTPEntity> totpList) {
        this.totpList = totpList;
    }

    public void setCountingDown(boolean countingDown) {
        this.countingDown = countingDown;
    }

    public void setCurrentMode(int currentMode) {
        if (this.currentMode != currentMode) {
            showAnimation = true;
        }
        this.currentMode = currentMode;
    }

    public void setShowAnimation(boolean showAnimation) {
        this.showAnimation = showAnimation;
    }

    @Override
    public int getCount() {
        return totpList == null ? 0 : totpList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TOTPViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.authing_authenticator_item, parent, false);
            viewHolder = new TOTPViewHolder();
            viewHolder.codeView = convertView.findViewById(R.id.otp_item_code);
            viewHolder.imgEdit = convertView.findViewById(R.id.otp_item_img_edit);
            viewHolder.imgDelete = convertView.findViewById(R.id.otp_item_img_delete);
            viewHolder.countDownPie = convertView.findViewById(R.id.otp_item_countdown_pie);
            viewHolder.tvAccount = convertView.findViewById(R.id.tv_account);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TOTPViewHolder) convertView.getTag();
        }

        refreshView(parent.getContext(), position, viewHolder);

        return convertView;
    }

    public void refreshView(Context context, int position, TOTPViewHolder viewHolder) {
        TOTPEntity data = totpList.get(position);
        viewHolder.tvAccount.setText(data.getAccountDetail());

        viewHolder.codeView.setShowText(isDefaultMode());
        viewHolder.codeView.setText(data.getTotpCode());

        viewHolder.imgEdit.setVisibility(isDefaultMode() ? View.GONE : View.VISIBLE);
        viewHolder.imgEdit.setOnClickListener(v -> {
            if (null == totpList || position >= totpList.size()) {
                return;
            }
            TOTPEntity totpEntity = totpList.get(position);
            if (onItemViewClickListener != null) {
                onItemViewClickListener.onEditClick(totpEntity);
            }
        });

        viewHolder.imgDelete.setVisibility(isDefaultMode() ? View.GONE : View.VISIBLE);
        viewHolder.imgDelete.setOnClickListener(v -> {
            if (null == totpList || position >= totpList.size()) {
                return;
            }
            TOTPEntity totpEntity = totpList.get(position);
            if (onItemViewClickListener != null) {
                onItemViewClickListener.onDeleteClick(totpEntity);
            }
        });

        viewHolder.countDownPie.setVisibility(isDefaultMode() ? View.VISIBLE : View.GONE);
        viewHolder.countDownPie.setListener(this);
        if (TOTPGenerator.getRemainingMilliSeconds() <= 5000) {
            viewHolder.codeView.setTextColor(Color.parseColor("#ED5659"));
            viewHolder.countDownPie.setColor(Color.parseColor("#ED5659"));
        } else {
            viewHolder.codeView.setTextColor(context.getColor(R.color.code_text));
            viewHolder.countDownPie.setColor(context.getColor(R.color.code_text));
        }
        viewHolder.codeView.postInvalidate();
        viewHolder.countDownPie.invalidate();
    }

    private boolean isDefaultMode() {
        return currentMode == AuthenticatorFragment.MODE_DEFAULT;
    }

    public void countDown() {
        if (countingDown) {
            new Handler().postDelayed(this::countDown, 1000);
            // TODO each item might have different period
            int delta = TOTPGenerator.getRemainingMilliSeconds();
            degree = 360f / TOTPGenerator.TIME_STEP * delta / 1000;
            if (!showAnimation) {
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public float getDegree() {
        return degree;
    }

    public static class TOTPViewHolder {
        private CodeView codeView;
        private ImageView imgEdit;
        private ImageView imgDelete;
        private TextView tvAccount;
        private CountDownPie countDownPie;
    }

    public interface onItemViewClickListener {

        void onEditClick(TOTPEntity totpEntity);

        void onDeleteClick(TOTPEntity totpEntity);
    }

}
