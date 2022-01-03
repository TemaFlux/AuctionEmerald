package me.temaflux.auctionemerald.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;

public class ListUtil {
	public static <T> List<List<T>> partitionIntegerListBasedOnSize(List<T> inputList, int size) {
		return new ArrayList<>(inputList.stream().collect(Collectors.groupingBy(s -> inputList.indexOf(s) / size)).values());
	}
}
