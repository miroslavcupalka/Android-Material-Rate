package hotchemi.android.rate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import static hotchemi.android.rate.IntentHelper.createIntentForGooglePlay;
import static hotchemi.android.rate.PreferenceHelper.setAgreeShowDialog;
import static hotchemi.android.rate.PreferenceHelper.setRemindInterval;
import static hotchemi.android.rate.Utils.getDialogBuilder;

final class DialogManager {

    private static final int DIALOG_BUTTON_POSITIVE_ID = -1;
    private static final int DIALOG_BUTTON_NEUTRAL_ID = -3;
    private static final int DIALOG_BUTTON_NEGATIVE_ID = -2;

    private DialogManager() {
    }

    static Dialog createDialog(final Context context, DialogOptions options){
        return options.isEnforcingMaterialDialog() ? createMaterial(context, options) : create(context,options);
    }

    static Dialog create(final Context context, DialogOptions options) {
        AlertDialog.Builder builder = getDialogBuilder(context);
        builder.setMessage(options.getMessageResId());

        if (options.shouldShowTitle()) builder.setTitle(options.getTitleResId());

        builder.setCancelable(options.getCancelable());

        View view = options.getView();
        if (view != null) builder.setView(view);

        final OnClickButtonListener listener = options.getListener();

        builder.setPositiveButton(options.getTextPositiveResId(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(createIntentForGooglePlay(context));
                setAgreeShowDialog(context, false);
                if (listener != null) listener.onClickButton(which);
            }
        });

        if (options.shouldShowNeutralButton()) {
            builder.setNeutralButton(options.getTextNeutralResId(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setRemindInterval(context);
                    if (listener != null) listener.onClickButton(which);
                }
            });
        }

        builder.setNegativeButton(options.getTextNegativeResId(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setAgreeShowDialog(context, false);
                if (listener != null) listener.onClickButton(which);
            }
        });

        return builder.create();
    }

    static MaterialDialog createMaterial(final Context context, DialogOptions options) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);

        builder.content(options.getMessageResId());

        if (options.shouldShowTitle()) builder.title(options.getTitleResId());

        builder.cancelable(options.getCancelable());

        View view = options.getView();
        if (view != null) builder.customView(view, true);

        final OnClickButtonListener listener = options.getListener();
        builder.positiveText(options.getTextPositiveResId());

        if (options.shouldShowNeutralButton()) {
            builder.neutralText(options.getTextNeutralResId());
        }

        builder.negativeText(options.getTextNegativeResId());


        builder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                context.startActivity(createIntentForGooglePlay(context));
                setAgreeShowDialog(context, false);
                if (listener != null) listener.onClickButton(DIALOG_BUTTON_POSITIVE_ID);
            }

            @Override
            public void onNeutral(MaterialDialog dialog) {
                setRemindInterval(context);
                if (listener != null) listener.onClickButton(DIALOG_BUTTON_NEUTRAL_ID);
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                setAgreeShowDialog(context, false);
                if (listener != null) listener.onClickButton(DIALOG_BUTTON_NEGATIVE_ID);
            }
        });


        return builder.build();
    }

}