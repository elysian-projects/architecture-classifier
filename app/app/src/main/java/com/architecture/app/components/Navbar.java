package com.architecture.app.components;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.architecture.app.R;
import com.architecture.app.databinding.ActivityNavbarBinding;
import com.architecture.app.fragments.CameraFragment;
import com.architecture.app.fragments.CheckFragment;
import com.architecture.app.fragments.HomeFragment;
import com.architecture.app.fragments.QuestionFragment;

public class Navbar {
    private AppCompatActivity _context;

    private ActivityNavbarBinding _binding;

    public Navbar(AppCompatActivity context) {
        _context = context;

        initializeButtons();
    }

    private void initializeButtons() {
        _binding = ActivityNavbarBinding.inflate(_context.getLayoutInflater());
        _context.setContentView(_binding.getRoot());
        replaceFragment(new HomeFragment());

        _binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.camera:
                    replaceFragment(new CameraFragment());
                    break;
                case R.id.check:
                    replaceFragment(new CheckFragment());
                    break;
                case R.id.question:
                    replaceFragment(new QuestionFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = _context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}
