package com.architecture.app.components;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.architecture.app.R;

public class DialogWindow {
    // `AppCompatActivity` type is used instead of `Context` as we need to find item in the
    // layout from this class but the `Context` type does not include the `findViewById` method
    private final AppCompatActivity _parentActivity;

    private Dialog _dialog;
    private TextView _resultText;
    private TextView _detailsText;
    private Button _okButton;

    public DialogWindow(AppCompatActivity context) {
        _parentActivity = context;

        setupDialog();
        setupComponents();
        addEventListeners();
    }

    public void open(String result, String details) {
        setLabels(result, details);
        _dialog.show();
    }

    private void setLabels(String result, String details) {
        _resultText.setText(result);
        _detailsText.setText(details);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupDialog() {
        _dialog = new Dialog(_parentActivity);

        _dialog.setContentView(R.layout.dialog_layout);
        _dialog.getWindow().setBackgroundDrawable(_parentActivity.getDrawable(R.drawable.dialog_background));
        _dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        _dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        _dialog.setCancelable(false);
    }

    private void setupComponents() {
        _resultText = _parentActivity.findViewById(R.id.summaryResultMessage);
        _detailsText = _parentActivity.findViewById(R.id.resultDescription);
        _okButton = _parentActivity.findViewById(R.id.okButton);
    }

    private void addEventListeners() {
        _okButton.setOnClickListener(view -> _dialog.dismiss());
    }
}
