package com.obsez.mobile.meijue.ui.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HorizontalLineDivider extends RecyclerView.ItemDecoration {

    private final Paint paint;
    private int leftPadding = 0;
    private int rightPadding = 0;
    private int dividerHeight = 6;
    private int startPosition = 0;
    private int skipPositions = 0;

    public HorizontalLineDivider(final int color) {
        this.paint = new Paint();
        this.paint.setColor(color);
    }

    public HorizontalLineDivider setLeftPadding(final int leftPadding) {
        this.leftPadding = leftPadding;
        return this;
    }

    public HorizontalLineDivider setRightPadding(final int rightPadding) {
        this.rightPadding = rightPadding;
        return this;
    }

    public HorizontalLineDivider setDividerHeight(final int dividerHeight) {
        this.dividerHeight = dividerHeight;
        return this;
    }

    public HorizontalLineDivider setStartPosition(final int startPosition) {
        this.startPosition = startPosition;
        return this;
    }

    public HorizontalLineDivider skipNEndPositions(final int skipPositions) {
        this.skipPositions = skipPositions;
        return this;
    }

    @Override
    public void onDraw(final Canvas canvas, final RecyclerView parent, final RecyclerView.State state) {
        final int dividerLeft = parent.getPaddingLeft() + this.leftPadding;
        final int dividerRight = parent.getWidth() - parent.getPaddingRight() - this.rightPadding;

        for (int i = 0; i < parent.getChildCount() - 1 - this.skipPositions; i++) {
            final View child = parent.getChildAt(i);
            final int childPosition = parent.getChildAdapterPosition(child);
            if (childPosition >= this.startPosition) {
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int dividerTop = child.getBottom() + params.bottomMargin - (this.dividerHeight / 2);
                final int dividerBottom = dividerTop + (this.dividerHeight / 2);
                canvas.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, paint);
            }
        }
    }
}
