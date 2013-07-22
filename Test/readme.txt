・VerifyConvert およびVerifyConvertAll の使い方

・VerifyConvert

./Test/VerifyConvert [(r | ri)] [イメージファイル] [udfv の引数]

指定したイメージファイルをXML (1)化し、そのXML をもとにイメージ(2)を作り、
さらにそのイメージをXML(3) 化し、(1) と(3) のXML と比較し、
その結果を標準出力に出力する。

(1)は、イメージファイル.cmpxml1 という名前でカレントディレクトリに生成する。
(2)は、イメージファイル.cmpimg という名前でカレントディレクトリに生成する。
(3)は、イメージファイル.cmpxml2 という名前でカレントディレクトリに生成する。

第2引数にr を指定すると、比較結果を標準出力に出力した後、
(1)〜(3) のファイルを全て削除する。
第2引数にri を指定すると、比較結果を標準出力に出力した後、
(2) のイメージのみを削除する。

(3) のXML は(2) のイメージから生成されるため、通常はsrc-file となる
イメージファイルの値が異なるが、(3) ではこの値はオリジナルのイメージファイル名に
修正されるため、この部分に関しての差異が比較結果に出力されることはない。


例：
./Test/VerifyConvertAll /export6/verifyimg/udf34/T0000.IMG



・VerifyConvertAll

./Test/VerifyConvertAll [ディレクトリ] [udfv の引数]

指定したディレクトリ直下にある、
拡張子IMG のファイル全てに対してVerifyConvert を行う。
イメージファイルが大量に生成されるため、
VerifyConvertAll では(1) と(3) のXML ファイルのみ生成し、
(2) のイメージは生成後自動的に削除する。

また、イメージファイル数が大量にあると非常に時間がかかるため、
通常はudfv の引数として'-filecopy false' を指定する。


例：
./Test/VerifyConvertAll /export6/verifyimg/udf34/ -filecopy false



