package com.czy;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpUtil;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName: ReptilesHutools
 * Package: com.czy
 * Description:
 *  利用Hutools工具实现爬取操作
 * @Author Chen Ziyun
 * @Version 1.0
 */
public class ReptilesHutools {
    public static void main(String[] args) throws IOException {
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

        //请求列表页
        String faimlyContent = HttpUtil.get(faimlyUrl);
        String boyNameContent = HttpUtil.get(boyNameUrl);
        String girlNameContent = HttpUtil.get(girlNameUrl);

        List<String> faimlyTempList = ReUtil.findAll("([\\u4e00-\\u9fa5]{4})(，|。)", faimlyContent, 1);
        List<String> boyNameTempList = ReUtil.findAll("([\\u4e00-\\u9fa5]{2})(、|。)", boyNameContent, 1);
        List<String> girlNameTempList = ReUtil.findAll("(.. ){4}..", girlNameContent, 0);

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

}
