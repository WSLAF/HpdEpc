package org.example.model;

import lombok.Data;
import org.dom4j.Element;

import java.util.List;

@Data
public class TestRecurse {

    private List<Item> result;

    public static void main(String[] args) {

    }

    public void recurse(Item item, int index1, int index2, List<List<String>> option) {
        //什么时候返回result

        List<String> ob = option.get(index1);

        // 遍历完所有可选项
        if (index2 == ob.size() - 1) {
            if (index1 == option.size() - 1) {
                result.add(item);
                return;
            }
        }

        //创建一个新的，避免后面的递归修改前面的。
//        List<Element> curResult = new ArrayList<>(result);

        //如果是一维组合
        //能不能简化
//        for (Element element : result) {
            for (int j = index2; j < ob.size(); j++) {
                Item itemCopy = new Item(item.a, item.b);
                //添加当前元素

                result.add(itemCopy);
//                recurse();
                result.remove(itemCopy);
                //还原

            }
//        }
        //todo 设置年份
    }

}
