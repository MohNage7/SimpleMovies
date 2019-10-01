package com.mohnage7.movies.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    private Toast mToast;

    @LayoutRes
    protected abstract int layoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes());
        ButterKnife.bind(this);
    }

    /**
     * this method allows only one toast to be shown at a time.
     * if there's a toast is shown on the screen, cancel it and display the new one.
     *
     * @param title content to be displayed inside the toast
     */
    public void showToast(CharSequence title) {
        if (mToast == null) {
            mToast = Toast.makeText(this, title, Toast.LENGTH_SHORT);
        } else {
            if (mToast.getView().isShown())
                mToast.cancel();
            mToast.setText(title);
        }
        // display the toast new/changed
        mToast.show();
    }

    public void showToast(int stringResources) {
        if (mToast == null) {
            mToast = Toast.makeText(this, stringResources, Toast.LENGTH_SHORT);
        } else {
            if (mToast.getView().isShown())
                mToast.cancel();
            mToast.setText(stringResources);
        }
        // display the toast new/changed
        mToast.show();
    }

}
