# ゲーム制作編 その2

## これまでのあらすじ

JavaとJFrame(swing)を使ってウィンドウを表示、画像描画等が出来るようになった！

ついに†任意のゲーム†がプログラミングできるようになった！！

でも、そのコード、読みにくくない……？

そこでクラス！！

## ファイル分割としてのクラスの利用

クラスは、変数とメソッドを持つことが出来て、オブジェクトを生成して、それらにアクセスすることが出来る

ならば、クラスを作って、そこにひとまとまりの処理を書いてやれば、Mainから分離できるね？

手始めに、タイマーを分離してみよう

```java
// Timer.java
package gameprog;

import java.awt.*;
import javax.swing.*;

public class Timer {
	public int time;
	public Timer(){
		time = 0;
	}
	public float getSec(){
		return (float)time/60f;
	}
	public void move(Graphics2D g2, JFrame fr){
		time ++;
		float sec = getSec();
		g2.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
		g2.setColor(Color.black);
		g2.drawString(String.format("%.5f秒",sec),0,25);
	}
}
```

```java
// Main.java
public class Main {
	public static void main(String[] args){
		(new Main()).run();
	}
	public JFrame fr;
	public BufferedImage buf;
	public boolean[] keybef, keynow, keynext;
	public Image dman;
	public Timer timer;									// 追加
	public void run(){
		// (中略)
	}
	public int x = 400,y = 300;
	public ArrayList<Float> bxList, byList, bvxList, bvyList;
	public int br = 5;
														// time 削除
	// state 0:Title 1:Game 2:Result
	public int state = 0;
	public void move(){
		Graphics2D g2 = (Graphics2D)buf.getGraphics();
		if(state==0){
			g2.setColor(Color.black);
			g2.setFont(new Font(Font.SERIF, Font.PLAIN, 48));
			g2.drawString("ゲーム入門制作",30,100);

			g2.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
			g2.drawString("Zキーを押してスタート",400,400);

			if(onPressed(KeyEvent.VK_Z)){
				state = 1;
				// 初期化
				x = 400;
				y = 300;
				timer = new Timer();					// 変更
				bxList = new ArrayList<>();
				byList = new ArrayList<>();
				bvxList = new ArrayList<>();
				bvyList = new ArrayList<>();
			}
		}else if(state==1){
			// (中略)

			// 新しく弾を生成
			if(timer.time%60==0){						// 変更
				// (中略)
			}

			for(int i=0;i<bxList.size();i++){
				// (中略)
			}

			timer.move(g2, fr);							// 変更
			// (後略)
		} // (後略)
	}
	// (後略)
}
```

ほんのすこしすっきりしました？

少なくとも、Timerクラスに時間関係の処理を押し込んだため、Timerクラスを見れば時間処理がどうなっているかが分かるようになりますね

## プレイヤー・弾の分離

プレイヤーと弾の共通点、それは

- 座標を持つ
- 動く
- 当たり判定の処理を書く

です。そこで次のようなクラス構造を考えます

- Entityクラスという、当たり判定を持つ物体一般を管理するクラスを作る
- Playerクラスという、プレイヤーを扱うクラスを、*Entityクラスを継承して*作る
- Enemyクラスという、弾を扱うクラスを、*Entityクラスを継承して*作る

実際作ってみましょう

なお、移すにあたって一気に書き換えてしまっていますが、基本は同じことをやっていますし、前のゲームと全く同じ挙動をしています

```java
// Entity.java
package gameprog;

import java.awt.*;

public class Entity {
    // (x,y)、(x+w,y+h)の長方形を領域とし、当たり判定の際は、中心(x+w/2,y+h/2)半径rとする物体
    public float x,y;
    public float w,h;
    public float r;
    public Entity(float x,float y,float w,float h,float r){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
    }
    public void move(Graphics2D g2, Main par){} // Entityクラスにmoveメソッドはあるよ！という明示 オーバーライドさせる
    // EntityとEntity同士の衝突判定 (円同士)
    public boolean isHit(Entity that){
        float dx = (this.x + this.w/2f) - (that.x + that.w/2f);     // 中心座標同士の距離
        float dy = (this.y + this.h/2f) - (that.y + that.h/2f);
        float dr = this.r + that.r;
        return (dx*dx + dy*dy <= dr*dr);
    }
}
```

```java
// Player.java
package gameprog;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class Player extends Entity{
    public Player(float x, float y, float w, float h){
        super(x,y,w,h,10);
        this.w = w;
        this.h = h;
    }
    public void move(Graphics2D g2, Main par){
        JFrame fr = par.fr;
        int v = 6;
        if(par.isPressed(KeyEvent.VK_RIGHT)){
            x += v;
        }
        if(par.isPressed(KeyEvent.VK_LEFT)){
            x -= v;
        }
        if(par.isPressed(KeyEvent.VK_DOWN)){
            y += v;
        }
        if(par.isPressed(KeyEvent.VK_UP)){
            y -= v;
        }
        // 画面外に出さない
        if(x>800-w) x = 800-w;
        if(x<0) x = 0;
        if(y>600-h) y = 600-h;
        if(y<0) y = 0;
        g2.drawImage(par.dman,(int)x,(int)y,(int)w,(int)h,fr);
    }
}
```

```java
// Bullet.java
package gameprog;

import java.awt.*;

public class Bullet extends Entity{
    public float vx,vy;
    public Bullet(float x, float y, float vx, float vy){
        super(x-5,y-5,10,10,5);
        this.vx = vx;
        this.vy = vy;
    }
    public void move(Graphics2D g2, Main par){
        x += vx;
        y += vy;
        // 完全に画面外に出たらループさせる
        if(x <= 0-w){
            x = 800;
        }else if(x >= 800){
            x = 0-w;
        }
        if(y <= 0-h){
            y = 600;
        }else if(y >= 600){
            y = 0-h;
        }
        g2.setColor(Color.blue);
        g2.fillOval((int)x,(int)y,(int)w,(int)h);
    }
}
```

```java
// Main.java
package gameprog;

import java.awt.*;
import javax.swing.*;

import java.io.File;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.awt.event.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        (new Main()).run();
    }
    public JFrame fr;
    public BufferedImage buf;
    public boolean[] keybef, keynow, keynext;
    public Image dman;
    public Timer timer;
    public void run(){
        buf = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);

        keybef = new boolean[256];
        keynow = new boolean[256];
        keynext = new boolean[256];
        for(int i=0;i<256;++i)
            keybef[i] = keynow[i] = keynext[i] = false;

        // ウィンドウ生成
        fr = new JFrame("タイトル");
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
            // このtryの中で画像を読み込む
            dman = ImageIO.read(new File("src/d3.png"));
        }catch(Exception e){
            e.printStackTrace();
        }

        // 無限ループ
        while(true){
            long beg = System.nanoTime();
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
            long range = System.nanoTime() - beg;
            long sleeptime = (16666666L - range)/1000000L;
            if(sleeptime < 0) sleeptime = 0;
            try{
                Thread.sleep(sleeptime);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    // クラスを使うと変数がすっきり！
    public Player pl;                                               // 変更
    public ArrayList<Bullet> bullets;                               // 変更
    
    // state 0:Title 1:Game 2:Result
    public int state = 0;
    public void move(){
        Graphics2D g2 = (Graphics2D)buf.getGraphics();
        if(state==0){
            g2.setColor(Color.black);
            g2.setFont(new Font(Font.SERIF, Font.PLAIN, 48));
            g2.drawString("ゲーム入門制作",30,100);

            g2.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
            g2.drawString("Zキーを押してスタート",400,400);

            if(onPressed(KeyEvent.VK_Z)){
                state = 1;
                // 初期化
                float w = (float)dman.getWidth(fr)/4f;              // 変更
                float h = (float)dman.getHeight(fr)/4f;             // 変更
                pl = new Player(400,300,w,h);                       // 変更
                bullets = new ArrayList<>();                        // 変更
                timer = new Timer();
            }
        }else if(state==1){
            // ゲーム画面の中身がかなりすっきりしたと思います
            pl.move(g2,this);                                       // 変更

            // 新しく弾を生成
            if(timer.time%60==0){
                // [0,1) の範囲の乱数を2π倍して [0,2π) の範囲の乱数にする
                float angle = (float)Math.random() * (float)Math.PI * 2f;
                // [0,1) の範囲を4倍して3足すことで [3,7) の範囲の乱数にする
                float len = (float)Math.random() * 4f + 3f;
                float vx = (float)Math.cos(angle) * len;
                float vy = (float)Math.sin(angle) * len;
                bullets.add(new Bullet(400f, 0f, vx, vy));          // 変更
            }

            for(int i=0;i<bullets.size();i++){                      // 変更
                bullets.get(i).move(g2, this);                      // 変更
            }

            timer.move(g2,fr);

            // 当たったらゲームオーバー
            // check
            for(int i=0;i<bullets.size();++i){                      // 変更
                if(bullets.get(i).isHit(pl)){                       // 変更
                    // ゲームオーバーに移行
                    state = 2;
                    break;
                }
            }
        }else if(state == 2){
            g2.setColor(Color.black);
            g2.setFont(new Font(Font.SERIF, Font.PLAIN, 32));
            g2.drawString("ゲームオーバー",30,100);

            float sec = timer.getSec();
            g2.setColor(Color.red);
            g2.setFont(new Font(Font.SERIF, Font.PLAIN, 48));
            g2.drawString(String.format("スコア : %05.5f 秒",sec),100,200);

            g2.setColor(Color.black);
            g2.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
            g2.drawString("Zキーでコンティニュー",400,500);

            if(onPressed(KeyEvent.VK_Z)){
                state = 0;
            }
        }
    }

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
```

はい。

Entityクラスはx,y,w,h,rの変数を保持し、必ずmoveメソッドを持っていることが保証されていて、isHitを使うことで他のEntityとの衝突判定ができるクラス

Playerクラスはmoveメソッドを上書き(オーバーライド)して、プレイヤーの入力で移動するように変更したEntity継承クラス

Bulletクラスはmoveメソッドを上書きして、等速直線運動をするように変更したEntity継承クラス

という感じになっています

MainでやっていたことをEntity継承クラスのmoveにまとめたため、Mainの中身がかなり綺麗になったと思います

moveメソッドにMainクラスのオブジェクトを渡すように設定されていますが、これはisPressedやdmanという変数はMainクラスの変数なので、Mainクラスオブジェクトを渡してあげないと使うことができないためです。ちなみにparはparent(親)を略して使いました

---

なお、あえてEntityにmoveメソッドを実装しましたが、これによって、以下のように変更しても動作するようになっています

```java
    public Player pl;
    public ArrayList<Bullet> bullets;
```

↓

```java
    public Entity pl;
    public ArrayList<Entity> bullets;
```

この考え方は非常に大事です。今回は使えませんでしたが、例えば弾の種類がいっぱいあったとします(直線運動、円周上運動、速度が変化したり)

それらをBullet1, Bullet2, Bullet3というクラスで実装します。

もし、共通のクラス(Bullet)を継承せずに書いて、いざ使おうとすると、こうなります

```java
    public ArrayList<Bullet1> bullets1;
    public ArrayList<Bullet2> bullets2;
    public ArrayList<Bullet2> bullets3;
    bullets1.add(new Bullet1());
    bullets2.add(new Bullet2());
    bullets3.add(new Bullet3());
```

ですが、共通のクラスBulletを継承して書けば、こうなります

```java
    public ArrayList<Bullet> bullets;
    bullets.add(new Bullet1());
    bullets.add(new Bullet2());
    bullets.add(new Bullet3());
```

このようにすれば管理するArrayListは1つで済みますし、今後新しい種類(Bullet4)が追加されても変更することがなくなります

これが、クラスの力です

## まとめ

クラスを使うと処理を分離できるようになり、単体で見ればシンプルなコードに落とすことができます

クラスにはメソッドのオーバーロード(オーバーライドとはまた違うやつ)、interface/implementsによるインターフェースの実装という機能、今回は省略したstaticによる静的変数・静的メソッド、などなど、これ以上に便利な関数がいっぱいあります

ぜひJavaの勉強を進めて、これらを用いて効率的に開発できるようになってください

以上で講習会を終了します

## Extra 課題

今回クラスによって処理を分離し、読みやすくしましたが、一つだけ残していることがあります

それは、タイトル画面→ゲーム画面→リザルト画面 という部分です

これをクラスを使ってエレガントに実装する、デザインパターン、Stateパターンというものがあります。

Stateパターンを使うと、画面の状態が増えても(Bulletの種類が増えた時の場合と同様に)綺麗に書くことができます

ぜひ調べて実装して、クラスの良さを感じて欲しいです
