package com.pacifyr.client.utility;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pacifyr.client.R;
import com.pacifyr.client.event.AlertTypeEvent;
import com.pacifyr.client.event.NotifyEvent;
import com.pacifyr.client.quickblox.activities.CallActivity;
import com.pacifyr.client.twilio.VideoActivity;
import com.pdfview.PDFView;

import java.io.File;

import static com.pacifyr.client.twilio.VideoActivity.isCallActive;


public class PdfViewer extends PacifyrAppCompactActivity {

    public static File pdf;
    private PDFView pdfView;
    private View back;
    private TextView titleText;
    private boolean restoreCall;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_viewer);
        back = findViewById(R.id.back_imv);
        titleText = findViewById(R.id.title_txv);
        pdfView = findViewById(R.id.pdfview);
        restoreCall = getIntent().getBooleanExtra("restoreCall", false);
        if (pdf != null) {
            String filename = pdf.getPath();
            filename = filename.substring(filename.lastIndexOf("/"));
            if (isValid(getIntent()) && isValid(getIntent().getExtras()) && isValid(getIntent().getExtras().getString("title")))
            {
                filename=getIntent().getExtras().getString("title");
            }
            if (filename != null) {
                titleText.setText(filename);
            }
            pdfView.fromFile(pdf).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pdf = null;
    }

    @Override
    public void onSetValues() throws Exception {

    }

    @Override
    public void onNotifyEvent(NotifyEvent event) {

    }

    @Override
    public void onAlertTypeEvent(AlertTypeEvent event) {

    }

    @Override
    public void onBackPressed() {
        if (restoreCall && isCallActive) {
            Intent i = new Intent(PdfViewer.this, VideoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        }
        finish();
    }
}
