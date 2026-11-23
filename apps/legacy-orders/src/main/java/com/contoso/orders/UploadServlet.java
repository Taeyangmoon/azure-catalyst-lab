package com.contoso.orders;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/** 주문 첨부 파일을 서버 로컬 디스크에 저장합니다. (스토리지 현대화 대상) */
@WebServlet(urlPatterns = "/upload")
public class UploadServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(UploadServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File dir = new File(DbConfig.uploadDir());
        if (!dir.exists() && !dir.mkdirs()) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "업로드 폴더를 만들 수 없습니다.");
            return;
        }
        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    File target = new File(dir, new File(item.getName()).getName());
                    item.write(target);
                    LOG.info("첨부 파일 저장: " + target.getAbsolutePath());
                }
            }
            resp.sendRedirect(req.getContextPath() + "/orders");
        } catch (FileUploadException e) {
            throw new ServletException(e);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
