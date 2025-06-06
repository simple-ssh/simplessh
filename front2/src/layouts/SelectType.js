 import React from 'react'

 export const optionType=()=>{
  return(<>
  <option value="">Select type</option>
  <option title="A 4-byte integer, signed range is -2,147,483,648 to 2,147,483,647, unsigned range is 0 to 4,294,967,295" value="INT">INT</option>
  <option title="A variable-length (0-65,535) string, the effective maximum length is subject to the maximum row size" value="VARCHAR">VARCHAR</option>
  <option title="A TEXT column with a maximum length of 65,535 (2^16 - 1) characters, stored with a two-byte prefix indicating the length of the value in bytes" value="TEXT">TEXT</option>
  <option title="A date, supported range is 1000-01-01 to 9999-12-31" value="DATE">DATE</option>
  <optgroup label="Numeric">
      <option title="A 1-byte integer, signed range is -128 to 127, unsigned range is 0 to 255" value="TINYINT">TINYINT</option>
      <option title="A 2-byte integer, signed range is -32,768 to 32,767, unsigned range is 0 to 65,535" value="SMALLINT">SMALLINT</option>
      <option title="A 3-byte integer, signed range is -8,388,608 to 8,388,607, unsigned range is 0 to 16,777,215" value="MEDIUMINT">MEDIUMINT</option>
      <option title="A 4-byte integer, signed range is -2,147,483,648 to 2,147,483,647, unsigned range is 0 to 4,294,967,295" value="INT">INT</option>
      <option title="An 8-byte integer, signed range is -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807, unsigned range is 0 to 18,446,744,073,709,551,615" value="BIGINT">BIGINT</option>
      <option disabled="disabled">-</option>
      <option title="A fixed-point number (M, D) - the maximum number of digits (M) is 65 (default 10), the maximum number of decimals (D) is 30 (default 0)" value="DECIMAL">DECIMAL</option>
      <option title="A small floating-point number, allowable values are -3.402823466E+38 to -1.175494351E-38, 0, and 1.175494351E-38 to 3.402823466E+38" value="FLOAT">FLOAT</option>
      <option title="A double-precision floating-point number, allowable values are -1.7976931348623157E+308 to -2.2250738585072014E-308, 0, and 2.2250738585072014E-308 to 1.7976931348623157E+308" value="DOUBLE">DOUBLE</option>
      <option title="Synonym for DOUBLE (exception: in REAL_AS_FLOAT SQL mode it is a synonym for FLOAT)" value="REAL">REAL</option>
      <option disabled="disabled">-</option>
      <option title="A bit-field type (M), storing M of bits per value (default is 1, maximum is 64)" value="BIT">BIT</option>
      <option title="A synonym for TINYINT(1), a value of zero is considered false, nonzero values are considered true" value="BOOLEAN">BOOLEAN</option>
      <option title="An alias for BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE" value="SERIAL">SERIAL</option>
  </optgroup>
  <optgroup label="Date and time">
       <option title="A date, supported range is 1000-01-01 to 9999-12-31" value="DATE">DATE</option>
       <option title="A date and time combination, supported range is 1000-01-01 00:00:00 to 9999-12-31 23:59:59" value="DATETIME">DATETIME</option>
       <option title="A timestamp, range is 1970-01-01 00:00:01 UTC to 2038-01-09 03:14:07 UTC, stored as the number of seconds since the epoch (1970-01-01 00:00:00 UTC)" value="TIMESTAMP">TIMESTAMP</option>
       <option title="A time, range is -838:59:59 to 838:59:59" value="TIME">TIME</option>
       <option title="A year in four-digit (4, default) or two-digit (2) format, the allowable values are 70 (1970) to 69 (2069) or 1901 to 2155 and 0000" value="YEAR">YEAR</option>
  </optgroup>
  <optgroup label="String">
       <option title="A fixed-length (0-255, default 1) string that is always right-padded with spaces to the specified length when stored" value="CHAR">CHAR</option>
       <option  title="A variable-length (0-65,535) string, the effective maximum length is subject to the maximum row size" value="VARCHAR">VARCHAR</option>
       <option disabled="disabled">-</option>
       <option title="A TEXT column with a maximum length of 255 (2^8 - 1) characters, stored with a one-byte prefix indicating the length of the value in bytes" value="TINYTEXT">TINYTEXT</option>
       <option title="A TEXT column with a maximum length of 65,535 (2^16 - 1) characters, stored with a two-byte prefix indicating the length of the value in bytes" value="TEXT">TEXT</option>
       <option title="A TEXT column with a maximum length of 16,777,215 (2^24 - 1) characters, stored with a three-byte prefix indicating the length of the value in bytes" value="MEDIUMTEXT">MEDIUMTEXT</option>
       <option title="A TEXT column with a maximum length of 4,294,967,295 or 4GiB (2^32 - 1) characters, stored with a four-byte prefix indicating the length of the value in bytes" value="LONGTEXT">LONGTEXT</option>
       <option disabled="disabled">-</option><option title="Similar to the CHAR type, but stores binary byte strings rather than non-binary character strings" value="BINARY">BINARY</option>
       <option title="Similar to the VARCHAR type, but stores binary byte strings rather than non-binary character strings" value="VARBINARY">VARBINARY</option>
       <option disabled="disabled">-</option>
       <option title="A BLOB column with a maximum length of 255 (2^8 - 1) bytes, stored with a one-byte prefix indicating the length of the value" value="TINYBLOB">TINYBLOB</option>
       <option title="A BLOB column with a maximum length of 16,777,215 (2^24 - 1) bytes, stored with a three-byte prefix indicating the length of the value" value="MEDIUMBLOB">MEDIUMBLOB</option>
       <option title="A BLOB column with a maximum length of 65,535 (2^16 - 1) bytes, stored with a two-byte prefix indicating the length of the value" value="BLOB">BLOB</option>
       <option title="A BLOB column with a maximum length of 4,294,967,295 or 4GiB (2^32 - 1) bytes, stored with a four-byte prefix indicating the length of the value" value="LONGBLOB">LONGBLOB</option>
       <option disabled="disabled">-</option>
       <option title="An enumeration, chosen from the list of up to 65,535 values or the special '' error value" value="ENUM">ENUM</option>
       <option title="A single value chosen from a set of up to 64 members" value="SET">SET</option>
  </optgroup>
  <optgroup label="Spatial">
       <option title="A type that can store a geometry of any type" value="GEOMETRY">GEOMETRY</option>
       <option title="A point in 2-dimensional space" value="POINT">POINT</option>
       <option title="A curve with linear interpolation between points" value="LINESTRING">LINESTRING</option>
       <option title="A polygon" value="POLYGON">POLYGON</option>
       <option title="A collection of points" value="MULTIPOINT">MULTIPOINT</option>
       <option title="A collection of curves with linear interpolation between points" value="MULTILINESTRING">MULTILINESTRING</option>
       <option title="A collection of polygons" value="MULTIPOLYGON">MULTIPOLYGON</option>
       <option title="A collection of geometry objects of any type" value="GEOMETRYCOLLECTION">GEOMETRYCOLLECTION</option>
  </optgroup>
  <optgroup label="JSON">
         <option title="Stores and enables efficient access to data in JSON (JavaScript Object Notation) documents" value="JSON">JSON</option>
  </optgroup>
</>)
}

export const optionCollation=()=>{
  return(<>

       <option value=""></option>
       <optgroup label="armscii8" title="ARMSCII-8 Armenian">
       <option value="armscii8_bin" title="Armenian, Binary" value="armscii8_bin">armscii8_bin</option>
       <option value="armscii8_general_ci" title="Armenian, case-insensitive" value="armscii8_general_ci">armscii8_general_ci</option>
       </optgroup>
       <optgroup label="ascii" title="US ASCII">
       <option value="ascii_bin" title="West European (multilingual), Binary" value="ascii_bin">ascii_bin</option>
       <option value="ascii_general_ci" title="West European (multilingual), case-insensitive" value="ascii_general_ci">ascii_general_ci</option>
       </optgroup>
       <optgroup label="big5" title="Big5 Traditional Chinese">
       <option value="big5_bin" title="Traditional Chinese, Binary" value="big5_bin">big5_bin</option>
       <option value="big5_chinese_ci" title="Traditional Chinese, case-insensitive" value="big5_chinese_ci">big5_chinese_ci</option>
       </optgroup>
       <optgroup label="binary" title="Binary pseudo charset">
       <option value="binary" title="Binary" value="binary">binary</option>
       </optgroup>
       <optgroup label="cp1250" title="Windows Central European">
       <option value="cp1250_bin" title="Central European (multilingual), Binary" value="cp1250_bin">cp1250_bin</option>
       <option value="cp1250_croatian_ci" title="Croatian, case-insensitive" value="cp1250_croatian_ci">cp1250_croatian_ci</option>
       <option value="cp1250_czech_cs" title="Czech, case-sensitive" value="cp1250_czech_cs">cp1250_czech_cs</option>
       <option value="cp1250_general_ci" title="Central European (multilingual), case-insensitive" value="cp1250_general_ci">cp1250_general_ci</option>
       <option value="cp1250_polish_ci" title="Polish, case-insensitive" value="cp1250_polish_ci">cp1250_polish_ci</option>
       </optgroup>
       <optgroup label="cp1251" title="Windows Cyrillic">
       <option value="cp1251_bin" title="Cyrillic (multilingual), Binary" value="cp1251_bin">cp1251_bin</option>
       <option value="cp1251_bulgarian_ci" title="Bulgarian, case-insensitive" value="cp1251_bulgarian_ci">cp1251_bulgarian_ci</option>
       <option value="cp1251_general_ci" title="Cyrillic (multilingual), case-insensitive" value="cp1251_general_ci">cp1251_general_ci</option>
       <option value="cp1251_general_cs" title="Cyrillic (multilingual), case-sensitive" value="cp1251_general_cs">cp1251_general_cs</option>
       <option value="cp1251_ukrainian_ci" title="Ukrainian, case-insensitive" value="cp1251_ukrainian_ci">cp1251_ukrainian_ci</option>
       </optgroup>
       <optgroup label="cp1256" title="Windows Arabic">
       <option value="cp1256_bin" title="Arabic, Binary" value="cp1256_bin">cp1256_bin</option>
       <option value="cp1256_general_ci" title="Arabic, case-insensitive" value="cp1256_general_ci">cp1256_general_ci</option>
       </optgroup>
       <optgroup label="cp1257" title="Windows Baltic">
       <option value="cp1257_bin" title="Baltic (multilingual), Binary" value="cp1257_bin">cp1257_bin</option>
       <option value="cp1257_general_ci" title="Baltic (multilingual), case-insensitive" value="cp1257_general_ci">cp1257_general_ci</option>
       <option value="cp1257_lithuanian_ci" title="Lithuanian, case-insensitive" value="cp1257_lithuanian_ci">cp1257_lithuanian_ci</option>
       </optgroup>
       <optgroup label="cp850" title="DOS West European">
       <option value="cp850_bin" title="West European (multilingual), Binary" value="cp850_bin">cp850_bin</option>
       <option value="cp850_general_ci" title="West European (multilingual), case-insensitive" value="cp850_general_ci">cp850_general_ci</option>
       </optgroup>
       <optgroup label="cp852" title="DOS Central European">
       <option value="cp852_bin" title="Central European (multilingual), Binary" value="cp852_bin">cp852_bin</option>
       <option value="cp852_general_ci" title="Central European (multilingual), case-insensitive" value="cp852_general_ci">cp852_general_ci</option>
       </optgroup>
       <optgroup label="cp866" title="DOS Russian">
       <option value="cp866_bin" title="Russian, Binary" value="cp866_bin">cp866_bin</option>
       <option value="cp866_general_ci" title="Russian, case-insensitive" value="cp866_general_ci">cp866_general_ci</option>
       </optgroup>
       <optgroup label="cp932" title="SJIS for Windows Japanese">
       <option value="cp932_bin" title="Japanese, Binary" value="cp932_bin">cp932_bin</option>
       <option value="cp932_japanese_ci" title="Japanese, case-insensitive" value="cp932_japanese_ci">cp932_japanese_ci</option>
       </optgroup>
       <optgroup label="dec8" title="DEC West European">
       <option value="dec8_bin" title="West European (multilingual), Binary" value="dec8_bin">dec8_bin</option>
       <option value="dec8_swedish_ci" title="Swedish, case-insensitive" value="dec8_swedish_ci">dec8_swedish_ci</option>
       </optgroup>
       <optgroup label="eucjpms" title="UJIS for Windows Japanese">
       <option value="eucjpms_bin" title="Japanese, Binary" value="eucjpms_bin">eucjpms_bin</option>
       <option value="eucjpms_japanese_ci" title="Japanese, case-insensitive" value="eucjpms_japanese_ci">eucjpms_japanese_ci</option>
       </optgroup>
       <optgroup label="euckr" title="EUC-KR Korean">
       <option value="euckr_bin" title="Korean, Binary" value="euckr_bin">euckr_bin</option>
       <option value="euckr_korean_ci" title="Korean, case-insensitive" value="euckr_korean_ci">euckr_korean_ci</option>
       </optgroup>
       <optgroup label="gb18030" title="China National Standard GB18030">
       <option value="gb18030_bin" title="unknown, Binary" value="gb18030_bin">gb18030_bin</option>
       <option value="gb18030_chinese_ci" title=", case-insensitive" value="gb18030_chinese_ci">gb18030_chinese_ci</option>
       <option value="gb18030_unicode_520_ci" title="Unicode (multilingual)" value="gb18030_unicode_520_ci">gb18030_unicode_520_ci</option>
       </optgroup>
       <optgroup label="gb2312" title="GB2312 Simplified Chinese">
       <option value="gb2312_bin" title="Simplified Chinese, Binary" value="gb2312_bin">gb2312_bin</option>
       <option value="gb2312_chinese_ci" title="Simplified Chinese, case-insensitive" value="gb2312_chinese_ci">gb2312_chinese_ci</option>
       </optgroup>
       <optgroup label="gbk" title="GBK Simplified Chinese">
       <option value="gbk_bin" title="Simplified Chinese, Binary" value="gbk_bin">gbk_bin</option>
       <option value="gbk_chinese_ci" title="Simplified Chinese, case-insensitive" value="gbk_chinese_ci">gbk_chinese_ci</option>
       </optgroup>
       <optgroup label="geostd8" title="GEOSTD8 Georgian">
       <option value="geostd8_bin" title="Georgian, Binary" value="geostd8_bin">geostd8_bin</option>
       <option value="geostd8_general_ci" title="Georgian, case-insensitive" value="geostd8_general_ci">geostd8_general_ci</option>
       </optgroup>
       <optgroup label="greek" title="ISO 8859-7 Greek">
       <option value="greek_bin" title="Greek, Binary" value="greek_bin">greek_bin</option>
       <option value="greek_general_ci" title="Greek, case-insensitive" value="greek_general_ci">greek_general_ci</option>
       </optgroup>
       <optgroup label="hebrew" title="ISO 8859-8 Hebrew">
       <option value="hebrew_bin" title="Hebrew, Binary" value="hebrew_bin">hebrew_bin</option>
       <option value="hebrew_general_ci" title="Hebrew, case-insensitive" value="hebrew_general_ci">hebrew_general_ci</option>
       </optgroup>
       <optgroup label="hp8" title="HP West European">
       <option value="hp8_bin" title="West European (multilingual), Binary" value="hp8_bin">hp8_bin</option>
       <option value="hp8_english_ci" title="English, case-insensitive" value="hp8_english_ci">hp8_english_ci</option>
       </optgroup>
       <optgroup label="keybcs2" title="DOS Kamenicky Czech-Slovak">
       <option value="keybcs2_bin" title="Czech-Slovak, Binary" value="keybcs2_bin">keybcs2_bin</option>
       <option value="keybcs2_general_ci" title="Czech-Slovak, case-insensitive" value="keybcs2_general_ci">keybcs2_general_ci</option>
       </optgroup>
       <optgroup label="koi8r" title="KOI8-R Relcom Russian">
       <option value="koi8r_bin" title="Russian, Binary" value="koi8r_bin">koi8r_bin</option>
       <option value="koi8r_general_ci" title="Russian, case-insensitive" value="koi8r_general_ci">koi8r_general_ci</option>
       </optgroup>
       <optgroup label="koi8u" title="KOI8-U Ukrainian">
       <option value="koi8u_bin" title="Ukrainian, Binary" value="koi8u_bin">koi8u_bin</option>
       <option value="koi8u_general_ci" title="Ukrainian, case-insensitive" value="koi8u_general_ci">koi8u_general_ci</option>
       </optgroup>
       <optgroup label="latin1" title="cp1252 West European">
       <option value="latin1_bin" title="West European (multilingual), Binary" value="latin1_bin">latin1_bin</option>
       <option value="latin1_danish_ci" title="Danish, case-insensitive" value="latin1_danish_ci">latin1_danish_ci</option>
       <option value="latin1_general_ci" title="West European (multilingual), case-insensitive" value="latin1_general_ci">latin1_general_ci</option>
       <option value="latin1_general_cs" title="West European (multilingual), case-sensitive" value="latin1_general_cs">latin1_general_cs</option>
       <option value="latin1_german1_ci" title="German (dictionary), case-insensitive" value="latin1_german1_ci">latin1_german1_ci</option>
       <option value="latin1_german2_ci" title="German (phone book), case-insensitive" value="latin1_german2_ci">latin1_german2_ci</option>
       <option value="latin1_spanish_ci" title="Spanish, case-insensitive" value="latin1_spanish_ci">latin1_spanish_ci</option>
       <option value="latin1_swedish_ci" title="Swedish, case-insensitive"  value="latin1_swedish_ci">latin1_swedish_ci</option>
       </optgroup>
       <optgroup label="latin2" title="ISO 8859-2 Central European">
       <option value="latin2_bin" title="Central European (multilingual), Binary" value="latin2_bin">latin2_bin</option>
       <option value="latin2_croatian_ci" title="Croatian, case-insensitive" value="latin2_croatian_ci">latin2_croatian_ci</option>
       <option value="latin2_czech_cs" title="Czech, case-sensitive" value="latin2_czech_cs">latin2_czech_cs</option>
       <option value="latin2_general_ci" title="Central European (multilingual), case-insensitive" value="latin2_general_ci">latin2_general_ci</option>
       <option value="latin2_hungarian_ci" title="Hungarian, case-insensitive" value="latin2_hungarian_ci">latin2_hungarian_ci</option>
       </optgroup>
       <optgroup label="latin5" title="ISO 8859-9 Turkish">
       <option value="latin5_bin" title="Turkish, Binary" value="latin5_bin">latin5_bin</option>
       <option value="latin5_turkish_ci" title="Turkish, case-insensitive" value="latin5_turkish_ci">latin5_turkish_ci</option>
       </optgroup>
       <optgroup label="latin7" title="ISO 8859-13 Baltic">
       <option value="latin7_bin" title="Baltic (multilingual), Binary" value="latin7_bin">latin7_bin</option>
       <option value="latin7_estonian_cs" title="Estonian, case-sensitive" value="latin7_estonian_cs">latin7_estonian_cs</option>
       <option value="latin7_general_ci" title="Baltic (multilingual), case-insensitive" value="latin7_general_ci">latin7_general_ci</option>
       <option value="latin7_general_cs" title="Baltic (multilingual), case-sensitive" value="latin7_general_cs">latin7_general_cs</option>
       </optgroup>
       <optgroup label="macce" title="Mac Central European">
       <option value="macce_bin" title="Central European (multilingual), Binary" value="macce_bin">macce_bin</option>
       <option value="macce_general_ci" title="Central European (multilingual), case-insensitive" value="macce_general_ci">macce_general_ci</option>
       </optgroup>
       <optgroup label="macroman" title="Mac West European">
       <option value="macroman_bin" title="West European (multilingual), Binary" value="macroman_bin">macroman_bin</option>
       <option value="macroman_general_ci" title="West European (multilingual), case-insensitive" value="macroman_general_ci">macroman_general_ci</option>
       </optgroup>
       <optgroup label="sjis" title="Shift-JIS Japanese">
       <option value="sjis_bin" title="Japanese, Binary" value="sjis_bin">sjis_bin</option>
       <option value="sjis_japanese_ci" title="Japanese, case-insensitive" value="sjis_japanese_ci">sjis_japanese_ci</option>
       </optgroup>
       <optgroup label="swe7" title="7bit Swedish">
       <option value="swe7_bin" title="Swedish, Binary" value="swe7_bin">swe7_bin</option>
       <option value="swe7_swedish_ci" title="Swedish, case-insensitive" value="swe7_swedish_ci">swe7_swedish_ci</option>
       </optgroup>
       <optgroup label="tis620" title="TIS620 Thai">
       <option value="tis620_bin" title="Thai, Binary" value="tis620_bin">tis620_bin</option>
       <option value="tis620_thai_ci" title="Thai, case-insensitive" value="tis620_thai_ci">tis620_thai_ci</option>
       </optgroup>
       <optgroup label="ucs2" title="UCS-2 Unicode">
       <option value="ucs2_bin" title="Unicode (multilingual), Binary" value="ucs2_bin">ucs2_bin</option>
       <option value="ucs2_croatian_ci" title="Croatian, case-insensitive" value="ucs2_croatian_ci">ucs2_croatian_ci</option>
       <option value="ucs2_czech_ci" title="Czech, case-insensitive" value="ucs2_czech_ci">ucs2_czech_ci</option>
       <option value="ucs2_danish_ci" title="Danish, case-insensitive" value="ucs2_danish_ci">ucs2_danish_ci</option>
       <option value="ucs2_esperanto_ci" title="Esperanto, case-insensitive" value="ucs2_esperanto_ci">ucs2_esperanto_ci</option>
       <option value="ucs2_estonian_ci" title="Estonian, case-insensitive" value="ucs2_estonian_ci">ucs2_estonian_ci</option>
       <option value="ucs2_general_ci" title="Unicode (multilingual), case-insensitive" value="ucs2_general_ci">ucs2_general_ci</option>
       <option value="ucs2_general_mysql500_ci" title="Unicode (multilingual)" value="ucs2_general_mysql500_ci">ucs2_general_mysql500_ci</option>
       <option value="ucs2_german2_ci" title="German (phone book), case-insensitive" value="ucs2_german2_ci">ucs2_german2_ci</option>
       <option value="ucs2_hungarian_ci" title="Hungarian, case-insensitive" value="ucs2_hungarian_ci">ucs2_hungarian_ci</option>
       <option value="ucs2_icelandic_ci" title="Icelandic, case-insensitive" value="ucs2_icelandic_ci">ucs2_icelandic_ci</option>
       <option value="ucs2_latvian_ci" title="Latvian, case-insensitive" value="ucs2_latvian_ci">ucs2_latvian_ci</option>
       <option value="ucs2_lithuanian_ci" title="Lithuanian, case-insensitive" value="ucs2_lithuanian_ci">ucs2_lithuanian_ci</option>
       <option value="ucs2_persian_ci" title="Persian, case-insensitive" value="ucs2_persian_ci">ucs2_persian_ci</option>
       <option value="ucs2_polish_ci" title="Polish, case-insensitive" value="ucs2_polish_ci">ucs2_polish_ci</option>
       <option value="ucs2_roman_ci" title="West European, case-insensitive" value="ucs2_roman_ci">ucs2_roman_ci</option>
       <option value="ucs2_romanian_ci" title="Romanian, case-insensitive" value="ucs2_romanian_ci">ucs2_romanian_ci</option>
       <option value="ucs2_sinhala_ci" title="Sinhalese, case-insensitive" value="ucs2_sinhala_ci">ucs2_sinhala_ci</option>
       <option value="ucs2_slovak_ci" title="Slovak, case-insensitive" value="ucs2_slovak_ci">ucs2_slovak_ci</option>
       <option value="ucs2_slovenian_ci" title="Slovenian, case-insensitive" value="ucs2_slovenian_ci">ucs2_slovenian_ci</option>
       <option value="ucs2_spanish2_ci" title="Traditional Spanish, case-insensitive" value="ucs2_spanish2_ci">ucs2_spanish2_ci</option>
       <option value="ucs2_spanish_ci" title="Spanish, case-insensitive" value="ucs2_spanish_ci">ucs2_spanish_ci</option>
       <option value="ucs2_swedish_ci" title="Swedish, case-insensitive" value="ucs2_swedish_ci">ucs2_swedish_ci</option>
       <option value="ucs2_turkish_ci" title="Turkish, case-insensitive" value="ucs2_turkish_ci">ucs2_turkish_ci</option>
       <option value="ucs2_unicode_520_ci" title="Unicode (multilingual)" value="ucs2_unicode_520_ci">ucs2_unicode_520_ci</option>
       <option value="ucs2_unicode_ci" title="Unicode (multilingual), case-insensitive" value="ucs2_unicode_ci">ucs2_unicode_ci</option>
       <option value="ucs2_vietnamese_ci" title="Vietnamese, case-insensitive" value="ucs2_vietnamese_ci">ucs2_vietnamese_ci</option>
       </optgroup>
       <optgroup label="ujis" title="EUC-JP Japanese">
       <option value="ujis_bin" title="Japanese, Binary" value="ujis_bin">ujis_bin</option>
       <option value="ujis_japanese_ci" title="Japanese, case-insensitive" value="ujis_japanese_ci">ujis_japanese_ci</option>
       </optgroup>
       <optgroup label="utf16" title="UTF-16 Unicode">
       <option value="utf16_bin" title="unknown, Binary" value="utf16_bin">utf16_bin</option>
       <option value="utf16_croatian_ci" title="Croatian, case-insensitive" value="utf16_croatian_ci">utf16_croatian_ci</option>
       <option value="utf16_czech_ci" title="Czech, case-insensitive" value="utf16_czech_ci">utf16_czech_ci</option>
       <option value="utf16_danish_ci" title="Danish, case-insensitive" value="utf16_danish_ci">utf16_danish_ci</option>
       <option value="utf16_esperanto_ci" title="Esperanto, case-insensitive" value="utf16_esperanto_ci">utf16_esperanto_ci</option>
       <option value="utf16_estonian_ci" title="Estonian, case-insensitive" value="utf16_estonian_ci">utf16_estonian_ci</option>
       <option value="utf16_general_ci" title="unknown, case-insensitive" value="utf16_general_ci">utf16_general_ci</option>
       <option value="utf16_german2_ci" title="German (phone book), case-insensitive" value="utf16_german2_ci">utf16_german2_ci</option>
       <option value="utf16_hungarian_ci" title="Hungarian, case-insensitive" value="utf16_hungarian_ci">utf16_hungarian_ci</option>
       <option value="utf16_icelandic_ci" title="Icelandic, case-insensitive" value="utf16_icelandic_ci">utf16_icelandic_ci</option>
       <option value="utf16_latvian_ci" title="Latvian, case-insensitive" value="utf16_latvian_ci">utf16_latvian_ci</option>
       <option value="utf16_lithuanian_ci" title="Lithuanian, case-insensitive" value="utf16_lithuanian_ci">utf16_lithuanian_ci</option>
       <option value="utf16_persian_ci" title="Persian, case-insensitive" value="utf16_persian_ci">utf16_persian_ci</option>
       <option value="utf16_polish_ci" title="Polish, case-insensitive" value="utf16_polish_ci">utf16_polish_ci</option>
       <option value="utf16_roman_ci" title="West European, case-insensitive" value="utf16_roman_ci">utf16_roman_ci</option>
       <option value="utf16_romanian_ci" title="Romanian, case-insensitive" value="utf16_romanian_ci">utf16_romanian_ci</option>
       <option value="utf16_sinhala_ci" title="Sinhalese, case-insensitive" value="utf16_sinhala_ci">utf16_sinhala_ci</option>
       <option value="utf16_slovak_ci" title="Slovak, case-insensitive" value="utf16_slovak_ci">utf16_slovak_ci</option>
       <option value="utf16_slovenian_ci" title="Slovenian, case-insensitive" value="utf16_slovenian_ci">utf16_slovenian_ci</option>
       <option value="utf16_spanish2_ci" title="Traditional Spanish, case-insensitive" value="utf16_spanish2_ci">utf16_spanish2_ci</option>
       <option value="utf16_spanish_ci" title="Spanish, case-insensitive" value="utf16_spanish_ci">utf16_spanish_ci</option>
       <option value="utf16_swedish_ci" title="Swedish, case-insensitive" value="utf16_swedish_ci">utf16_swedish_ci</option>
       <option value="utf16_turkish_ci" title="Turkish, case-insensitive" value="utf16_turkish_ci">utf16_turkish_ci</option>
       <option value="utf16_unicode_520_ci" title="Unicode (multilingual)" value="utf16_unicode_520_ci">utf16_unicode_520_ci</option>
       <option value="utf16_unicode_ci" title="Unicode (multilingual), case-insensitive" value="utf16_unicode_ci">utf16_unicode_ci</option>
       <option value="utf16_vietnamese_ci" title="Vietnamese, case-insensitive" value="utf16_vietnamese_ci">utf16_vietnamese_ci</option>
       </optgroup>
       <optgroup label="utf16le" title="UTF-16LE Unicode">
       <option value="utf16le_bin" title="unknown, Binary" value="utf16le_bin">utf16le_bin</option>
       <option value="utf16le_general_ci" title="unknown, case-insensitive" value="utf16le_general_ci">utf16le_general_ci</option>
       </optgroup>
       <optgroup label="utf32" title="UTF-32 Unicode">
       <option value="utf32_bin" title="unknown, Binary" value="utf32_bin">utf32_bin</option>
       <option value="utf32_croatian_ci" title="Croatian, case-insensitive" value="utf32_croatian_ci">utf32_croatian_ci</option>
       <option value="utf32_czech_ci" title="Czech, case-insensitive" value="utf32_czech_ci">utf32_czech_ci</option>
       <option value="utf32_danish_ci" title="Danish, case-insensitive" value="utf32_danish_ci">utf32_danish_ci</option>
       <option value="utf32_esperanto_ci" title="Esperanto, case-insensitive" value="utf32_esperanto_ci">utf32_esperanto_ci</option>
       <option value="utf32_estonian_ci" title="Estonian, case-insensitive" value="utf32_estonian_ci">utf32_estonian_ci</option>
       <option value="utf32_general_ci" title="unknown, case-insensitive" value="utf32_general_ci">utf32_general_ci</option>
       <option value="utf32_german2_ci" title="German (phone book), case-insensitive" value="utf32_german2_ci">utf32_german2_ci</option>
       <option value="utf32_hungarian_ci" title="Hungarian, case-insensitive" value="utf32_hungarian_ci">utf32_hungarian_ci</option>
       <option value="utf32_icelandic_ci" title="Icelandic, case-insensitive" value="utf32_icelandic_ci">utf32_icelandic_ci</option>
       <option value="utf32_latvian_ci" title="Latvian, case-insensitive" value="utf32_latvian_ci">utf32_latvian_ci</option>
       <option value="utf32_lithuanian_ci" title="Lithuanian, case-insensitive" value="utf32_lithuanian_ci">utf32_lithuanian_ci</option>
       <option value="utf32_persian_ci" title="Persian, case-insensitive" value="utf32_persian_ci">utf32_persian_ci</option>
       <option value="utf32_polish_ci" title="Polish, case-insensitive" value="utf32_polish_ci">utf32_polish_ci</option>
       <option value="utf32_roman_ci" title="West European, case-insensitive" value="utf32_roman_ci">utf32_roman_ci</option>
       <option value="utf32_romanian_ci" title="Romanian, case-insensitive" value="utf32_romanian_ci">utf32_romanian_ci</option>
       <option value="utf32_sinhala_ci" title="Sinhalese, case-insensitive" value="utf32_sinhala_ci">utf32_sinhala_ci</option>
       <option value="utf32_slovak_ci" title="Slovak, case-insensitive" value="utf32_slovak_ci">utf32_slovak_ci</option>
       <option value="utf32_slovenian_ci" title="Slovenian, case-insensitive" value="utf32_slovenian_ci">utf32_slovenian_ci</option>
       <option value="utf32_spanish2_ci" title="Traditional Spanish, case-insensitive" value="utf32_spanish2_ci">utf32_spanish2_ci</option>
       <option value="utf32_spanish_ci" title="Spanish, case-insensitive" value="utf32_spanish_ci">utf32_spanish_ci</option>
       <option value="utf32_swedish_ci" title="Swedish, case-insensitive" value="utf32_swedish_ci">utf32_swedish_ci</option>
       <option value="utf32_turkish_ci" title="Turkish, case-insensitive" value="utf32_turkish_ci">utf32_turkish_ci</option>
       <option value="utf32_unicode_520_ci" title="Unicode (multilingual)" value="utf32_unicode_520_ci">utf32_unicode_520_ci</option>
       <option value="utf32_unicode_ci" title="Unicode (multilingual), case-insensitive" value="utf32_unicode_ci">utf32_unicode_ci</option>
       <option value="utf32_vietnamese_ci" title="Vietnamese, case-insensitive" value="utf32_vietnamese_ci">utf32_vietnamese_ci</option>
       </optgroup>
       <optgroup label="utf8" title="UTF-8 Unicode">
       <option value="utf8_bin" title="Unicode (multilingual), Binary" value="utf8_bin">utf8_bin</option>
       <option value="utf8_croatian_ci" title="Croatian, case-insensitive" value="utf8_croatian_ci">utf8_croatian_ci</option>
       <option value="utf8_czech_ci" title="Czech, case-insensitive" value="utf8_czech_ci">utf8_czech_ci</option>
       <option value="utf8_danish_ci" title="Danish, case-insensitive" value="utf8_danish_ci">utf8_danish_ci</option>
       <option value="utf8_esperanto_ci" title="Esperanto, case-insensitive" value="utf8_esperanto_ci">utf8_esperanto_ci</option>
       <option value="utf8_estonian_ci" title="Estonian, case-insensitive" value="utf8_estonian_ci">utf8_estonian_ci</option>
       <option value="utf8_general_ci" title="Unicode (multilingual), case-insensitive" value="utf8_general_ci">utf8_general_ci</option>
       <option value="utf8_general_mysql500_ci" title="Unicode (multilingual)" value="utf8_general_mysql500_ci">utf8_general_mysql500_ci</option>
       <option value="utf8_german2_ci" title="German (phone book), case-insensitive" value="utf8_german2_ci">utf8_german2_ci</option>
       <option value="utf8_hungarian_ci" title="Hungarian, case-insensitive" value="utf8_hungarian_ci">utf8_hungarian_ci</option>
       <option value="utf8_icelandic_ci" title="Icelandic, case-insensitive" value="utf8_icelandic_ci">utf8_icelandic_ci</option>
       <option value="utf8_latvian_ci" title="Latvian, case-insensitive" value="utf8_latvian_ci">utf8_latvian_ci</option>
       <option value="utf8_lithuanian_ci" title="Lithuanian, case-insensitive" value="utf8_lithuanian_ci">utf8_lithuanian_ci</option>
       <option value="utf8_persian_ci" title="Persian, case-insensitive" value="utf8_persian_ci">utf8_persian_ci</option>
       <option value="utf8_polish_ci" title="Polish, case-insensitive" value="utf8_polish_ci">utf8_polish_ci</option>
       <option value="utf8_roman_ci" title="West European, case-insensitive" value="utf8_roman_ci">utf8_roman_ci</option>
       <option value="utf8_romanian_ci" title="Romanian, case-insensitive" value="utf8_romanian_ci">utf8_romanian_ci</option>
       <option value="utf8_sinhala_ci" title="Sinhalese, case-insensitive" value="utf8_sinhala_ci">utf8_sinhala_ci</option>
       <option value="utf8_slovak_ci" title="Slovak, case-insensitive" value="utf8_slovak_ci">utf8_slovak_ci</option>
       <option value="utf8_slovenian_ci" title="Slovenian, case-insensitive" value="utf8_slovenian_ci">utf8_slovenian_ci</option>
       <option value="utf8_spanish2_ci" title="Traditional Spanish, case-insensitive" value="utf8_spanish2_ci">utf8_spanish2_ci</option>
       <option value="utf8_spanish_ci" title="Spanish, case-insensitive" value="utf8_spanish_ci">utf8_spanish_ci</option>
       <option value="utf8_swedish_ci" title="Swedish, case-insensitive" value="utf8_swedish_ci">utf8_swedish_ci</option>
       <option value="utf8_turkish_ci" title="Turkish, case-insensitive" value="utf8_turkish_ci">utf8_turkish_ci</option>
       <option value="utf8_unicode_520_ci" title="Unicode (multilingual)" value="utf8_unicode_520_ci">utf8_unicode_520_ci</option>
       <option value="utf8_unicode_ci" title="Unicode (multilingual), case-insensitive" value="utf8_unicode_ci">utf8_unicode_ci</option>
       <option value="utf8_vietnamese_ci" title="Vietnamese, case-insensitive" value="utf8_vietnamese_ci">utf8_vietnamese_ci</option>
       </optgroup>
       <optgroup label="utf8mb4" title="UTF-8 Unicode">
       <option value="utf8mb4_bin" title="Unicode (multilingual), Binary" value="utf8mb4_bin">utf8mb4_bin</option>
       <option value="utf8mb4_croatian_ci" title="Croatian, case-insensitive" value="utf8mb4_croatian_ci">utf8mb4_croatian_ci</option>
       <option value="utf8mb4_czech_ci" title="Czech, case-insensitive" value="utf8mb4_czech_ci">utf8mb4_czech_ci</option>
       <option value="utf8mb4_danish_ci" title="Danish, case-insensitive" value="utf8mb4_danish_ci">utf8mb4_danish_ci</option>
       <option value="utf8mb4_esperanto_ci" title="Esperanto, case-insensitive" value="utf8mb4_esperanto_ci">utf8mb4_esperanto_ci</option>
       <option value="utf8mb4_estonian_ci" title="Estonian, case-insensitive" value="utf8mb4_estonian_ci">utf8mb4_estonian_ci</option>
       <option value="utf8mb4_general_ci" title="Unicode (multilingual), case-insensitive" value="utf8mb4_general_ci">utf8mb4_general_ci</option>
       <option value="utf8mb4_german2_ci" title="German (phone book), case-insensitive" value="utf8mb4_german2_ci">utf8mb4_german2_ci</option>
       <option value="utf8mb4_hungarian_ci" title="Hungarian, case-insensitive" value="utf8mb4_hungarian_ci">utf8mb4_hungarian_ci</option>
       <option value="utf8mb4_icelandic_ci" title="Icelandic, case-insensitive" value="utf8mb4_icelandic_ci">utf8mb4_icelandic_ci</option>
       <option value="utf8mb4_latvian_ci" title="Latvian, case-insensitive" value="utf8mb4_latvian_ci">utf8mb4_latvian_ci</option>
       <option value="utf8mb4_lithuanian_ci" title="Lithuanian, case-insensitive" value="utf8mb4_lithuanian_ci">utf8mb4_lithuanian_ci</option>
       <option value="utf8mb4_persian_ci" title="Persian, case-insensitive" value="utf8mb4_persian_ci">utf8mb4_persian_ci</option>
       <option value="utf8mb4_polish_ci" title="Polish, case-insensitive" value="utf8mb4_polish_ci">utf8mb4_polish_ci</option>
       <option value="utf8mb4_roman_ci" title="West European, case-insensitive" value="utf8mb4_roman_ci">utf8mb4_roman_ci</option>
       <option value="utf8mb4_romanian_ci" title="Romanian, case-insensitive" value="utf8mb4_romanian_ci">utf8mb4_romanian_ci</option>
       <option value="utf8mb4_sinhala_ci" title="Sinhalese, case-insensitive" value="utf8mb4_sinhala_ci">utf8mb4_sinhala_ci</option>
       <option value="utf8mb4_slovak_ci" title="Slovak, case-insensitive" value="utf8mb4_slovak_ci">utf8mb4_slovak_ci</option>
       <option value="utf8mb4_slovenian_ci" title="Slovenian, case-insensitive" value="utf8mb4_slovenian_ci">utf8mb4_slovenian_ci</option>
       <option value="utf8mb4_spanish2_ci" title="Traditional Spanish, case-insensitive" value="utf8mb4_spanish2_ci">utf8mb4_spanish2_ci</option>
       <option value="utf8mb4_spanish_ci" title="Spanish, case-insensitive" value="utf8mb4_spanish_ci">utf8mb4_spanish_ci</option>
       <option value="utf8mb4_swedish_ci" title="Swedish, case-insensitive" value="utf8mb4_swedish_ci">utf8mb4_swedish_ci</option>
       <option value="utf8mb4_turkish_ci" title="Turkish, case-insensitive" value="utf8mb4_turkish_ci">utf8mb4_turkish_ci</option>
       <option value="utf8mb4_unicode_520_ci" title="Unicode (multilingual)" value="utf8mb4_unicode_520_ci">utf8mb4_unicode_520_ci</option>
       <option value="utf8mb4_unicode_ci" title="Unicode (multilingual), case-insensitive" value="utf8mb4_unicode_ci">utf8mb4_unicode_ci</option>
       <option value="utf8mb4_vietnamese_ci" title="Vietnamese, case-insensitive" value="utf8mb4_vietnamese_ci">utf8mb4_vietnamese_ci</option>
       </optgroup>
  </>)
}