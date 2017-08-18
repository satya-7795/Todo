package servlets;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

public class FetchJs extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FetchJs() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/javascript");
        ArrayList fileList = getFileList(request.getParameter("fileList"));
        String combinedFile = getCombinedFile(fileList);
        System.out.println("Combined JavaScript " + combinedFile);
        String outputJavaScript = combinedFile;
        response.getWriter().println(outputJavaScript);
    }

    private ArrayList getFileList(String fileListStr) {
        StringTokenizer st = new StringTokenizer(fileListStr, ",");
        ArrayList fileList = new ArrayList();
        while (st.hasMoreElements()) {
            fileList.add((String) st.nextElement());
        }
        return fileList;
    }

    private String getCombinedFile(ArrayList fileList) throws IOException {
        StringBuffer combinedJavaScript = new StringBuffer();
        for (int i = 0; i < fileList.size(); i++) {
            String fileName = (String) fileList.get(i);
            System.out.println("Read the file " + fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    getServletContext().getResourceAsStream(fileName)));
            String line = null;
            while ((line = br.readLine()) != null) {
                combinedJavaScript.append(line);
                combinedJavaScript.append("\n");
            }
        }
        return combinedJavaScript.toString();
    }
}
