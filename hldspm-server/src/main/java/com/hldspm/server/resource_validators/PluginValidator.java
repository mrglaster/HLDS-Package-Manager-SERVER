package com.hldspm.server.resource_validators;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

public class PluginValidator extends GeneralValidator{

    public PluginValidator(String base64String) {
        super(base64String);
        try{
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64String));
            GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            super.tarInputStream = new TarArchiveInputStream(gzipInputStream);
        } catch (Exception e) {
            super.tarInputStream = null;
        }
    }

    public boolean isValidPlugin() throws IOException {
        if (super.tarInputStream == null || !isBase64(base64String)) return false;
        if (!super.isTarGz()) return false;

        TarArchiveEntry entry;
        boolean containsAddonsFolder = false;
        boolean containsAmxModxFolder = false;
        boolean containsScriptingFolder = false;
        boolean containsSmaFile = false;

        while ((entry = super.tarInputStream.getNextTarEntry()) != null) {
            String name = entry.getName();
            if(name.split("/").length - 1 == 0 && !name.endsWith(".txt") && !name.endsWith(".ini") && !name.endsWith(".html") && !name.endsWith(".wad")) return false;
            if (name.contains("addons/amxmodx/scripting") && name.endsWith(".sma")) return true;
            if (name.startsWith("addons/")) {
                containsAddonsFolder = true;
                if (name.startsWith("addons/amxmodx/")) {
                    containsAmxModxFolder = true;
                    if (name.startsWith("addons/amxmodx/scripting/")) {
                        containsScriptingFolder = true;
                        if (name.endsWith(".sma")) {
                            containsSmaFile = true;
                        }
                    }
                }
            }
        }
        return containsAddonsFolder && containsAmxModxFolder &&
                containsScriptingFolder && containsSmaFile;
    }
}
