package cn.zjh.otp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dommy.qrcode.util.Constant;
import com.dommy.qrcode.util.ScanUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.zjh.otp.R;
import cn.zjh.otp.TOTP;
import cn.zjh.otp.TOTPEntity;
import cn.zjh.otp.util.ToastUtils;

public class AuthenticatorFragment extends Fragment implements TOTPAdapter.onItemViewClickListener {

    private View view;
    private FrameLayout content;
    private ListView listView;
    private List<TOTPEntity> totpList;
    private TOTPAdapter adapter;
    private LinearLayout defaultLayout;
    private Button scanButton;
    private Button inputKeyButton;
    private AuthenticatorDetailDialog dialog;
    private OnAuthenticatorListener listener;

    public static final int MODE_DEFAULT = 1;
    public static final int MODE_EDIT = 2;
    private int currentMode = MODE_DEFAULT;

    private ObjectAnimator rotationXAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_authenticator, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        if (getView() == null) {
            return;
        }
        content = getView().findViewById(R.id.authenticator_content);
        listView = getView().findViewById(R.id.authenticator_list_otp);
        adapter = new TOTPAdapter(this);
        listView.setAdapter(adapter);
        defaultLayout = getView().findViewById(R.id.authenticator_no_data_layout);
        scanButton = getView().findViewById(R.id.authenticator_btn_scan_qr);
        inputKeyButton = getView().findViewById(R.id.authenticator_btn_input_key);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
        if (rotationXAnimator != null) {
            rotationXAnimator.removeAllListeners();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshTotpData();
    }

    public void refreshTotpData() {
        if (getActivity() == null) {
            defaultLayout.setVisibility(View.VISIBLE);
            return;
        }
        if (totpList != null) {
            totpList.clear();
            if (adapter != null){
                adapter.notifyDataSetChanged();
            }
        }
        new Thread(() -> {
            List<TOTPEntity> totpList = TOTP.getTotpList(getActivity());
            getActivity().runOnUiThread(() -> refreshView(totpList));
        }).start();
    }

    private void refreshView(List<TOTPEntity> list) {
        this.totpList = list;
        adapter.setTotpList(totpList);
        if (null == totpList || totpList.size() == 0) {
            setCurrentMode(MODE_DEFAULT);
            content.setBackgroundColor(getContext().getColor(R.color.otp_white));
            defaultLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            scanButton.setOnClickListener(v ->
                    ScanUtil.startQrCode(getActivity(), 1000, 1001, Constant.PAGE_ADD_ACCOUNT, false));
            inputKeyButton.setOnClickListener(v -> {
                if (null != getActivity()) {
                    getActivity().startActivity(new Intent(getActivity(), InputKeyActivity.class));
                }
            });
            return;
        }
        content.setBackgroundColor(getContext().getColor(R.color.otp_background));
        defaultLayout.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
        adapter.setCountingDown(true);
        adapter.countDown();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (currentMode == MODE_EDIT || null == getActivity()
                    || null == totpList || position >= totpList.size()) {
                return;
            }
            ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("text", totpList.get(position).getTotpCode());
            if (manager != null) {
                manager.setPrimaryClip(clipData);
                ToastUtils.show(getActivity(), getString(R.string.copy_success));
            }
        });
    }

    public int getCurrentMode() {
        return currentMode;
    }


    public void setCurrentMode(int currentMode) {
        if (this.currentMode != currentMode) {
            startAnimation();
        }
        adapter.setCurrentMode(currentMode);
        this.currentMode = currentMode;
    }

    private void startAnimation() {
        rotationXAnimator = ObjectAnimator.ofFloat(listView, "rotationY", 0f, 90f);
        rotationXAnimator.setDuration(300);
        if (null != getActivity()) {
            listView.setCameraDistance(getActivity().getResources().getDisplayMetrics().density * 80000);
        }
        rotationXAnimator.setInterpolator(new AccelerateInterpolator());
        rotationXAnimator.addListener(new AnimUpdateListener(listView, adapter));
        rotationXAnimator.start();
    }

    static class AnimUpdateListener extends AnimatorListenerAdapter {

        private final WeakReference<ListView> listViewWeakReference;
        private final TOTPAdapter adapter;

        public AnimUpdateListener(ListView listView, TOTPAdapter adapter) {
            this.listViewWeakReference = new WeakReference<>(listView);
            this.adapter = adapter;
        }

        @Override
        public void onAnimationEnd(Animator animation, boolean isReverse) {
            super.onAnimationEnd(animation, isReverse);
            if (listViewWeakReference.get() == null) {
                return;
            }
            ObjectAnimator rotationXAnimator = ObjectAnimator.ofFloat(listViewWeakReference.get(), "rotationY", -90f, 0f);
            rotationXAnimator.setDuration(300);
            listViewWeakReference.get().setCameraDistance(listViewWeakReference.get().getResources().getDisplayMetrics().density * 80000);
            rotationXAnimator.setInterpolator(new DecelerateInterpolator());
            rotationXAnimator.start();
            if (adapter != null){
                adapter.setShowAnimation(false);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.setCountingDown(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != dialog) {
            dialog.dismiss();
            dialog = null;
        }
        if (rotationXAnimator != null) {
            rotationXAnimator.removeAllListeners();
        }
    }

    @Override
    public void onEditClick(TOTPEntity totpEntity) {
        if (null == dialog) {
            dialog = new AuthenticatorDetailDialog(getActivity(), R.style.BaseDialog);
            dialog.setOnDismissListener(dialog -> onResume());
        }
        dialog.setData(totpEntity);
        dialog.show();
    }

    @Override
    public void onDeleteClick(TOTPEntity totpEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.sure_delete))
                .setMessage(getString(R.string.sure_delete_message, totpEntity.getAccountDetail()))
                .setPositiveButton(getString(R.string.delete_account), (dialog, which) -> {
                    TOTP.deleteTotp(getActivity(), totpEntity);
                    refreshTotpData();
                    if (null != listener) {
                        listener.onAccountDelete(totpEntity);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss()).show();
    }

    public void setListener(OnAuthenticatorListener listener) {
        this.listener = listener;
    }

    public interface OnAuthenticatorListener {
        void onAccountDelete(TOTPEntity totpEntity);
    }
}
