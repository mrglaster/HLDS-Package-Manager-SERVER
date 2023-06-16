package com.hldspm.server.resource_validators;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

/**General archive validator. Created for maps, plugins, modules archives validation*/
public class GeneralValidator {
    protected TarArchiveInputStream tarInputStream;
    protected String base64String;

    /**Constructor*/
    public GeneralValidator(String base64String){
        this.base64String = base64String;
        try{
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64String));
            GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            this.tarInputStream = new TarArchiveInputStream(gzipInputStream);
        } catch (Exception e) {
            this.tarInputStream = null;
        }
    }

    /**Checks if the uploaded map is a tar.gz archive*/
    public boolean isTarGz() {
        return !this.base64String.isEmpty() && this.tarInputStream != null;
    }
}
