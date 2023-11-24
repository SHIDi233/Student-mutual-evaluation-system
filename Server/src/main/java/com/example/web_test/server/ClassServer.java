package com.example.web_test.server;

import com.example.web_test.utils.ExcelData;

import java.util.List;
import java.util.Map;

public interface ClassServer {

    public List<Map<String, Object>> getClasses(int ID);

    public int createClass(int uID, String name, String subject, String introduction);

    public int joinClass(int uID, int cID);

    public List<Map<String, Object>> listMembers(int uID, int cID);

    public int kickMember(int uID, int cID, int kID);

    public Map<String, Object> getCode(int cID);

    Map<String, Object> searchClass(String code);

    int deleteClass(int uID, int cID);

    List<String> importMember(int uID, int cID, List<ExcelData> dataList);
}
