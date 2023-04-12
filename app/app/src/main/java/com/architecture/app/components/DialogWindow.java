package com.architecture.app.components;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.app.R;

public class DialogWindow {
    private final Context _context;

    private Dialog _dialog;
    private ImageView _icon;
    private TextView _resultText;
    private TextView _detailsText;
    private Button _okButton;

    public DialogWindow(Context context) {
        _context = context;

        setupDialog();
        setupComponents();
        addEventListeners();
        setSuccessfulState();
    }

    public void show(String result, String details) {
        setLabels(result, details);
        _dialog.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setSuccessfulState() {
        _icon.setImageDrawable(_context.getDrawable(R.drawable.success1));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setFailedState() {
        _icon.setImageDrawable(_context.getDrawable(R.drawable.info));
    }

    private void setLabels(String result, String details) {
        _resultText.setText(result);
        _detailsText.setText(details);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupDialog() {
        _dialog = new Dialog(_context);

        _dialog.setContentView(R.layout.dialog_layout);
        _dialog.getWindow().setBackgroundDrawable(_context.getDrawable(R.drawable.dialog_background));
        _dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        _dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        _dialog.setCancelable(false);
    }

    private void setupComponents() {
        _icon = _dialog.findViewById(R.id.dialogIcon);
        _resultText = _dialog.findViewById(R.id.summaryResultMessage);
        _detailsText = _dialog.findViewById(R.id.resultDescription);
        _okButton = _dialog.findViewById(R.id.okButton);
    }

    private void addEventListeners() {
        _okButton.setOnClickListener(view -> _dialog.dismiss());
    }
}
