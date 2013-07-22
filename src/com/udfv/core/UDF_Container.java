/*
   Copyright 2013 Heart Solutions Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
/*

*/
package com.udfv.core;

import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;

/**
<dl>
<dt>概要</dt>
<dd>
コンテナを表わすクラス
</dd>
<dt>詳細</dt>
<dd>
<p>
UDF_SimpleContainerはそれ自身は何もしないが、内部に任意の子供を持つことができる。
UDF_bytesと違い、バイトデータを保持しないので、メモリを節約できる。
</p>
*/

public interface UDF_Container
{
    ;
}
