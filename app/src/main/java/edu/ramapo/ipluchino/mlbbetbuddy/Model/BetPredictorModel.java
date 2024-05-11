/**
 *********************************************************************************************************************************
 * Author: Ian Pluchino                                                                                                          *
 * Class: BetPredictorModel class                                                                                                *
 * Description: The model class for the application, mainly used to receive data from the MLB Bet Buddy Server.                  *
 * Date: 5/2/24                                                                                                                  *
 *********************************************************************************************************************************
 */

package edu.ramapo.ipluchino.mlbbetbuddy.Model;

import android.util.Log;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.net.*;
import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesException;
import java.util.HashMap;
import java.util.Vector;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class BetPredictorModel {
    //CONSTANTS
    //The endpoint used to obtain data from the MLB Bet Buddy server.
    private static final String VIEW_LINK = "Example MLB Bet Buddy Server Link";

    //The amount of time to wait for the server to respond before timing out.
    private static final int TIMEOUT_TIME = 3000;

    //The abbreviations for each team name.
    public static final HashMap<String, String> TEAM_ABBREVIATION = new HashMap<String, String>() {{
        put("Arizona Diamondbacks", "ARI");
        put("Atlanta Braves", "ATL");
        put("Baltimore Orioles", "BAL");
        put("Boston Red Sox", "BOS");
        put("Chicago Cubs", "CHC");
        put("Chicago White Sox", "CWS");
        put("Cincinnati Reds", "CIN");
        put("Cleveland Guardians", "CLE");
        put("Colorado Rockies", "COL");
        put("Detroit Tigers", "DET");
        put("Houston Astros", "HOU");
        put("Kansas City Royals", "KC");
        put("Los Angeles Angels", "LAA");
        put("Los Angeles Dodgers", "LAD");
        put("Miami Marlins", "MIA");
        put("Milwaukee Brewers", "MIL");
        put("Minnesota Twins", "MIN");
        put("New York Mets", "NYM");
        put("New York Yankees", "NYY");
        put("Oakland Athletics", "OAK");
        put("Philadelphia Phillies", "PHI");
        put("Pittsburgh Pirates", "PIT");
        put("San Diego Padres", "SD");
        put("San Francisco Giants", "SF");
        put("Seattle Mariners", "SEA");
        put("St. Louis Cardinals", "STL");
        put("Tampa Bay Rays", "TB");
        put("Texas Rangers", "TEX");
        put("Toronto Blue Jays", "TOR");
        put("Washington Nationals", "WSH");
    }};

    /**
     * Default constructor of the BetPredictorModel class. Nothing to do here.
     */
    public BetPredictorModel() {
        //Nothing to do here.
    }

    /**
     * Gets data from the MLB Bet Buddy Server.
     * <p>
     * This method is used to get data from the MLB Bet Buddy server, and parse it into a Vector of HashMaps so that it can easily be
     * accessed and manipulated. A connection is first attempted to be made with the MLB Bet Buddy Server. If a response is not received
     * within the timeout time, the request will timeout and an empty vector will be returned. If a response is received, the JSON data
     * returned is read line by line and built into one single string. This string is then parsed into a Vector of HashMaps (see ParseData()),
     * and returned.
     * <p>
     * Assistance Received:
     * <p>
     * https://docs.oracle.com/javase/tutorial/networking/urls/readingWriting.html
     * <p>
     * https://stackoverflow.com/questions/2026260/java-how-to-use-urlconnection-to-post-request-with-authorization
     *
     * @param a_tableName A string, representing the table name located in the database to get the data from.
     * @return A {@code Vector<HashMap<String, Object>>} that represents the table data returned from the MLB Bet Buddy Server.
     * @throws IOException if a URL object cannot be successfully created.
     */
    public Vector<HashMap<String, Object>> GetDataFromServer(String a_tableName) throws IOException {
        //Create the url string and URL object to make a request to the server.
        String urlLink = VIEW_LINK + a_tableName;
        URL URLObj = new URL(urlLink);

        //Attempt to create a connection with the server and set up an input stream.
        BufferedReader in;
        try {
            URLConnection connection = URLObj.openConnection();

            //Make sure to set the timeout time, so that the app does not hang if the server is offline or takes too long to respond.
            connection.setConnectTimeout(TIMEOUT_TIME);
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }
        //If a connection with the server could not be made, or an invalid table name was provided, simply return an empty vector.
        catch (IOException e) {
            return new Vector<>();
        }

        //Read in the data from the server line by line into a string builder object.
        StringBuilder sb = new StringBuilder();
        String inputLine;

        //Loop through each line of the input stream to gather the entire JSON returned by the server.
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();

        //Parse the string returned by the server into a Vector of HashMaps.
        String toParse = sb.toString();
        return ParseData(toParse);
    }

    /**
     * Parses a string containing JSON data representing a table into a Vector of HashMaps.
     * <p>
     * This method is used to parse a large string containing JSON data that is returned from the MLB Bet Buddy server into a Vector of
     * Hashmaps so that the data can be easily accessed and manipulated. Each HashMap in the Vector returned represents a row from a table.
     * Every key of the HashMap will always be a string, but the value for that key will vary between strings, doubles, and integers. The
     * parsing of the JSON string is handled with the Google's "Gson" library.
     * <p>
     * Assistance Received:
     * <p>
     * https://stackoverflow.com/questions/17970128/how-to-convert-string-array-to-object-using-gson-json
     * <p>
     * https://www.baeldung.com/gson-json-to-map
     *
     * @param a_toParse A string, representing JSON data that needs to be parsed.
     * @return A {@code Vector<HashMap<String, Object>>}, representing the parsed JSON data.
     */
    public Vector<HashMap<String, Object>> ParseData(String a_toParse) {
        //Set the type that the input string representing a JSON will be parsed into.
        //NOTE: a_toParse will be in the format [{}, {}, {}...] where each {} is an individual JSON object.
        Type type = new com.google.gson.reflect.TypeToken<Vector<Object>>() {}.getType();

        //Parse the input string into a vector of "JSON" objects so that they can be individually separated.
        Vector<Object> vectorOfJSONs = new Gson().fromJson(a_toParse, type);

        Vector<HashMap<String, Object>> resultVector = new Vector<HashMap<String, Object>>();
        //Loop through each individual JSON object.
        for (Object jsonElement : vectorOfJSONs) {
            //Parse each element in the vector of JSONs into a HashMap. That way, each field of the JSON can be accessed easily.
            //Note: The key will always be a string, the value can depend on the field (Strings, Doubles, Integers, etc.).
            HashMap<String, Object> jsonHashMap = new Gson().fromJson(new Gson().toJson(jsonElement), HashMap.class);

            //Add on each newly created HashMap to the return result.
            resultVector.add(jsonHashMap);
        }

        return resultVector;
    }

    /**
     * Converts a UTC date into a provided timezone.
     * <p>
     * This method converts a UTC date string into the requested timezone, and formats it into the format "h:mm a". If the timezone
     * ID that is provided to this method is invalid, a default timezone of EST (Eastern Standard Time) is used.
     * <p>
     * Assistance Received:
     * <p>
     * https://stackoverflow.com/questions/6543174/how-can-i-parse-utc-date-time-string-into-something-more-readable
     * <p>
     * https://stackoverflow.com/questions/42425393/how-to-format-a-zoneddatetime-to-a-string
     *
     * @param a_dateToConvert A string, representing UTC date that will be converted.
     * @param a_timeZoneID A string, representing the ID of the timezone to convert to.
     * @return A string, representing the converted UTC date into the requested timezone in the format "h:mm a". Example: 1:15 PM.
     */
    public String ConvertToTimezone(String a_dateToConvert, String a_timeZoneID) {
        //Create a UTC instant of the provided UTC date.
        Instant instant = Instant.parse(a_dateToConvert);

        //Attempt to create a ZoneID object to model the desired timezone.
        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(a_timeZoneID);
            //If the timezone ID provided does not exist, default to EST (Eastern Standard Time).
        } catch (ZoneRulesException e) {
            zoneId = ZoneId.of("America/New_York");
        }

        //Convert the UTC DateTime object to the correct timezone.
        ZonedDateTime convertedDateTime = ZonedDateTime.ofInstant(instant, zoneId);

        //Format the date to the correct format. Example: 7:10 PM.
        return DateTimeFormatter.ofPattern("h:mm a").format(convertedDateTime);
    }

    /**
     The main function of the BetPredictorModel class - used for testing purposes.
     @param args An array of strings, representing command line arguments.
     */
    public static void main(String[] args) throws IOException {
        BetPredictorModel BPModel = new BetPredictorModel();

        //Testing database query.
        Vector<HashMap<String, Object>> data = BPModel.GetDataFromServer("TodaySchedule");
        for (HashMap<String, Object> hm: data) {
            System.out.println(hm);
        }

        //Testing time conversion.
        String sampleDateTime = "2024-04-02T02:10:00Z";
        String convertedTime = BPModel.ConvertToTimezone(sampleDateTime, "America/New_York");
        System.out.println(convertedTime);
    }
}
