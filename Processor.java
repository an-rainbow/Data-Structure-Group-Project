package edu.upenn.cit594.processor;

import java.util.TreeMap;
import java.util.List;
import java.util.Map;

import edu.upenn.cit594.data.Park;
import edu.upenn.cit594.data.Population;
import edu.upenn.cit594.datamanagement.ParkReader;
import edu.upenn.cit594.datamanagement.PopulationReader;

public class Processor {
	private Map<Integer, Integer> populationMap_;
	private List<Park> parks_;

	public Processor(PopulationReader poulationReader, ParkReader parkReader) {
		populationMap_ = new TreeMap<>();
		List<Population> populationList = poulationReader.getAllPopulation();
		for (Population p : populationList) {
			populationMap_.put(p.getZipcode(), p.getPopulation());
		}
		parks_ = parkReader.getAllParks();
	}

	/**
	 * Function 1 calculate the total population.
	 * 
	 * @return total population for all zipcodes
	 */
	public int getTotalPopulation() {
		int totalPopulation = 0;
		for (Map.Entry<Integer, Integer> entry : populationMap_.entrySet()) {
			totalPopulation += entry.getValue();
		}
		return totalPopulation;
	}

	/**
	 * Function 2 calculate the per capita fine.
	 *
	 * @return a TreeMap from zipcode to fines per capita
	 */
	public TreeMap<Integer, Double> getTotalFinesPerCapita() {
		Map<Integer, Double> totalFineMap = new TreeMap<>();
		for (Park park : parks_) {
			int zipcode = park.getZipcode();
			if (zipcode > 0 && park.getState().equals("PA")) {
				if (totalFineMap.containsKey(zipcode)) {
					totalFineMap.put(zipcode, totalFineMap.get(zipcode) + park.getFine());
				} else {
					totalFineMap.put(zipcode, park.getFine());
				}
			}
		}
		TreeMap<Integer, Double> perCapitaFineMap = new TreeMap<>();
		for (Map.Entry<Integer, Double> entry : totalFineMap.entrySet()) {
			if (entry.getValue() > 0) {
				int zipcode = entry.getKey();
				if (populationMap_.containsKey(zipcode)) {
					int populationCount = populationMap_.get(zipcode);
					if (populationCount > 0) {
						perCapitaFineMap.put(zipcode, entry.getValue() / populationCount);
					}
				}
			}
		}
		return perCapitaFineMap;
	}

}
