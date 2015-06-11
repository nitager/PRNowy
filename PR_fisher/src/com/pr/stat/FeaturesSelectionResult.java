package statystyczne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FeaturesSelectionResult {
	
	
	private List<Integer> featuresNumbers = new ArrayList<Integer>();
	private Double fisherLD = null;
	
	public void setFeature(Integer featureNum) {
		this.featuresNumbers = new ArrayList<>();
		this.featuresNumbers.add(featureNum);
	}
	
	public void setFeatures(int[] featuresNum) {
		this.featuresNumbers = new ArrayList<>();
		for (int i = 0; i < featuresNum.length; i++) {
			this.featuresNumbers.add(featuresNum[i]);
		}
	}
	
	public String getFeaturesNumbersAsString() {
		String result = "";
		Iterator<Integer> it = this.featuresNumbers.iterator();
		while(it.hasNext()) {
			result += it.next();
			
			if (it.hasNext()) {
				result += ", ";
			}
		}
		
		return result;
	}
	
	public int[] getFeaturesAsDoubles() {
		int[] result = new int[this.featuresNumbers.size()];
		Iterator<Integer> it = this.featuresNumbers.iterator();
		int i = 0;
		while(it.hasNext()) {
			result[i] = it.next();
			i++;
		}
		
		return result;
	}
	
	public Double getFisherLD() {
		return fisherLD;
	}
	
	public void setFisherLD(Double fisherLD) {
		this.fisherLD = fisherLD;
	}
	
	public List<Integer> getFeatures() {
		return this.featuresNumbers;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeaturesSelectionResult [featuresNumbers=");
		builder.append(Arrays.toString(featuresNumbers.toArray()));
		builder.append(", fisherLD=");
		builder.append(fisherLD);
		builder.append("]");
		return builder.toString();
	}
}
