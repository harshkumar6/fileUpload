package clink;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.database.DatabaseFunction;

public class FeedsFIlesProcess {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// logic1();
	}

	public static String logic1(String SAMPLE_CSV_FILE_PATH, Boolean isCheckHeader) throws Exception {
		DatabaseFunction dbf = new DatabaseFunction();
		Set<String> allDates = new HashSet<String>();
		 try (BufferedReader br = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH))) {

	            // read line by line
	            String line = br.readLine();
	            	String[] header = line.split("\\^");
	            	if (header.length != 43) {
	    				String errorMessage = "Invalid file, As file Headers are not valid";
	    				return errorMessage;
	    			}
	            while ((line = br.readLine()) != null) {
	            	String[] stringContent = line.split("\\^");
	            	String dtValue = stringContent[3].trim().split(" ")[0].trim();
	    			allDates.add(dtValue);
	            }

	        } catch (IOException e) {
	            System.err.format("IOException: %s%n", e);
	            throw e;
	        }
		
		System.out.println("allDates size = " + allDates.size());

		String queryString = "";

		for (String s : allDates) {
			queryString = queryString + "`call_date` LIKE '%" + s + "%' OR";
		}
		System.out.println(" queryString = " + queryString.substring(0, queryString.length() - 1));
		String subQueryString = queryString.substring(0, queryString.length() - 3);

		String query = "DELETE from CLK_Feed_File where " + subQueryString;
		System.out.println("final query = " + query);
		dbf.deleteRecords(query);

		String loadQuery1 = loadqueryEval(SAMPLE_CSV_FILE_PATH);
		dbf.loadCsv(loadQuery1);

		dbf.delete(
				"DELETE FROM `CLK_Feed_File` WHERE `affiliation`=\"centurylink\" or `form_name`=\"QA Compliance Form - COR\" ");
		// now we have to delete the data where affiliation is centurylink
		// we have to delete the file from server
		File f = new File(SAMPLE_CSV_FILE_PATH);
		f.delete();
		return null;

	}

	public static String loadqueryEval(String filePath) {
		String s = "LOAD DATA LOCAL INFILE '" + filePath
				+ "'  INTO TABLE CLK_Feed_File FIELDS TERMINATED BY '^'  ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 ROWS (`ExternalID`,`agent_id`,`agent_name`,`call_date`,`evaluation_date`,`supervisor_id`,`supervisor_name`,`manager2_id`,`manager2_name`,`status`,`evaluation_type`,`evaluation_title`,`evaluator_id`,`evaluator_name`,`affiliation`,`Type of Evaluation`,`lob`,`campaign`,`vendor`,`location`,`customer_state`,`Possible Points`,`NetScore`,`RawPoints`,`autofail_flag`,`form_name`,`category_id`,`category_name`,`category_score`,`category_possible_score`,`category_net_score`,`question_id`,`question_name`,`question_type`,`scorable`,`answer_id`,`Answer`,`answer_actual_points`,`answer_possible_points`,`manager_id`,`manager_name`,`sequence`,`C.R.M.`);";
		return s;
	}
}
