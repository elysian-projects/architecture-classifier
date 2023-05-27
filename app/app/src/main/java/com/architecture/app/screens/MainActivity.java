package com.architecture.app.screens;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.architecture.app.R;
import com.architecture.app.components.Navbar;
import com.architecture.app.components.dialog.DialogVariant;
import com.architecture.app.components.dialog.DialogWindow;
import com.architecture.app.utils.AssetsParser;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        new Navbar().attachToLayout(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_reset_progress) {
            new DialogWindow(this)
                .setVariant(DialogVariant.WARNING, this)
                .setTitle("Подтвердите действие")
                .setMessage("Вы уверены, что хотите удалить все данные?")
                .setButtonsLayout(R.layout.button_layout_ok_cancel, this)
                .setClickHandler(view -> {
                    if(view.getId() == R.id.button_ok){
                        Log.i("MainActivity", "Ok clicked");
                        try {
                            AssetsParser.resetData(this);
                            new DialogWindow(this)
                                .setVariant(DialogVariant.SUCCESS, this)
                                .setTitle("Успех")
                                .setMessage("Данные успешно удалены")
                                .setClickHandler(v -> recreate())
                                .show();
                        } catch(IOException exception) {
                            new DialogWindow(this)
                                .setVariant(DialogVariant.DANGER, this)
                                .setTitle("Ошибка")
                                .setMessage("Не удалось удалить данные")
                                .show();

                            Log.w("MainActivity", exception.getMessage(), exception);
                        }
                    }
                })
                .show();
        }

        return super.onOptionsItemSelected(item);
    }
 }