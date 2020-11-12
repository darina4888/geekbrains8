package ru.lesson8;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class App extends JFrame {

    /**
     * Нет попадания в бомбу
     */
    protected boolean isAlive = true;

    public App(final int SIZE) {
        setTitle("Bombs and Deaths");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(300, 300, 400, 400);

        boolean[][] bombs = new boolean[SIZE][SIZE];
        AtomicInteger bombCnt = new AtomicInteger();
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++) {
                bombs[i][j] = Math.random() <= 0.25;
                if(bombs[i][j]) bombCnt.getAndIncrement();
            }

        JButton[][] buttons = new JButton[SIZE][SIZE];
        setLayout(new GridLayout(SIZE,SIZE));
        AtomicInteger btn = new AtomicInteger();

        //количество "чистых" ячеек
        int count = (SIZE * SIZE) - bombCnt.get();

        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++) {
                JButton button = new JButton();
                buttons[i][j] = button;
                button.setText("???");
                int ii = i;
                int jj = j;

                button.addActionListener(e -> {
                    if(!isAlive) return;
                    if(bombs[ii][jj]) {
                        setButtonStyle(button,"\u2620",Color.RED,Color.WHITE,50.F);

                        //если попадание на бомбу, то открываются все бомбы другого цвета
                        for(int i_ = 0; i_ < SIZE; i_++)
                            for(int j_ = 0; j_ < SIZE; j_++)
                                if(bombs[i_][j_] && buttons[ii][jj]!=buttons[i_][j_])
                                    setButtonStyle(buttons[i_][j_],"\u2620",Color.ORANGE,Color.WHITE,50.F);

                        isAlive = false;
                    } else {
                        //вывод количества бомбочек в соседних клетках
                        int bumb = 0;

                        if(0 <= (ii-1) && (ii-1) < SIZE)
                            if(bombs[ii-1][jj]) bumb++;
                        if(0 <= (ii+1) && (ii+1) < SIZE)
                            if(bombs[ii+1][jj]) bumb++;

                        if(0 <= (jj-1) && (jj-1) < SIZE)
                            if(bombs[ii][jj-1]) bumb++;
                        if(0 <= (jj+1) && (jj+1) < SIZE)
                            if(bombs[ii][jj+1]) bumb++;

                        button.setText("" + bumb + "");
                        button.setBackground(Color.GREEN);
                        btn.getAndIncrement();

                        //выиграл
                       if(count == btn.get()) {
                           isAlive = false;
                           JOptionPane.showMessageDialog(null, "You win!","Message", JOptionPane.INFORMATION_MESSAGE);
                       }
                    }
                });

                add(button);
            }

        setVisible(true);
    }

    /**
     * Установка стилей для кнопки
     * @param button
     * @param icon
     * @param backgroundColor
     * @param fontColor
     * @param fontSize
     */
    public static void setButtonStyle(JButton button,String icon,Color backgroundColor,Color fontColor,float fontSize) {
        button.setText(icon);
        button.setForeground(fontColor);
        button.setFont(button.getFont().deriveFont(fontSize));
        button.setBackground(backgroundColor);
    }
}
