package com.hldspm.server.resource_validators;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;


/**Class for the uploaded map validation*/
public class MapValidator extends GeneralValidator {


    /**Constructor*/
    public MapValidator(String base64String) {
        super(base64String);
        try{
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64String));
            GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            super.tarInputStream = new TarArchiveInputStream(gzipInputStream);
        } catch (Exception e) {
            super.tarInputStream = null;
        }
    }

    /**Checks if the uploaded map is valid*/
    public boolean isValidMap() {
        String targetFolder = "maps/";
        String targetFileExt = ".bsp";
        if (!super.isTarGz()) {
            return false;
        }
        try {
            TarArchiveEntry entry;
            while ((entry = super.tarInputStream.getNextTarEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().startsWith(targetFolder) &&
                        entry.getName().endsWith(targetFileExt)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }
}
