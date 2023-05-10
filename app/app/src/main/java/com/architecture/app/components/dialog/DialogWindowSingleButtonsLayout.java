package com.architecture.app.components.dialog;

import android.content.Context;

/**
 * This class helps create a dialog window with buttons layout only
 */
public class DialogWindowSingleButtonsLayout extends DialogWindow {
    public DialogWindowSingleButtonsLayout(Context context) {
        super(context);
        removeExtraElements();
    }

    public DialogWindowSingleButtonsLayout(Context context, boolean cancellable) {
        super(context, cancellable);
        removeExtraElements();
    }

    private void removeExtraElements() {
        removeItem(icon);
        removeItem(titleText);
        removeItem(messageText);
    }
}