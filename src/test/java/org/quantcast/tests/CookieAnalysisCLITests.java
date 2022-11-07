package org.quantcast.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quantcast.CookieAnalysisCLI;
import org.quantcast.MostActiveCookieApplication;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.DateTimeException;
import java.util.List;

public class CookieAnalysisCLITests {

    private static final String TEST_FILEPATH = "src/main/resources/cookie_log_test.csv";
    private static final String INCORRECT_TEST_FILEPATH = "cookie_file_does_not_exist.csv";

    private static final Logger logger = LogManager.getLogger(CookieAnalysisCLITests.class);

    public CookieAnalysisCLI setupCLI(String cookieFileName, String date) {
        logger.debug("CookieAnalysisCLI object initialized");
        return new CookieAnalysisCLI(new String[]{cookieFileName, "-d", date});
    }

    @Test
    public void testIncorrectInputDateFormat() {
        logger.debug("Testing incorrect input date format");
        CookieAnalysisCLI cookieAnalysisCLI = setupCLI(TEST_FILEPATH, "2018-08-19");
        List<String> mostActiveCookies = cookieAnalysisCLI.getMostActiveCookies("2018-2018-2018");
        assert mostActiveCookies.size() == 0;
        logger.debug("Number of mostActiveCookies is 0 as expected. Test successful.");
    }

    @Test
    public void testDateNotInDataRange() {
        logger.debug("Testing for date not in range");
        CookieAnalysisCLI cookieAnalysisCLI = setupCLI(TEST_FILEPATH, "2018-12-12");
        List<String> mostActiveCookie = cookieAnalysisCLI.getMostActiveCookies("2018-12-12");
        Assert.assertEquals(mostActiveCookie.size(), 0);
        logger.debug("Number of mostActiveCookies is 0 as expected. Test successful.");
    }

    @Test
    public void testDateInRangeOneActiveCookie() {
        logger.debug("Testing for date with one active cookie");
        CookieAnalysisCLI cookieAnalysisCLI = setupCLI(TEST_FILEPATH, "2018-12-09");
        List<String> mostActiveCookie = cookieAnalysisCLI.getMostActiveCookies("2018-12-09");
        Assert.assertEquals(mostActiveCookie.size(), 1);
        Assert.assertTrue(mostActiveCookie.contains("AtY0laUfhglK3lC7"));
        logger.debug("Number of mostActiveCookies is 1 as expected. Test successful.");
    }

    @Test
    public void testDateInRangeMultipleActiveCookie() {
        logger.debug("Testing for date with multiple active cookies");
        CookieAnalysisCLI cookieAnalysisCLI = setupCLI(TEST_FILEPATH, "2018-12-08");
        List<String> mostActiveCookie = cookieAnalysisCLI.getMostActiveCookies("2018-12-08");
        Assert.assertEquals(mostActiveCookie.size(), 3);
        Assert.assertTrue(mostActiveCookie.contains("SAZuXPGUrfbcn5UA"));
        Assert.assertTrue(mostActiveCookie.contains("4sMM2LxV07bPJzwf"));
        Assert.assertTrue(mostActiveCookie.contains("fbcn5UAVanZf6UtG"));
        logger.debug("Number of mostActiveCookies is 3 as expected. Test successful.");
    }

    @Test(expectedExceptions = FileNotFoundException.class)
    public void testFileDoesNotExists() throws FileNotFoundException {
        logger.debug("Testing for incorrect file input");
        CookieAnalysisCLI cookieAnalysisCLI = setupCLI(INCORRECT_TEST_FILEPATH, "2018-12-08");
        cookieAnalysisCLI.readFile();
        logger.debug("FileNotFoundException thrown as expected. Test successful.");
    }

}

