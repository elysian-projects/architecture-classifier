package com.architecture.app.components.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.architecture.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DialogWindow {
    private static final HashMap<DialogVariant, Integer> variants = new HashMap<>();

    private Dialog _dialog;
    private ImageView _icon;
    private TextView _titleText;
    private TextView _messageText;
    private FrameLayout _buttonsPlaceholder;
    private ButtonClickHandler _clickHandler = view -> {};

    private boolean _cancellable = true;

    public DialogWindow(@NonNull Context context) {
        setupDialog(context);
        setupVariants();
        setupComponents();

        setVariant(DialogVariant.INFO, context);
        setButtonsLayout(R.layout.button_layout_ok, context);
    }

    public DialogWindow(@NonNull Context context, boolean cancellable) {
        this(context);

        _cancellable = cancellable;
    }

    public DialogWindow setTitle(String title) {
        _titleText.setText(title);

        return this;
    }

    public DialogWindow setMessage(String message) {
        _messageText.setText(message);

        return this;
    }

    public DialogWindow setIcon(Drawable icon) {
        _icon.setImageDrawable(icon);

        return this;
    }

    public DialogWindow setIcon(Bitmap icon) {
        _icon.setImageBitmap(icon);

        return this;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public DialogWindow setVariant(DialogVariant variant, @NotNull Context context) {
        try {
            _icon.setImageDrawable(context.getDrawable(variants.get(variant)));
        } catch(NullPointerException exception) {
            Log.i("DialogWindow", "Count not set dialog window variant", exception);
        }

        return this;
    }

    public DialogWindow setButtonsLayout(int layout, @NotNull Context context) {
        _buttonsPlaceholder.removeAllViews();
        _buttonsPlaceholder.addView(LayoutInflater.from(context).inflate(layout, null));

        addEventListeners();

        return this;
    }

    public DialogWindow setClickHandler(ButtonClickHandler clickHandler) {
        _clickHandler = clickHandler;

        addEventListeners();

        return this;
    }

    public void show() {
        _dialog.show();
    }

    protected TextView getTitleText() {
        return _titleText;
    }

    protected TextView getMessageText() {
        return _messageText;
    }

    protected ImageView getIcon() {
        return _icon;
    }

    protected <T extends View> void removeItem(T item) {
        try {
            ((ViewManager)item.getParent()).removeView(item);
        } catch(Exception exception) {
            Log.i("DialogWindow", "Unable to remove view!", exception);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupDialog(Context context) {
        _dialog = new Dialog(context);

        _dialog.setContentView(R.layout.dialog_layout);
        _dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.dialog_background));
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
        _icon = _dialog.findViewById(R.id.dialog_icon);
        _titleText = _dialog.findViewById(R.id.dialog_title);
        _messageText = _dialog.findViewById(R.id.dialog_message);
        _buttonsPlaceholder = _dialog.findViewById(R.id.dialog_button_placeholder);
    }

    private void addEventListeners() throws IllegalStateException {
        View layout = _buttonsPlaceholder.getChildAt(0);

        if(layout instanceof ViewGroup == false) {
            throw new IllegalStateException("Invalid layout for buttons was provided!");
        }

        ViewGroup viewGroup = (ViewGroup)layout;

        for(int i = 0; i < viewGroup.getChildCount(); i++) {
            View item = viewGroup.getChildAt(i);

            if(!shouldAddClickListener(item)) {
                continue;
            }

            item.setOnClickListener(view -> {
                _clickHandler.execute(view);
                _dialog.dismiss();
            });
        }
    }

    private boolean shouldAddClickListener(View view) {
        return view instanceof Button;
    }
}
