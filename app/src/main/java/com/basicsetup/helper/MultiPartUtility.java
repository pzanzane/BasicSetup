package deocos.mahapitch.helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class MultiPartUtility {
    private static final String LINE_FEED = "\r\n";
    private final String boundary;
    private HttpURLConnection httpConn;
    private String charset;
    private DataOutputStream outputStream;
    private int maxChunkSize = 40960;

    private JSONObject jsonFormData = null;
    private HashMap<String, File> mapFiles = null;

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultiPartUtility(String requestURL, String charset)
            throws IOException {
        this.charset = charset;
        jsonFormData = new JSONObject();
        mapFiles = new HashMap<>();

        // creates a unique boundary based on time stamp
        boundary = "---" + System.currentTimeMillis();

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setChunkedStreamingMode(maxChunkSize);

        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Accept", "application/json,text/plain,*/*");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
        httpConn.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=" + boundary
        );

        outputStream = new DataOutputStream(httpConn.getOutputStream());

    }


    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) throws JSONException {
        jsonFormData.put(name, value);

    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        mapFiles.put(fieldName, uploadFile);
    }


    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public HTTPHelper.ResponseObject finish() throws IOException {
        StringBuilder response = new StringBuilder();

        outputStream.writeBytes(LINE_FEED);

        outputStream.writeBytes("--" + boundary);
        outputStream.writeBytes(LINE_FEED);

        outputStream.writeBytes("Content-Disposition: form-data; name=\"data\"");
        outputStream
                .writeBytes(LINE_FEED);
        outputStream
                .writeBytes(LINE_FEED);

        outputStream.writeBytes(jsonFormData.toString());
        outputStream.writeBytes(LINE_FEED);

        //This goes in loop for each file
        Iterator<String> iterator = mapFiles.keySet().iterator();

        int count = 0;
        while (iterator.hasNext()) {
            String key = iterator.next();

            outputStream.writeBytes("--" + boundary);
            outputStream.writeBytes(LINE_FEED);

            outputStream.writeBytes(
                    "Content-Disposition: form-data; name=file" + count + "; filename=\"" + key + "\""
            );
            outputStream.writeBytes(LINE_FEED);

            outputStream.writeBytes("Content-Type:image/jpeg");
            outputStream.writeBytes(LINE_FEED);
            outputStream.writeBytes(LINE_FEED);

            int countSize = 0;
            FileInputStream inputStream = new FileInputStream(mapFiles.get(key));
            byte[] buffer = new byte[maxChunkSize];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                countSize = countSize + bytesRead;
            }
            outputStream.flush();
            inputStream.close();
            count++;

            outputStream.writeBytes(LINE_FEED);

        }

        outputStream.writeBytes("--" + boundary + "--");

        outputStream.writeBytes(LINE_FEED);

        outputStream.close();

        int status = httpConn.getResponseCode();

        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            httpConn.getInputStream()
                    )
            );
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }

        HTTPHelper.ResponseObject responseObject = new HTTPHelper.ResponseObject(response.toString(), status);

        return responseObject;


    }
}