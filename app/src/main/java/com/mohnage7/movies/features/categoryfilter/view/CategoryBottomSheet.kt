package com.mohnage7.movies.features.categoryfilter.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.StringDef
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mohnage7.movies.R
import com.mohnage7.movies.features.categoryfilter.model.Category
import com.mohnage7.movies.features.movies.view.callback.OnCategoryClickListener
import com.mohnage7.movies.features.movies.view.callback.OnCategorySelectedListener

import java.lang.annotation.Retention
import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

import java.lang.annotation.RetentionPolicy.SOURCE

class CategoryBottomSheet : BottomSheetDialogFragment(), OnCategoryClickListener {
    @BindView(R.id.paymentRecyclerView)
    internal var marketRecyclerView: RecyclerView? = null
    private var categoryList: MutableList<Category>? = null
    private var paymentMethodsAdapter: CategoryAdapter? = null
    private var selectedCategory: Category? = null
    private var onCategorySelectedInterActionListener: OnCategorySelectedListener? = null

    /**
     * Create categories to be filtered with. can be replaced later with list from API.
     *
     * @return List of categories
     */
    private val categoriesList: List<Category>
        get() {
            categoryList = ArrayList()
            categoryList!!.add(Category(getString(R.string.popular), POPULAR, R.drawable.ic_popular))
            categoryList!!.add(Category(getString(R.string.top_rated), TOP_RATED, R.drawable.ic_top_rated))
            categoryList!!.add(Category(getString(R.string.upcoming), UP_COMING, R.drawable.ic_upcoming))
            return categoryList
        }

    @Retention(SOURCE)
    @StringDef(POPULAR, TOP_RATED, UP_COMING)
    annotation class FilterBy

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (activity == null || activity!!.isFinishing) return
        val rootView = activity!!.layoutInflater.inflate(R.layout.bottomsheet_category_filter, null, false)
        ButterKnife.bind(this, rootView)
        dialog.setContentView(rootView)
        setBottomBarStyle(dialog)
        setupPaymentMethodsRecycler()
    }

    private fun setBottomBarStyle(dialog: Dialog) {
        if (dialog.window != null) {
            val bottomSheet = dialog.window!!.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet.setBackgroundResource(R.color.transparent)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null && bundle.containsKey(SELECTED_CATEGORY)) {
            selectedCategory = bundle.getParcelable(SELECTED_CATEGORY)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCategorySelectedListener) {
            onCategorySelectedInterActionListener = context
        } else {
            throw RuntimeException("$context must implement OnCheckoutFragmentInteraction")
        }
    }

    private fun setupPaymentMethodsRecycler() {
        val categoryList = categoriesList
        paymentMethodsAdapter = CategoryAdapter(categoryList, this)
        marketRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        marketRecyclerView!!.adapter = paymentMethodsAdapter
        // if there's default selected category display check icon for it.
        if (selectedCategory != null) {
            onCategoryClick(selectedCategory)
        }
    }

    /**
     * this method sets new selected category inside the list and refresh the list UI
     * to display/hide check icon.
     *
     * @param selectedCategory
     */
    override fun onCategoryClick(selectedCategory: Category) {
        this.selectedCategory = selectedCategory
        for (category in categoryList!!) {
            category.isChecked = category == selectedCategory
        }
        paymentMethodsAdapter!!.notifyDataSetChanged()
    }

    @OnClick(R.id.select_category_btn)
    internal fun onCategorySelected() {
        dismiss()
        onCategorySelectedInterActionListener!!.onCategoryClick(selectedCategory!!)
    }

    companion object {

        val SELECTED_CATEGORY = "selected_category"
        val POPULAR = "popular"
        val TOP_RATED = "top_rated"
        val UP_COMING = "upcoming"
    }
}
