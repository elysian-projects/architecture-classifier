package com.architecture.app.components;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.architecture.app.R;
import com.architecture.app.databinding.ActivityNavbarBinding;
import com.architecture.app.screens.fragments.AchievementsFragment;
import com.architecture.app.screens.fragments.HomeFragment;
import com.architecture.app.screens.fragments.QuestionFragment;
import com.architecture.app.screens.fragments.UploadFragment;

public class Navbar {
    private AppCompatActivity _context;

    public void attachToLayout(AppCompatActivity context) {
        _context = context;

        ActivityNavbarBinding binding = ActivityNavbarBinding.inflate(context.getLayoutInflater());
        context.setContentView(binding.getRoot());

        initializeButtons(binding);
    }

    private void initializeButtons(ActivityNavbarBinding binding) {
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragmentToLoad;

            switch(item.getItemId()) {
                case R.id.home:
                    fragmentToLoad = new HomeFragment();
                    break;
                case R.id.upload:
                    fragmentToLoad = new UploadFragment();
                    break;
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
