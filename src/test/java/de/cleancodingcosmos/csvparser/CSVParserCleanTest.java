package de.cleancodingcosmos.csvparser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.cleancodingcosmos.csvparser.CSVParserClean.*;

/**
 * Die letzten beiden Tests schlagen fehl. <code>CSVParserDirty</code> ist so zu modifizieren,
 * dass alles Tests fehlerfrei sind.
 */
public class CSVParserCleanTest {

    private CSVParserClean csvParser;

    @Before
    public void setUp() {
        csvParser = new CSVParserClean();
    }


    @Test
    public void testEmptyString() {
        ArrayList<String> actual = csvParser.parseLine("");
        List<String> expected = Arrays.asList("");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testOneDelimeter() {
        ArrayList<String> actual = csvParser.parseLine(""+ DELIM);
        List<String> expected = Arrays.asList("", "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTwoQuotes() {
        ArrayList<String> actual = csvParser.parseLine(""+QUOTE+QUOTE);
        List<String> expected = Arrays.asList("");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testThreeQuotes() {
        ArrayList<String> actual = csvParser.parseLine(""+QUOTE+QUOTE+QUOTE);
        List<String> expected = Arrays.asList(""+QUOTE);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFourQuotes() {
        ArrayList<String> actual = csvParser.parseLine(""+QUOTE+QUOTE+QUOTE+QUOTE);
        List<String> expected = Arrays.asList(""+QUOTE);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTwoDelimeters() {
        ArrayList<String> actual = csvParser.parseLine(""+ DELIM + DELIM);
        List<String> expected = Arrays.asList("", "", "");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testOneElement() {
        ArrayList<String> actual = csvParser.parseLine("Reik");
        List<String> expected = Arrays.asList("Reik");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testTwoElements() {
        ArrayList<String> actual = csvParser.parseLine("Jörg"+DELIM+"Reik");
        List<String> expected = Arrays.asList("Jörg", "Reik");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testTwoElementsWithSpace() {
        ArrayList<String> actual = csvParser.parseLine("Jörg"+DELIM+" Reik");
        List<String> expected = Arrays.asList("Jörg", " Reik");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testOneElementWithQuotes() {
        ArrayList<String> actual = csvParser.parseLine(QUOTE+"Reik"+QUOTE);
        List<String> expected = Arrays.asList("Reik");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testTwoElementsWithQuotes() {
        ArrayList<String> actual = csvParser.parseLine(QUOTE+"Jörg"+QUOTE+DELIM+QUOTE+"Reik"+QUOTE);
        List<String> expected = Arrays.asList("Jörg", "Reik");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testOneElementWithQuotesAndCommaInside() {
        ArrayList<String> actual = csvParser.parseLine(QUOTE+"Jörg"+DELIM+" Reik"+QUOTE);
        List<String> expected = Arrays.asList("Jörg"+DELIM+" Reik");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testCommaAndElement() {
        ArrayList<String> actual = csvParser.parseLine(DELIM+"Reik");
        List<String> expected = Arrays.asList("", "Reik");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testElementAndComma() {
        ArrayList<String> actual = csvParser.parseLine("Reik"+DELIM);
        List<String> expected = Arrays.asList("Reik", "");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testQuoteInsideUnquotedElement() {
        ArrayList<String> actual = csvParser.parseLine("Pe"+QUOTE+"ter");
        List<String> expected = Arrays.asList("Pe"+QUOTE+"ter");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testQuoteInsideUnQuotedElementAfterSep() {
        ArrayList<String> actual = csvParser.parseLine("Rolf"+DELIM+"Pe"+QUOTE+"ter");
        List<String> expected = Arrays.asList("Rolf", "Pe"+QUOTE+"ter");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testCharsAfterQuotedElement() {
        ArrayList<String> actual = csvParser.parseLine(QUOTE+"Pe"+QUOTE+"ter"); // "Pe"ter
        List<String> expected = Arrays.asList("Peter");
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testCharsAfterQuotedElementWithFollowing() {
        ArrayList<String> actual = csvParser.parseLine(QUOTE+"Pe"+QUOTE+"ter"+DELIM+"Rolf"); // "Pe"ter;Rolf
        List<String> expected = Arrays.asList("Peter", "Rolf");
        Assert.assertEquals(expected, actual);
    }


}