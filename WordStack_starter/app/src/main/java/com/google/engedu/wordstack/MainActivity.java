package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private Stack<LetterTile> placedTiles;
    private String word1, word2;
    private View word1LinearLayout, word2LinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if (word.length()==WORD_LENGTH){
                    words.add(word);
                }
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        //View
        word1LinearLayout = findViewById(R.id.word1);
//        word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        //View
        word2LinearLayout = findViewById(R.id.word2);
//        word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
        placedTiles = new Stack<LetterTile>();
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                placedTiles.push(tile);
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    placedTiles.push(tile);

                    return true;
            }
            return false;
        }
    }

    protected boolean onStartGame(View view) {
        placedTiles.clear();
        stackedLayout.clear();
        ((LinearLayout) word1LinearLayout).removeAllViews();
        ((LinearLayout) word2LinearLayout).removeAllViews();
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");
        final String TAG = "StartingGame";
        Log.d(TAG,"Choosing Random words");
        word1 =  words.remove((int)(Math.random()*words.size())%words.size());
        word2 =  words.remove((int)(Math.random()*words.size())%words.size());
        Log.d(TAG,"Chose Random words as: " + word1 + ", " + word2);
        int full_length = word1.length()+ word2.length();
        char[] scrambled = new char[full_length];
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        while(counter3 < full_length){
            if(counter1 >= word1.length()) {
                scrambled[counter3++] = word2.charAt(counter2++);
            }
            else if(counter2>=word2.length()) {
                scrambled[counter3++] = word1.charAt(counter1++);
            }
            else{
                int whichWord = (int)(Math.random()*2)%2;
                if (whichWord%2==0){
                    scrambled[counter3++] = word2.charAt(counter2++);
                }
                else{
                    scrambled[counter3++] = word1.charAt(counter1++);
                }
            }
        }
//        String scrambledS = Arrays.toString(scrambled);
        String scrambledS = new String(scrambled);
        Log.d(TAG,"Chose Scrambled word words as: " + scrambledS );
        for(int i=scrambled.length-1 ;i>=0;i--){
            LetterTile tile = new LetterTile(getApplicationContext(), scrambled[i]);
            stackedLayout.push(tile);
            Log.d(TAG,"Pushed tile for char: " + scrambled[i]);
        }
        messageBox.setText(scrambledS);
        return true;
    }

    protected boolean onUndo(View view) {
        if (placedTiles.size()<=0) {
            return false;
        }
        LetterTile tile = placedTiles.pop();
        tile.moveToViewGroup((ViewGroup) stackedLayout);
        return true;
    }
}
