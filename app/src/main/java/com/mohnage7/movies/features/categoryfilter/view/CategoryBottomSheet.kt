package com.mohnage7.movies.features.categoryfilter.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.StringDef
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mohnage7.movies.R
import com.mohnage7.movies.features.categoryfilter.callback.OnCategoryClickListener
import com.mohnage7.movies.features.categoryfilter.callback.OnCategorySelectedListener
import com.mohnage7.movies.features.categoryfilter.model.Category
import kotlinx.android.synthetic.main.bottomsheet_category_filter.view.*

const val SELECTED_CATEGORY = "selected_category"
const val POPULAR = "popular"
const val TOP_RATED = "top_rated"
const val UP_COMING = "upcoming"

class CategoryBottomSheet : BottomSheetDialogFragment(), OnCategoryClickListener {

    private var paymentMethodsAdapter: CategoryAdapter? = null
    private var selectedCategory: Category? = null
    private var onCategorySelectedInterActionListener: OnCategorySelectedListener? = null

    @Retention()
    @StringDef(POPULAR, TOP_RATED, UP_COMING)
    annotation class FilterBy

    private var categoriesList: List<Category>? = null

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (activity == null || activity!!.isFinishing) return
        val rootView = View.inflate(context, R.layout.bottomsheet_category_filter, null);
        dialog.setContentView(rootView)
        setBottomBarStyle(dialog)
        setupRecyclerView(rootView.categoryRecyclerView)
        rootView.selectCategoryBtn.setOnClickListener { onCategorySelected() }
    }

    private fun setBottomBarStyle(dialog: Dialog) {
        dialog.window?.let {
            val bottomSheet = it.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet.setBackgroundResource(R.color.transparent)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedCategory = it.getParcelable(SELECTED_CATEGORY)
        }

        categoriesList = listOf(
                Category(getString(R.string.popular), POPULAR, R.drawable.ic_popular),
                Category(getString(R.string.top_rated), TOP_RATED, R.drawable.ic_top_rated),
                Category(getString(R.string.upcoming), UP_COMING, R.drawable.ic_upcoming))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCategorySelectedListener) {
            onCategorySelectedInterActionListener = context
        } else {
            throw RuntimeException("$context must implement OnCheckoutFragmentInteraction")
        }
    }

    private fun setupRecyclerView(categoryRecyclerView: RecyclerView) {
        paymentMethodsAdapter = CategoryAdapter(categoriesList!!, this)
        categoryRecyclerView.layoutManager = LinearLayoutManager(activity)
        categoryRecyclerView.adapter = paymentMethodsAdapter
        // if there's default selected category display check icon for it.
        selectedCategory?.let {
            onCategoryClick(it)
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
        categoriesList?.forEach { category ->
            category.isChecked = category == selectedCategory
        }
        paymentMethodsAdapter!!.notifyDataSetChanged()
    }

    private fun onCategorySelected() {
        dismiss()
        selectedCategory?.let {
            onCategorySelectedInterActionListener!!.onCategorySelected(it)
        }
    }
}
