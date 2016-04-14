import java.awt.*;
import javax.swing.*;

import java.io.File;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.awt.event.*;

class Main {
  public static void main(String[] args){
    (new Main()).run();
  }
  public JFrame fr;
  public BufferedImage buf;
  public boolean[] keybef, keynow, keynext;
  public Image dman;
  public void run(){
    buf = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);

    keybef = new boolean[256];
    keynow = new boolean[256];
    keynext = new boolean[256];
    for(int i=0;i<256;++i)
      keybef[i] = keynow[i] = keynext[i] = false;

    // ウィンドウ生成
    fr = new JFrame();
    // 閉じるボタンの挙動設定
    fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // ウィンドウサイズ変更不可に
    fr.setResizable(false);
    // ウィンドウの中身のサイズを調節 ここでは横800 x 縦600
    fr.getContentPane().setPreferredSize(new Dimension(800, 600));
    // 表示
    fr.setVisible(true);
    // サイズ調整
    fr.pack();
    // キーリスナー登録
    fr.addKeyListener(new keyclass());

    try{
      dman = ImageIO.read(new File("d3.png"));
    }catch(Exception e){
      e.printStackTrace();
    }

    // 無限ループ
    while(true){
      for(int i=0;i<256;++i){
        keybef[i] = keynow[i];
        keynow[i] = keynext[i];
      }
      Graphics2D g2 = (Graphics2D)buf.getGraphics();
      g2.setColor(Color.white);
      g2.fillRect(0,0,800,600);
      move();
      g2 = (Graphics2D)fr.getContentPane().getGraphics();
      g2.drawImage(buf,0,0,fr);
      // 60FPS用
      try{
        Thread.sleep(16);
      } catch (Exception e){
        e.printStackTrace();
      }
    }
  }
  public int x = 0;
  public void move(){
    if(isPressed(KeyEvent.VK_Z)){
      x -= 3;
    }
    if(isPressed(KeyEvent.VK_X)){
      x += 3;
    }
    Graphics2D g2 = (Graphics2D)buf.getGraphics();
    int w = dman.getWidth(null);
    int h = dman.getHeight(null);
    g2.drawImage(dman,x,0,w,h,null);

    g2.setColor(new Color(255,0,0));
    g2.fillRect(10,100,100,20);

    g2.setColor(Color.blue);
    g2.drawOval(140,40,20,20);

    g2.setColor(Color.green);
    g2.setFont(new Font(Font.SERIF, Font.PLAIN, 36));
    g2.drawString("D言語くん in Java",400,600);
  }

  // 以下コピペ
  public boolean isPressed(int key){
    return keynow[key];
  }
  public boolean onPressed(int key){
    return !keybef[key] && keynow[key];
  }
  public class keyclass implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
      keynext[e.getKeyCode()] = true;
    }
    @Override
    public void keyReleased(KeyEvent e) {
      keynext[e.getKeyCode()] = false;
    }
  }
}