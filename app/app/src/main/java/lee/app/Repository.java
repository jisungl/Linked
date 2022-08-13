package lee.app;

import android.util.Pair;

import com.google.gson.Gson;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;

public class Repository {
    public static final String StorageConnectionString = "DefaultEndpointsProtocol=https;"
            + "AccountName=dreamstudy;"
            + "AccountKey=NV+ttpT3qnE4f2HoRuM03NIqcAeKj0M4kkZvqnvviEfRxDu+C5r12OcmJUgYKudwsyih3VQDuXPzJW8RTzUioA==";

    public static final String StorageMeetingContainer = "0224jisungmeeting";
    public static final String StoragePersonContainer = "0224jisungperson";

    private Gson gson = new Gson();

    public Response<Person> getPerson(String id)  {
        try {
            CloudBlobContainer container = getPersonContainer();
            CloudBlockBlob blob = container.getBlockBlobReference(id);

            if (blob.exists()) {
                Person person = gson.fromJson(blob.downloadText(), Person.class);
                return new Response.Success(person);
            } else {
                return new Response.NotExist();
            }

        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<Person> createPerson(Person person)  {
        try {
            CloudBlobContainer container = getPersonContainer();
            CloudBlockBlob blob = container.getBlockBlobReference(person.id);

            if (blob.exists()) {
                return new Response.AlreadyExist();
            } else {
                blob.uploadText(gson.toJson(person));
                return new Response.Success(person);
            }
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<StudySession> getStudySession(String date) {
        try {
            CloudBlobContainer container = getStudySessionContainer();
            CloudBlockBlob blob = container.getBlockBlobReference(date);

            if (blob.exists()) {
                StudySession studySession = gson.fromJson(blob.downloadText(), StudySession.class);
                return new Response.Success(studySession);
            } else {
                return new Response.NotExist();
            }
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<StudySession> updateAttendee(String date, Person attendee) {
        try {
            CloudBlobContainer container = getStudySessionContainer();
            CloudBlockBlob blob = container.getBlockBlobReference(date);
            StudySession studySession;
            if (blob.exists()) {
                studySession = gson.fromJson(blob.downloadText(), StudySession.class);
            } else {
                studySession = new StudySession();
            }
            studySession.attendees.add(attendee);
            blob.uploadText(gson.toJson(studySession));
            return new Response.Success(studySession);
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<StudySession> updateMatchingRessult(String date, List<Pair<Person, Person>> matching) throws URISyntaxException, InvalidKeyException, StorageException, IOException {
        try {
            CloudBlobContainer container = getStudySessionContainer();
            CloudBlockBlob blob = container.getBlockBlobReference(date);
            StudySession studySession = gson.fromJson(blob.downloadText(), StudySession.class);
            studySession.matching = matching;
            blob.uploadText(gson.toJson(studySession));
            return new Response.Success(studySession);
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    private CloudBlobContainer getPersonContainer() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount account = CloudStorageAccount.parse(StorageConnectionString);
        CloudBlobClient blobClient = account.createCloudBlobClient();
        return blobClient.getContainerReference(StoragePersonContainer);
    }

    private CloudBlobContainer getStudySessionContainer() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount account = CloudStorageAccount.parse(StorageConnectionString);
        CloudBlobClient blobClient = account.createCloudBlobClient();
        return blobClient.getContainerReference(StorageMeetingContainer);
    }
}

