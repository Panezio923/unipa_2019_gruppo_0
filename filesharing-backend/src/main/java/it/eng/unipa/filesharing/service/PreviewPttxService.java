package it.eng.unipa.filesharing.service;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xslf.usermodel.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.stereotype.Component;


@Component
public class PreviewPttxService implements PreviewService {
    @Override
    public byte[] convert(byte[] inputStream) throws IOException {

        XMLSlideShow ppt = new XMLSlideShow(new ByteArrayInputStream(inputStream));

        //getting the dimensions and size of the slide

        Dimension pgsize = ppt.getPageSize();
        List<XSLFSlide> slide = Arrays.asList(ppt.getSlides());
        BufferedImage img = null;
        Document doc = new Document();

        try {
            PdfWriter.getInstance(doc, new ByteArrayOutputStream());

            doc.open();

            for (int i = 0; i < slide.size(); i++) {
                img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();

                //clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

                //render
                slide.get(i).draw(graphics);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                javax.imageio.ImageIO.write(img, "png", out);
                ppt.write(out);
                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(out.toByteArray());
                doc.setPageSize(new com.itextpdf.text.Rectangle(image.getScaledWidth(), image.getScaledHeight()));
                doc.newPage();
                image.setAbsolutePosition(0, 0);
                doc.add(image);

                out.close();
                return out.toByteArray();
            }
            System.out.println("ERROE");
            doc.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
