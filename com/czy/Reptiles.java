package com.czy;

import com.sun.source.tree.WhileLoopTree;
import org.w3c.dom.NameList;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * ClassName: Reptiles
 * Package: com.czy
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
public class Reptiles {
    public static void main(String[] args) throws URISyntaxException, IOException {
        /**
            制造假数据：
             获取姓氏：https://hanyu.baidu.com/shici/detail?pid=0b2f26d4c0ddb3ee693fdb1137ee1b0d&from=kg0
             获取男生名字：http://www.haoming8.cn/baobao/10881.html
             获取女生名字：http://www.haoming8.cn/baobao/7641.html
         */
        // 1.从网页中获取数据 百家姓：赵钱孙李，周吴郑王
        String faimlyUrl = "https://hanyu.baidu.com/shici/detail?pid=0b2f26d4c0ddb3ee693fdb1137ee1b0d&from=kg0";
        String boyNameUrl = "http://www.haoming8.cn/baobao/10881.html";
        String girlNameUrl = "http://www.haoming8.cn/baobao/7641.html";

        String faimlyAllStr = getAllInfo(faimlyUrl);
        String boyNameAllStr = getAllInfo(boyNameUrl);
        String girlNameAllStr = getAllInfo(girlNameUrl);

        // 2.将从网页中获取的海量数据，进行解析，提取出需要的数据
        List<String> faimlyTempList = getData(faimlyAllStr, "([\\u4e00-\\u9fa5]{4})(，|。)", 1);
        List<String> boyNameTempList = getData(boyNameAllStr, "([\\u4e00-\\u9fa5]{2})(、|。)", 1);
        List<String> girlNameTempList = getData(girlNameAllStr, "[\\u4e00-\\u9fa5]{2} [\\u4e00-\\u9fa5]{2} [\\u4e00-\\u9fa5]{2} [\\u4e00-\\u9fa5]{2}", 0);

        // 3.处理获取的数据
        // 3.1 姓数据
        ArrayList<String> faimlyList = new ArrayList<>();
        for (String s : faimlyTempList) {
            // 遍历字符串，并将其添加到数组中
            for (int i = 0; i < s.length(); ++i){
                faimlyList.add(s.charAt(i) + "");
            }
        }
        // 3.2 男孩名数据
        List<String> boyNameList = boyNameTempList.stream().distinct().collect(Collectors.toList());// 去重操作
        // 3.3女孩名数据
        List<String> girlNameList = new ArrayList<>();
        for (String str : girlNameTempList) {
            for (String name : str.split(" ")) {
                girlNameList.add(name);
            }
        }

        // 4.对获取到的数据进行拼接
        ArrayList<String> allNames = (ArrayList<String>) getAllName(faimlyList, boyNameList, girlNameList, 10, 5);
        Collections.shuffle(allNames);
        System.out.println(allNames);

        // 5.将数据写入到文件中
        BufferedWriter writer = new BufferedWriter(new FileWriter("com/czy/name.txt"));
        for (String name : allNames) {
            // 将数据写入到文件
            writer.write(name);
            writer.newLine();// 换行
        }

        writer.close();
    }

    /**
     * 获取拼接好的姓和名，进行返回
     * @param faimlyList
     * @param boyNameList
     * @param girlNameList
     * @param boyCount
     * @param girlCount
     * @return
     */
    private static List<String> getAllName(ArrayList<String> faimlyList, List<String> boyNameList, List<String> girlNameList, int boyCount, int girlCount) {
        HashSet<String> result = new HashSet<>();
        // 1.获取男孩的姓名
        // 1.1 获取随机数，姓和名
        Random r = new Random();
        int faimlyIndex;
        int boyIndex;
        int age;
        while (result.size() <= boyCount){
            faimlyIndex = r.nextInt(faimlyList.size());
            boyIndex = r.nextInt(boyNameList.size());
            // 随机年龄 18- 27
            age = r.nextInt(10) + 10;
            result.add(faimlyList.get(faimlyIndex) + boyNameList.get(boyIndex) + "-男-" + age);
        }
        // 2.获取女孩的姓名
        int girlIndex;
        while (result.size() <= boyCount + girlCount){
            faimlyIndex = r.nextInt(faimlyList.size());
            girlIndex = r.nextInt(girlNameList.size());
            // 随机年龄 18- 25
            age = r.nextInt(8) + 10;
            result.add(faimlyList.get(faimlyIndex) + boyNameList.get(girlIndex) + "-女-" + age);
        }
        return result.stream().collect(Collectors.toList());
    }

    /**
     * 根据正则表达式，获取网页中所需要的数据
     * @param str
     * @param regix
     * @param index
     * @return
     */
    private static List<String> getData(String str, String regix, int index) {
        // 1.根据正则表达式获取所需的数据
        List<String> strs = new ArrayList<String>();
        Pattern p = Pattern.compile(regix);
        Matcher m = p.matcher(str);
        while(m.find()) {
            strs.add(m.group(index));
        }
        return strs;
    }

    /**
     * 根据提供的网络地址，获取到网页上的所有数据并返回字符串
     * @param urlStr: 提供的url地址
     * @return 网页中的字符串
     */
    private static String getAllInfo(String urlStr) throws IOException {
        // 1.和网页建立连接
        URL url = new URL(urlStr);
        URLConnection connection = url.openConnection();

        // 2.从网页中获取数据
        StringBuilder result = new StringBuilder();
        InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
        int len;
        while ((len = streamReader.read()) != -1){
            result.append((char) len);
        }
        return result.toString();
    }
}
