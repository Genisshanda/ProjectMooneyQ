package com.pab.mooneyq.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pab.mooneyq.R;
import com.pab.mooneyq.adapters.AvatarAdapter;
import com.pab.mooneyq.databinding.FragmentAvatarBinding;
import com.pab.mooneyq.sharedpreferences.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class AvatarFragment extends Fragment {

    private FragmentAvatarBinding binding;
    private AvatarAdapter avatarAdapter;
    private List<Integer> avatars = new ArrayList<>();
    private PreferencesManager pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAvatarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new PreferencesManager(getContext());
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        avatars.add(R.drawable.avatar1);
        avatars.add(R.drawable.avatar2);
        avatars.add(R.drawable.avatar3);
        avatars.add(R.drawable.avatar4);
        avatars.add(R.drawable.avatar5);
        avatars.add(R.drawable.avatar6);
        avatars.add(R.drawable.avatar7);
        avatars.add(R.drawable.avatar8);
        avatars.add(R.drawable.avatar9);
        avatars.add(R.drawable.avatar10);
        avatarAdapter = new AvatarAdapter(avatars, new AvatarAdapter.AdapterListener() {
            @Override
            public void onClick(Integer avatar) {
                pref.put("pref_user_avatar", avatar);
                NavHostFragment.findNavController(AvatarFragment.this)
                        .navigateUp();
            }
        });
        binding.listAvatar.setAdapter( avatarAdapter );
    }
}