package com.lynch.idiom.utils;

import com.lynch.idiom.entity.Idiom;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lynch
 * @date 2020-12-19
 */
@Slf4j
public class IdionApp {

    private static final List<Idiom> sourceList = loadIdiomList();
    private static final String[] idiomArr = loadIdiomNameArray();
    /**
     * 图的顶点数
     */
    private static final int VERTEX_COUNT = sourceList.size();
    public static final String SIMPLE_JSON = "/Users/fire/git/idiom-game/src/main/resources/static/idiomSimple.json";
    private static List<String> targetList = new ArrayList<>(16);
    /**
     * 图的边数
     */
    private static int edgeCount = 0;

    public static void main(String[] args) {

        long t1 = System.currentTimeMillis();

        long t2 = System.currentTimeMillis();
        byte[][] matrix = getMatrix();

        long t3 = System.currentTimeMillis();
        String idion = "花好月圆";
        List<String> nextList = getNextList(matrix, idion);
        long t4 = System.currentTimeMillis();
        List<String> previousList = getPreviousList(matrix, idion);
        log.info("t2-t1={}ms", t2 - t1);
        log.info("t3-t2={}ms", t3 - t2);
        log.info("t4-t3={}ms", t4 - t3);

    }

    /**
     * 获取正序全部接龙列表
     *
     * @param matrix 邻接矩阵
     * @param source 起始词
     * @return
     */
    public static List<String> getNextAllList(byte[][] matrix, String source) {
        List<String> list = new ArrayList<>();
        List<String> nextList = getNextList(matrix, source);
        for (int i = 0; i < nextList.size(); i++) {
            String sub = nextList.get(i);
            List<String> nextAllList = getNextAllList(matrix, sub);
        }
        return list;
    }


    /**
     * 获取正序接龙列表
     *
     * @param matrix 邻接矩阵
     * @param source 起始词
     * @return
     */
    public static List<String> getNextList(byte[][] matrix, String source) {
        return getList(matrix, source, true);
    }

    /**
     * 获取倒序接龙列表
     *
     * @param matrix 邻接矩阵
     * @param source 起始词
     * @return
     */
    public static List<String> getPreviousList(byte[][] matrix, String source) {
        return getList(matrix, source, false);
    }

    /**
     * 获取接龙列表
     *
     * @param matrix 邻接矩阵
     * @param source 起始词
     * @param next   正序，下一个为true；倒序，上一个为false
     * @return
     */
    private static List<String> getList(byte[][] matrix, String source, boolean next) {
        /*1为XY连接，即下一个；2为YX连接，即上一个*/
        final int flag = next ? 1 : 2;
        List<String> list = new ArrayList<>();
        int sourceIndex = -1;
        for (int i = 0; i < idiomArr.length; i++) {
            if (source.equals(idiomArr[i])) {
                sourceIndex = i;
                break;
            }
        }
        if (sourceIndex < 0) {
            return null;
        }
        int count = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            if (matrix[sourceIndex][i] == flag || matrix[sourceIndex][i] == 3) {
                list.add(idiomArr[i]);
                count++;
                log.info("[{},{}]={},idiomY={}", sourceIndex, i, matrix[sourceIndex][i], idiomArr[i]);
            }
        }
        log.info("count:{}", count);
        return list;
    }

    /**
     * 获取邻接矩阵
     *
     * @return
     */
    private static byte[][] getMatrix() {
        byte[][] matrix = new byte[VERTEX_COUNT][VERTEX_COUNT];
        for (int x = 0; x < VERTEX_COUNT; x++) {
            for (int y = 0; y < VERTEX_COUNT; y++) {
                boolean x1EqualY0 = sourceList.get(x).getTail().equals(sourceList.get(y).getHead());
                boolean y1EqualX0 = sourceList.get(y).getTail().equals(sourceList.get(x).getHead());
                if (x1EqualY0) {
                    if (y1EqualX0) {
                        matrix[x][y] = 3;
                    } else {
                        matrix[x][y] = 1;
                    }
                } else {
                    if (y1EqualX0) {
                        matrix[x][y] = 2;
                    } else {
                        matrix[x][y] = 0;
                    }
                }
            }
        }
        return matrix;
    }

    private static void printNotNull(byte[][] matrix) {
        for (int x = 0; x < VERTEX_COUNT; x++) {
            for (int y = 0; y < VERTEX_COUNT; y++) {
                if (matrix[x][y] != 0) {
                    edgeCount++;
                    log.info("[{},{}]={},idiomX={},idiomY={}", x, y, matrix[x][y], idiomArr[x], idiomArr[y]);
                }
            }
        }
        log.info("图的顶点数:{},边数:{},占用堆内存:{}MB", VERTEX_COUNT, edgeCount, (int) Math.ceil(VERTEX_COUNT * VERTEX_COUNT / 1024.0 / 1024.0));
    }

    /**
     * 双向连接，如：坐无虚席，席地而坐
     *
     * @param matrix
     */
    private static void print3(byte[][] matrix) {
        for (int x = 0; x < VERTEX_COUNT; x++) {
            for (int y = 0; y < VERTEX_COUNT; y++) {
                if (matrix[x][y] == 3) {
                    edgeCount++;
                    log.info("[{},{}]={},idiomX={},idiomY={}", x, y, matrix[x][y], idiomArr[x], idiomArr[y]);
                }
            }
        }
        log.info("图的顶点数:{},边数:{},占用堆内存:{}MB", VERTEX_COUNT, edgeCount, (int) Math.ceil(VERTEX_COUNT * VERTEX_COUNT / 1024.0 / 1024.0));
    }


    /**
     * 最短遍历
     *
     * @param beginWord
     * @return
     */
    private static List<String> getFirstIdionList(String beginWord) {
        if (!targetList.contains(beginWord)) {
            targetList.add(beginWord);
        }
        beginWord = beginWord.trim().substring(beginWord.length() - 1);
        for (Idiom e : sourceList) {
            boolean flag = !targetList.contains(e.getName());
            String head = e.getHead();
            if (head.equals(beginWord) && flag) {
                targetList.add(e.getName());
                getFirstIdionList(e.getName());
                break;
            }
        }
        return targetList;
    }

    public static String[] loadIdiomNameArray() {
        List<Idiom> list = loadIdiomList();
        String[] idiomArr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            idiomArr[i] = list.get(i).getName();
        }
        return idiomArr;
    }

    public static List<Idiom> loadIdiomList() {
        List<Idiom> idioms = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(SIMPLE_JSON));
            lines.forEach(e -> {
                try {
                    Idiom idiom = new Idiom();
                    idiom.setHead(e.substring(0, 1));
                    idiom.setHeadSymbol(e.substring(e.indexOf("(") + 1, e.indexOf(" ")));
                    idiom.setTail(e.substring(e.indexOf("(") - 1, e.indexOf("(")));
                    idiom.setTailSymbol(e.substring(e.lastIndexOf(" "), e.indexOf(")")));
                    idiom.setName(e.substring(0, e.indexOf("(")));
                    idioms.add(idiom);
                } catch (Exception exception) {
                    System.err.println(e);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return idioms;
    }

}
