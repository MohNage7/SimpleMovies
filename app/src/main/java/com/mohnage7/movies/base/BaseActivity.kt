package com.mohnage7.movies.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    private var mToast: Toast? = null

    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes())
    }

    /**
     * this method allows only one toast to be shown at a time.
     * if there's a toast is shown on the screen, cancel it and display the new one.
     *
     * @param title content to be displayed inside the toast
     */
    fun showToast(title: CharSequence) {
        if (mToast == null) {
            mToast = Toast.makeText(this, title, Toast.LENGTH_SHORT)
        } else {
            if (mToast!!.view.isShown)
                mToast!!.cancel()
            mToast!!.setText(title)
        }
        // display the toast new/changed
        mToast!!.show()
    }

    fun showToast(stringResources: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(this, stringResources, Toast.LENGTH_SHORT)
        } else {
            if (mToast!!.view.isShown)
                mToast!!.cancel()
            mToast!!.setText(stringResources)
        }
        // display the toast new/changed
        mToast!!.show()
    }

}
