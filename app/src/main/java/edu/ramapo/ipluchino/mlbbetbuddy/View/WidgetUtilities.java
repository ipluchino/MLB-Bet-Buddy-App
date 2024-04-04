package edu.ramapo.ipluchino.mlbbetbuddy.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class WidgetUtilities {
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
