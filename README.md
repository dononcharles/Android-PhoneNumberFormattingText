# Android-PhoneNumberFormattingText
How to format Phone Number using Android Input Type

# How to use PhoneNumberFormattingText custom class that extend the native one PhoneNumberFormattingTextWatcher.

After creating PhoneNumberFormattingText.java under your package, go into your Activity or Fragment and do this below:

        EditText ET = (EditText) findViewById(R.id.phoneInput);
        ET.addTextChangedListener(new PhoneNumberFormattingTextWatcher(ET));
        
 Thanks for contributions
