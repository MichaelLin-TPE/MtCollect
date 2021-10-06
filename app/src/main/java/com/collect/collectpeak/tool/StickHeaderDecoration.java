package com.collect.collectpeak.tool;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.collect.collectpeak.log.MichaelLog;

public class StickHeaderDecoration extends RecyclerView.ItemDecoration {

    public interface StickHeaderInterface {
        /**
         * is this item need stick
         * @param position now item position in the recyclerView
         * @return true : need stick else not
         */
        boolean isStick(int position);
    }

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private StickHeaderInterface stickHeaderInterface;

    /**
     * 進行一些容錯檢查
     */
    public StickHeaderDecoration(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.manager = recyclerView.getLayoutManager();
        this.adapter = recyclerView.getAdapter();

        if (adapter == null) {
            throw new RuntimeException("please set Decoration after set adapter");
        }

        if (adapter instanceof StickHeaderInterface) {
            stickHeaderInterface = (StickHeaderInterface) adapter;
            return;
        }
        throw new RuntimeException("please make your adapter implements StickHeaderInterface");
    }

    /**
     * 繪製頭部的stick view
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        View childAt = parent.getChildAt(0);
        if (childAt == null)
            return;
        RecyclerView.ViewHolder childViewHolder = parent.getChildViewHolder(childAt);
        int position = childViewHolder.getPosition();

        for (int i = position; i >= 0; i--) {
            if (stickHeaderInterface.isStick(i)) {
                MichaelLog.Companion.i("描繪頭部："+i);
                int top = 0;
                if (position + 1 < adapter.getItemCount()) {
                    if (stickHeaderInterface.isStick(position + 1)) {
                        View childNext = parent.getChildAt(1);
                        top = manager.getDecoratedTop(childNext) < 0 ? 0 : manager
                                .getDecoratedTop(childNext);
                    }
                }
                RecyclerView.ViewHolder inflate = recyclerView.getAdapter().createViewHolder(parent,
                        recyclerView.getAdapter().getItemViewType(i));
                recyclerView.getAdapter().bindViewHolder(inflate, i);
                int measureHeight = getMeasureHeight(inflate.itemView);
                c.save();
                if (top < inflate.itemView.getMeasuredHeight() && top > 0) {
                    c.translate(0, top - measureHeight);
                }
                inflate.itemView.draw(c);
                c.restore();
                return;
            }
        }
    }

    /**
     * 測量控制元件的高度
     *
     * @param header
     */
    private int getMeasureHeight(View header) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View
                .MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        header.measure(widthSpec, heightSpec);
        header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
        return header.getMeasuredHeight();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int spanCount = 7;
        int spacing = 30;//spacing between views in grid

        if (position > 0) {
            int column = position % spanCount; // item column

            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }
}