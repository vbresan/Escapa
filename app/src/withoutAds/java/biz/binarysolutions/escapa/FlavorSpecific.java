package biz.binarysolutions.escapa;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

public class FlavorSpecific {

    private void showSupportDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(R.string.SupportDialogTitle);
        builder.setMessage(R.string.SupportDialogText);

        builder.setPositiveButton(R.string.Continue, (dialog, which) -> {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(activity.getString(R.string.donation_url)));
            activity.startActivity(intent);

            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private final Activity activity;

    public FlavorSpecific(Activity activity) {
        this.activity = activity;
    }

    public void initialize() {

        Button button = activity.findViewById(R.id.buttonSupport);
        if (button != null) {
            button.setOnClickListener(view -> showSupportDialog());
        }
    }
}
