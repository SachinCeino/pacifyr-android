package com.pacifyr.omhealthcare;

/**
 * Created by Samsad on 08/August/2017.
 * samsad@ceinotech.com
 */
        import android.content.Context;
        import android.util.AttributeSet;
        import android.view.ViewGroup;
        import android.widget.ListView;

/**
 * Created by Mani-Ceino on 3/8/2017.
 */

public class ExpandableHeightListView extends ListView {
    boolean expanded = false;

    public ExpandableHeightListView(Context context) {
        super(context);
    }

    public ExpandableHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableHeightListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isExpanded()) {
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
