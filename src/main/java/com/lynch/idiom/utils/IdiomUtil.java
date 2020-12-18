package com.lynch.idiom.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Lynch
 * @date 2020-12-16
 */
@Slf4j
public class IdiomUtil {

    private static final String idiomPath = "/Users/fire/git/idiom-game/src/main/resources/static/";

    public static void main(String[] args) {
        createIdiomSimpleJson();
    }

    /**
     * 根据原始成语库生成简略版成语库，大约能压缩十倍内容，为后续处理提高效率
     */
    private static void createIdiomSimpleJson() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(idiomPath + "idiom.json"));
            StringBuilder sb = new StringBuilder();
            lines.forEach(e -> sb.append(e));
            JSONArray info = JSONObject.parseArray(sb.toString());
            StringBuilder wordSb = new StringBuilder();
            info.stream().forEach(e -> {
                JSONObject item = (JSONObject) e;
                String word = item.getString("word");
                /*全角空格替换为半角空格*/
                String pinyin = item.getString("pinyin").replace("　", " ");
                wordSb.append(word).append("(").append(pinyin).append(")").append("\n");
            });
            Files.write(Paths.get(idiomPath + "idiomSimple.json"), wordSb.toString().getBytes());
            log.info("创建精简成语json文件成功！共{}个成语。", info.size());
        } catch (IOException e) {
            log.info("创建精简成语json文件失败！e:{}", e.getMessage());
        }
    }
}
