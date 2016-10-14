package com.google.engedu.wordstack;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Stack;

public class StackedLayout extends LinearLayout {

    private Stack<View> tiles = new Stack();

    public StackedLayout(Context context) {
        super(context);
    }

    public void push(View tile) {
        if(tiles.size()>0){
            removeView(tiles.peek());
        }
        tiles.push(tile);
        addView(tiles.peek());
    }

    public View pop() {
        View popped = null;
        popped = tiles.pop();
        removeView(popped);
        if(tiles.size()>0) {
            addView(tiles.peek());
        }
        return popped;
    }

    public View peek() {
        return tiles.peek();
    }

    public boolean empty() {
        return tiles.empty();
    }

    public void clear() {
        tiles.clear();
        this.removeAllViews();
    }
}
