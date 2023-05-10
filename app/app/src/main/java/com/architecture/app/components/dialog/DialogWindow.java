package com.architecture.app.components.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.architecture.app.R;

import java.util.HashMap;

public class DialogWindow {
    private static final HashMap<DialogVariant, Integer> variants = new HashMap<>();

    private final Context _context;

    // These fields must be protected, as there should be a way to use them
    // from the children classes to provide some additional features
    protected ImageView icon;
    protected TextView titleText;
    protected TextView messageText;

    private Dialog _dialog;
    private FrameLayout _buttonsPlaceholder;
    private ButtonClickHandler _clickHandler = view -> {};

    private boolean _cancellable = true;

    public DialogWindow(Context context) {
        _context = context;

        setupDialog();
        setupVariants();
        setupComponents();

        setVariant(DialogVariant.INFO);
        setButtonsLayout(R.layout.button_layout_ok);
    }

    public DialogWindow(Context context, ButtonClickHandler clickHandler) {
        this(context);

        _clickHandler = clickHandler;
    }

    public DialogWindow(Context context, ButtonClickHandler clickHandler, boolean cancellable) {
        this(context, clickHandler);

        _cancellable = cancellable;
    }

    public DialogWindow setTitle(String title) {
        titleText.setText(title);

        return this;
    }

    public DialogWindow setMessage(String message) {
        messageText.setText(message);

        return this;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public DialogWindow setVariant(DialogVariant variant) {
        try {
            icon.setImageDrawable(_context.getDrawable(variants.get(variant)));
        } catch(NullPointerException exception) {
            Log.i("DialogWindow", "Count not set dialog window variant", exception);
        }

        return this;
    }

    public DialogWindow setButtonsLayout(int layout) {
        _buttonsPlaceholder.removeAllViews();
        _buttonsPlaceholder.addView(LayoutInflater.from(_context).inflate(layout, null));

        addEventListeners();

        return this;
    }

    public void show() {
        _dialog.show();
    }

    protected <T extends View> void removeItem(T item) {
        try {
            ((ViewManager)item.getParent()).removeView(item);
        } catch(Exception exception) {
            Log.i("DialogWindow", "Unable to remove view!", exception);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupDialog() {
        _dialog = new Dialog(_context);

        _dialog.setContentView(R.layout.dialog_layout);
        _dialog.getWindow().setBackgroundDrawable(_context.getDrawable(R.drawable.dialog_background));
        _dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        _dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        _dialog.setCancelable(_cancellable);
    }

    private void setupVariants() {
        if(!variants.isEmpty()) {
            return;
        }

        variants.put(DialogVariant.INFO, R.drawable.info);
        variants.put(DialogVariant.SUCCESS, R.drawable.success);
        variants.put(DialogVariant.WARNING, R.drawable.warning);
        variants.put(DialogVariant.DANGER, R.drawable.danger);
    }

    private void setupComponents() {
        icon = _dialog.findViewById(R.id.dialog_icon);
        titleText = _dialog.findViewById(R.id.dialog_title);
        messageText = _dialog.findViewById(R.id.dialog_message);
        _buttonsPlaceholder = _dialog.findViewById(R.id.dialog_button_placeholder);
    }

    private void addEventListeners() throws IllegalStateException {
        View layout = _buttonsPlaceholder.getChildAt(0);

        if(layout instanceof ViewGroup == false) {
            throw new IllegalStateException("Invalid layout for buttons was provided!");
        }

        ViewGroup viewGroup = (ViewGroup)layout;

        for(int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setOnClickListener(view -> {
                _clickHandler.execute(view);
                _dialog.dismiss();
            });
        }
    }
}
