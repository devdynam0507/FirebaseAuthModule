package com.example.authmodule.module.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 남대영 on 2018-06-07.
 */

public class ArrayUtility {

    public static <T> List<T> typeChange(List<?> list)
    {
        List<T> newList = new ArrayList<>();
        for(Object object: list)
            newList.add((T) object);
        return newList;
    }

    public static <T> T[] toArray(List<?> list, Class<?> classType)
    {
        T[] arr = (T[]) Array.newInstance(classType, list.size());
        for(int i = 0; i < list.size(); i++) {
            arr[i] = (T) list.get(i);
        }
        return arr;
    }

}
