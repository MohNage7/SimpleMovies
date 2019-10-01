package com.mohnage7.movies.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mohnage7.movies.R;

import butterknife.ButterKnife;

public abstract class BaseBottomSheet extends BottomSheetDialogFragment {
    public Activity mContext;
    private BottomSheetBehavior mBottomSheetBehavior;

    @CallSuper
    public void onViewReady(Dialog dialog) {
    }

 
    /**
     * this method should be override by every childs
     *
     * @return layout int reference
     */
    protected abstract int getContentView();

    /**
     * this method is override instead of onCreateView to set top corners for bottom sheet
     *
     * @param dialog
     * @param style
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View rootView = getActivity().getLayoutInflater().inflate(getContentView(), null, false);
        ButterKnife.bind(this, rootView);
        mContext = getActivity();
        dialog.setContentView(rootView);
        FrameLayout bottomSheet = dialog.getWindow().findViewById(R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.color.transparent);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        onViewReady(dialog);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        dismiss();
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // no implementation needed
            }
        });

    }

    public void setSheetStateExpanded() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

}
