package net.timsmanter.app3;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private DrawingCanvas canvas = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvas = (DrawingCanvas)findViewById(R.id.canvas);

        findViewById(R.id.red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.setPaintColor(Color.RED);
            }
        });

        findViewById(R.id.yellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.setPaintColor(Color.YELLOW);
            }
        });

        findViewById(R.id.blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.setPaintColor(Color.BLUE);
            }
        });

        findViewById(R.id.green).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.setPaintColor(Color.GREEN);
            }
        });

        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.clearCanvas();
            }
        });
    }
}
