package org.insightlab.vonqbe.service;

import java.util.List;

public interface QBEService {
	
	List<String> helper(String text);
	
	String query(String text);
}