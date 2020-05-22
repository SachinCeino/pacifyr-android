package com.pacifyr.client.utility;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pacifyr.client.R;
import com.pdfview.PDFView;

import java.io.File;

public class PdfViewer extends AppCompatActivity {

    public static File pdf;
    private PDFView pdfView;
    private View back;
    private TextView titleText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_viewer);
        back = findViewById(R.id.back_imv);
        titleText = findViewById(R.id.title_txv);
        pdfView = findViewById(R.id.pdfview);
        if (pdf != null) {
            String filename = pdf.getPath();
            filename = filename.substring(filename.lastIndexOf("/"));
            if (filename != null) {
                titleText.setText(filename);
            }
            pdfView.fromFile(pdf).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pdf = null;
    }
}
