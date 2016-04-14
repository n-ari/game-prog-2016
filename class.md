# クラス

## クラスの機能

複数の変数とメソッドを、1つの変数にまとめられる

## 例

```java
class Sushi {
	public String sushiName;
	public int cost;
	public int createdAt;
	public int yummy;
	public Sushi(String name, int cost, int time, int yummy){
		this.sushiName = name;
		this.cost = cost;
		this.createdAt = time;
		this.yummy = yummy;
	}
}
class Main {
	public static void main(String[] args){
		(new Main()).run();
	}
	public void run(){
		Sushi maguro = new Sushi("maguro",108,19250225,100);

		System.out.println(getCostPerformance(maguro));
	}
	public float getCostPerformance(Sushi s){
		return (float)(s.yummy)/(float)(s.cost);
	}
}
```

今まで書いてきたMainと果てしなく似てる……

## 何が出来る？

- 一つのオブジェクト(物体)を一つの変数で書ける
- →コードがスッキリする

## 使わないと

```java
class Main {
	public static void main(String[] args){
		(new Main()).run();
	}
	public void run(){
		String maguro_sushiName = "maguro";
		int maguro_cost = 108;
		int maguro_createdAt = 19250225;
		int maguro_yummy = 100;

		System.out.println(getCostPerformance(maguro_cost, maguro_yummy));
	}
	public float getCostPerformance(int cost, int yummy){
		return (float)(yummy)/(float)(cost);
	}
}
```

1つの物体を管理するのに複数の変数を使わなきゃいけないし、メソッドでの受け渡しも煩雑になる

→バグの温床になる！

## クラスを宣言

class Main ~ は、実はクラスの宣言だった

それと同じ感じで書けばクラスになる

クラスの中では変数宣言とメソッド宣言ができる(Mainと同じ！)

```java
class Sushi {
	public String sushiName;
	public int cost, yummy;
	public float getCostPerformance(){
		return (float)(yummy)/(float)(cost);
	}
	public void changeName(String newName){
		this.sushiName = newName;
	}
}
```

なるほど、書きたい放題だ

ただし、Javaは「1クラス1ファイル」という原則がある

そのため、Sushiというクラスを作る場合は、Sushi.javaというファイルを作って、そこにSushiクラスを書く必要がある

## オブジェクトを生成する

```java
	// class Sushi が定義されているとする
	Sushi mysushi = new Sushi();
```

クラス名 変数名 = new クラス名(コンストラクタの引数...);

とすると、クラスを使ったオブジェクトが生成される

オブジェクトの中身には . (ドット、コロン)を使ってアクセスする

```java
	// class Sushi, Sushi.cost が定義されているとする
	System.out.println(mysushi.cost);
```

これが基本的な使い方

## コンストラクタ

クラスを使ってオブジェクトを生成した時に呼び出されるメソッド

初期化処理と言っても良い

コンストラクタはクラス内に「クラス名と同じ名前のメソッド」を「返り値の型無し」で書く

```java
class Hello {								 // <- Hello クラス
	public int x;
	public Hello(String say){	 // <- Hello クラスだから Hello という名前のメソッドを書くと、コンストラクタになる
		System.out.println("Hello, "+say);
		x = 3;
	}
}
class Main {
	public static void main(String[] args){
		Hello w = new Hello("World!");		// コンストラクタの引数に "World!" を渡している
		// ↑の文が実行されるとHelloクラスのコンストラクタが呼び出される
		System.out.println(w.x);
	}
}
```

実行結果は以下のとおり

```
Hello, World!
3
```

## アクセス修飾子

public とか private とか protected とか

これはアクセスを制限するもの

例えば

```java
class Usamin {
	public int age;
	private int truthAge;
	public Usamin(){
		// 初期化処理
	}
}
class Main {
	public static void main(String[] args){
		Usamin usa = new Usamin();
		System.out.println(usa.age);			// 出来る
		System.out.println(usa.truthAge); // ダメーーーー！！！！
	}
}
```

これの嬉しいところは、「意図しないところで値が変更されたりしない」ことを保証できること

大規模なプロジェクトになると意識する必要があるけど、超短期間で作るなら**とりあえず全部public付ければ良い**

## static

static とは 静的メンバ をあらわす修飾子

「クラスに対して固有のもの」を表す

例えば、クラスを使っていくつもオブジェクトを作ると、オブジェクト一つ一つがクラスの変数を持つが、

クラス内にstaticな変数があると、そのクラスの全てのオブジェクトが1つの変数を参照することになる

使えるところでは便利だけどとりあえず今回はカット

## 継承・オーバーライド

これがクラスのすごいところ

まずはコードを見よう

```java
class Sushi {
	public int cost;
	private int id;
	public Sushi(){
		cost = 108;
		id = 30;
	}
	public String getSushiName(){
		return "ただの寿司";
	}
}
class Maguro extends Sushi {
	public Maguro(){
		super();
		this.cost = 216;
		// System.out.println(this.id);	 // -> Error!
	}
	public String getSushiName(){
		return "マグロ";
	}
	public String getKanjiName(){
		return "鮪";
	}
}

class Main {
	public static void main(String[] args){ (new Main()).run(); }
	public void run(){
		Sushi s1 = new Sushi();	 // Sushi型の変数s1にSushiクラスのオブジェクトを代入
		Sushi s2 = new Maguro();	// Sushi型の変数s2にMaguroクラスのオブジェクトを代入
		Maguro m = new Maguro();	// Maguro型の変数mにMaguroクラスのオブジェクトを代入
		System.out.println(s1.getSushiName());	// -> "ただの寿司"
		System.out.println(s2.getSushiName());	// -> "マグロ"
		System.out.println(m.getSushiName());	 // -> "マグロ"

		System.out.println(s1.cost);						// -> 108
		System.out.println(s2.cost);						// -> 216
		System.out.println(m.cost);						 // -> 216

		System.out.println(m.getKanjiName());	 // -> "鮪"

		// System.out.println(s2.getKanjiName());	// -> Error
	}
}
```

---

- 継承したクラスのコンストラクタ内では親クラスのコンストラクタを呼ぶということが必要
- そのために super というメソッドが存在し、それは親クラスのコンストラクタそのもの

---

- 継承すると親クラスの(public または protected)な変数・メソッドにアクセスできる
- private はダメ
- いろいろなものを受け継げるので、共通する要素を親クラスにまとめて、それを継承させるようにすると、コードの量が減る

---

- Sushi型の変数にはSushiクラスを継承したクラスのオブジェクトが代入できる！ (重要)
- Sushi型の変数からはSushiクラスの変数・メソッドが触れる
- Sushi型にMaguroクラスのオブジェクトを入れても、getKanjiNameメソッドは使えない (重要)
- Maguro型にMaguroクラスのオブジェクトを入れればgetKanjiNameメソッドが使える

---

- MaguroクラスはSushiクラスを継承しているため、getSushiNameメソッドが元々あるが、それを「上書き」するかのように再宣言できる
- (↑これを「メソッドのオーバーライド」(覆い隠すこと)と呼ぶ)
- これを使うと、同じ型の変数でもメソッドの挙動が違う、ということが実現できる！ (超重要)

## まとめ

- クラスを使うと変数・メソッドをまとめられる
- クラスはString型等と同様に扱えて変数の型にできる
- クラスは継承ができる
- 継承すると親クラスから受け継いだりできる
- 継承すると親クラスの型の変数に代入できたりする

プログラミング入門に比べて一気に抽象度が上がってしまい、混乱の種となることが多いです

ここらへんはいわゆる慣れの問題が大きく、実際に書いてみて理解できる、ということもあると思います

## クラスの説明 終わり
