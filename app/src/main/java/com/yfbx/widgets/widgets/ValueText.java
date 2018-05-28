package com.yfbx.widgets.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yfbx.widgets.R;

/**
 * Author:Edward
 * Date:2018/5/25
 * Description:
 */

public class ValueText extends View {

    private Context context;
    private Paint paint;

    /**
     * 文字尺寸
     */
    private Rect titleRect;
    private Rect textRect;
    /**
     * textAlign 常量
     */
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;

    /**
     * View尺寸
     */
    private float width;
    private float height;
    /**
     * View属性
     */
    private String text;
    private String title;
    private int textColor;
    private int titleColor;
    private float textSize;
    private float titleSize;
    private Drawable drawableLeft;
    private Drawable drawableRight;
    private float drawablePadding;
    private int textAlign;


    public ValueText(Context context) {
        this(context, null);
    }

    public ValueText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ValueText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttr(context, attrs);
        init();
    }

    /**
     * 获取属性
     */
    private void getAttr(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ValueText);
        text = array.getString(R.styleable.ValueText_android_text);
        title = array.getString(R.styleable.ValueText_title);
        textColor = array.getColor(R.styleable.ValueText_android_textColor, Color.GRAY);
        titleColor = array.getColor(R.styleable.ValueText_titleColor, Color.GRAY);
        textSize = array.getDimension(R.styleable.ValueText_android_textSize, sp2px(14));
        titleSize = array.getDimension(R.styleable.ValueText_titleSize, sp2px(14));
        drawableLeft = array.getDrawable(R.styleable.ValueText_android_drawableLeft);
        drawableRight = array.getDrawable(R.styleable.ValueText_android_drawableRight);
        drawablePadding = array.getDimension(R.styleable.ValueText_android_drawablePadding, dp2px(8));
        textAlign = array.getInt(R.styleable.ValueText_textAlign, ALIGN_LEFT);
        array.recycle();
    }


    /**
     * 初始化
     */
    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        textRect = new Rect();
        titleRect = new Rect();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //width
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = measureWidth();
        }

        //height
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = measureHeight();
        }
        setMeasuredDimension((int) width, (int) height);//存在精度损失
    }

    /**
     * 计算宽度
     */
    private float measureWidth() {
        float measureWidth = 0;
        //测量title宽度
        if (title != null) {
            paint.setTextSize(titleSize);
            paint.getTextBounds(title, 0, title.length(), titleRect);
            measureWidth = titleRect.width();
        }
        //测量text宽度
        if (text != null) {
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), textRect);
            measureWidth = measureWidth + textRect.width();
        }
        //drawableLeft
        if (drawableLeft != null) {
            measureWidth = measureWidth + drawableLeft.getIntrinsicWidth() + drawablePadding;
        }
        //drawableRight
        if (drawableRight != null) {
            measureWidth = measureWidth + drawableRight.getIntrinsicWidth() + drawablePadding;
        }
        return measureWidth + getPaddingLeft() + getPaddingRight();
    }

    /**
     * 计算高度
     */
    private float measureHeight() {
        float measureHeight = 0;
        //测量title高度
        if (title != null) {
            paint.setTextSize(titleSize);
            paint.getTextBounds(title, 0, title.length(), titleRect);
            measureHeight = titleRect.height();
        }
        //测量text高度
        if (text != null) {
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), textRect);
            measureHeight = Math.max(measureHeight, textRect.height());
        }
        //drawableLeft
        if (drawableLeft != null) {
            measureHeight = Math.max(measureHeight, drawableLeft.getIntrinsicHeight());
        }
        //drawableRight
        if (drawableRight != null) {
            measureHeight = Math.max(measureHeight, drawableRight.getIntrinsicHeight());
        }
        return measureHeight + getPaddingTop() + getPaddingBottom();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        //title尺寸
        if (title != null) {
            paint.setTextSize(titleSize);
            paint.getTextBounds(title, 0, title.length(), titleRect);
        }
        //text尺寸
        if (text != null) {
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), textRect);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(0, height / 2);

        //drawableLeft
        if (drawableLeft != null) {
            drawableLeft.setBounds(getPaddingLeft(), -drawableLeft.getIntrinsicHeight() / 2, getPaddingLeft() + drawableLeft.getIntrinsicWidth(), drawableLeft.getIntrinsicHeight() / 2);
            drawableLeft.draw(canvas);
        }

        //drawableRight
        if (drawableRight != null) {
            drawableRight.setBounds((int) (width - getPaddingRight() - drawableRight.getIntrinsicWidth()), -drawableRight.getIntrinsicHeight() / 2, (int) (width - getPaddingRight()), drawableRight.getIntrinsicHeight() / 2);
            drawableRight.draw(canvas);
        }

        //title
        if (title != null) {
            paint.setTextSize(titleSize);
            paint.setColor(titleColor);
            float left = drawableLeft == null ? getPaddingLeft() : getPaddingLeft() + drawableLeft.getIntrinsicWidth() + drawablePadding;
            canvas.drawText(title, left, -titleRect.exactCenterY(), paint);
        }

        //text
        if (text != null) {
            drawText(canvas);
        }

    }


    /**
     * 绘制 text
     */
    private void drawText(Canvas canvas) {
        paint.setTextSize(textSize);
        paint.setColor(textColor);

        //最大可用宽度
        float maxWidth = width - getPaddingLeft() - getPaddingRight() - drawablePadding;
        if (drawableLeft != null) {
            maxWidth = maxWidth - drawableLeft.getIntrinsicWidth() - drawablePadding;
        }
        if (drawableRight != null) {
            maxWidth = maxWidth - drawableRight.getIntrinsicWidth() - drawablePadding;
        }
        if (title != null) {
            maxWidth = maxWidth - titleRect.width() - drawablePadding;
        }

        //超过最大可用宽度，截取文字
        subText(maxWidth);

        //靠左
        if (textAlign == ALIGN_LEFT) {
            float left = getPaddingLeft();
            if (drawableLeft != null) {
                left = left + drawableLeft.getIntrinsicWidth() + drawablePadding;
            }
            if (title != null) {
                left = left + titleRect.width() + drawablePadding;
            }
            canvas.drawText(text, left, -textRect.exactCenterY(), paint);
        }

        //靠右
        if (textAlign == ALIGN_RIGHT) {
            float left = width - getPaddingRight() - textRect.width() - drawablePadding;
            if (drawableRight != null) {
                left = left - drawableRight.getIntrinsicWidth();
            }
            canvas.drawText(text, left, -textRect.exactCenterY(), paint);
        }
    }


    /**
     * 超过最大可用宽度，截取文字
     */
    private String subText(float maxWidth) {
        if (textRect.width() < maxWidth) {
            return text;
        }
        while (textRect.width() > maxWidth) {
            text = text.substring(0, text.length() - 1);
            paint.getTextBounds(text, 0, text.length(), textRect);
        }
        text = text.substring(0, text.length() - 1) + "...";
        return text;
    }


    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setTitle(String title) {
        this.title = title;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setTitleSize(float titleSize) {
        this.titleSize = titleSize;
        invalidate();
    }

    public void setDrawableLeft(Drawable drawableLeft) {
        this.drawableLeft = drawableLeft;
        invalidate();
    }

    public void setDrawableRight(Drawable drawableRight) {
        this.drawableRight = drawableRight;
        invalidate();
    }

    public void setDrawablePadding(float drawablePadding) {
        this.drawablePadding = drawablePadding;
        invalidate();
    }

    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
        invalidate();
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public float getTitleSize() {
        return titleSize;
    }

    public Drawable getDrawableLeft() {
        return drawableLeft;
    }

    public Drawable getDrawableRight() {
        return drawableRight;
    }

    public float getDrawablePadding() {
        return drawablePadding;
    }

    public int getTextAlign() {
        return textAlign;
    }

    /**
     * sp转换成px
     */
    protected float sp2px(float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

    /**
     * dp 转换为 px
     */
    private float dp2px(float value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return value * scale + 0.5f;
    }


}