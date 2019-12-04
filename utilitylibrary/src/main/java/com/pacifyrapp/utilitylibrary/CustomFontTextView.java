package com.pacifyrapp.utilitylibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.widget.TextView;


/**
 * Created by jp on 12/3/16.
 */
public class CustomFontTextView extends TextView {


    private static int DEFAULT_PERCENTAGEWIDTH = 0;
    private int mPercentageWidth = DEFAULT_PERCENTAGEWIDTH;
    private static int DEFAULT_PERCENTAGEHEIGHT = 0;
    private int mPercentageHeight = DEFAULT_PERCENTAGEHEIGHT;
    private final RectF _availableSpaceRect = new RectF();
    private final SparseIntArray _textCachedSizes = new SparseIntArray();
    private  SizeTester _sizeTester;
    private float _maxTextSize;
    private float _spacingMult = 1.0f;
    private float _spacingAdd = 0.0f;
    private float _minTextSize;
    private int _widthLimit;
    private int _maxLines;
    private boolean _enableSizeCache = true;
    private boolean _initiallized = false;
    private TextPaint paint;
    private static final int NO_LINE_LIMIT = -1;

    public CustomFontTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypefaceManager.initTypeface(this, context, attrs, defStyle);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView, defStyle, 0);
        DEFAULT_PERCENTAGEHEIGHT = getHeight();
        DEFAULT_PERCENTAGEWIDTH = getWidth();
        mPercentageWidth = a.getInt(R.styleable.CustomFontTextView_percentage_width, DEFAULT_PERCENTAGEWIDTH);
        mPercentageHeight = a.getInt(R.styleable.CustomFontTextView_percentage_height, DEFAULT_PERCENTAGEHEIGHT);

        a.recycle();
        if(mPercentageWidth >= 6) setDefaultPercentageWidth();
        if(mPercentageHeight >= 6) setDefaultPercentageheight();
        // using the minimal recommended font size
        _minTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
        _maxTextSize = getTextSize();
        if(_maxLines == 0)
            // no value was assigned during construction
            _maxLines = NO_LINE_LIMIT;
        // prepare size tester:
        _sizeTester = new SizeTester() {
            final RectF textRect = new RectF();

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public int onTestSize(final int suggestedSize, final RectF availableSPace) {
                paint.setTextSize(suggestedSize);
                final String text = getText().toString();
                final boolean singleline = getMaxLines() == 1;
                if(singleline) {
                    textRect.bottom = paint.getFontSpacing();
                    textRect.right = paint.measureText(text);
                } else {
                    final StaticLayout layout = new StaticLayout(text, paint, _widthLimit, Layout.Alignment.ALIGN_NORMAL, _spacingMult, _spacingAdd, true);
                    // return early if we have more lines
                    if(getMaxLines() != NO_LINE_LIMIT && layout.getLineCount() > getMaxLines())
                        return 1;
                    textRect.bottom = layout.getHeight();
                    int maxWidth = -1;
                    for (int i = 0; i < layout.getLineCount(); i++)
                        if(maxWidth < layout.getLineRight(i) - layout.getLineLeft(i))
                            maxWidth = (int) layout.getLineRight(i) - (int) layout.getLineLeft(i);
                    textRect.right = maxWidth;
                }
                textRect.offsetTo(0, 0);
                if(availableSPace.contains(textRect))
                    // may be too small, don't worry we will find the best match
                    return -1;
                // else, too big
                return 1;
            }
        };
        _initiallized = true;
    }
    public void setPercentageHeight(int mPercentageHeight) {
        this.mPercentageHeight = mPercentageHeight;

    }

    public void setPercentageWidth(int mPercentageWidth) {
        this.mPercentageWidth = mPercentageWidth;
    }

    void setDefaultPercentageWidth() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width_pixel = (int) metrics.widthPixels * mPercentageWidth / 100;
        setWidth(width_pixel);
    }

    void setDefaultPercentageheight() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int height_pixel = (int) metrics.heightPixels * mPercentageHeight / 100;
        setHeight(height_pixel);
    }

    @Override
    public void setTypeface(final Typeface tf) {
        if(paint == null) paint = new TextPaint(getPaint());
        paint.setTypeface(tf);
        super.setTypeface(tf);
    }

    @Override
    public void setTextSize(final float size) {
        _maxTextSize = size;
        _textCachedSizes.clear();
        adjustTextSize();
    }

    @Override
    public int getMaxLines() {
        return _maxLines;
    }

    @Override
    public void setMaxLines(final int maxlines) {
        super.setMaxLines(maxlines);
        _maxLines = maxlines;
        reAdjust();
    }

    @Override
    public void setSingleLine() {
        super.setSingleLine();
        _maxLines = 1;
        reAdjust();
    }

    @Override
    public void setSingleLine(final boolean singleLine) {
        super.setSingleLine(singleLine);
        if(singleLine) _maxLines = 1;
        else _maxLines = NO_LINE_LIMIT;
        reAdjust();
    }


    @Override
    public void setLines(final int lines) {
        super.setLines(lines);
        _maxLines = lines;
        reAdjust();
    }

    @Override
    public void setTextSize(final int unit, final float size) {
        final Context c = getContext();
        Resources r;
        if(c == null) r = Resources.getSystem();
        else r = c.getResources();
        _maxTextSize = TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
        _textCachedSizes.clear();
        adjustTextSize();
    }

    @Override
    public void setLineSpacing(final float add, final float mult) {
        super.setLineSpacing(add, mult);
        _spacingMult = mult;
        _spacingAdd = add;
    }


    public void setMinTextSize(final float minTextSize) {
        _minTextSize = minTextSize;
        reAdjust();
    }

    private void reAdjust() {
        adjustTextSize();
    }

    private void adjustTextSize() {
        // This is a workaround for truncated text issue on ListView, as shown here: https://github.com/AndroidDeveloperLB/DeviceFitTextView/pull/14
        // TODO think of a nicer, elegant solution.
        post(new Runnable() {
            @Override
            public void run() {
                if(!_initiallized) return;
                final int startSize = (int) _minTextSize;
                final int heightLimit = getMeasuredHeight() - getCompoundPaddingBottom() - getCompoundPaddingTop();
                _widthLimit = getMeasuredWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
                if(_widthLimit <= 0) return;
                _availableSpaceRect.right = _widthLimit;
                _availableSpaceRect.bottom = heightLimit;
                superSetTextSize(startSize);
            }
        });
    }

    private void superSetTextSize(int startSize) {
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, efficientTextSizeSearch(startSize, (int) _maxTextSize, _sizeTester, _availableSpaceRect));
    }


    public void setEnableSizeCache(final boolean enable) {
        _enableSizeCache = enable;
        _textCachedSizes.clear();
        adjustTextSize();
    }

    private int efficientTextSizeSearch(final int start, final int end, final SizeTester sizeTester, final RectF availableSpace) {
        if(!_enableSizeCache) return binarySearch(start, end, sizeTester, availableSpace);
        final String text = getText().toString();
        final int key = text == null ? 0 : text.length();
        int size = _textCachedSizes.get(key);
        if(size != 0) return size;
        size = binarySearch(start, end, sizeTester, availableSpace);
        _textCachedSizes.put(key, size);
        return size;
    }

    private int binarySearch(final int start, final int end, final SizeTester sizeTester, final RectF availableSpace) {
        int lastBest = start;
        int lo = start;
        int hi = end - 1;
        int mid = 0;
        while (lo <= hi) {
            mid = lo + hi >>> 1;
            final int midValCmp = sizeTester.onTestSize(mid, availableSpace);
            if(midValCmp < 0) {
                lastBest = lo;
                lo = mid + 1;
            } else if(midValCmp > 0) {
                hi = mid - 1;
                lastBest = hi;
            } else return mid;
        }
        // make sure to return last best
        // this is what should always be returned
        return lastBest;
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        super.onTextChanged(text, start, before, after);
        reAdjust();
    }

    @Override
    protected void onSizeChanged(final int width, final int height, final int oldwidth, final int oldheight) {
        _textCachedSizes.clear();
        super.onSizeChanged(width, height, oldwidth, oldheight);
        if(width != oldwidth || height != oldheight) reAdjust();
    }

    private interface SizeTester {
        public int onTestSize(int suggestedSize, RectF availableSpace);
    }


    //AvenirLTStd-Black.otf
    //AvenirLTStd-Book.otf
    //AvenirLTStd-Medium.otf
}
