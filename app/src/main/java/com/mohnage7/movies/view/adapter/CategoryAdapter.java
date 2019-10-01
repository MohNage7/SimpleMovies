package com.mohnage7.movies.view.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mohnage7.movies.R;
import com.mohnage7.movies.base.BaseViewHolder;
import com.mohnage7.movies.model.Category;
import com.mohnage7.movies.view.callback.OnCategoryClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<Category> categoryList;
    private OnCategoryClickListener onArticleClickListener;

    public CategoryAdapter(List<Category> categoryList, OnCategoryClickListener onCategoryClickListener) {
        this.categoryList = categoryList;
        this.onArticleClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_filter, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (categoryList != null && !categoryList.isEmpty()) {
            return categoryList.size();
        } else {
            return 0;
        }
    }


    protected class MoviesViewHolder extends BaseViewHolder {
        @BindView(R.id.category_image)
        ImageView categoryImgView;
        @BindView(R.id.category_name_tv)
        TextView categoryTitle;


        MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(int position) {
            super.bindViews(position);
            Category category = categoryList.get(position);
            categoryTitle.setText(category.getName());
            categoryImgView.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), category.getDrawable()));
            if (category.isChecked()) {
                Drawable img = itemView.getContext().getResources().getDrawable(R.drawable.ic_check);
                categoryTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            } else {
                categoryTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            itemView.setOnClickListener(v -> {
                category.setChecked(true);
                onArticleClickListener.onCategoryClick(category);
            });
        }


        @Override
        public void clear() {
            categoryTitle.setText("");
            categoryImgView.setImageDrawable(null);
        }
    }
}
