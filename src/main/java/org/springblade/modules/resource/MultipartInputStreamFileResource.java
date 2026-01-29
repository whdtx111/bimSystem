package org.springblade.modules.resource;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;
/**
 * 上传文件资源
 */
public class MultipartInputStreamFileResource extends InputStreamResource {

    private final String filename;

    public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() throws IOException {
        return -1;  // We cannot determine the content length in this case
    }
}
