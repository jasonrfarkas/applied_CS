package com.google.engedu.wordstack;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LetterTile extends TextView {

    public static final int TILE_SIZE = 150;
    private Character letter;
    private boolean frozen;
    private static final String TAG = "LetterTile";

    public LetterTile(Context context, Character letter) {
        super(context);
        this.letter = letter;
        Log.d(TAG, "Created Tile with char " + letter);
        setText(letter.toString());
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setHeight(TILE_SIZE);
        setWidth(TILE_SIZE);
        setTextSize(30);
        setTextColor(Color.BLACK);
        setBackgroundColor(Color.rgb(255, 255, 200));
    }

    public void moveToViewGroup(ViewGroup targetView) {
        ViewParent parent = getParent();
        if (parent instanceof StackedLayout ) {
            StackedLayout owner = (StackedLayout) parent;
            owner.pop();
            targetView.addView(this);
            freeze();
            setVisibility(View.VISIBLE);
        } else {
            ViewGroup owner = (ViewGroup) parent;
            owner.removeView(this);
            ((StackedLayout) targetView).push(this);
            unfreeze();
        }
    }

    public void freeze() {
        frozen = true;
    }

    public void unfreeze() {
        frozen = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.frozen && motionEvent.getAction()== MotionEvent.ACTION_DOWN){
//            View.DragShadowBuilder myShadow = new MyDragShadowBuilder(imageView);
            DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(this);
            this.startDrag(ClipData.newPlainText("", ""), shadowBuilder, this, 0);
//            startDrag(null,shadowBuilder, );
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }
}
