/**
 *********************************************************************************************************************************
 * Author: Ian Pluchino                                                                                                          *
 * Class: WidgetUtilities class                                                                                                  *
 * Description: A utility class that all other activity classes use, used to create and modify widgets.                          *
 * Date: 5/2/24                                                                                                                  *
 *********************************************************************************************************************************
 */

package edu.ramapo.ipluchino.mlbbetbuddy.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;

import edu.ramapo.ipluchino.mlbbetbuddy.Model.BetPredictorModel;

public class WidgetUtilities {
    //Constants
    //Valid table names that are stored in the database.
    private static final List<String> VALID_TABLES = Arrays.asList("TodaySchedule", "TodayNRFI", "TodayHitting", "ArchiveSchedule", "ArchiveNRFI", "ArchiveHitting");

    //Hex codes representing colors.
    private static final String GOLD_HEX = "#FFD700";
    private static final String SILVER_HEX = "#C0C0C0";
    private static final String BRONZE_HEX = "#D48231";

    /**
     * Default constructor of the WidgetUtilities class. Nothing to do here.
     */
    public WidgetUtilities() {
        //Nothing to do here.
    }

    /**
     * Creates an ImageView object that represents a team's logo.
     *
     * This method is used to create an ImageView object that is a team logo. First, the correct ID of the logo image in the drawable
     * folder is determined (see GetLogoName()), and the ImageView's image resource is set to that image. Then, the ImageView object is
     * configured based on the parameters passed to this method, and the ImageView is returned.
     *
     * @param a_context A Context object, representing the context the logo will be created for.
     * @param a_teamName A string, representing the name of the team the logo is being created for.
     * @param a_width An integer, representing the width of the ImageView in pixels.
     * @param a_height An integer, representing the height of the ImageView in pixels.
     * @param a_padL An integer, representing the amount of left padding in pixels.
     * @param a_padT An integer, representing the amount of top padding in pixels.
     * @param a_padR An integer, representing the amount of right padding in pixels.
     * @param a_padB An  integer, representing the amount of bottom padding in pixels.
     * @return An ImageView object, representing the team's logo.
     */
    public static ImageView CreateTeamLogo(Context a_context, String a_teamName, int a_width, int a_height, int a_padL, int a_padT, int a_padR, int a_padB) {
        //Get the image name and ID for the team name.
        String logoName = GetLogoName(a_teamName);
        int logoID = a_context.getResources().getIdentifier(logoName, "drawable", a_context.getPackageName());

        //Create and set the attributes of the image.
        ImageView teamLogo = new ImageView(a_context);
        teamLogo.setLayoutParams(new TableRow.LayoutParams(a_width, a_height));
        teamLogo.setImageResource(logoID);
        teamLogo.setPadding(a_padL, a_padT, a_padR, a_padB);

        return teamLogo;
    }

    /**
     * Creates a TextView object, based on several passed parameters.
     *
     * This method creates and returns a TextView object based on several parameters provided to the method. The TextView object is
     * created and configured, then returned.
     *
     * @param a_context A Context object, representing the context the TextView will be created for.
     * @param a_text A string, representing the text that will go inside the TextView object.
     * @param a_size An integer, representing the text size in pixels.
     * @param a_padL An integer, representing the amount of left padding in pixels.
     * @param a_padT An integer, representing the amount of top padding in pixels.
     * @param a_padR An integer, representing the amount of right padding in pixels.
     * @param a_padB An  integer, representing the amount of bottom padding in pixels.
     * @return A TextView object, representing the configured TextView.
     */
    public static TextView CreateTextView(Context a_context, String a_text, int a_size, int a_padL, int a_padT, int a_padR, int a_padB) {
        //Set the attributes of the TextView object based on the parameters sent to this function.
        TextView textView = new TextView(a_context);
        textView.setTextColor(Color.BLACK);
        textView.setText(a_text);
        textView.setTextSize(a_size);
        textView.setPadding(a_padL, a_padT, a_padR, a_padB);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);

        return textView;
    }

    /**
     * Creates an ImageView object that represents a weather icon.
     *
     * This method is used to create an ImageView object that is a weather icon. First, the correct ID of the weather icon image in the
     * drawable folder is determined (see GetWeatherIconName()), and the ImageView's image resource is set to that image. Then, the ImageView
     * object is configured based on the parameters passed to this method, and the ImageView is returned.
     *
     * @param a_context A Context object, representing the context the weather icon will be created for.
     * @param a_weatherDescription A string, representing the description of weather to create an icon for.
     * @param a_width An integer, representing the width of the ImageView in pixels.
     * @param a_height An integer, representing the height of the ImageView in pixels.
     * @param a_padL An integer, representing the amount of left padding in pixels.
     * @param a_padT An integer, representing the amount of top padding in pixels.
     * @param a_padR An integer, representing the amount of right padding in pixels.
     * @param a_padB An  integer, representing the amount of bottom padding in pixels.
     * @return An ImageView object, representing a weather icon.
     */
    public static ImageView CreateWeatherIcon(Context a_context, String a_weatherDescription, int a_width, int a_height, int a_padL, int a_padT, int a_padR, int a_padB) {
        //Get the image name and ID based on the weather description.
        String weatherIconName = GetWeatherIconName(a_weatherDescription);
        int weatherID = a_context.getResources().getIdentifier(weatherIconName, "drawable", a_context.getPackageName());

        //Create and set the attributes of the image.
        ImageView weatherIcon = new ImageView(a_context);
        weatherIcon.setLayoutParams(new TableRow.LayoutParams(a_width, a_height));
        weatherIcon.setImageResource(weatherID);
        weatherIcon.setPadding(a_padL, a_padT, a_padR, a_padB);

        return weatherIcon;
    }

    /**
     * Creates a SpannableString object that contains partially bolded text.
     *
     * Assistance Received:
     * https://stackoverflow.com/questions/14371092/how-to-make-a-specific-text-on-textview-bold
     *
     * @param a_boldText A string, representing the portion of the text that should be bolded.
     * @param a_restText A string, representing the rest of the text that will not be bolded.
     * @return A SpannableString object that contains the requested partially bolded text.
     */
    public static SpannableString MakePartialTextBold(String a_boldText, String a_restText) {
        SpannableString partialBoldedString = new SpannableString(a_boldText + a_restText);
        partialBoldedString.setSpan(new StyleSpan(Typeface.BOLD), 0, a_boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return partialBoldedString;
    }

    /**
     * Gets data from the MLB Bet Buddy Server.
     *
     * This method is used to retrieve data from the MLB Bet Buddy server, and return it as a Vector of HashMaps. If the table name provided
     * to this method is invalid, an empty Vector is returned. If the table name is valid, a new thread is created and the data is attempted
     * to be retrieved from the server (see GetDataFromServer() in the BetPredictorModel class). If the server does not respond, and the request
     * times out, an empty Vector is returned.
     *
     * Assistance Received:
     * https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception
     * https://stackoverflow.com/questions/60408193/local-atomicreference-and-array-with-lambda
     *
     * @param a_tableName A string, representing the name of the table the data will be extracted from.
     * @return A Vector<HashMap<String, Object>> representing the extracted data from the MLB Bet Buddy server.
     */
    public static Vector<HashMap<String, Object>> GetData(String a_tableName) {
        //Make sure the table being provided to this function is a valid one.
        if (!VALID_TABLES.contains(a_tableName)) {
            return new Vector<HashMap<String, Object>>();
        }

        //Note: Create an AtomicReference object because you cannot have a mutable object (the vector of hashmaps) in a lambda expression.
        AtomicReference<Vector<HashMap<String, Object>>> result = new AtomicReference<>();
        BetPredictorModel BPModelObj = new BetPredictorModel();

        //Set up a new thread to query the server.
        Thread thread = new Thread(() -> {
            try {
                result.set(BPModelObj.GetDataFromServer(a_tableName));
                //If the server does not respond or is offline, use an empty vector to represent the games.
            } catch (IOException e) {
                result.set(new Vector<HashMap<String, Object>>());
            }
        });

        //Start the thread and wait for it to finish fetching the data from the server.
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            result.set(new Vector<HashMap<String, Object>>());
        }

        return result.get();
    }

    /**
     * Displays an alert dialog window to the user if there is no data returned from the MLB Bet Buddy server.
     *
     * @param a_context A Context object, representing the context the alert dialog window will be created for.
     */
    public static void DisplayLackOfData(Context a_context) {
        //Create an alert dialog box to alert the user.
        AlertDialog.Builder builder = new AlertDialog.Builder(a_context);
        builder.setTitle("Oops!");
        builder.setMessage("There was no data found today. Please try again later.");

        //OK button to clear the alert dialog and go back to the home screen of the app.
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Navigate back to the home screen of the app.
                Intent intent = new Intent(a_context, HomeScreenActivity.class);
                a_context.startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Fills in the text of several TextView objects dynamically.
     *
     * This method is used to dynamically set the text of several TextView objects representing either schedule information or bet prediction
     * information. Each Vector in a_fieldInformation contains the TextView object to modify, the key in a_dataHashMap to find the text to fill in,
     * whether or not the text should be partially bolded, and what to round the text to if it is a double. Each of those Vectors is looped through,
     * and every TextView is filled in with the correct text based on the information within each Vector.
     *
     * @param a_fieldInformation A Vector<Vector<Object>>, where each inner Vector contains the TextView objects that need to be filled with text,
     *                           the keys in a_dataHashMap to find the text to fill the TextView objects with, whether or not the text should be
     *                           partially bolded, and how many decimal places to round the text to for fields that are doubles.
     * @param a_dataHashMap A HashMap<String, Object>, containing the information returned from the MLB Bet Buddy server that needs to be
     *                      displayed on the screen.
     */
    public static void FillInTableTextViews(Vector<Vector<Object>> a_fieldInformation, HashMap<String, Object> a_dataHashMap) {
        //Loop through each of the fields, and dynamically fill them in based on information from the hash map containing the data.
        for (Vector<Object> fieldInfo : a_fieldInformation) {
            TextView textViewObj = (TextView) fieldInfo.get(0);
            String key = (String) fieldInfo.get(1);
            Boolean partialBold = (Boolean) fieldInfo.get(2);

            //Extract the value that the field should be set to.
            String fieldValue;
            if (a_dataHashMap.get(key) instanceof Double) {
                //Ensure that fields that are doubles are rounded to their correct number of decimal places.
                String numDigitsToRound = fieldInfo.get(3).toString();
                fieldValue = String.format("%." + numDigitsToRound + "f", a_dataHashMap.get(key));
            }
            else {
                fieldValue = String.valueOf(a_dataHashMap.get(key));
            }

            //If this is a single line column (ex: stadium column), make sure the first part of the text is bolded ("ex: Stadium:").
            if (partialBold) {
                textViewObj.setText(WidgetUtilities.MakePartialTextBold((String) textViewObj.getText(), fieldValue));
            }
            else
            {
                textViewObj.setText(fieldValue);
            }
        }
    }

    /**
     * Sets the top bets to different backgrounds, depending on how good they are.
     *
     * This method is used to set the backgrounds of the individual rows in a_betTable, for the top bets only. If there are not enough rows to
     * change all the requested rows, none of rows are changed. For example, if the number of gold, silver, and bronze top bets are 1, 2, and 3
     * respectively, the first row will have a gold background, the next two a silver background, and the next three a bronze background.
     *
     * @param a_betTable A TableLayout object, representing the table that contains the bet information.
     * @param a_numGold An integer, representing the number of top bets to make gold.
     * @param a_numSilver An integer, representing the number of top bets to make silver.
     * @param a_numBronze An integer, representing the number of top bets to make bronze.
     */
    public static void SetTopBets(TableLayout a_betTable, int a_numGold, int a_numSilver, int a_numBronze) {
        int numRows = a_betTable.getChildCount();

        //If there are not enough rows to color all of them, don't change any row background colors.
        if ((a_numGold + a_numSilver + a_numBronze) > numRows-1) {
            return;
        }

        //Set the desired number of top bets as gold.
        //Note: The first row is an empty row that acts as a divider.
        int betIndex = 1;
        for(;betIndex <= a_numGold; betIndex++) {
            TableRow row = (TableRow) a_betTable.getChildAt(betIndex);
            row.setBackgroundColor(Color.parseColor(GOLD_HEX));
        }

        //Set the desired number of top bets as silver.
        for(;betIndex <= (a_numGold + a_numSilver); betIndex++) {
            TableRow row = (TableRow) a_betTable.getChildAt(betIndex);
            row.setBackgroundColor(Color.parseColor(SILVER_HEX));
        }

        //Set the desired number of top bets as bronze.
        for(;betIndex <= (a_numGold + a_numSilver + a_numBronze); betIndex++) {
            TableRow row = (TableRow) a_betTable.getChildAt(betIndex);
            row.setBackgroundColor(Color.parseColor(BRONZE_HEX));
        }

        //The rest of the bet predictions will not have any special background color.
    }

    /**
     * Shortens a player's name for spacing purposes.
     *
     * This method is used to shorten a player's name into their first initial and last name. Also, if the player contains multiple names
     * separated by dashes, only the any names after the first dash is removed. Example: Jack Jackson --> J. Jackson.
     *
     * @param a_fullName A string, representing a player's full name.
     * @return A string, representing the shortened name.
     */
    public static String ShortenName(String a_fullName) {
        //First, extract the players first name and shorten it.
        int indexOfFirstSpace = a_fullName.indexOf(" ");
        String firstName = a_fullName.substring(0, indexOfFirstSpace);
        char firstNameFirstLetter = firstName.charAt(0);

        //Create the shortened name. The shortened name is in the format: A. Judge
        String shortenedName = firstNameFirstLetter + "." + a_fullName.substring(indexOfFirstSpace);

        //Sometimes players can have additional names, separated by dashes. If this is case, remove the dashed names.
        if (a_fullName.contains("-")) {
            shortenedName = shortenedName.substring(0, shortenedName.indexOf("-"));
        }

        return shortenedName;
    }

    /**
     * Gets the file name of the logo image for a team.
     *
     * @param a_teamName A string, representing the name of the team to get the logo image's file name for.
     * @return A string, representing the file name of the logo image corresponding to the team.
     */
    private static String GetLogoName(String a_teamName) {
        //Remove any periods from the team name (if they exist).
        String logoName = a_teamName.replace(".", "");

        //Replace the spaces with underscores.
        logoName = logoName.replace(" ", "_");

        //Convert everything to lowercase.
        logoName = logoName.toLowerCase();

        return logoName + "_logo";
    }

    /**
     * Gets the file name of a weather icon for a weather description.
     *
     * This method is used to obtain the file name of a weather icon depending on the provided weather description. There are several different
     * types of weather descriptions, but the weather icon is determined via keywords. For example, any weather description that contains "thunder"
     * such as "Moderate Thunderstorm" or "Heavy Thunderstorm" is associated with the thunderstorm weather icon.
     *
     * @param a_weatherDescription A string, representing the weather description to get the weather icon's file name for.
     * @return A string, representing the file name of the weather icon corresponding to the weather description.
     */
    private static String GetWeatherIconName(String a_weatherDescription) {
        //Determine the name of the image to use based on the weather description.
        if (a_weatherDescription.toLowerCase().contains("snow")) {
            return "snow";
        }
        else if (a_weatherDescription.toLowerCase().contains("sleet") ||
                a_weatherDescription.toLowerCase().contains("freezing rain") ||
                a_weatherDescription.toLowerCase().contains("freezing drizzle") ||
                a_weatherDescription.toLowerCase().contains("ice pellets")) {
            return "sleet";
        }
        else if (a_weatherDescription.toLowerCase().contains("thunder")) {
            return "thunderstorm";
        }
        else if (a_weatherDescription.toLowerCase().contains("patchy rain") ||
                a_weatherDescription.toLowerCase().contains("mist") ||
                a_weatherDescription.toLowerCase().contains("drizzle")) {
            return "drizzle";
        }
        else if (a_weatherDescription.toLowerCase().contains("rain")) {
            return "rain";
        }
        else if (a_weatherDescription.toLowerCase().contains("overcast") ||
                a_weatherDescription.toLowerCase().contains("cloudy")) {
            return "cloudy";
        }
        else
        {
            return "sunny";
        }
    }
}