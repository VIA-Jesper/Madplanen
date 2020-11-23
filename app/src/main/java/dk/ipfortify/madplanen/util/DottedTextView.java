package dk.ipfortify.madplanen.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

public class DottedTextView extends androidx.appcompat.widget.AppCompatTextView {

    // the vertical offset scaling factor (10% of the height of the text)
    private static final float VERTICAL_OFFSET_SCALING_FACTOR = 0.1f;

    // the dashed line scale factors
    private static final float DASHED_LINE_ON_SCALE_FACTOR = 0.008f;
    private static final float DASHED_LINE_OFF_SCALE_FACTOR = 0.0125f;

    // the paint we will use to draw the lines
    private Paint dashedLinePaint;

    // a reusable rect object
    private Rect reuseableRect;

    public DottedTextView(Context context) {
        super(context);
        init();
    }

    public DottedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DottedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        // instantiate the rect
        reuseableRect = new Rect();

        // instantiate the paint
        dashedLinePaint = new Paint();
        dashedLinePaint.setARGB(200, 0, 0, 0);
        dashedLinePaint.setStyle(Paint.Style.STROKE);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        // set the path effect based on the view width
        dashedLinePaint.setPathEffect(
                new DashPathEffect(
                        new float[]{
                                getWidth() * DASHED_LINE_ON_SCALE_FACTOR,
                                getWidth() * DASHED_LINE_OFF_SCALE_FACTOR},
                        0));

        // get the height of the view
        int height = getHeight();

        // get the height of each line (not subtracting one from the line height makes lines uneven)
        int lineHeight = getLineHeight() - 1;

        // set the vertical offset basef off of the view width
        int verticalOffset = (int) (lineHeight * VERTICAL_OFFSET_SCALING_FACTOR);

        // the number of lines equals the height divided by the line height
        int numberOfLines = height / lineHeight;

        // if there are more lines than what appear on screen
        if (getLineCount() > numberOfLines) {

            // set the number of lines to the line count
            numberOfLines = getLineCount();
        }

        // get the baseline for the first line
        int baseline = getLineBounds(0, reuseableRect);

        // for each line
        for (int i = 0; i < numberOfLines; i++) {

            // draw the line
            canvas.drawLine(
                    reuseableRect.left,             // left
                    baseline + verticalOffset,      // top
                    reuseableRect.right,            // right
                    baseline + verticalOffset,      // bottom
                    dashedLinePaint);               // paint instance

            // get the baseline for the next line
            baseline += lineHeight;
        }

        super.onDraw(canvas);
    }
}

// source: https://stackoverflow.com/questions/21243969/drawing-background-lines-in-an-edittext-that-uses-a-custom-font-or-typeface