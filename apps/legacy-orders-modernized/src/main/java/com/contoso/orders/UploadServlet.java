package com.contoso.orders;

import java.io.File;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import org.apache.log4j.Logger;

/** Stores order attachments on the server local disk. (a storage modernization target) */
@WebServlet(urlPatterns = "/upload")
public class UploadServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(UploadServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File dir = new File(DbConfig.uploadDir());
        if (!dir.exists() && !dir.mkdirs()) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot create the upload folder.");
            return;
        }
        try {
            DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
            JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload = new JakartaServletFileUpload<>(factory);
            List<DiskFileItem> items = upload.parseRequest(req);
            for (DiskFileItem item : items) {
                if (!item.isFormField()) {
                    File target = new File(dir, new File(item.getName()).getName());
                    item.write(target.toPath());
                    LOG.info("Attachment saved: " + target.getAbsolutePath());
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
