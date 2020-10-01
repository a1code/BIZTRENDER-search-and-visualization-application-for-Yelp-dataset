package com.biztrender.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is intended for providing application wide common functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class CommonUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class.getName());

	/**
	 * This function is responsible for providing MD5 hash
	 * 
	 * @param input
	 * @return hash
	 * @throws SecurityException
	 * @throws Security
	 */
	public static String getMD5Hash(String input) throws SecurityException {
		byte[] data = input.getBytes();
		String hex = "";
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException(e);
		}
		md.update(data, 0, data.length);
		byte[] b = md.digest();
		int msb;
		int lsb = 0;
		for (int i = 0; i < b.length; i++) {
			msb = ((int) b[i] & 0X000000FF) / 16;
			lsb = ((int) b[i] & 0X000000FF) % 16;
			hex = hex + hexChars[msb] + hexChars[lsb];
		}
		LOGGER.debug(String.format("getMD5Hash(%s) = %s", input, hex));
		return hex;
	}

	public static String getCacheKey(Object... vals) {
		StringBuilder sb = new StringBuilder();
		for (Object val : vals) {
			sb.append(val != null ? val.toString() : null);
		}
		return sb.toString();
	}

	public static String getDefaultStartDateForChart(String date) {
		Calendar cal = Calendar.getInstance();
		final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(DATE_FORMAT.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.MONTH, -3);
		String endDate = DATE_FORMAT.format(cal.getTime());
		return endDate.toString();
	}

	public static String removeSpaceAndLowercase(String str) {
		return str.toLowerCase().replaceAll("\\s+", "");
	}

	public static <T extends Object> boolean isNotEmpty(List<T> list) {
		return list != null && !list.isEmpty();
	}

	public static List<String> getEnglishStopWords() {
		return Arrays.asList(new String[] {"-", "", "the", "i'm", "i'll", "i've", "there", "would", "don't", "i", "me", "my",
				"myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he",
				"him", "his", "himself", "she", "her", "hers", "herself", "it", "it'll", "its", "itself", "they", "them",
				"their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am",
				"is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did",
				"doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at",
				"by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after",
				"above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
				"further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
				"few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
				"too", "very", "s", "t", "can", "will", "just", "don", "should", "now", "0", "1", "2", "3", "4", "5",
				"6", "7", "8", "9" });
	}

}
