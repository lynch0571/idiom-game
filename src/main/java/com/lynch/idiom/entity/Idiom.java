package com.lynch.idiom.entity;

import lombok.Data;

/**
 * 成语实体类
 *
 * @author Lynch
 * @date 2020-12-16
 */
@Data
public class Idiom {
    /**
     * 成语名称，如：花好月圆
     */
    private String name;
    /**
     * 成语的第一个字，如：花
     */
    private String head;
    /**
     * 第一个字的拼音，如：huā
     */
    private String headSymbol;
    /**
     * 成语的最后一个字，如：圆
     */
    private String tail;
    /**
     * 最后一个字的拼音，如：yuán
     */
    private String tailSymbol;
    /**
     * 成语的出处，如：宋·张先《木兰花》词：“人意共怜花月满，花好月圆人又散。欢情去逐远云空，往事过如幽梦断。”
     */
    private String derivation;
    /**
     * 成语的解释，如：花儿正盛开，月亮正圆满。比喻美好圆满的生活，多用作新婚颂辞。
     */
    private String explanation;


}
