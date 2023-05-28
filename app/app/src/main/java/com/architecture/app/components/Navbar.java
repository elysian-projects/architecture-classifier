package com.architecture.app.components;

import android.util.Log;

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
    public void attachToLayout(AppCompatActivity context) {
        ActivityNavbarBinding binding = ActivityNavbarBinding.inflate(context.getLayoutInflater());
        context.setContentView(binding.getRoot());

        initializeButtons(binding, context);
    }

    private void initializeButtons(ActivityNavbarBinding binding, AppCompatActivity context) {
        replaceFragment(new HomeFragment(), context.getString(R.string.app_name), context);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragmentToLoad;
            String title;

            switch(item.getItemId()) {
                case R.id.home:
                    fragmentToLoad = new HomeFragment();
                    title = context.getString(R.string.app_name);
                    break;
                case R.id.upload:
                    fragmentToLoad = new UploadFragment();
                    title = "Загрузка изображений";
                    break;
                case R.id.check:
                    fragmentToLoad = new AchievementsFragment();
                    title = "Достижения";
                    break;
                case R.id.question:
                    fragmentToLoad = new QuestionFragment();
                    title = "QUIZ";
                    break;
                default:
                    return false;
            }

            replaceFragment(fragmentToLoad, title, context);

            return true;
        });
    }

    private void replaceFragment(Fragment fragment, String title, AppCompatActivity context) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
        fragmentTransaction.runOnCommit(() -> {
            try {
                context.getSupportActionBar().setTitle(title);
            } catch(NullPointerException exception) {
                Log.w("Navbar", exception.getMessage(), exception);
            }
        });
    }
}
