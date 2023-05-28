package com.architecture.app.components;

import android.content.Context;
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

import java.util.HashMap;
import java.util.Map;

public class Navbar {
    private static final Map<Class<? extends Fragment>, String> TITLES = new HashMap<>();

    public void attachToLayout(AppCompatActivity context) {
        ActivityNavbarBinding binding = ActivityNavbarBinding.inflate(context.getLayoutInflater());
        context.setContentView(binding.getRoot());

        initializeButtons(binding, context);
    }

    private void initializeButtons(ActivityNavbarBinding binding, AppCompatActivity context) {
        setTitles(context);
        replaceFragment(new HomeFragment(), context.getString(R.string.app_name), context);

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

            replaceFragment(fragmentToLoad, TITLES.get(fragmentToLoad.getClass()), context);

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

    private void setTitles(Context context) {
        if(TITLES.isEmpty()) {
            TITLES.put(HomeFragment.class, context.getString(R.string.app_name));
            TITLES.put(UploadFragment.class, "Загрузка изображений");
            TITLES.put(AchievementsFragment.class, "Достижения");
            TITLES.put(QuestionFragment.class, "QUIZ");
        }
    }
}
