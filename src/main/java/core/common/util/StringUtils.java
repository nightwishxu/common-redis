package core.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringUtils {
    public static final List<Character> HEX_CHAR_LIST = new ArrayList();

    static {
        HEX_CHAR_LIST.add(new Character('0'));
        HEX_CHAR_LIST.add(new Character('1'));
        HEX_CHAR_LIST.add(new Character('2'));
        HEX_CHAR_LIST.add(new Character('3'));
        HEX_CHAR_LIST.add(new Character('4'));
        HEX_CHAR_LIST.add(new Character('5'));
        HEX_CHAR_LIST.add(new Character('6'));
        HEX_CHAR_LIST.add(new Character('7'));
        HEX_CHAR_LIST.add(new Character('8'));
        HEX_CHAR_LIST.add(new Character('9'));
        HEX_CHAR_LIST.add(new Character('a'));
        HEX_CHAR_LIST.add(new Character('b'));
        HEX_CHAR_LIST.add(new Character('c'));
        HEX_CHAR_LIST.add(new Character('d'));
        HEX_CHAR_LIST.add(new Character('e'));
        HEX_CHAR_LIST.add(new Character('f'));
    }

    public StringUtils() {
    }

    public static String notNull(String str) {
        return str == null ? "" : str;
    }

    public static String format(String str, Object... args) {
        String result = str;
        Pattern p = Pattern.compile("\\{(\\d+)\\}");
        Matcher m = p.matcher(str);

        while(m.find()) {
            int index = Integer.parseInt(m.group(1));
            if (index < args.length) {
                result = result.replace(m.group(), ObjectUtils.notNull(args[index], "").toString());
            }
        }

        return result;
    }

    public static String coding(String str) {
        return coding(str, "ISO-8859-1");
    }

    public static String coding(String str, String charset) {
        return coding(str, charset, "UTF-8");
    }

    public static String coding(String str, String charset, String tocharset) {
        try {
            return str == null ? "" : new String(str.getBytes(charset), tocharset);
        } catch (Exception var4) {
            return str;
        }
    }

    public static String escape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);

        for(int i = 0; i < src.length(); ++i) {
            char j = src.charAt(i);
            if (!Character.isDigit(j) && !Character.isLowerCase(j) && !Character.isUpperCase(j)) {
                if (j < 256) {
                    tmp.append("%");
                    if (j < 16) {
                        tmp.append("0");
                    }

                    tmp.append(Integer.toString(j, 16));
                } else {
                    tmp.append("%u");
                    tmp.append(Integer.toString(j, 16));
                }
            } else {
                tmp.append(j);
            }
        }

        return tmp.toString();
    }

    public static String substring(String src, int start, int length, String ov) {
        if (src != null && length(src) > length) {
            try {
                byte[] rc = src.getBytes("GBK");
                short charlen = 2;
                int count = 0;
                if (!(new String(rc)).equals(src)) {
                    rc = src.getBytes("UTF-8");
                    charlen = 3;
                }

                length = Math.max(Math.min(length - start, rc.length), 1);
                byte[] bs = new byte[length];
                System.arraycopy(rc, start, bs, 0, length);
                byte[] var11 = bs;
                int var10 = bs.length;

                for(int var9 = 0; var9 < var10; ++var9) {
                    byte c = var11[var9];
                    if (c < 0) {
                        ++count;
                    }
                }

                if (count % charlen != 0) {
                    return substring(src, start, length - count % charlen, ov);
                } else {
                    return new String(bs) + ov;
                }
            } catch (Exception var12) {
                return src.substring(start, length - ov.length()) + ov;
            }
        } else {
            return src;
        }
    }

    public static String bytes2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        byte[] var5 = bytes;
        int var4 = bytes.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            byte b = var5[var3];
            int i = b & 255;
            if (i <= 15) {
                sb.append("0");
            }

            sb.append(Integer.toHexString(i));
        }

        return sb.toString();
    }

    private static byte hex2Byte(String s) {
        int high = HEX_CHAR_LIST.indexOf(new Character(s.charAt(0))) << 4;
        int low = HEX_CHAR_LIST.indexOf(new Character(s.charAt(1)));
        return (byte)(high + low);
    }

    public static byte[] hex2Bytes(String input) {
        int len = input.length() / 2;
        byte[] rtn = new byte[len];

        for(int i = 0; i < len; ++i) {
            rtn[i] = hex2Byte(input.substring(i * 2, i * 2 + 2));
        }

        return rtn;
    }

    public static String getPrefix(String content, String regex) {
        int _index = content.indexOf(regex);
        return _index >= 0 ? content.substring(0, _index) : content;
    }

    public static String lowerCase(String str, int beginIndex, int endIndex) {
        StringBuilder builder = new StringBuilder();
        builder.append(str.substring(0, beginIndex));
        builder.append(str.substring(beginIndex, endIndex).toLowerCase());
        builder.append(str.substring(endIndex));
        return builder.toString();
    }

    public static String upperCase(String str, int beginIndex, int endIndex) {
        StringBuilder builder = new StringBuilder();
        builder.append(str.substring(0, beginIndex));
        builder.append(str.substring(beginIndex, endIndex).toUpperCase());
        builder.append(str.substring(endIndex));
        return builder.toString();
    }

    public static String lowerCaseFirstChar(String iString) {
        StringBuilder builder = new StringBuilder();
        builder.append(iString.substring(0, 1).toLowerCase());
        builder.append(iString.substring(1));
        return builder.toString();
    }

    public static String upperCaseFirstChar(String iString) {
        StringBuilder builder = new StringBuilder();
        builder.append(iString.substring(0, 1).toUpperCase());
        builder.append(iString.substring(1));
        return builder.toString();
    }

    public static int timesOf(String str, String subStr) {
        int foundCount = 0;
        if (subStr.equals("")) {
            return 0;
        } else {
            for(int fromIndex = str.indexOf(subStr); fromIndex != -1; fromIndex = str.indexOf(subStr, fromIndex + subStr.length())) {
                ++foundCount;
            }

            return foundCount;
        }
    }

    public static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    public static String rightTrim(String str) {
        if (str == null) {
            return "";
        } else {
            int length = str.length();

            for(int i = length - 1; i >= 0 && str.charAt(i) == ' '; --i) {
                --length;
            }

            return str.substring(0, length);
        }
    }

    public static String leftTrim(String str) {
        if (str == null) {
            return "";
        } else {
            int start = 0;
            int i = 0;

            for(int n = str.length(); i < n && str.charAt(i) == ' '; ++i) {
                ++start;
            }

            return str.substring(start);
        }
    }

    public static String[] split(String originalString, String delimiterString) {
        int index = false;
        String[] returnArray = null;
        int length = 0;
        if (originalString != null && delimiterString != null && !originalString.equals("")) {
            if (!originalString.equals("") && !delimiterString.equals("") && originalString.length() >= delimiterString.length()) {
                int index;
                for(String strTemp = originalString; strTemp != null && !strTemp.equals(""); strTemp = strTemp.substring(index + delimiterString.length())) {
                    index = strTemp.indexOf(delimiterString);
                    if (index == -1) {
                        break;
                    }

                    ++length;
                }

                ++length;
                returnArray = new String[length];

                for(int i = 0; i < length - 1; ++i) {
                    index = originalString.indexOf(delimiterString);
                    returnArray[i] = originalString.substring(0, index);
                    originalString = originalString.substring(index + delimiterString.length());
                }

                returnArray[length - 1] = originalString;
                return returnArray;
            } else {
                return new String[]{originalString};
            }
        } else {
            return new String[0];
        }
    }

    public static Map<String, String> toMap(String str, String splitString) {
        return strToMap(str, splitString, "=");
    }

    public static Map<String, String> jsonToMap(String jsonStr, String splitStr) {
        jsonStr = jsonStr.startsWith("{") ? jsonStr.substring(1) : jsonStr;
        jsonStr = jsonStr.endsWith("}") ? jsonStr.substring(0, jsonStr.length() - 1) : jsonStr;
        return strToMap(jsonStr, splitStr, ":");
    }

    public static Map<String, String> strToMap(String str, String splitStr, String linkStr) {
        Map<String, String> map = Collections.synchronizedSortedMap(new TreeMap());
        String[] values = split(str, splitStr);

        for(int i = 0; i < values.length; ++i) {
            String tempValue = values[i];
            int pos = tempValue.indexOf(linkStr);
            String key = "";
            String value = "";
            if (pos > -1) {
                key = tempValue.substring(0, pos);
                value = tempValue.substring(pos + splitStr.length());
            } else {
                key = tempValue;
            }

            map.put(key.replace("\"", ""), value.replace("\"", ""));
        }

        return map;
    }

    public static String iso2gbk(String s) {
        if (s == null) {
            return "";
        } else {
            try {
                return (new String(s.getBytes("ISO-8859-1"), "GBK")).trim();
            } catch (Exception var2) {
                return s;
            }
        }
    }

    public static String gbk2iso(String s) {
        if (s == null) {
            return "";
        } else {
            try {
                return (new String(s.getBytes("GBK"), "ISO-8859-1")).trim();
            } catch (Exception var2) {
                return s;
            }
        }
    }

    public static String utf2iso(String s) {
        if (s == null) {
            return "";
        } else {
            try {
                return (new String(s.getBytes(), "UTF-8")).trim();
            } catch (Exception var2) {
                return s;
            }
        }
    }

    public static String notNull(String str, String str1) {
        return str != null && !"".equals(str) ? str : str1;
    }

    public static boolean isNull(String str) {
        return str == null;
    }

    public static boolean isEmpty(String... str) {
        String[] var4 = str;
        int var3 = str.length;

        for(int var2 = 0; var2 < var3; ++var2) {
            String s = var4[var2];
            if (isNull(s) || notNull(s).trim().length() < 1) {
                return true;
            }
        }

        return false;
    }

    public static int length(String str) {
        try {
            return str.getBytes("GBK").length;
        } catch (UnsupportedEncodingException var2) {
            return str.length();
        }
    }

    public static String escapeSQLTags(String input) {
        if (input != null && input.length() != 0) {
            StringBuffer buf = new StringBuffer();
            char ch = true;

            for(int i = 0; i < input.length(); ++i) {
                char ch = input.charAt(i);
                if (ch == '\\') {
                    buf.append("\\");
                } else if (ch == '\'') {
                    buf.append("'");
                } else {
                    buf.append(ch);
                }
            }

            return buf.toString();
        } else {
            return input;
        }
    }

    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0;
        boolean var3 = false;

        while(lastPos < src.length()) {
            int pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                char ch;
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char)Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char)Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else if (pos == -1) {
                tmp.append(src.substring(lastPos));
                lastPos = src.length();
            } else {
                tmp.append(src.substring(lastPos, pos));
                lastPos = pos;
            }
        }

        return tmp.toString();
    }

    public static String native2ascii(String str) {
        char[] ca = str.toCharArray();
        StringBuffer buffer = new StringBuffer(ca.length * 6);

        for(int x = 0; x < ca.length; ++x) {
            char a = ca[x];
            if (a > 255) {
                buffer.append("\\u").append(Integer.toHexString(a));
            } else {
                buffer.append(a);
            }
        }

        return buffer.toString();
    }

    public static String getPartString(String str, int len, String showStr) throws UnsupportedEncodingException {
        int counterOfDoubleByte = 0;
        byte[] b = str.getBytes("UTF-8");
        if (b.length <= len) {
            return str;
        } else {
            for(int i = 0; i < len; ++i) {
                if (b[i] < 0) {
                    ++counterOfDoubleByte;
                }
            }

            if (counterOfDoubleByte % 2 == 0) {
                return new String(b, 0, len, "UTF-8") + showStr;
            } else {
                return new String(b, 0, len - 1, "UTF-8") + showStr;
            }
        }
    }

    public static String pagination(String text, int pageIndex, int pageSize) {
        return isEmpty(text) ? text : pagination(text, pageIndex, pageSize, text.length());
    }

    public static String pagination(String text, int pageIndex, int pageSize, int lineLength) {
        if (isEmpty(text)) {
            return text;
        } else {
            StringBuffer sb = new StringBuffer();
            Integer totalPage = text.length() % pageSize == 0 ? text.length() / pageSize : text.length() / pageSize + 1;
            if (pageIndex > totalPage) {
                return sb.append("页码无效").toString();
            } else {
                String result = text.toString().substring((pageIndex - 1) * pageSize, (pageIndex - 1) * pageSize + pageSize >= text.length() - 1 ? text.length() : (pageIndex - 1) * pageSize + pageSize);
                char[] chars = result.toCharArray();
                int length = lineLength;

                for(int i = 0; i < chars.length; ++i) {
                    if (length == 0) {
                        length = lineLength;
                        sb.append("\n");
                    }

                    --length;
                    sb.append(chars[i]);
                }

                return sb.toString();
            }
        }
    }

    public static int getBytesLength(String str) {
        return str == null ? -1 : str.getBytes().length;
    }

    public static int indexOf(String str, String subStr, int fromIndex, boolean caseSensitive) {
        return !caseSensitive ? str.toLowerCase().indexOf(subStr.toLowerCase(), fromIndex) : str.indexOf(subStr, fromIndex);
    }

    public static String replace(String str, String searchStr, String replaceStr, boolean caseSensitive) {
        String result = "";
        int i = 0;
        int j = false;
        if (str == null) {
            return null;
        } else if (str.equals("")) {
            return "";
        } else if (searchStr != null && !searchStr.equals("")) {
            if (replaceStr == null) {
                replaceStr = "";
            }

            int j;
            while((j = indexOf(str, searchStr, i, caseSensitive)) > -1) {
                result = result + str.substring(i, j) + replaceStr;
                i = j + searchStr.length();
            }

            result = result + str.substring(i, str.length());
            return result;
        } else {
            return str;
        }
    }

    public static String replace(String str, String searchStr, String replaceStr) {
        return replace(str, searchStr, replaceStr, true);
    }

    public static String replace(String str, char searchChar, String replaceStr) {
        return replace(str, String.valueOf(searchChar), replaceStr, true);
    }

    public static String replace(String str, int beginIndex, String replaceStr) {
        String result = null;
        if (str == null) {
            return null;
        } else {
            if (replaceStr == null) {
                replaceStr = "";
            }

            result = str.substring(0, beginIndex) + replaceStr + str.substring(beginIndex + replaceStr.length());
            return result;
        }
    }

    public static String absoluteTrim(String str) {
        String result = replace(str, " ", "");
        return result;
    }

    public static Map<String, String> sortEnglishNumberWord(Map<String, String> map) {
        Map<String, String> resultMap = new LinkedHashMap(0);
        Map<Integer, String> tempMap = new LinkedHashMap(0);
        Set<String> keys = map.keySet();
        int s = 2147483647;

        for(Iterator var6 = keys.iterator(); var6.hasNext(); --s) {
            String key = (String)var6.next();
            if (key.indexOf("One") > -1) {
                tempMap.put(1, key);
            } else if (key.indexOf("Two") > -1) {
                tempMap.put(2, key);
            } else if (key.indexOf("Three") > -1) {
                tempMap.put(3, key);
            } else if (key.indexOf("Four") > -1) {
                tempMap.put(4, key);
            } else if (key.indexOf("Five") > -1) {
                tempMap.put(5, key);
            } else if (key.indexOf("Six") > -1) {
                tempMap.put(6, key);
            } else if (key.indexOf("Seven") > -1) {
                tempMap.put(7, key);
            } else if (key.indexOf("Eight") > -1) {
                tempMap.put(8, key);
            } else if (key.indexOf("Nine") > -1) {
                tempMap.put(9, key);
            } else {
                tempMap.put(s, key);
            }
        }

        Set<Integer> keyNum = tempMap.keySet();
        Object[] num_obj = keyNum.toArray();
        Integer[] nums = new Integer[num_obj.length];
        Integer tempInt = 0;

        int i;
        for(i = 0; i < num_obj.length; ++i) {
            nums[i] = new Integer(num_obj[i].toString());
        }

        int j;
        for(i = 0; i < nums.length; ++i) {
            for(j = 0; j < nums.length - i - 1; ++j) {
                if (nums[j] > nums[j + 1]) {
                    tempInt = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = tempInt;
                }
            }
        }

        Integer[] var12 = nums;
        int var11 = nums.length;

        for(j = 0; j < var11; ++j) {
            Integer num = var12[j];
            resultMap.put((String)tempMap.get(num), (String)map.get(tempMap.get(num)));
        }

        return resultMap;
    }

    public static String removeHtml(String content) {
        if (content == null) {
            return "";
        } else {
            try {
                Pattern p_html = Pattern.compile("<[^>]+>", 2);
                Matcher m_html = p_html.matcher(content);
                content = m_html.replaceAll("");
                return content;
            } catch (Exception var4) {
                return "";
            }
        }
    }

    public static String removeIframe(String content) {
        if (content == null) {
            return "";
        } else {
            try {
                Pattern p_html = Pattern.compile("<iframe[^>]+>", 2);
                Matcher m_html = p_html.matcher(content);
                content = m_html.replaceAll("");
                return content;
            } catch (Exception var4) {
                return "";
            }
        }
    }

    public static String removeStyle(String content) {
        if (content == null) {
            return "";
        } else {
            try {
                Pattern p_html = Pattern.compile("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", 2);
                Matcher m_html = p_html.matcher(content);
                content = m_html.replaceAll("");
                return content;
            } catch (Exception var4) {
                return "";
            }
        }
    }

    public static String removeScript(String content) {
        if (content == null) {
            return "";
        } else {
            try {
                Pattern p_html = Pattern.compile("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", 2);
                Matcher m_html = p_html.matcher(content);
                content = m_html.replaceAll("");
                return content;
            } catch (Exception var4) {
                return "";
            }
        }
    }

    public static String removeSpace(String content) {
        return content == null ? "" : content.replaceAll("\\s*(\\r\\n)\\s*", "").replaceAll(">(\\s+)", ">").replaceAll("(\\s+)<", "<");
    }

    public static String ListToStr(List<String> list, String regex) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < list.size(); ++i) {
            sb.append((String)list.get(i));
            if (i < list.size() - 1) {
                sb.append(regex);
            }
        }

        return sb.toString();
    }

    public static String ArrToStr(Object[] strArr, String regex) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < strArr.length; ++i) {
            sb.append(strArr[i]);
            if (i < strArr.length - 1) {
                sb.append(regex);
            }
        }

        return sb.toString();
    }

    public static List<String> arrToList(String[] strArr) {
        List<String> strList = new ArrayList();
        String[] var5 = strArr;
        int var4 = strArr.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            String string = var5[var3];
            strList.add(string);
        }

        return strList;
    }

    public static String encode(String str, String enc) {
        String enstr = "";
        if (str != null && !str.equals("")) {
            try {
                enstr = URLEncoder.encode(str, enc);
            } catch (UnsupportedEncodingException var4) {
                var4.printStackTrace();
            }

            return enstr;
        } else {
            return enstr;
        }
    }

    public static String decode(String str, String enc) {
        String enstr = "";
        if (str != null && !str.equals("")) {
            try {
                enstr = URLDecoder.decode(str, enc);
            } catch (UnsupportedEncodingException var4) {
                var4.printStackTrace();
            }

            return enstr;
        } else {
            return enstr;
        }
    }

    public static String encodeBase64(String str) {
        byte[] b = null;
        String s = null;

        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

        if (b != null) {
            s = (new BASE64Encoder()).encode(b);
        }

        return s;
    }

    public static String decoderBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();

            try {
                byte[] b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return result;
    }
}
