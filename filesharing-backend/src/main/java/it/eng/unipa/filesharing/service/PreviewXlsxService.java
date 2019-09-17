package it.eng.unipa.filesharing.service;

import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

@Component
public class PreviewXlsxService implements PreviewService{

    @Override
    public byte[] convert(byte[] toConvert) {
        try  {
            InputStream xlsx = new ByteArrayInputStream(toConvert);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            IConverter converter = LocalConverter.builder().build();
            converter.convert(xlsx).as(DocumentType.XLSX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();
            System.out.println("success");
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
