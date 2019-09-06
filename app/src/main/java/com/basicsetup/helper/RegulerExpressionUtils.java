package com.anisolutions.BeanLogin.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegulerExpressionUtils {

    private String emailString;
    private static final String TAG = "RegulerExpressionUtils";
    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
    private static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private static String urlCheck = "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}\\.([a-z]+)?$";
    //private static String urlCheck = "^((https|http)?://)?([-a-z0-9]{1,63}\\.)*?[a-z0-9][-a-z0-9]{0,61}[a-z0-9]\\.[a-z]{2,6}(/[-\\w@\\+\\.~#\\?&/=%]*)?$";
    private static final String P_PATTERN="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*_,.+(){}:<>=`~;'\\\"\\\\/\\\\-]).{6,}$";
    private static final String LOWER_CASE = ".*[A-Z].*";
    private static final String UPPER_CASE = ".*[a-z].*";
    private static final String NUMBER_EXIST = ".*[0-9].*";
    private static final String SPECIAL_CHARACTER = ".*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?].*";
    private static final String ALPHABETS= "^[a-zA-Z ]+$";
    private static final String[] punctuationArray = {".", ",", ":", "-", "_", "$", "%", "&", "!", "#", "'", "*", "+", "/", "?",
            ";", "<", ">", "{", "}", "[", "]", "(", ")", "@", "~", "`", "^", ""};
    private static final String SPECIAL_CHARS = "!@#$%&*()_+=-|<>?{}[]~";

    public RegulerExpressionUtils(String emailString) {
        this.emailString = emailString;
    }

    public static boolean isUrlValid(String Url) {
        if (Url.matches(urlCheck) && Url.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {

        if (email.matches(emailPattern) && email.length() > 1) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isValid() {
        CharSequence inputStr = emailString;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidPData(final String pData) {

        int minimumRequiredCount = 4;
        int count =0;

        Pattern lowerCase = Pattern.compile(LOWER_CASE);
        Pattern upperCase= Pattern.compile(UPPER_CASE);
        Pattern specialChar = Pattern.compile(SPECIAL_CHARACTER);
        Pattern numbers= Pattern.compile(NUMBER_EXIST);

        Matcher matcherlowerCase = lowerCase.matcher(pData);
        Matcher matcherupperCase= upperCase.matcher(pData);
        Matcher matcherspecialChar= specialChar.matcher(pData);
        Matcher matchernumbers= numbers.matcher(pData);

        if(matcherlowerCase.matches()){
            Log.d("WASTE","matcherlowerCase");
            count++;
        }
        if(matcherupperCase.matches()){
            Log.d("WASTE","matcherupperCase");
            count++;
        }
        if(matcherspecialChar.matches()){
            Log.d("WASTE","matcherspecialChar");
            count++;
        }
        if(matchernumbers.matches()){
            Log.d("WASTE","matchernumbers");
            count++;
        }

        Log.d("WASTE","count: " + count);
        return count >= minimumRequiredCount;

    }

    public static boolean isValidOnlyAlphabet(String name){

        Pattern alphabets = Pattern.compile(ALPHABETS);
        Matcher matcherAlphabets = alphabets.matcher(name);
        return  matcherAlphabets.matches();
    }
    public static ArrayList<Integer> printPStatus(String pData) {
        Log.d("WASTE","Password Data before check: "+pData);
        ArrayList<Integer> dataArray = new ArrayList<>();
        int numOfSpecial = 0;
        int numOfLetters = 0;
        int numOfUpperLetters = 0;
        int numOfLowerLetters = 0;
        int numOfDigits = 0;

        byte[] bytes = pData.getBytes();
        for (byte tempByte : bytes) {
            if (tempByte >= 33 && tempByte <= 47) {
                numOfSpecial++;
            }

            char tempChar = (char) tempByte;
            if (SPECIAL_CHARS.contains(String.valueOf(tempChar))) {
                numOfSpecial++;
            }
            if (Character.isDigit(tempChar)) {
                numOfDigits++;
            }

            if (Character.isLetter(tempChar)) {
                numOfLetters++;
            }

            if (Character.isUpperCase(tempChar)) {
                numOfUpperLetters++;
            }

            if (Character.isLowerCase(tempChar)) {
                numOfLowerLetters++;
            }
        }
        dataArray.add(numOfSpecial);
        dataArray.add(numOfDigits);
        dataArray.add(numOfUpperLetters);
        dataArray.add(numOfLowerLetters);

        dataArray.add(numOfLetters);
        return dataArray;
    }

}
