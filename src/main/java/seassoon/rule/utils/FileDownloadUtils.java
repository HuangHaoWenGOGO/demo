package seassoon.rule.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author: 王智超
 * @date:2018年6月12日
 * @Copyright: 2018 www.seassoon.com Inc. All rights reserved.
 * 注意：本内容仅限于上海思贤信息技术股份有限公司内部传阅，禁止外泄以及用于其他的商业行为
 */
public class FileDownloadUtils {

    public static void downloadReport(File file, String s, HttpServletResponse response) {
        try {
            //response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(s, "UTF-8"));
        	response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(s, "UTF-8"));

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            OutputStream os = null;
            int i = 0;
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                while ((i = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, i);
                    os.flush();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new Exception("下载失败");

            }
            try {
                os.flush();
                os.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception("下载失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
