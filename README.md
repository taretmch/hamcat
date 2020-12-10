# プログラマーのための圏論 Scala 版

本プロジェクトは、Bartosz Milewski 氏著 [Category Theory for Programmers - Scala Edition](https://github.com/hmemcpy/milewski-ctfp-pdf) を翻訳し、読みながら作成した勉強記録です。

ドキュメントの章構成は基本的に原文に則っていますが、省略している箇所もあります。なお、非公式な翻訳であり、個人的な解釈を含む表現もあるため、閲覧の際はそれらの点に留意してお読みください。

内容や表現が間違っている箇所、修正した方が良い箇所、修正すればよりわかりやすくなる箇所等ありましたら、プルリクエストにて提案いただけると助かります。

# ディレクトリ構成

本プロジェクトは以下のディレクトリ構成を持ちます。

- docs/src/
  - main/ : 圏論に関するドキュメント
  - drafts/ : ドキュメントの下書き
- src/main/scala/
  - data/ : データ構造を定義したパッケージ
    - instance/ : データ構造のインスタンスを定義したパッケージ
  - syntax/ : データ構造の文法を定義したパッケージ
  - universal/ : 普遍性の構造を定義したパッケージ

# 使い方

まだ作成中のプロジェクトであり、maven 上での公開等はしておりません。

コードを動かしたい場合、本プロジェクトをクローンして、ローカル環境にてお使いください。