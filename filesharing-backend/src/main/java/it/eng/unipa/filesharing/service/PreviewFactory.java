package it.eng.unipa.filesharing.service;

import org.springframework.stereotype.Component;

@Component
public class PreviewFactory {
    public PreviewService getService(String ext) {
        switch (ext.toLowerCase()) {
            case "docx": {
                return new PreviewDocxService();
            }
            case "pptx": {
                return new PreviewPttxService();
            }
            default:
               return new PreviewGenericService();
        }
    }
}
