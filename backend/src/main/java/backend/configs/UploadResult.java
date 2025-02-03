package backend.configs;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Objects;


public class UploadResult {
    private HttpStatus status;
    private String[] fileKeys;

    public UploadResult(HttpStatus status, String[] fileKeys) {
        this.status = status;
        this.fileKeys = fileKeys;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String[] getFileKeys() {
        return fileKeys;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setFileKeys(String[] fileKeys) {
        this.fileKeys = fileKeys;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UploadResult that = (UploadResult) o;
        return status == that.status && Objects.deepEquals(fileKeys, that.fileKeys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, Arrays.hashCode(fileKeys));
    }

    @Override
    public String toString() {
        return "UploadResult{" +
                "status=" + status +
                ", fileKeys=" + Arrays.toString(fileKeys) +
                '}';
    }
}