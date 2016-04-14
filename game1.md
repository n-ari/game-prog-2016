# とりあえず何か動くものを作ろう

## 今回作るゲーム

今回は

- プレイヤーはD言語くんを動かす
- ウィンドウの周りから弾が飛んでくる
- 避ける
- あたったらゲームオーバー

ということ

## プレイヤーが動くところを作ろう

前のコードの、moveの中身をいじることにする

```java
	public int x = 400,y = 300;
	public void move(){
		int v = 6;
		int w = dman.getWidth(fr)/4;
		int h = dman.getHeight(fr)/4;
		if(isPressed(KeyEvent.VK_RIGHT)){
			x += v;
		}
		if(isPressed(KeyEvent.VK_LEFT)){
			x -= v;
		}
		if(isPressed(KeyEvent.VK_DOWN)){
			y += v;
		}
		if(isPressed(KeyEvent.VK_UP)){
			y -= v;
		}
		// 画面外に出さない
		if(x>800-w) x = 800-w;
		if(x<0) x = 0;
		if(y>600-h) y = 600-h;
		if(y<0) y = 0;
		Graphics2D g2 = (Graphics2D)buf.getGraphics();
		g2.drawImage(dman,x,y,w,h,fr);
	}
```

とりあえず、D言語くんが動きます！！

// 画面外に出さない というところの4つのif文は、画面の外に出そうになったら、画面内に収めるように処理です

## タイマーを作ろう

今回はひたすら避けるゲームのため、タイマーを作ろう

```java
	public int x = 400,y = 300;
	public int time = 0;
	public void move(){
		// (中略)

		time ++;
		float sec = (float)time/60f;
		g2.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
		g2.setColor(Color.black);
		g2.drawString(String.format("%.4f秒",sec),0,25);
	}
```

time という変数を定義し、move一回につきtimeの値が1増えます

今回は60FPS、1秒に60回moveが呼ばれるため、timeが60増えるごとに1秒という計算になります

つまり、60で割ると、経過した秒数が得られます

また、String.formatというメソッドでは、変数を*いい感じに*整形してくれます

今回は、%.4fという部分で、float型の変数の値を小数点以下4桁で文字列に展開、ということをしています

## タイトル画面とリザルト画面とシーン遷移と

ゲームって、タイトル画面から始まって、ゲームをやって、リザルトを見て、タイトルに戻る、って流れがあるとゲームっぽさが出ると思うんですよ

ということで作りましょう

``` java
	public int x = 400,y = 300;
	public int time = 0;
	// state 0:Title 1:Game 2:Result
	public int state = 0;			 // 追加
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
			}
		}else if(state == 1){
			int v = 6;
			int w = dman.getWidth(fr)/4;
			int h = dman.getHeight(fr)/4;
			if(isPressed(KeyEvent.VK_RIGHT)){
				x += v;
			}
			if(isPressed(KeyEvent.VK_LEFT)){
				x -= v;
			}
			if(isPressed(KeyEvent.VK_DOWN)){
				y += v;
			}
			if(isPressed(KeyEvent.VK_UP)){
				y -= v;
			}
			// 画面外に出さない
			if(x>800-w) x = 800-w;
			if(x<0) x = 0;
			if(y>600-h) y = 600-h;
			if(y<0) y = 0;
			g2.drawImage(dman,x,y,w,h,fr);

			time ++;
			float sec = (float)time/60f;
			g2.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
			g2.setColor(Color.black);
			g2.drawString(String.format("%.5f秒",sec),0,25);

			// 今のところ便宜的にXキーを押すとゲームオーバーとする
			if(onPressed(KeyEvent.VK_X)){
				state = 2;
			}
		}else if(state == 2){
			g2.setColor(Color.black);
			g2.setFont(new Font(Font.SERIF, Font.PLAIN, 32));
			g2.drawString("ゲームオーバー",30,100);

			float sec = (float)time/60f;
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
```

