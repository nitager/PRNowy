package com.pr.utils;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.map.MultiKeyMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nitager
 */

public class CombinationIterator implements Iterator<int[]> {

	private final int m;

	private final int n;

	private BigInteger currentIndex;

	private final BigInteger startIndex;

	private final BigInteger combSize;

	private static final MultiKeyMap cache = new MultiKeyMap();

	private static synchronized BigInteger getCachedResult(int m, int n) {
		assert m > n : "m should be large than n";
		return (BigInteger) cache.get(m, n);
	}

	private static synchronized void putToCache(int m, int n, BigInteger result) {
		cache.put(m, n, result);
	}

	public static BigInteger combination(int m, int n) {
		if (m < n) {
			throw new IllegalArgumentException("m should be larger than n: m = " + m + "; n = " + n);
		} else if (n < 0) {
			throw new IllegalArgumentException("m and n should be positive: m = " + m + "; n = " + n);
		}
		if (m - n < n) {
			return combination(m, m - n);
		}
		BigInteger result = getCachedResult(m, n);
		if (result == null) {
			result = combination2(m, n);
			putToCache(m, n, result);
		}
		return result;
	}

	private static BigInteger combination2(int m, int n) {
		assert m >= n : "m should be large than n";
		assert n >= 0 : "m and n should be positive";
		if (n == 0) {
			return BigInteger.ONE;
		}
		BigInteger base = BigInteger.ZERO;
		if (m > n) {
			base = combination(m - 1, n);
		}
		return base.add(combination(m - 1, n - 1));
	}

	private CombinationIterator(int m, int n) {
		this.m = m;
		this.n = n;
		currentIndex = BigInteger.ZERO;
		startIndex = currentIndex;
		combSize = combination(m, n);
	}

	public boolean hasNext() {
		if (currentIndex.subtract(startIndex).compareTo(combSize) == 0) {
			return false;
		} else {
			return true;
		}
	}

	public int[] next() {
		if (!hasNext()) {
			throw new ArrayIndexOutOfBoundsException("currentIndex = " + currentIndex + ";startIndex = " + startIndex + ";combSize = " + combSize);
		}
		BigInteger index = currentIndex;
		if (currentIndex.compareTo(combSize) >= 0) {
			index = currentIndex.subtract(combSize);
		}
		currentIndex = currentIndex.add(BigInteger.ONE);
		return getCombination(m, n, index);
	}

	private int[] getCombination(int m, int n, BigInteger index) {
		assert index.compareTo(combination(m, n)) < 0 : "index should be less than the combSize";
		if (m == 1) {
			return new int[] { 0 };
		}
		BigInteger div = BigInteger.ZERO;
		if (m > n) {
			div = combination(m - 1, n);
			if (index.compareTo(div) < 0) {
				return getCombination(m - 1, n, index);
			}
		}
		BigInteger newIndex = index.subtract(div);
		int[] subComb = getCombination(m - 1, n - 1, newIndex);
		int[] ret = new int[n];
		for (int i = 0; i < subComb.length; i++) {
			ret[i] = subComb[i];
		}
		ret[n - 1] = m - 1;
		return ret;
	}

	public void remove() {
		throw new UnsupportedOperationException("This operation remove was not implemented!");
	}

	public static CombinationIterator combIterator(int m, int n) {
		return new CombinationIterator(m, n);
	}

	public <E> List<E> next(List<E> list) {
		if (list.size() != m) {
			throw new IllegalArgumentException("the size of the list should be " + m);
		}
		int[] next = next();
		List<E> ret = new ArrayList<E>(n);
		for (int i = 0; i < next.length; i++) {
			ret.add(list.get(next[i]));
		}
		return ret;
	}
}
