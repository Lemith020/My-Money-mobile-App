package org.example.ui.reports;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.example.domain.model.Transaction;
import org.example.util.CurrencyFormatter;
import org.example.util.DateUtils;

public class ExportPdfService {

    private final Context context;

    public ExportPdfService(Context context) {
        this.context = context;
    }

    public void export(List<Transaction> transactions, String currencySymbol) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        int y = 30;
        canvas.drawText("My Money - Transaction Report", 10, y, paint);
        y += 20;

        for (Transaction transaction : transactions) {
            String line = DateUtils.formatDate(transaction.getDate()) + " " + transaction.getCategory() + " " + CurrencyFormatter.format(transaction.getAmount(), currencySymbol);
            canvas.drawText(line, 10, y, paint);
            y += 18;
        }

        document.finishPage(page);

        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "my_money_report.pdf");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            document.writeTo(outputStream);
            outputStream.close();

            Toast.makeText(context, "Report exported successfully!", Toast.LENGTH_SHORT).show();


            openPdfFile(file);
        } catch (IOException e) {
            Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show();
        }

        document.close();
    }

    private void openPdfFile(File file) {
        try {
            Uri pdfUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    file
            );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(Intent.createChooser(intent, "Open Report PDF"));
        } catch (Exception e) {

            Toast.makeText(context, "Report exported to " + file.getPath(), Toast.LENGTH_LONG).show();
        }
    }
}