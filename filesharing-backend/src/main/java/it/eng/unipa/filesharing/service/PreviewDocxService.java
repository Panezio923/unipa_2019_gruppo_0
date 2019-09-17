package it.eng.unipa.filesharing.service;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class PreviewDocxService implements PreviewService {
    @Override
    public byte[] convert(byte[] toConvert) {
        try {
            InputStream in = new ByteArrayInputStream(toConvert);
            XWPFDocument document = new XWPFDocument(in);
            PdfOptions options = PdfOptions.create();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfConverter.getInstance().convert(document, out, options);

            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
