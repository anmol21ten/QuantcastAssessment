package org.quantcast;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MostActiveCookieApplication {

    private static final Logger logger = LogManager.getLogger(MostActiveCookieApplication.class);

    public static void main(String[] args) {
        logger.info("Most Active Cookie Application Started");
        CookieAnalysisCLI cookieAnalysisCLI = new CookieAnalysisCLI(args);
        if(cookieAnalysisCLI.getCmd() == null)
            return;
        cookieAnalysisCLI.runInputCommands();
    }
}