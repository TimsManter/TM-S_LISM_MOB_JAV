package net.timsmanter.app3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class DrawingCanvas extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;
    private Thread drawingThread;
    private boolean isThreadStopping;
    private boolean isThreadWorking;
    private Object lock = new Object();

    private Bitmap bitmap = null;
    private Canvas canvas = null;
    private Path path = null;

    public Paint paint = new Paint();

    public DrawingCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);

        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        clearCanvas();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isThreadWorking = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        synchronized (lock) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    continueDrawing();
                    canvas.drawCircle(event.getX(), event.getY(), 5, paint);
                    path = new Path();
                    path.moveTo(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(event.getX(), event.getY());
                    canvas.drawPath(path, paint);
                    break;
                case MotionEvent.ACTION_UP:
                    canvas.drawCircle(event.getX(), event.getY(), 5, paint);
                    pauseDrawing();
                    break;
            }
        }
        return true;
    }

    @Override
    public void run() {
        while (isThreadWorking) {
            Canvas canvas = null;
            try {
                synchronized (holder) {
                    if (!holder.getSurface().isValid()) continue;
                    canvas = holder.lockCanvas(null);
                    synchronized (lock) {
                        if (isThreadWorking) {
                            canvas.drawBitmap(bitmap, 0, 0, null);
                        }
                        if (isThreadStopping) {
                            isThreadWorking = false;
                            isThreadStopping = false;
                        }
                    }
                }
            }
            finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            try {
                Thread.sleep(1000 / 25);
            }
            catch (InterruptedException e) {

            }
        }
    }

    public boolean performClick() {
        return super.performClick();
    }

    public void continueDrawing() {
        drawingThread = new Thread(this);
        isThreadWorking = true;
        isThreadStopping = false;
        drawingThread.start();
    }

    public void pauseDrawing() {
        isThreadStopping = true;
    }

    public void setPaintColor(int color) {
        paint.setColor(color);
    }

    public void clearCanvas() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawARGB(255, 255, 255, 255);
        continueDrawing();
        canvas.drawBitmap(bitmap, 0, 0, null);
        pauseDrawing();
    }
}