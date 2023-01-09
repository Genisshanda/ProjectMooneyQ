package com.pab.mooneyq.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pab.mooneyq.databinding.AdapterAvatarBinding;

import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {

    private List<Integer> avatars;
    private AdapterListener listener;

    public AvatarAdapter(List<Integer> avatars, AdapterListener listener) {
        this.avatars    = avatars;
        this.listener   = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(
                AdapterAvatarBinding.inflate( LayoutInflater.from(parent.getContext()), parent, false )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Integer avatar = avatars.get(i);
        viewHolder.binding.ivAvatar.setImageResource(avatar);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(avatar);
            }
        });
    }

    @Override
    public int getItemCount() {
        return avatars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterAvatarBinding binding;
        public ViewHolder(AdapterAvatarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setData(List<Integer> data) {
        avatars.addAll(data);
        notifyDataSetChanged();
    }

    public interface AdapterListener {
        void onClick(Integer avatar);
    }

}
