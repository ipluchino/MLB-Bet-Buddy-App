package edu.ramapo.ipluchino.mlbbetbuddy.Model;

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
    //Constants
    public static final String VIEW_LINK = "Example MLB Bet Buddy Server Link";

    //Makes a request to the MLB Bet Buddy server to access data from a specific table, and parses the information returned.
    //SOURCE: https://docs.oracle.com/javase/tutorial/networking/urls/readingWriting.html
    public static Vector<HashMap<String, Object>> GetDataFromServer(String a_tableName) throws IOException {
        //Create the url string and URL object to make a request to the server.
        String urlLink = VIEW_LINK + a_tableName;
        URL URLObj = new URL(urlLink);

        //Attempt to create a connection with the server and set up an input stream.
        BufferedReader in;
        try {
            URLConnection connection = URLObj.openConnection();
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

    //Takes a list of JSON objects as a string, and parses them into a Vector of Hashmaps, so that each field can be easily accessed and manipulated.
    //https://stackoverflow.com/questions/17970128/how-to-convert-string-array-to-object-using-gson-json
    //https://www.baeldung.com/gson-json-to-map
    public static Vector<HashMap<String, Object>> ParseData(String a_toParse) {
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

    //Takes a UTC DateTime string and a Timezone ID and converts the DateTimeString to the given timezone.
    //Returns a time in the format 7:10 PM.
    //https://stackoverflow.com/questions/6543174/how-can-i-parse-utc-date-time-string-into-something-more-readable
    //https://stackoverflow.com/questions/42425393/how-to-format-a-zoneddatetime-to-a-string
    public static String ConvertToTimezone(String a_dateToConvert, String a_timeZoneID) {
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

    //TESTING.
    public static void main(String[] args) throws IOException {
        //Testing database query.
        Vector<HashMap<String, Object>> data = GetDataFromServer("TodaySchedule");
        for (HashMap<String, Object> hm: data) {
            System.out.println(hm);
        }

        //Testing time conversion.
        String sampleDateTime = "2024-04-02T02:10:00Z";
        String convertedTime = ConvertToTimezone(sampleDateTime, "America/New_York");
        System.out.println(convertedTime);
    }
}
