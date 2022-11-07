package org.quantcast;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.Buffer;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieAnalysisCLI {

    private String[] args;
    public static String DATE_OPTION = "d";
    private static final Logger logger = LogManager.getLogger(CookieAnalysisCLI.class);
    private CommandLine cmd;

    private String FILE_PATH = null;



    public CookieAnalysisCLI(String[] args) {
        this.args = args;
        initializeCLI();
    }

    private void initializeCLI() {
        logger.info("Most Active Cookie Engine Initialized");
        final Options options = CLIUtil.getOptions();

        final CommandLineParser parser = new DefaultParser();
        try {
            this.cmd = parser.parse(options,args);
            FILE_PATH = cmd.getArgs()[0];
            logger.debug("Added All Option to Parser. Initialized command line");

        } catch (ParseException e) {
            if(e instanceof MissingOptionException) {
                System.out.println("Missing required option: d");
            } else if (e instanceof  MissingArgumentException) {
                System.out.println("Missing argument for option: d");
            }
        }
    }
    public void runInputCommands() {
        if(cmd.hasOption(DATE_OPTION)) {
            String date = cmd.getOptionValue(DATE_OPTION);
            List<String> mostActiveCookies = getMostActiveCookies(date);
            CLIUtil.printToConsole(mostActiveCookies);
        }
    }

    public List<String> readFile() throws FileNotFoundException {

        FileReader cookieFileReader = null;
        if(FILE_PATH== null) {
            throw new FileNotFoundException();
        }
        cookieFileReader = new FileReader(FILE_PATH);
        BufferedReader bufferedReader = new BufferedReader(cookieFileReader);

        String currentLine;
        List<String> linesInFile = new ArrayList<>();

        while(true) {
            try {
                if ((currentLine = bufferedReader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            linesInFile.add(currentLine);
        }

        return linesInFile;
    }
    public List<String> getMostActiveCookies(String searchDateString) {

        List<String> mostActiveCookieList = new ArrayList<>();
        try {
            List<String> linesInFile = readFile();
            if(linesInFile == null) {
                return null;
            }
            Map<String, Integer> cookieFreqMap = new HashMap();
            LocalDate searchDate = LocalDate.parse(searchDateString);

            for(String line : linesInFile) {
                String[] dateCookieArray = line.split(",");
                LocalDate date = Instant.parse(dateCookieArray[1]).atZone(ZoneOffset.UTC).toLocalDate();
                if (searchDate.isEqual(date)){
                    if(cookieFreqMap.containsKey(dateCookieArray[0])) {
                        cookieFreqMap.put(dateCookieArray[0], cookieFreqMap.get(dateCookieArray[0]) + 1);
                    } else {
                        cookieFreqMap.put(dateCookieArray[0], 1);
                    }
                }
            }

            int mostActiveCookieCount = 0;

            for(Map.Entry<String, Integer> entry : cookieFreqMap.entrySet()) {
                int cookieCount = entry.getValue();
                if(mostActiveCookieCount < cookieCount) {
                    mostActiveCookieCount = cookieCount;
                }
            }
            for(Map.Entry<String, Integer> entry : cookieFreqMap.entrySet()) {
                if(entry.getValue()== mostActiveCookieCount) {
                    mostActiveCookieList.add(entry.getKey());
                }
            }

            if(mostActiveCookieList.size() == 0) {
                System.out.println("No most active cookie found for this date");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Incorrect file path. Please enter correct file path");
        } catch (DateTimeException e) {
            System.out.println("Invalid date. Please enter a valid date");
        }

        return mostActiveCookieList;
    }
    public CommandLine getCmd() {
        return cmd;
    }
}
