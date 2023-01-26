package graduation.first.post;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final FileRepository fileRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void uploadFile(Post post, List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            saveFile(post, file);
        }
    }

    private void saveFile(Post post, MultipartFile multipartFile) throws IOException{
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String storeFileName = UUID.randomUUID() + "." + ext;
        String key = "files/" + storeFileName;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }

        String storeFileUrl = amazonS3Client.getUrl(bucket, key).toString();
        fileRepository.save(new UploadFile(originalFilename, storeFileUrl, post));
    }
}
