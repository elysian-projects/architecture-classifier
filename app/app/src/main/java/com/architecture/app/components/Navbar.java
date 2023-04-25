package com.architecture.app.components;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.architecture.app.R;
import com.architecture.app.databinding.ActivityNavbarBinding;
import com.architecture.app.screens.fragments.AchievementsFragment;
import com.architecture.app.screens.fragments.HomeFragment;
import com.architecture.app.screens.fragments.QuestionFragment;
import com.architecture.app.screens.UploadActivity;

public class Navbar {
    private final AppCompatActivity _context;

    public Navbar(AppCompatActivity context) {
        _context = context;

        initializeButtons();
    }

    private void initializeButtons() {
        ActivityNavbarBinding binding = ActivityNavbarBinding.inflate(_context.getLayoutInflater());
        _context.setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragmentToLoad;

            switch(item.getItemId()) {
                case R.id.home:
                    fragmentToLoad = new HomeFragment();
                    break;
                case R.id.upload:

                    // This button must not change a fragment, it changes activity to the `UploadActivity`
                    _context.startActivity(new Intent(_context, UploadActivity.class));

                    return false;
                case R.id.check:
                    fragmentToLoad = new AchievementsFragment();
                    break;
                case R.id.question:
                    fragmentToLoad = new QuestionFragment();
                    break;
                default:
                    return false;
            }

            replaceFragment(fragmentToLoad);

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
