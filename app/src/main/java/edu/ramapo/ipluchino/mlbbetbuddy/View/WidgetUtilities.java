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
import android.util.Log;
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
    private static final List<String> VALID_TABLES = Arrays.asList("TodaySchedule", "TodayNRFI", "TodayHitting", "ArchiveSchedule", "ArchiveNRFI", "ArchiveHitting");

    private static final String GOLD_HEX = "#FFD700";
    private static final String SILVER_HEX = "#C0C0C0";
    private static final String BRONZE_HEX = "#D48231";

    //Creates an ImageView object that represents a team's logo.
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

    //Creates a TextView object, based on a bunch of parameters.
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

    //Creates an ImageView object that represents a weather icon.
    public static ImageView CreateWeatherIcon(Context a_context, String a_weatherDescription, int a_width, int a_height, int a_padL, int a_padT, int a_padR, int a_padB) {
        //Get the image name and ID based on the weather description.
        String weatherIconName = GetWeatherIconName(a_weatherDescription);
        int weatherID = a_context.getResources().getIdentifier(weatherIconName, "drawable", a_context.getPackageName());

        //Create and set the attributes of the image.
        ImageView weatherIcon = new ImageView(a_context);
        weatherIcon.setLayoutParams(new TableRow.LayoutParams(150, 150));
        weatherIcon.setImageResource(weatherID);
        weatherIcon.setPadding(10, 20, 20, 20);

        return weatherIcon;
    }

    //Helper function to make partial amount of text bold, dynamically. Returns a SpannableString object.
    //Assistance: https://stackoverflow.com/questions/14371092/how-to-make-a-specific-text-on-textview-bold
    public static SpannableString MakePartialTextBold(String a_boldText, String a_restText) {
        SpannableString partialBoldedString = new SpannableString(a_boldText + a_restText);
        partialBoldedString.setSpan(new StyleSpan(Typeface.BOLD), 0, a_boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return partialBoldedString;
    }

    //Gets data from the MLB Bet Buddy Server.
    //Assistance: https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception
    //Assistance: https://stackoverflow.com/questions/60408193/local-atomicreference-and-array-with-lambda
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

    //Displays an alert dialog if there is no data returned from the server.
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

    //Fills in all TextViews based on criteria from the Vector of Vectors "a_updateInformation".
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

    //Makes a certain number of bet predictions have different backgrounds, depending on how good they are.
    public static void SetTopBets(TableLayout a_betTable, int a_numGold, int a_numSilver, int a_numBronze) {
        int numRows = a_betTable.getChildCount();

        Log.d("test", String.valueOf(numRows));

        //If there are not enough rows to color all of them, don't change any row background colors.
        if ((a_numGold + a_numSilver + a_numBronze) > numRows) {
            return;
        }

        //Set the desired number of top bets as gold.
        int betIndex = 0;
        for(;betIndex < a_numGold; betIndex++) {
            TableRow row = (TableRow) a_betTable.getChildAt(betIndex);
            row.setBackgroundColor(Color.parseColor(GOLD_HEX));
        }

        //Set the desired number of top bets as silver.
        for(;betIndex < (a_numGold + a_numSilver); betIndex++) {
            TableRow row = (TableRow) a_betTable.getChildAt(betIndex);
            row.setBackgroundColor(Color.parseColor(SILVER_HEX));
        }

        //Set the desired number of top bets as bronze.
        for(;betIndex < (a_numGold + a_numSilver + a_numBronze); betIndex++) {
            TableRow row = (TableRow) a_betTable.getChildAt(betIndex);
            row.setBackgroundColor(Color.parseColor(BRONZE_HEX));
        }

        //Note: The rest of the bet predictions will not have any special background color.
    }

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

    //Helper function to get the file name of a team logo.
    private static String GetLogoName(String a_teamName) {
        //Remove any periods from the team name (if they exist).
        String logoName = a_teamName.replace(".", "");

        //Replace the spaces with underscores.
        logoName = logoName.replace(" ", "_");

        //Convert everything to lowercase.
        logoName = logoName.toLowerCase();

        return logoName + "_logo";
    }

    //Helper function to get the file name of a weather icon, based on a weather description.
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
