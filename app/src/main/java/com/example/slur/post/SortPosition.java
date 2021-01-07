package com.example.slur.post;

import java.util.List;

public class SortPosition {

    public static List<String> SortBy(List<String> itemsList) {

        String l;

        for (int i = 0; i < itemsList.size(); i++) {
            for (int j = 0; j < itemsList.size() - i - 1; j++) {
                int len1 = itemsList.get(j).length();
                int len2 = itemsList.get(j + 1).length();
                int lim = Math.min(len1, len2);
                String v1 = itemsList.get(j);
                String v2 = itemsList.get(j + 1);

                int check = 0;
                int flag = 0; //두 단어간 우선순위 없음
                int value = 0;
                while (check < lim) {
                    char c1 = v1.charAt(check);
                    char c2 = v2.charAt(check);
                    if (c1 != c2) {
                        flag = 1; //두 단어간 우선순위 있음
                        value = c1 - c2;
                        break;
                    }
                    check++;
                }
                if (flag == 0) value = len1 - len2;

                if ((flag == 1 && value > 0) || (flag == 0 && value > 0)) {

                    l = itemsList.get(j);
                    itemsList.set(j, itemsList.get(j + 1));
                    itemsList.set(j + 1, l);

                }
            }
        }
        return itemsList;
    }
}
