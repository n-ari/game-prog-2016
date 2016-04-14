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
