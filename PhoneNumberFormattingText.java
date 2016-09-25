package komi.outils;

import android.text.Editable;
import android.widget.EditText;

/**
 * Created by vovogan on 25/09/2016.
 */
public class PhoneNumberFormattingTextWatcher extends android.telephony.PhoneNumberFormattingTextWatcher {

    //we need to know if the user is erasing or inputing some new character
    private boolean backspacingFlag = false;
    //we need to block the :afterTextChanges method to be called again after we just replaced the EditText text
    private boolean editedFlag = false;
    //we need to mark the cursor position and restore it after the edition
    private int cursorComplement;

    // Variable for EditText that should be pass to constructor
    public EditText ET;

    /** country code in static mode **/
    private String code = "+228";

    public PhoneNumberFormattingTextWatcher() {
    }

    public PhoneNumberFormattingTextWatcher(EditText ET) {
        this.ET = ET;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //we store the cursor local relative to the end of the string in the EditText before the edition
        cursorComplement = s.length()-ET.getSelectionStart();
        //we check if the user ir inputing or erasing a character
        if (count > after) {
            backspacingFlag = true;
        } else {
            backspacingFlag = false;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // nothing to do here =D
    }

    @Override
    public void afterTextChanged(Editable s) {
        String string = s.toString();
        //what matters are the phone digits beneath the mask, so we always work with a raw string with only digits
        String phone = string.replaceAll("[^\\d]", "");

        //if the text was just edited, :afterTextChanged is called another time... so we need to verify the flag of edition
        //if the flag is false, this is a original user-typed entry. so we go on and do some magic
        if (!editedFlag) {

            //we start verifying the worst case, many characters mask need to be added
            //example: 999999999 <- 6+ digits already typed
            // masked: (999) 999-999
            if (phone.length() >= 8 && !backspacingFlag) {
                //we will edit. next call on this textWatcher will be ignored
                editedFlag = true;
                //here is the core. we substring the raw digits and add the mask as convenient
                String ans = code + " " + phone.substring(0,2) + " " + phone.substring(2,4) + " " + phone.substring(4,6) + " " + phone.substring(6,8);
                ET.setText(ans);
                //we deliver the cursor to its original position relative to the end of the string
                ET.setSelection(ET.getText().length()-cursorComplement);

                //we end at the most simple case, when just one character mask is needed
                //example: 99999 <- 3+ digits already typed
                // masked: (999) 99
            }/* else if (phone.length() >= 3 && !backspacingFlag) {
                editedFlag = true;
                String ans = code + phone.substring(3);
                ET.setText(ans);
                ET.setSelection(ET.getText().length()-cursorComplement);
            }*/
            // We just edited the field, ignoring this cicle of the watcher and getting ready for the next
        } else {
            editedFlag = false;
        }
    }

        }
