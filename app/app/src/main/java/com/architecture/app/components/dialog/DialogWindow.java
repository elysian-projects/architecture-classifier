package com.architecture.app.components.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.app.R;

import java.util.HashMap;

public class DialogWindow {
    private static final HashMap<DialogVariant, Integer> variants = new HashMap<>();

    private final Context _context;

    private Dialog _dialog;

    private ImageView _icon;
    private TextView _titleText;
    private TextView _messageText;

    private Button _okButton;

    public DialogWindow(Context context) {
        _context = context;

        setupDialog();
        setupVariants();
        setupComponents();
        addEventListeners();

        setVariant(DialogVariant.INFO);
    }

    public DialogWindow setTitle(String title) {
        _titleText.setText(title);

        return this;
    }

    public DialogWindow setMessage(String message) {
        _messageText.setText(message);

        return this;
    }

    public DialogWindow setVariant(DialogVariant variant) {
        try {
            if(variant == DialogVariant.NONE) {
                _icon.setImageResource(0);
            } else {
                _icon.setImageDrawable(_context.getDrawable(variants.get(variant)));
            }

        } catch(NullPointerException ignored) {}

        return this;
    }

    public void show() {
        _dialog.show();
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

    private void setupVariants() {
        variants.put(DialogVariant.INFO, R.drawable.info);
        variants.put(DialogVariant.SUCCESS, R.drawable.success);
        variants.put(DialogVariant.WARNING, R.drawable.warning);
        variants.put(DialogVariant.DANGER, R.drawable.danger);
    }

    private void setupComponents() {
        _icon = _dialog.findViewById(R.id.dialogIcon);
        _titleText = _dialog.findViewById(R.id.summaryResultMessage);
        _messageText = _dialog.findViewById(R.id.resultDescription);
        _okButton = _dialog.findViewById(R.id.okButton);
    }

    private void addEventListeners() {
        _okButton.setOnClickListener(view -> _dialog.dismiss());
    }
}
