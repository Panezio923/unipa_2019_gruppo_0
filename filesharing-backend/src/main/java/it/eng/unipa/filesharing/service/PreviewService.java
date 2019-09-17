package it.eng.unipa.filesharing.service;

import com.itextpdf.text.DocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

public interface PreviewService {
    byte[] convert(byte[] toConvert) throws InvalidFormatException, IOException, DocumentException;
}
