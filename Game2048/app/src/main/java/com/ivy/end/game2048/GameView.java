package com.ivy.end.game2048;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameView extends GridLayout {

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initGameView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int cardSize = (Math.min(w, h) - 10) / 4;  // 计算卡牌尺寸
        addCards(cardSize);

        startGame();    // 开始游戏
    }

    private void addCards(int cardSize) {
        Card card;
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                card = new Card(getContext());
                card.setNumber(0);  // 生成空点
                addView(card, cardSize, cardSize);
                cardMap[i][j] = card;   // 添加卡片
            }
        }
    }

    private void startGame() {

        MainActivity.getMainActivity().clearScore();    // 清零

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                cardMap[i][j].setNumber(0); // 清空游戏界面
            }
        }

        // 初始化时2个卡片
        addRandomNumber();
        addRandomNumber();
    }

    private void initGameView() {

        setColumnCount(4);  // 设置列数

        setBackgroundColor(0xFFBBADA0); // 设置背景

        setOnTouchListener(new View.OnTouchListener() {

            private float startX, startY;   // 起始位置
            private float endX, endY;   // 终了位置
            private float offsetX, offsetY; // 偏移量
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();

                        offsetX = endX - startX;
                        offsetY = endY - startY;

                        if(Math.abs(offsetX) > Math.abs(offsetY)) { // 水平
                            if(offsetX < -5) {
                                MoveLeft();
                            } else if(offsetX > 5) {
                                MoveRight();
                            }
                        } else {    // 数值
                            if(offsetY < -5) {
                                MoveUp();
                            } else if(offsetY > 5) {
                                MoveDown();
                            }
                        }


                }
                return true;
            }
        });
    }

    private void addRandomNumber() {

        emptyPoints.clear();    // 清空空格列表

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(cardMap[i][j].getNumber() <= 0) {    // 用0表示空点
                    emptyPoints.add(new Point(i, j));
                }
            }
        }

        Point point = emptyPoints.remove((int)(Math.random() * emptyPoints.size()));    // 随机获取空点
        cardMap[point.x][point.y].setNumber(Math.random() > 0.1 ? 2 : 4);   // 按9：1的概率生成2和4
    }

    private void MoveUp() {

        boolean move = false;   // 操作变量

        for(int j = 0; j < 4; j++) {    // 列循环
            for(int i = 0; i < 4; i++) {    // 行循环
                for(int x = i + 1; x < 4; x++) {    // 从当前位置往下扫描
                    if(cardMap[x][j].getNumber() > 0) {
                        if(cardMap[i][j].getNumber() <= 0) {    // 当前值为0
                            cardMap[i][j].setNumber(cardMap[x][j].getNumber()); // 空位，上移
                            cardMap[x][j].setNumber(0); // 清空
                            x = i + 1;    // 避免 2 0 2 不合并情况
                            move = true;    // 已操作
                        } else if(cardMap[i][j].equals(cardMap[x][j])) {    // 相同
                            cardMap[i][j].setNumber(cardMap[i][j].getNumber() * 2); // 左移，上移
                            cardMap[x][j].setNumber(0); // 清空
                            MainActivity.getMainActivity().addScore(cardMap[i][j].getNumber()); // 加分
                            updateBest();   // 更新最高分
                            move = true;    // 已操作
                        }
                    }
                }
            }
        }

        if(move) {  // 如果操作，添加新的卡片
            addRandomNumber();
        }
    }

    private void MoveDown() {

        boolean move = false;   // 操作变量

        for(int j = 0; j < 4; j++) {    // 列循环
            for(int i = 3; i >= 0; i--) {    // 行循环（向左遍历）
                for(int x = i - 1; x >= 0; x--) {    // 从当前位置往上扫描
                    if(cardMap[x][j].getNumber() > 0) {
                        if(cardMap[i][j].getNumber() <= 0) {    // 当前值为0
                            cardMap[i][j].setNumber(cardMap[x][j].getNumber()); // 空位，下移
                            cardMap[x][j].setNumber(0); // 清空
                            x = i - 1;    // 避免 2 0 2 不合并情况
                            move = true;    // 已操作
                        } else if(cardMap[i][j].equals(cardMap[x][j])) {    // 相同
                            cardMap[i][j].setNumber(cardMap[i][j].getNumber() * 2); // 下移，合并
                            cardMap[x][j].setNumber(0); // 清空
                            MainActivity.getMainActivity().addScore(cardMap[i][j].getNumber()); // 加分
                            updateBest();   // 更新最高分
                            move = true;    // 已操作
                        }
                    }
                }
            }
        }

        if(move) {  // 如果操作，添加新的卡片
            addRandomNumber();
        }
    }

    private void MoveLeft() {

        boolean move = false;   // 操作变量

        for(int i = 0; i < 4; i++) {    // 行循环
            for(int j = 0; j < 4; j++) {    // 列循环
                for(int y = j + 1; y < 4; y++) {    // 从当前位置往右扫描
                    if(cardMap[i][y].getNumber() > 0) {
                        if(cardMap[i][j].getNumber() <= 0) {    // 当前值为0
                            cardMap[i][j].setNumber(cardMap[i][y].getNumber()); // 空位，左移
                            cardMap[i][y].setNumber(0); // 清空
                            y = j + 1;    // 避免 2 0 2 不合并情况
                            move = true;    // 已操作
                        } else if(cardMap[i][j].equals(cardMap[i][y])) {    // 相同
                            cardMap[i][j].setNumber(cardMap[i][j].getNumber() * 2); // 左移，合并
                            cardMap[i][y].setNumber(0); // 清空
                            MainActivity.getMainActivity().addScore(cardMap[i][j].getNumber()); // 加分
                            updateBest();   // 更新最高分
                            move = true;    // 已操作
                        }
                    }
                }
            }
        }

        if(move) {  // 如果操作，添加新的卡片
            addRandomNumber();
        }
    }

    private void MoveRight() {

        boolean move = false;   // 操作变量

        for(int i = 0; i < 4; i++) {    // 行循环
            for(int j = 3; j >= 0; j--) {    // 列循环
                for(int y = j - 1; y >= 0; y--) {    // 从当前位置往左扫描
                    if(cardMap[i][y].getNumber() > 0) {
                        if(cardMap[i][j].getNumber() <= 0) {    // 当前值为0
                            cardMap[i][j].setNumber(cardMap[i][y].getNumber()); // 空位，右移
                            cardMap[i][y].setNumber(0); // 清空
                            y = j - 1;    // 避免 2 0 2 不合并情况
                            move = true;    // 已操作
                        } else if(cardMap[i][j].equals(cardMap[i][y])) {    // 相同
                            cardMap[i][j].setNumber(cardMap[i][j].getNumber() * 2); // 右移，合并
                            cardMap[i][y].setNumber(0); // 清空
                            MainActivity.getMainActivity().addScore(cardMap[i][j].getNumber()); // 加分
                            updateBest();   // 更新最高分
                            move = true;    // 已操作
                        }
                    }
                }
            }
        }

        if(move) {  // 如果操作，添加新的卡片
            addRandomNumber();
            checkGame();    // 判断游戏是否结束
        }
    }

    private void checkGame() {

        boolean complete = true;    // 默认游戏结束

        ALL:
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(cardMap[i][j].getNumber() <= 0 ||
                        (i > 0 && cardMap[i][j].equals(cardMap[i - 1][j])) ||
                        (i < 3 && cardMap[i][j].equals(cardMap[i + 1][j])) ||
                        (j > 0 && cardMap[i][j].equals(cardMap[i][j - 1])) ||
                        (j < 3 && cardMap[i][j].equals(cardMap[i][j + 1]))) {
                    complete = false; // 游戏未结束
                    break ALL;
                }
            }
        }

        if(complete) {
            new AlertDialog.Builder(getContext()).setTitle("2048").setMessage("游戏结束。").setPositiveButton("重新开始",
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();    // 重新开始
                }
            }).show();
        }
    }

    private void updateBest() {
        int bestScore, score;
        SharedPreferences sp = getContext().getSharedPreferences("game2048", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        score = MainActivity.getMainActivity().getScore();
        bestScore = sp.getInt("best", 0);
        if (bestScore < score) {
            editor.putInt("best", score);
            MainActivity.getMainActivity().setBestScore(score);
            editor.apply();
        }
    }

    private Card[][] cardMap = new Card [4][4]; // 记录游戏
    private List<Point> emptyPoints = new ArrayList<>();    // 空点列表
}