package com.example.web_test.utils.DuplicateCheck;

import java.io.*;

public class TXT_IO {
    /**
     * 读出txt文件
     * 传入文件绝对路径，将文件内容转化为 String字符串输出
     *  txtPath 文件路径
     *  返回 文件内容
     */
    public static String readTxt(String txtPath) {
        String str = "";
        String strLine="";
        // 将 txt文件按行读入 str中
        File storefile = new File(txtPath);
        FileInputStream fileInputStream = null;
        try {
            //将文件信息读入内存并利用InputStreamReader将字节信息转为字符流
            fileInputStream = new FileInputStream(storefile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // 利用BufferedReader创建对象，用readLine方法按行拼接字符串
            while ((strLine = bufferedReader.readLine()) != null) {
                str += strLine;
            }
            // 关闭资源
            inputStreamReader.close();
            bufferedReader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 写入txt文件
     * 传入内容、文件全路径名，将内容写入文件并换行
     *  txtElem 传入的内容
     *  txtPath 写入的文件路径
     */
    public static void writeTxt(double txtElem,String txtPath){
        String str = Double.toString(txtElem);
        File file = new File(txtPath);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.write(str, 0, (str.length() > 3 ? 4 : str.length()));
            fileWriter.write("\r\n");
            // 关闭资源
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
