package com.pd.rt.context.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.CodeSource;

public class Version {

    private static final Logger logger = LoggerFactory.getLogger(Version.class);

    private static final String VERSION = getVersion(Version.class, "4.3.1");

    public static String getVersion() {
        return VERSION;
    }

    public static String getVersion(Class<?> cls, String defaultVersion) {
        try {
            String version = cls.getPackage().getImplementationVersion();
            if (version == null || version.length() == 0) {
                version = cls.getPackage().getSpecificationVersion();
            }
            if (version == null || version.length() == 0) {
                CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
                if (codeSource == null) {
                    logger.info("No codeSource for class " + cls.getName() + " when getVersion, use default version " + defaultVersion);
                } else {
                    String file = codeSource.getLocation().getFile();
                    if (file != null && file.length() > 0 && file.endsWith(".jar")) {
                        file = file.substring(0, file.length() - 4);
                        int i = file.lastIndexOf(47);
                        if (i >= 0) {
                            file = file.substring(i + 1);
                        }
                        i = file.indexOf("-");
                        if (i >= 0) {
                            file = file.substring(i + 1);
                        }
                        while (file.length() > 0 && !Character.isDigit(file.charAt(0))) {
                            i = file.indexOf("-");
                            if (i < 0) {
                                break;
                            }
                            file = file.substring(i + 1);
                        }
                        version = file;
                    }
                }
            }

            return version != null && version.length() != 0 ? version : defaultVersion;
        } catch (Throwable var6) {
            logger.error("return default version, ignore exception " + var6.getMessage(), var6);
            return defaultVersion;
        }
    }

}
