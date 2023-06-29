package com.hldspm.server.banner;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

public class CustomBanner {
    private static final String customBannerText = """



            ██╗░░██╗██╗░░░░░██████╗░░██████╗  ██╗  ██████╗░███╗░░░███╗
            ██║░░██║██║░░░░░██╔══██╗██╔════╝  ╚═╝  ██╔══██╗████╗░████║
            ███████║██║░░░░░██║░░██║╚█████╗░  ░░░  ██████╔╝██╔████╔██║
            ██╔══██║██║░░░░░██║░░██║░╚═══██╗  ░░░  ██╔═══╝░██║╚██╔╝██║
            ██║░░██║███████╗██████╔╝██████╔╝  ██╗  ██║░░░░░██║░╚═╝░██║
            ╚═╝░░╚═╝╚══════╝╚═════╝░╚═════╝░  ╚═╝  ╚═╝░░░░░╚═╝░░░░░╚═╝
            ░██████╗███████╗██████╗░██╗░░░██╗███████╗██████╗░
            ██╔════╝██╔════╝██╔══██╗██║░░░██║██╔════╝██╔══██╗
            ╚█████╗░█████╗░░██████╔╝╚██╗░██╔╝█████╗░░██████╔╝
            ░╚═══██╗██╔══╝░░██╔══██╗░╚████╔╝░██╔══╝░░██╔══██╗
            ██████╔╝███████╗██║░░██║░░╚██╔╝░░███████╗██║░░██║
            ╚═════╝░╚══════╝╚═╝░░╚═╝░░░╚═╝░░░╚══════╝╚═╝░░╚═╝""";

    public static Banner customBanner = (environment, sourceClass, out) -> {
        System.out.println(customBannerText);
        System.out.println(":: Spring Boot ::                        (v3.1.0)\n\n");
    };
}
