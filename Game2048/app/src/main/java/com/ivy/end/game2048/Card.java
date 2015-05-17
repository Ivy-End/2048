package com.ivy.end.game2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

/**
 * Created by End on 2015/5/14.
 */
public class Card extends FrameLayout {

    public Card(Context context) {
        super(context);

        // 初始化tvNumber
        tvNumber = new TextView(getContext());
        tvNumber.setTextSize(32);
        tvNumber.setBackgroundColor(0x33FFFFFF);    // 设置背景
        tvNumber.setGravity(Gravity.CENTER);    // 居中

        // 添加tvNumber
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);    // 设置偏移量
        addView(tvNumber, lp);

        setNumber(0);   // 初始化数字为0
    }

    public int getNumber() {
        return number;  // 返回数字
    }

    public void setNumber(int number) {
        this.number = number;   // 设置数字

        if(number > 0) {
            tvNumber.setText(number + "");  // 设置tvNumber文本
        } else {
            tvNumber.setText("");   // 空文本，不显示数字0
        }

        switch (number) {
            case 0:
                tvNumber.setBackgroundColor(0x33FFFFFF);
                break;
            case 2:
                tvNumber.setTextColor(getResources().getColor(R.color.text2));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg2));
                break;
            case 4:
                tvNumber.setTextColor(getResources().getColor(R.color.text4));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg4));
                break;
            case 8:
                tvNumber.setTextColor(getResources().getColor(R.color.text8));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg8));
                break;
            case 16:
                tvNumber.setTextColor(getResources().getColor(R.color.text16));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg16));
                break;
            case 32:
                tvNumber.setTextColor(getResources().getColor(R.color.text32));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg32));
                break;
            case 64:
                tvNumber.setTextColor(getResources().getColor(R.color.text64));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg64));
                break;
            case 128:
                tvNumber.setTextColor(getResources().getColor(R.color.text128));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg128));
                break;
            case 256:
                tvNumber.setTextColor(getResources().getColor(R.color.text256));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg256));
                break;
            case 512:
                tvNumber.setTextColor(getResources().getColor(R.color.text512));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg512));
                break;
            case 1024:
                tvNumber.setTextColor(getResources().getColor(R.color.text1024));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg1024));
                break;
            case 2048:
                tvNumber.setTextColor(getResources().getColor(R.color.text2048));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bg2048));
                break;
            default:
                tvNumber.setTextColor(getResources().getColor(R.color.textsuper));
                tvNumber.setBackgroundColor(getResources().getColor(R.color.bgsuper));
                break;
        }
    }

    public boolean equals(Card o) {
        return getNumber() == o.getNumber();
    }

    private int number = 0; // 保存数字
    private TextView tvNumber;  // 显示数字
}
