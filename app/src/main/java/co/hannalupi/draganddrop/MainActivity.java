package co.hannalupi.draganddrop;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create longClickListener for each view
        //Each onClick will reference the same view

        findViewById(R.id.textCircle).setOnLongClickListener(longListener);
        findViewById(R.id.textSquare).setOnLongClickListener(longListener);
        findViewById(R.id.textTriangle).setOnLongClickListener(longListener);
        findViewById(R.id.textPentagon).setOnLongClickListener(longListener);
        findViewById(R.id.textHole).setOnDragListener(dragListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Add OnLongClickListener, drag to be called within OnLongClickListener
    OnLongClickListener longListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            //ClipData will not be used, create an empty object
            ClipData data = ClipData.newPlainText("", "");

            //Create a dragShadow
            View.DragShadowBuilder dragshadow = new DragShadow(v);

            //Create myLocalState
            TextView shape = (TextView) v;
            Toast.makeText(MainActivity.this, "Text long clicked - " + shape.getText(), Toast.LENGTH_SHORT).show();

            //(ClipData data, View.DragShadowBuilder shadowBuilder, MyLocal State myLocalState, Flags flags)
            //Pass dragshadow created to .startDrag()
            //Pass view into myLocalState
            v.startDrag(data, dragshadow, shape, 0);

            return true;
        }
    };

    //Add OnDragListener, will determine action based on drag status
    OnDragListener dragListener = new OnDragListener()
    {
        @Override
        public boolean onDrag(View v, DragEvent event)
        {
            int dragEvent = event.getAction();
            TextView dropText = (TextView) v;

            switch(dragEvent){
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.v("Action", "Enter");
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    Log.v("Action", "Exit");
                    break;

                case DragEvent.ACTION_DROP:
                    TextView draggedText = (TextView)event.getLocalState();
                    dropText.setText(draggedText.getText());
                    Log.v("Action", "Dropped");
                    break;
            }

            return true;
        }

    };


    private class DragShadow extends View.DragShadowBuilder{

        //Create a default view to be dragged
        //A rectangle W x H will be created, simple grey box
        ColorDrawable greyBox;

        //Implement constructor
        public DragShadow(View view) {
            super(view);
            greyBox = new ColorDrawable(Color.LTGRAY);
        }

        @Override
        public void onDrawShadow(Canvas canvas) { //The canvas is the area of the shadow
            super.onDrawShadow(canvas);
            greyBox.draw(canvas);
         }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            /// /This will get view that was longclicked
            View v = getView();

            //Get height and width of view
            //Cast back to an integer just incase Java returns a float
            int height = v.getHeight();
            int width = v.getWidth();

            //Set dimensions of box
            greyBox.setBounds(0,0,(2*width),(2*height));


            //The samller shadowsize is easier to drag around
            shadowSize.set((2*width), (2*height));

            //shadowTouchPoint is the part of the image which is being tracked/dragged with the user's finger
            //Set the shadowTouchPoint to the center of the image
            shadowTouchPoint.set(width, height);

        }
    }
}
