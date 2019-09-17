package it.eng.unipa.filesharing.service;

public class PreviewGenericService implements PreviewService{


    @Override
    public byte[] convert(byte[] toConvert) {
        return toConvert;
    }
}
