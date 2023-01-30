package graduation.first.post.dto;

import graduation.first.post.domain.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class UploadFileResponse {
    private Long id;
    private String fileUrl;

    public static List<UploadFileResponse> toResponseList(List<UploadFile> uploadFiles) {
        ArrayList<UploadFileResponse> list = new ArrayList<>();
        for (UploadFile file : uploadFiles) {
            list.add(new UploadFileResponse(file.getId(), file.getStoreFileUrl()));
        }
        return list;
    }
}
