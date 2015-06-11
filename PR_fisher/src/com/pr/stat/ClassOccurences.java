package com.pr.stat;

public class ClassOccurences implements Comparable<ClassOccurences> {

	public int classLabel;
	public int occurences;
	
	
	public ClassOccurences(int classLabel) {
		super();
		this.classLabel = classLabel;
	}
	@Override
	public int compareTo(ClassOccurences o) {
		
		return Integer.compare(this.occurences, o.occurences);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + classLabel;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassOccurences other = (ClassOccurences) obj;
		if (classLabel != other.classLabel)
			return false;
		return true;
	}
	
	
}
