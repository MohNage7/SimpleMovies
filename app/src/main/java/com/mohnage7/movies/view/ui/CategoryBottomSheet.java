package com.mohnage7.movies.view.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mohnage7.movies.R;
import com.mohnage7.movies.model.Filter;
import com.mohnage7.movies.utils.Constants;
import com.mohnage7.movies.view.adapter.FilterAdapter;
import com.mohnage7.movies.view.callback.OnCategoryClickListener;
import com.mohnage7.movies.view.callback.OnCategorySelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryBottomSheet extends BottomSheetDialogFragment implements OnCategoryClickListener {

    static final String SELECTED_CATEGORY = "selected_category";

    @BindView(R.id.paymentRecyclerView)
    RecyclerView marketRecyclerView;
    private List<Filter> filterList;
    private FilterAdapter paymentMethodsAdapter;
    private Filter selectedFilter;
    private OnCategorySelectedListener onCategorySelectedInterActionListener;

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        if (getActivity() == null || getActivity().isFinishing()) return;
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.bottomsheet_category_filter, null, false);
        ButterKnife.bind(this, rootView);
        dialog.setContentView(rootView);
        setBottomBarStyle(dialog);
        setupPaymentMethodsRecycler();
    }

    private void setBottomBarStyle(@NonNull Dialog dialog) {
        if (dialog.getWindow() != null) {
            FrameLayout bottomSheet = dialog.getWindow().findViewById(R.id.design_bottom_sheet);
            bottomSheet.setBackgroundResource(R.color.transparent);
            setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(SELECTED_CATEGORY)) {
            selectedFilter = bundle.getParcelable(SELECTED_CATEGORY);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCategorySelectedListener) {
            onCategorySelectedInterActionListener = (OnCategorySelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCheckoutFragmentInteraction");
        }
    }

    private void setupPaymentMethodsRecycler() {
        List<Filter> filterList = getCategoriesList();
        paymentMethodsAdapter = new FilterAdapter(filterList, this);
        marketRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        marketRecyclerView.setAdapter(paymentMethodsAdapter);
        // if there's default selected category display check icon for it.
        if (selectedFilter != null) {
            onCategoryClick(selectedFilter);
        }
    }

    /**
     * Create categories to be filtered with. can be replaced later with list from API.
     *
     * @return List of categories
     */
    private List<Filter> getCategoriesList() {
        filterList = new ArrayList<>();
        filterList.add(new Filter(getString(R.string.popular), Constants.POPULAR, R.drawable.ic_popular));
        filterList.add(new Filter(getString(R.string.top_rated), Constants.TOP_RATED, R.drawable.ic_top_rated));
        filterList.add(new Filter(getString(R.string.upcoming), Constants.UP_COMING, R.drawable.ic_upcoming));
        return filterList;
    }

    /**
     * this method sets new selected category inside the list and refresh the list UI
     * to display/hide check icon.
     *
     * @param selectedFilter
     */
    @Override
    public void onCategoryClick(Filter selectedFilter) {
        this.selectedFilter = selectedFilter;
        for (Filter filter : filterList) {
            filter.setChecked(filter.equals(selectedFilter));
        }
        paymentMethodsAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.select_category_btn)
    void onCategorySelected() {
        dismiss();
        onCategorySelectedInterActionListener.onCategoryClick(selectedFilter);
    }
}
