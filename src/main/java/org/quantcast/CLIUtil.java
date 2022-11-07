package org.quantcast;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CLIUtil {

    private static final Logger logger = LogManager.getLogger(MostActiveCookieApplication.class);

    public static Options getOptions() {
        final Options options = new Options();
        final Option dateOption = Option.builder(CookieAnalysisCLI.DATE_OPTION).required(true).hasArg().
                desc("Date for which Most active cookie needs to be searched Takes time in UTC time zone")
                .build();
        options.addOption(dateOption);
        logger.debug("Added Date Option to Parser");
        return options;
    }
    public static void printToConsole(List<String> mostActiveCookies) {
        for(String mostActiveCookie : mostActiveCookies) {
            System.out.println(mostActiveCookie);
        }
    }
}
