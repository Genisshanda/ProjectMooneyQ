package com.pab.mooneyq.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pab.mooneyq.R;
import com.pab.mooneyq.databinding.AdapterTransactionBinding;
import com.pab.mooneyq.models.TransactionResponse;
import com.pab.mooneyq.utillities.FormatUtil;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<TransactionResponse.Data> dataList;
    private AdapterListener listener;

    public TransactionAdapter(List<TransactionResponse.Data> dataList, AdapterListener listener) {
        this.dataList = dataList;
        this.listener   = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(
                AdapterTransactionBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final TransactionResponse.Data data = dataList.get(i);

        viewHolder.binding.tvCategory.setText(data.getCategory());
        viewHolder.binding.tvDescription.setText(data.getDescription());
        viewHolder.binding.tvDate.setText(data.getDate());
        viewHolder.binding.tvAmount.setText("Rp. " + FormatUtil.number(data.getAmount()) );

        if (data.getType().equals("IN")) viewHolder.binding.imageType.setImageResource(R.drawable.ic_in);
        else viewHolder.binding.imageType.setImageResource(R.drawable.ic_out);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(data);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(data);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterTransactionBinding binding;
        public ViewHolder(AdapterTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setData(List<TransactionResponse.Data> newList) {
        dataList.clear();
        dataList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface AdapterListener {
        void onClick(TransactionResponse.Data data);
        void onLongClick(TransactionResponse.Data data);
    }

}
