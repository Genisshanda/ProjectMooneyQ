package com.pab.mooneyq.adapters;

import static android.os.Looper.getMainLooper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.pab.mooneyq.R;
import com.pab.mooneyq.databinding.AdapterCategoryBinding;
import com.pab.mooneyq.models.CategoryResponse;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryResponse.Data> results;
    private AdapterListener listener;

    List<MaterialButton> buttonList = new ArrayList<>();
    private Context context;

    public CategoryAdapter(Context context, List<CategoryResponse.Data> results, AdapterListener listener) {
        this.context    = context ;
        this.results    = results ;
        this.listener   = listener ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(
                AdapterCategoryBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final CategoryResponse.Data data = results.get(i);
        viewHolder.binding.btnKategori.setText( data.getName() );
        buttonList.add(viewHolder.binding.btnKategori);
        viewHolder.binding.btnKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data);
                setButtonList(viewHolder.binding.btnKategori);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterCategoryBinding binding;
        public ViewHolder(AdapterCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setData(List<CategoryResponse.Data> data) {
        results.clear();
        results.addAll(data);
        notifyDataSetChanged();
    }

    public interface AdapterListener {
        void onClick(CategoryResponse.Data result);
    }

    private void setButtonList(MaterialButton button){
        for( MaterialButton buttons : buttonList){
            buttons.setTextColor(context.getResources().getColor(R.color.teal_200));
            ViewCompat.setBackgroundTintList(
                    buttons, ColorStateList.valueOf(context.getResources().getColor(R.color.white))
            );
        }
        button.setTextColor(context.getResources().getColor(R.color.white));
        ViewCompat.setBackgroundTintList(
                button, ColorStateList.valueOf(context.getResources().getColor(R.color.teal_700))
        );
    }

    public void setButtonList(CategoryResponse.Data category){
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                for( MaterialButton buttons : buttonList){
                    Log.e("setButtonList", "" + buttons.getText());
                    if (buttons.getText().toString().contains( category.getName() )) {
                        buttons.setTextColor(context.getResources().getColor(R.color.white));
                        ViewCompat.setBackgroundTintList(
                                buttons, ColorStateList.valueOf(context.getResources().getColor(R.color.teal_700))
                        );
                    }
                }
            }
        }, 500);
    }
}