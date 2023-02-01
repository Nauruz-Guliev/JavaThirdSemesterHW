package ru.kpfu.itis.gnt;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client implements Runnable {

    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private final String fileDirectory;

    public Client(Socket socket, String fileDirectory) {
        this.fileDirectory = fileDirectory;
        this.socket = socket;
        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(inputStream)));
            ) {
                String request = reader.readLine();
                System.out.println(request);
                if (!request.startsWith("GET")
                        && (!request.contains("HTTP/1.1") || !request.contains("HTTP/1.0"))
                ) {
                    createErrorResponse("400", "Bad Request", "Your browser send bad request. This request can not be read by server.", outputStream);
                } else {
                    String pathRequest = request.substring(4, request.length() - 9).trim();
                    if (pathRequest.contains("..") || pathRequest.endsWith("~")) {
                        createErrorResponse("403", "Forbidden", "You need permission to access this page.", outputStream);
                    } else {
                        File filePath = Path.of(fileDirectory).resolve(pathRequest.substring(1)).normalize().toFile();
                        if (!filePath.exists() || !filePath.isFile()) {
                            createErrorResponse("404", "Not Found", "Such a file does not exist.", outputStream);
                        } else {
                            Long fileLength = filePath.length();
                            String contentType = URLConnection.guessContentTypeFromName(filePath.getName());
                            try (FileInputStream in = new FileInputStream(filePath)) {
                                createSuccessResponse(
                                        fileLength, contentType, dataOutputStream, in
                                );
                            }
                        }

                    }
                }
            } catch (IOException ex) {
                try {
                    createErrorResponse("503", "Internal Server Error", "There is an error on server.", outputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void createErrorResponse(String errorCode, String statusMessage, String message, OutputStream outputStream) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write("HTTP/1.1 " + errorCode + " " + statusMessage + "\r\n\r\n");
        bufferedWriter.write("<!DOCTYPE html>");
        bufferedWriter.write("<html>");
        bufferedWriter.write("<title>" + errorCode + " " + statusMessage + "</title>");
        bufferedWriter.write("</head>");
        bufferedWriter.write("<body>");
        bufferedWriter.write("<h1>" + errorCode + " " + statusMessage + "</h1>");
        bufferedWriter.write("<p style=\"color: red\">" + message + "</p>");
        bufferedWriter.write("</body>");
        bufferedWriter.write("</html>");
        bufferedWriter.flush();
    }

    private void createSuccessResponse(Long fileLength, String contentType, DataOutputStream dataOutputStream, FileInputStream fileInputStream) throws IOException {
        dataOutputStream.writeBytes("HTTP/1.1 200 OK\r\n");
        dataOutputStream.writeBytes("Content-Type: " + contentType + "\r\n");
        dataOutputStream.writeBytes("Content-Length: " + fileLength + "\r\n\r\n");
        int d;
        byte[] bytes = new byte[1024];
        while ((d = fileInputStream.read(bytes)) != -1) {
            dataOutputStream.write(bytes, 0, d);
        }
        dataOutputStream.flush();
    }


    // на всякий случай
    private void closeClient() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
