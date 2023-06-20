package com.weavx.web.utils;

import com.weavx.web.model.CsvFileAddUsersModel;
import com.weavx.web.model.CsvFileModel;
import com.weavx.web.model.CsvModelRestrict;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    public static byte[] getBytesFromMultiPartFile(MultipartFile multipartFile) throws IOException {

        return multipartFile.getBytes();
    }

    public static ArrayList<CsvFileModel> prepareCsvParsing(MultipartFile data) throws IOException {


        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(new BufferedReader(new InputStreamReader(data.getInputStream(), "UTF-8")));
        ArrayList<CsvFileModel> allData = new ArrayList<>();
        for (CSVRecord record : records) {

            CsvFileModel model = new CsvFileModel();

            csvMapping(model,record);

            allData.add(model);
        }
        return allData;


    }
    
    public static ArrayList<CsvFileAddUsersModel> prepareCsvParsingAddsUsers(MultipartFile data) throws IOException {

        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(new BufferedReader(new InputStreamReader(data.getInputStream(), "UTF-8")));
        ArrayList<CsvFileAddUsersModel> allData = new ArrayList<>();
        for (CSVRecord record : records) {

            CsvFileAddUsersModel model = new CsvFileAddUsersModel();

            csvMappingAddUser(model,record);

            allData.add(model);
        }
        return allData;
    }
    
    public static ArrayList<CsvModelRestrict> prepareCsvParsingRestrict(MultipartFile data) throws IOException {


        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(new BufferedReader(new InputStreamReader(data.getInputStream(), "UTF-8")));
        ArrayList<CsvModelRestrict> allData = new ArrayList<>();
        for (CSVRecord record : records) {

        	CsvModelRestrict model = new CsvModelRestrict();

            csvMappingRestrict(model,record);

            allData.add(model);
        }
        return allData;


    }


    private static void csvMapping(CsvFileModel model, CSVRecord csvRecord){
        model.setName(csvRecord.get("name"));
        model.setLastname(csvRecord.get("lastname"));
        model.setEmail(csvRecord.get("email"));
        model.setPayment_details(csvRecord.get("paymentDetails"));
        model.setPayment_type(csvRecord.get("paymentType"));
        model.setPhone(csvRecord.get("phone"));
        model.setLang(csvRecord.get("lang") != null && csvRecord.get("lang").length() > 0 ? csvRecord.get("lang") : "1");
    }
    
    private static void csvMappingAddUser(CsvFileAddUsersModel model, CSVRecord csvRecord){
        model.setFullname(csvRecord.get("fullname"));
        model.setEmail(csvRecord.get("email"));
        model.setMobilephone(csvRecord.get("mobilephone"));
        model.setSdigroup(csvRecord.get("sdigroup"));
    }
    
    private static void csvMappingRestrict(CsvModelRestrict model, CSVRecord csvRecord){
        model.setEmail(csvRecord.get("email"));
    }


}
