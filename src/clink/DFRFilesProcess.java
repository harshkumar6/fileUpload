package clink;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;;

//DFR Logic
public class DFRFilesProcess {
	 
    public static void main(String args[])throws Exception {
    	
    	//logic1();
    	//logicDate1();
    }
   
    public static String logic1(String SAMPLE_CSV_FILE_PATH, Boolean isCheckHeader) throws Exception{
    	 com.database.DatabaseFunction dbf = new com.database.DatabaseFunction();
    	 Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
          CSVReader csvReader = new CSVReaderBuilder(reader).build();
          List<String[]> records = csvReader.readAll();
          System.out.println("records = "+records.size());
          Set<String> allDates = new HashSet<String>();

          if(isCheckHeader) {
				String[] headers = records.get(0);
				if (headers.length != 199) {
				String	errorMessage = "Invalid file, As file Headers are not valid";
					return errorMessage;
				}
          }
          records.remove(0);
          for(String s[] : records) {
         	 String dtValue = s[15].trim().split(" ")[0].trim();
         	 allDates.add(dtValue);
          }
          System.out.println("allDates = "+allDates);
          
          String queryString = "";
          
          for(String s : allDates) {
           	  queryString = queryString+"`Status Date` LIKE '%"+s+"%' OR";
             }
          System.out.println(" queryString = "+queryString.substring(0, queryString.length()-1));
          String subQueryString = queryString.substring(0, queryString.length()-3);
                   
          String query = "DELETE from centurylinkdata where "+subQueryString;
          System.out.println("final query = "+query);
          dbf.deleteRecords(query);
          
          String loadQuery1 = loadqueryDFR(SAMPLE_CSV_FILE_PATH);
          dbf.loadCsv(loadQuery1);
          
          // we have to delete the file from server
          File f = new File(SAMPLE_CSV_FILE_PATH);
          f.delete();
          return null;
    }
    
    public static String loadqueryDFR(String filePath) {
    	String s = "LOAD DATA LOCAL INFILE '"+filePath+"'  INTO TABLE centurylinkdata FIELDS TERMINATED BY ','  ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 ROWS (@ignore, `LobName1`,	`Campaign name`,	`Vendor Name`,	`Location Name`,	`Call Date`,	`Duration`,	`ExternalID`,	`agent_external_id`,	`Call Type Name`,	`CallDirectionName`,	`EvaluationId`,	`EvaluatorUserName`,	`EvaluatorFirstName`,	`EvaluatorLastName`,	`Status Date`,	`EvaluationDuration`,	`RawPoints`,	`NetScore`,	`Possible Points`,	`AutoFailed`,	`AuditorUserName`,	`AuditorFirstName`,	`AuditorLastName`,	`Audit Status`,	`Audit Date`,	`agent_first_name`,	`agent_last_name`,	`Supervisor`,`Manager`, `Manager2`,	`MediaType1`,	`MediaCodecName1`,	`registered_affiliation`, `Evaluator type`,	`Type of Evaluation`,	`Customer State`,	`C.R.M.`,	`Language`,	`BTN/CBR`,	`Auditor Comments`,	`Evaluator Comments`,	`Performance Area Notes`,	`Invalidation Comments`,	`Form Change Comments`,	`Agent Comments`,	`CTL QA Management Comments`,	`HQ Audit Feedback Loop`,	`Repeat Call/ Chat`,	`Repeat Call/ Chat -Comment`,	`Product or Service Type (multi-select)`,	`Product or Service Type (multi-select) -Comment`,	`Products on the CTL Account`,	`Products on the CTL Account -Comment`,	`Products Available but not on CTL Account`,	`Products Available but not on CTL Account -Comment`,	`Current HSI Speed`,	`Current HSI Speed -Comment`,	`If HSI Upgrade Available - Highest Speed?`,	`If HSI Upgrade Available - Highest Speed? -Comment`,	`Primary Reason for the Call`,	`Primary Reason for the Call -Comment`,	`Billing Inquiry - Sub-reasons - PC`,	`Billing Inquiry - Sub-reasons - PC -Comment`,	`BI - OTC Dispute - Sub-reasons - PC`,	`BI - OTC Dispute - Sub-reasons - PC -Comment`,	`COMP: Did the agent verify the caller thru authorized means?`,	`COMP: Did the agent verify the caller -Comment`,	`NO - Verify caller`,	`NO - Verify caller -Comment`,	`COMP: Did the agent verify and/or capture the email address?`,	`COMP: Did the agent verify and/or capture the email -Comment`,	`NO - Email`,	`NO - Email -Comment`,	`Did the agent utilize the SMS script?`,	`Did the agent utilize the SMS script? -Comment`,	`YES or NO - Did the agent utilize the SMS script?`,	`YES or NO - Did the agent utilize the SMS script? -Comment`,	`Sales Category Applicable`,	`Sales Category Applicable -Comment`,	`Did the agent ask discovery questions?`,	`Did the agent ask discovery questions? -Comment`,	`YES - Discovery Questions`,	`YES - Discovery Questions -Comment`,	`Did the agent discuss a feature of the product or service?`,	`Did the agent discuss a feature of the product -Comment`,	`YES - Feature Discussion`,	`YES - Feature Discussion -Comment`,	`Did the agent offer product(s)?`,	`Did the agent offer product(s)? -Comment`,	`Product Offer Tracking`,	`Product Offer Tracking -Comment`,	`Did the agent lead with the correct offer?`,	`Did the agent lead with the correct offer? -Comment`,	`Correct Offer Tracking`,	`Correct Offer Tracking -Comment`,	`If the agent didn't lead with the correct offer why?`,	`If the agent didn't lead with the correct offer why? -Comment`,	`Did the agent ask for the sale on product(s) that were offered?`,	`Did agent ask for the sale on product(s) that were offer -c`,	`YES - Ask for Sale Tracking`,	`YES - Ask for Sale Tracking -Comment`,	`Did  agent attempt to overcome objections with regards to  sale`,	`Did  agent attempt overcome objections with regards to sale -C`,	`What was the reason the customer declined the sales offer?`,	`What was reason customer declined  sales offer -C`,	`Did the agent close the sale?`,	`Did the agent close the sale? -Comment`,	`YES - Products Sold`,	`YES - Products Sold -Comment`,	`Retention Category Applicable`,	`Retention Category Applicable -Comment`,	`Does this call require a save attempt/offer?`,	`Does this call require a save attempt/offer? -Comment`,	`Did the agent complete an account review for the customer?`,	`Did  agent complete an account review for customer -C`,	`What was the primary reason for the disconnect?`,	`What was the primary reason for the disconnect? -Comment`,	`Did agent ask discovery qus to determine appropriate save offer?`,	`not defined1`,	`Did the agent make a save attempt?`,	`Did the agent make a save attempt? -Comment`,	`YES - Save Attempt Product Tracking`,	`YES - Save Attempt Product Tracking -Comment`,	`Did agent atmt to overcome objections with regard save attempt`,	`overcome objections save attempt? -Comment`,	`What save offers did the agent extend to the customer?`,	`What save offers did the agent extend to the customer? -Comment`,	`Did the agent save the customer?`,	`Did the agent save the customer? -Comment`,	`Save Outcome Tracking - Offer Accepted`,	`Save Outcome Tracking - Offer Accepted -Comment`,	`Did agent offer bill cycle end date as disconnect date`,	`  bill cycle disconnect date? -Comment`,	`Which product(s) were saved?`,	`Which product(s) were saved? -Comment`,	`not defined2`,	`not defined3`,	`What products were added to the account?`,	`What products were added to the account? -Comment`,	`Did the agent follow all applicable regulatory compliance?`,	`Did agent follow all applicable regulatory compliance -c`,	`NO - Applicable Compliance Tracking`,	`NO - Applicable Compliance Tracking -Comment`,	`COMP: Did agent present disclosures and RCCs to the customer?`,	`COMP: Did agent present disclosures and RCCs to the customer-c`,	`NO - Applicable Disclosure Tracking`,	`NO - Applicable Disclosure Tracking -Comment`,	`Did the agent read Disclosures/RCC's that were not applicable?`,	`Did agent read Disclosures/RCC's that were not applicable -c`,	`YES - Not Applicable Disclosure Tracking`,	`YES - Not Applicable Disclosure Tracking -Comment`,	`information provided and/orders issued,  accurate and complete`,	`information provided and/orders issued,accurate and complete -c`,	`NO - Entry Error Tracking`,	`NO - Entry Error Tracking -Comment`,	`Did the agent meet recap expectations?`,	`Did the agent meet recap expectations? -Comment`,	`NO - Recap Expectations Tracking`,	`NO - Recap Expectations Tracking -Comment`,	`Did the agent advise the customer of the NPS survey?`,	`Did the agent advise the customer of the NPS survey? -Comment`,	`NO - NPS Survey Tracking`,	`NO - NPS Survey Tracking -Comment`,	`COMP: Based on call,account notations reflect what agent stated`,	`COMP:Based on call,account notations reflect what agent stated-c`,	`COMP Based call notations reflect what agent stated?Is Y,select`,	`COMP Based call notation reflect what agent stated?Is Y,select-c`,	`COMP: Did the agent follow the call handling process?`,	`COMP: Did the agent follow the call handling process? -Comment`,	`IF Did agent follow call handling process? Is  Y select`,	`IF Did agent follow call handling process? Is  Y select-c`,	`If the call was transferred where to?`,	`If the call was transferred where to? -Comment`,	`Did the agent put the customer on hold?`,	`Did the agent put the customer on hold? -Comment`,	`YES - Hold Process Tracking`,	`YES - Hold Process Tracking -Comment`,	`Hold Duration`,	`Hold Duration -Comment`,	`COMP Did agent demonstrate integrity during customer contact?`,	`COMP Did agent demonstrate integrity during customer contact?-c`,	`NO - Integrity Demonstration Tracking`,	`NO - Integrity Demonstration Tracking -Comment`,	`COMP: Did the agent mistreat the customer during the contact?`,	`COMP Did agent mistreat customer during  contact? -c`,	`YES - Customer Mistreatment Tracking`,	`YES - Customer Mistreatment Tracking -Comment`,	`Did the agent take ownership of the call?`,	`Did the agent take ownership of the call? -Comment`,	`No - Did the agent take ownership of the call?`,	`No - Did the agent take ownership of the call? -Comment`,	`Was an Auto-Failure observed?`,	`Was an Auto-Failure observed? -Comment`,	`If Was an Auto-Failure observed? is Yes, please select`,	`If Was an Auto-Failure observed? is Yes, please select -Comment`,	`What was the overall rating of this observation?`,	`What was the overall rating of this observation? -Comment`, `formname`);";
    	return s;
    }
  
   
}
