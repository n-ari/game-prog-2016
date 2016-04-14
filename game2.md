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
class Main {
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
