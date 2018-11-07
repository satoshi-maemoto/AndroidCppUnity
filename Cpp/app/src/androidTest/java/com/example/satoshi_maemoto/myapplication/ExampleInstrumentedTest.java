package com.example.satoshi_maemoto.myapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    static {
        System.loadLibrary("native-lib");
    }
    public native String stringFromJNI();

    @Rule
    public ActivityTestRule rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.satoshi_maemoto.myapplication", appContext.getPackageName());
    }

    private void showMessageOnUiThread(String message)
    {
        class ShowTextAction implements Runnable
        {
            private String message;
            public ShowTextAction(String message)
            {
                this.message = message;
            }

            @Override
            public void run() {
                TextView messageText = rule.getActivity().findViewById(R.id.textMessage);
                messageText.setText(this.message);
            }
        }

        this.rule.getActivity().runOnUiThread(new ShowTextAction(message));
    }

    @Test
    public void stringFromJNITest() throws InterruptedException {
        String message = stringFromJNI();
        this.showMessageOnUiThread(message);

        Thread.sleep(5000);

        assertEquals("Hello from C++ to InstrumentedTest", message);
    }
}
