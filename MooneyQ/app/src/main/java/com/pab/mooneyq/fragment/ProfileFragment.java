package com.pab.mooneyq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pab.mooneyq.R;
import com.pab.mooneyq.activities.MainActivity;
import com.pab.mooneyq.activities.StartActivity;
import com.pab.mooneyq.databinding.FragmentProfileBinding;
import com.pab.mooneyq.retrofit.ApiEndpoint;
import com.pab.mooneyq.retrofit.ApiService;
import com.pab.mooneyq.sharedpreferences.PreferencesManager;
import com.pab.mooneyq.utillities.FormatUtil;
import com.shashank.sony.fancytoastlib.FancyToast;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private final ApiEndpoint api = ApiService.endpoint();
    private Integer userId = 0;
    private PreferencesManager pref;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new PreferencesManager(getContext());
        setupView();
        setupListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.ivAvatar.setImageResource( pref.getInt("pref_user_avatar") );
        FirebaseUser user = mAuth.getCurrentUser();
    }

    private void setupView(){
        binding.textName.setText( pref.getString("pref_user_name") );
        binding.textBalance.setText( getActivity().getIntent().getStringExtra("balance") );
        binding.textEmail.setText( pref.getString("pref_user_email") );
        binding.tvDate.setText( FormatUtil.date( pref.getString("pref_user_date") ) );
    }

    private void setupListener(){
        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProfileFragment.this)
                        .navigate(R.id.action_profileFragment_to_avatarFragment);
            }
        });

        binding.cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.clear();
                mAuth.signOut();
                FancyToast.makeText(getContext(), "Akun berhasil keluar!"
                        , FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                startActivity(new Intent(requireActivity(), StartActivity.class));
                MainActivity.instance.finish();
                requireActivity().finish();
            }
        });
    }
}