# ゲームプログラミングの基礎の基礎

## ゲームとは

ここではコンピュータゲームを考えます

- プレイヤーがいる
- コンピュータがある
- プレイヤーがコンピュータを操作する
- コンピュータが応答する
- ディスプレイの表示が変わる
- それに応じてプレイヤーが操作する
- 以下ループ

これがゲーム (あとゲーム性があるというのも大事)

## GUI

ゲームが作りたいですね？

ウィンドウに画像を表示したり文字を表示したりするゲームが作りたいですね？

プログラミング入門では何をやりました？

```java
package gameprog;

import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		System.out.println("Hello, World!");
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		System.out.println("入力した数は"+n);
	}
}
```

```sh
Hello, World!
3[Enter]
入力した数は3
```

**どう考えても色鮮やかで画像いっぱいのゲームになりそうじゃ無いじゃん！？！？！？！？！？！？！**

(上記のプログラムは)

ということで、まずはそういったところから始める必要があります(が、画像表示とか以外のプログラミングは入門の知識を使います)

## 60FPS

人間の目は1秒に60コマ程度のパラパラ漫画を見せられると、それをあたかも「実際に動いている」と認識する

基本的にゲームでもちゃんと動かすなら60FPS(Frames per second)が基本となる

## Javaプロジェクトの作成

プログラミング入門でやったと思いますが一応ここでおさらい&パッケージ名等を統一したいと思います

1. ファイル→新規→Javaプロジェクト
1. プロジェクト名を gameprog にして 完了
1. パッケージエクスプローラーでgameprogを開いてsrcを選択
1. 新規Javaパッケージ(もしくはファイル→新規→パッケージ)で gameprog パッケージを作成
1. 新規Javaクラス(もしくはファイル→新規→クラス)で Main クラスを作成

![ファイル構造](scs-base-1.png)

このような構造になればOKです

パッケージ名が違う場合は以下で出てくるコードも適宜読み替えてください

## Javaによるテンプレート

とりあえずウィンドウが表示したいですね

以下がそのコードになります

```java
package gameprog;

import java.awt.*;
import javax.swing.*;

public class Main {
	public static void main(String[] args){
		(new Main()).run(); // non-static
	}
	public JFrame fr;
	public void run(){
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

		// 無限ループ
		while(true){
			long beg = System.nanoTime();
			// ここがメインの処理！
			move();
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
	// この下を頻繁に切り貼りします
	public void move(){
		// ここでいろいろやる
		System.out.println("Hello, GUI World!");
	}
}
```

とりあえずウィンドウが表示されました！！

mainメソッドの中で、なんかやっていますが、これは特に気にしないでください。しいていうなら、staticメソッドからはstaticメソッドしか呼べないという制約があるため、それを回避するために使っています

ウィンドウ生成の部分は「こういうものなんだ」ととりあえず覚えてください。

コピペで構いません。詳しい意味を知ってる人は5割もいないと思います。

その後、while(true)を使うことで「無限ループ」を起こします

無限ループをするとPCがフリーズしてしまいますが、try-catchの中のThread.sleep()で毎回16ミリ秒休憩するようにしています

そのためフリーズしないのです

なお、その前の計算処理等で数ミリ秒使ってしまっている場合を考えて、System.nanoTime()で実行時間を計測しておき、その分を16ミリ秒から削っておくことで、全体として毎フレーム16ミリ秒を実現しています

もし無限ループがないと、すぐにrunメソッドが終了してしまいます、するとウィンドウも消えてしまいます！

そのためこれは**必要な**無限ループなのです

これで、16ミリ秒ごとにmove()が呼ばれるようなプログラムが完成しました。

16ミリ秒というのは1000(ミリ秒)/60(フレーム) = 16.66666...(ミリ秒/フレーム) から来ています。

とりあえずはこれで、ゲームの土台ができました

##　丸・四角・文字の表示

白画面は寂しいのでなにか表示しましょう(プログラミング入門で言うHello Worldがしたい)

```java
	// この下を頻繁に切り貼りします
	public void move(){
		Graphics2D g2 = (Graphics2D)fr.getContentPane().getGraphics();
		g2.setColor(Color.black);
		g2.fillRect(100,100,100,100);
		g2.setColor(Color.red);
		g2.fillOval(200,100,100,100);
		g2.setColor(Color.blue);
		g2.fillOval(300,100,200,100);

		g2.setColor(Color.black);
		g2.drawRect(100,300,100,100);
		g2.setColor(Color.red);
		g2.drawOval(200,300,100,100);
		g2.setColor(Color.blue);
		g2.drawOval(300,300,200,100);

		g2.setColor(new Color(255,127,127));
		g2.fillRect(20,20,100,1);
		g2.setColor(Color.red);
		g2.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
		g2.drawString("Hello, World",20,20);
	}
```

これで四角・丸・文字が描画できるようになります

まず、ウィンドウのグラフィックを扱うためのオブジェクトをGraphics2D型のg2に代入します

このg2のメソッドを使うことでウィンドウにいろいろと描画することができるようになります

---

g2.setColorは描画色を変更します

RGBを指定する方法とデフォルトで用意されている色を使う方法があり、両方使えます

---

g2.drawRect(x,y,w,h)は、(x,y)と(x+w,y+h)を対角とする長方形を描画します

g2.fillRect(x,y,w,h)もあります

違いは中身を塗るか塗らないかです。fillするかfillしないか、です

ここで二次元座標は、ウィンドウの左上の端を(0,0)、右下の端を(800,600)としています

800,600という数字はウィンドウを生成した時に指定しています

そこを変更するとウィンドウサイズも座標の右下も変わります

---

g2.drawOval(x,y,w,h)は、(x,y)と(x+w,y+h)を対角とする長方形に内接する楕円を描画します

こちらもg2.fillOval(x,y,w,h)が存在します

原点(cx,cy)、半径rの円を描画したいときは、g2.drawOval(cx-r,cy-r,2\*r,2\*r)とします

---

g2.drawString(text,x,y)は文字列を描画します

g2.setFont(new Font(Font.SERIF, Font.PLAIN, size))としてからやるとフォントサイズを変更してから描画できます

また上の例では(x,y)の位置が分かりやすいように補助線を引いていますが、お分かりの通り(x,y)は文字列のベースラインを表しています

表示位置が予想外のところになるかもしれませんがそこは気をつけましょう

---

これで、とりあえず「画面に何か描画する」ということができるようになりました

## 動かしてみる

おもむろにこうしてみましょう

```java
package gameprog;

import java.awt.*;
import javax.swing.*;

public class Main {
	public static void main(String[] args){
		(new Main()).run(); // non-static
	}
	public JFrame fr;
	public void run(){
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

		// 無限ループ
		while(true){
			long beg = System.nanoTime();
			// ここがメインの処理！
			move();
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
	// この下を頻繁に切り貼りします
	public int x = 0;				// 🍣追加🍣
	public void move(){
		Graphics2D g2 = (Graphics2D)fr.getContentPane().getGraphics();
		g2.setColor(Color.black);
		g2.fillRect(x,100,100,100);
		x += 3;
	}
}
```

画面がちょっと動くようになりました！

でもなんか黒い物体が伸びるだけですね……

これは「画面に黒い正方形を書く」→「画面に黒い正方形を書く」→……

と、ただ落書きをガンガン上書きしているからこうなるのです

そのため、アニメーションの1コマができたら、一回画面を真っ白にリセットしてあげると良いです


```java
	// この下を頻繁に切り貼りします
	public int x = 0;
	public void move(){
		Graphics2D g2 = (Graphics2D)fr.getContentPane().getGraphics();
		g2.setColor(Color.white);				// 🍣追加🍣
		g2.fillRect(0,0,800,600);				// 🍣追加🍣
		g2.setColor(Color.black);
		g2.fillRect(x,100,100,100);
		x += 3;
	}
```

基本的にはこうやってアニメーションさせることになります

これをいろいろな変数を使っていろいろな画像や物体を描画すれば、ゲームになります！

## 画像描画

ところでちょっとカクカクしますね……それはさておき

画像を描画してみましょう

画像を描画できればおよそ任意のゲーム画面が表現できるようになります

まず、以下のD言語くんの画像を保存してください(右クリック→名前をつけて保存)

![D言語くん](d3.png)

次にこれをEclipseのプロジェクトに取り込みます

1. プロジェクトエクスプローラーのsrcを選択、右クリック → インポート
1. 一般 の中の ファイル・システム を選択
1. 参照 をクリックしてd3.pngを保存したフォルダを選択
1. 右側の窓からd3.pngを選択
1. 完了

これで、以下の様なファイル構造になればOKです

![ファイル構造2](scs-base-2.png)

ではこの上で以下のようなコードを書いてみましょう

```java
package gameprog;

import java.awt.*;
import javax.swing.*;

import java.io.File;					// 🍣追加🍣
import javax.imageio.ImageIO;			// 🍣追加🍣

public class Main {
	public static void main(String[] args){
		(new Main()).run();
	}
	public JFrame fr;
	public Image dman;					// 🍣追加🍣
	public void run(){
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

		// 🍣追加🍣
		try{
			// このtryの中で画像を読み込む
			dman = ImageIO.read(new File("src/d3.png"));
		}catch(Exception e){
			e.printStackTrace();
		}

		// 無限ループ
		while(true){
			long beg = System.nanoTime();
			// ここがメインの処理！
			move();
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
	// この下を頻繁に切り貼りします
	public void move(){
		Graphics2D g2 = (Graphics2D)fr.getContentPane().getGraphics();
		int w = dman.getWidth(fr);
		int h = dman.getHeight(fr);
		g2.drawImage(dman,0,0,w,h,fr);
	}
}
```

D言語くん(d3.png)がウィンドウに表示されると思います

画像はImage型で保存しておきます

これは無限ループで毎回ファイルから読み込む必要はなく、一度だけ読みこめばいくらでも再利用できるので、ループの前に代入してしまいます

try-catchで書いています。これについて説明すると時間が足りないので「こういうもの」と考えてください

さて、Image型に画像を読み込んだら、Graphics2Dのメソッドにそれを渡すことで描画ができます

g2.drawImage(img,x,y,w,h,fr) で、imgの画像をdrawRect同様に描画します。

なお、wとhが元画像の比と違う場合、引き伸ばされます。hの代わりに2*hするとD言語くんが細長く表示されます。

## チカチカする問題の解決

さて、チカチカする問題に戻りましょう

なんでチカチカするかというと、たとえ高速で描画していると言っても、プレイヤーが見ている前で「画面を白塗り」→「物体を描画」→「物体を描画」→「16ミリ秒待機」をやっていると？

「画面を白塗り」した瞬間が見えたりしちゃうわけですね

そこで、「画面を白塗り」→「物体を描画」→「物体を描画」の部分を**隠して**、その変更をあとでまとめてウィンドウに描画すればよさそうですね

今回は、BufferedImageという書き込める画像型の変数を用意しておいて、そこにウィンドウに書き込む代わりにその画像変数に書き込むようにします

そして、すべての書き込みが終わった後で、その画像をウィンドウに書き込みましょう

```java
package gameprog;

import java.awt.*;
import javax.swing.*;

import java.io.File;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;			// 🍣追加🍣

public class Main {
	public static void main(String[] args){
		(new Main()).run();
	}
	public JFrame fr;
	public BufferedImage buf;					// 🍣追加🍣
	public Image dman;
	public void run(){
		// 🍣追加🍣
		buf = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);

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

		try{
			// このtryの中で画像を読み込む
			dman = ImageIO.read(new File("src/d3.png"));
		}catch(Exception e){
			e.printStackTrace();
		}

		// 無限ループ
		while(true){
			long beg = System.nanoTime();
			Graphics2D g2 = (Graphics2D)buf.getGraphics();				// 🍣追加🍣
			g2.setColor(Color.white);									// 🍣追加🍣
			g2.fillRect(0,0,800,600);									// 🍣追加🍣
			move();
			g2 = (Graphics2D)fr.getContentPane().getGraphics();			// 🍣追加🍣
			g2.drawImage(buf,0,0,fr);									// 🍣追加🍣
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
	// この下を頻繁に切り貼りします
	public int x = 0;
	public void move(){
		Graphics2D g2 = (Graphics2D)buf.getGraphics();					// 🍣変更🍣
		int w = dman.getWidth(fr);
		int h = dman.getHeight(fr);
		g2.drawImage(dman,x,0,w,h,fr);								// 動かすように
		x += 3;
	}
}
```

はい、どうでしょうか

なめらかに動きますかね？

bufという変数に仮の画面を表す画像を用意しておきます

その変数からgetGraphics()でGraphics2D変数を取るようにするだけで、ウィンドウ同様にbufに書き込めます

そして、move()を呼び出した後に、ウィンドウのGraphics2Dを取って、bufをまとめて描画します

こうすることで作業途中の画面を見せることがなくなるため、チカチカがなくなり、なめらかになります

この技法を**ダブルバッファリング**と呼びます

## キー入力

おまじないです

```java
package gameprog;

import java.awt.*;
import javax.swing.*;

import java.io.File;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.awt.event.*;								// 🍣追加🍣

public class Main {
	public static void main(String[] args){
		(new Main()).run();
	}
	public JFrame fr;
	public BufferedImage buf;
	public boolean[] keybef, keynow, keynext;		   // 🍣追加🍣
	public Image dman;
	public void run(){
		buf = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);

		// 🍣追加🍣
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
		fr.addKeyListener(new keyclass());			  // 🍣追加🍣

		try{
			// このtryの中で画像を読み込む
			dman = ImageIO.read(new File("src/d3.png"));
		}catch(Exception e){
			e.printStackTrace();
		}

		// 無限ループ
		while(true){
			long beg = System.nanoTime();
			for(int i=0;i<256;++i){		 // 🍣追加🍣
				keybef[i] = keynow[i];	  // 🍣追加🍣
				keynow[i] = keynext[i];	 // 🍣追加🍣
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

	// 🍣追加🍣
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

	// この下を頻繁に切り貼りします
	public void move(){
		// 🍣変更🍣
		Graphics2D g2 = (Graphics2D)buf.getGraphics();
		if(onPressed(KeyEvent.VK_Z)){
			g2.drawImage(dman, 0, 0, dman.getWidth(fr), dman.getHeight(fr), fr);
		}
		if(isPressed(KeyEvent.VK_X)){
			g2.setColor(Color.blue);
			g2.fillRect(300, 100, 100, 400);
		}
	}
}
```

簡単に説明すると、ウィンドウのキーリスナーに自作クラスを登録して、キーイベントを受け取るようにしています。

受け取ったキーイベントを配列に格納し、前の状態と合わせて持つことで、「今押されているか」「今この瞬間押されたか」を判定できるようになります

小難しいことなしに、このように追記すると 

- isPressed(KeyEvent.VK_Z)でZキーが押されているかどうか
- onPressed(KeyEvent.VK_ENTER)で今エンターキーが押されたかどうか

が取得できます。これを用いることでプレイヤーがゲームを操作できるようになります

## まとめ

このパートではついにJavaのプログラムでウィンドウの表示、無限ループによる処理の継続、様々なものの描画ができるようになりました。

次はついに、ゲームを作っていきたいと思います

以下に現在の全体のコードを載せます

```java
package gameprog;

import java.awt.*;
import javax.swing.*;

import java.io.File;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.awt.event.*;

public class Main {
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
	
	// この下を頻繁に切り貼りします
	public int x = 0;
	public void move(){
		if(isPressed(KeyEvent.VK_Z)){
			x -= 3;
		}
		if(isPressed(KeyEvent.VK_X)){
			x += 3;
		}
		Graphics2D g2 = (Graphics2D)buf.getGraphics();
		int w = dman.getWidth(fr);
		int h = dman.getHeight(fr);
		g2.drawImage(dman,x,0,w,h,fr);

		g2.setColor(new Color(255,0,0));
		g2.fillRect(10,100,100,20);
		
		g2.setColor(Color.blue);
		g2.drawOval(140,40,20,20);
		
		g2.setColor(Color.green);
		g2.setFont(new Font(Font.SERIF, Font.PLAIN, 36));
		g2.drawString("D言語くん in Java",400,600);
	}
}
```

## ゲームの基礎の基礎 終わり
