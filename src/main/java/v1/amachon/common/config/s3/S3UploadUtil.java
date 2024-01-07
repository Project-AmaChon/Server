package v1.amachon.common.config.s3;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
@Service
public class S3UploadUtil {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile) {
        File uploadFile = convert(multipartFile).get();
        return upload(uploadFile, "amachon", multipartFile.getOriginalFilename());
    }

    private String upload(File uploadFile, String dirName, String originalName) {
        //파일명 중복방지를 위한 UUID
        String fileName = dirName +
                "/" + UUID.randomUUID() +
                "_" + originalName;

        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client
                .putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    public void fileDelete(String fileUrl) {

        String fileKey = fileUrl.substring(48);
        String key = fileKey; // 폴더/파일.확장자
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("ap-northeast-2").build();
        try {
            s3.deleteObject(bucket, key);
        } catch (AmazonServiceException e) {
            throw new RuntimeException("파일 삭제에 실패했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) {
        File convertFile = new File("/" + UUID.randomUUID());
        try {
            if (convertFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(convertFile);
                fos.write(file.getBytes());
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("MultipartFile -> File로 전환이 실패했습니다.");
        }
        return Optional.empty();
    }
}