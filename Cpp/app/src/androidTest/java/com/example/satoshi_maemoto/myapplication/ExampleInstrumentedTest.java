package com.example.satoshi_maemoto.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;
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
    public native boolean getBufferArray(int[][] buffer);

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

    private void refreshImageOnUiThread(Bitmap[] bitmaps)
    {
        class ShowImagesAction implements Runnable
        {
            private Bitmap[] bitmaps;
            public ShowImagesAction(Bitmap[] bitmaps)
            {
                this.bitmaps = bitmaps;
            }

            @Override
            public void run() {
                ImageView imageView01 = rule.getActivity().findViewById(R.id.imageView01);
                imageView01.setImageBitmap(this.bitmaps[0]);

                ImageView imageView02 = rule.getActivity().findViewById(R.id.imageView02);
                imageView02.setImageBitmap(this.bitmaps[1]);
            }
        }

        this.rule.getActivity().runOnUiThread(new ShowImagesAction(bitmaps));
    }

    @Test
    public void imageRefreshTest() throws InterruptedException {
        int stride = 320;
        int lines = 240;
        Bitmap[] bitmaps = {
                Bitmap.createBitmap(stride, lines, Bitmap.Config.ARGB_8888),
                Bitmap.createBitmap(stride, lines, Bitmap.Config.ARGB_8888)
        };
        int[][] buffers = {
                new int[stride * lines],
                new int[stride * lines]
        };

        assertTrue(getBufferArray(buffers));

        bitmaps[0].setPixels(buffers[0], 0, stride, 0, 0, stride, lines);
        bitmaps[1].setPixels(buffers[1], 0, stride, 0, 0, stride, lines);
        this.refreshImageOnUiThread(bitmaps);

        Thread.sleep(5000);
    }
}
